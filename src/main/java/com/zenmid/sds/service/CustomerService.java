package com.zenmid.sds.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.zenmid.sds.dto.AddressResponse;
import com.zenmid.sds.dto.CreditCardRequest;
import com.zenmid.sds.dto.CreditCardResponse;
import com.zenmid.sds.dto.CustomerRequest;
import com.zenmid.sds.dto.CustomerResponse;
import com.zenmid.sds.master.Address;
import com.zenmid.sds.master.AddressRepository;
import com.zenmid.sds.master.CreditCard;
import com.zenmid.sds.master.Customer;
import com.zenmid.sds.master.CustomerRepository;
import com.zenmid.sds.split.SplitCreditCard;
import com.zenmid.sds.split.SplitCustomer;
import com.zenmid.sds.split.SplitCustomerRepository;

@Service
public class CustomerService {

	public static final Logger LOGGER = LoggerFactory.getLogger(CustomerService.class);

	@Autowired
	private CustomerRepository customerRepository;

	@Autowired
	private AddressRepository addressRepository;

	@Autowired
	private SplitCustomerRepository splitCustomerRepository;

	@Autowired
	private CacheManager cacheManager;

	public String createCustomer(CustomerRequest customerRequest) {
		String message = "";
		try {
			customerRequest = applyRules(customerRequest);
			CreditCardRequest cardRequest = customerRequest.getCreditCard();
			CreditCard card = CreditCard.builder().cardNumberPart1(cardRequest.getCardNumberPart1())
					.cardType(cardRequest.getCardType()).cvv(cardRequest.getCvv()).expiry(cardRequest.getExpiry())
					.pinPart1(cardRequest.getPinPart1()).build();
			Customer customer = Customer.builder().address(customerRequest.getAddress()).age(customerRequest.getAge())
					.name(customerRequest.getName()).ssnPart1(customerRequest.getSsnPart1()).creditCard(card).build();
			for (Address address : customerRequest.getAddress()) {
				address.setCustomer(customer);
			}
			customer = customerRepository.save(customer);
			addressRepository.saveAll(customerRequest.getAddress());

			SplitCreditCard splitCreditCard = SplitCreditCard.builder().id(customer.getCreditCard().getId())
					.cardNumberPart2(cardRequest.getCardNumberPart2()).pinPart2(cardRequest.getPinPart2()).build();
			SplitCustomer splitCustomer = SplitCustomer.builder().id(customer.getId()).creditCard(splitCreditCard)
					.ssnPart2(customerRequest.getSsnPart2()).build();
			splitCustomerRepository.save(splitCustomer);
			message = "Customer successfully created!";
		} catch (Exception e) {
			LOGGER.error("Exception occurred while creating customer", e);
			message = "Failed to create customer, exception occurred - " + e.getMessage();
		}

		return message;
	}

	public String upateCreditCardPIN(Long custId, String cardPin) {
		Optional<Customer> optCust = customerRepository.findById(custId);
		if (optCust.isPresent()) {
			Customer customer = optCust.get();
			SplitCustomer splitCustomer = splitCustomerRepository.findById(custId).get();
			String[] pinParts = splitEqually(cardPin);
			customer.getCreditCard().setPinPart1(pinParts[0]);
			splitCustomer.getCreditCard().setPinPart2(pinParts[1]);
			customerRepository.save(customer);
			splitCustomerRepository.save(splitCustomer);
			evictCacheByNameAndKey("customerCache", customer.getName());
			return "Card pin updated successfully!";
		} else {
			return "Customer does not exists for the id: " + custId;
		}
	}

	@Cacheable(value = "customerCache")
	public CustomerResponse getCustomerByName(String name) {
		Customer customer = customerRepository.findByName(name);
		if (null != customer) {
			SplitCustomer splitCustomer = splitCustomerRepository.findById(customer.getId()).get();
			return buildCustomerResponse(customer, splitCustomer);
		}
		return null;
	}

	@Cacheable(value = "customerCache")
	public List<CustomerResponse> getAllCustomers() {
		List<Customer> customers = customerRepository.findAll();
		List<CustomerResponse> responses = new ArrayList<>();
		for (Customer customer : customers) {
			SplitCustomer splitCustomer = splitCustomerRepository.findById(customer.getId()).get();
			responses.add(buildCustomerResponse(customer, splitCustomer));
		}
		return responses;
	}

	public String evictAllCache() {
		for (String cacheName : cacheManager.getCacheNames()) {
			cacheManager.getCache(cacheName).clear();
		}
		return "Cache clear executed succesfully";
	}

	public void evictCacheByNameAndKey(String name, String key) {
		cacheManager.getCache(name).evict(key);
	}

	public void evictCacheByName(String name) {
		cacheManager.getCache(name).clear();
	}

	private CustomerRequest applyRules(CustomerRequest customerRequest) {
		CreditCardRequest creditCardRequest = customerRequest.getCreditCard();
		String[] splittedSsn = splitEqually(customerRequest.getSsn());
		String[] splittedPin = splitEqually(creditCardRequest.getPin());
		String[] splittedCardNo = splitEqually(creditCardRequest.getCardNumber());
		customerRequest.setSsnPart1(splittedSsn[0]);
		customerRequest.setSsnPart2(splittedSsn[1]);
		creditCardRequest.setPinPart1(splittedPin[0]);
		creditCardRequest.setPinPart2(splittedPin[1]);
		creditCardRequest.setCardNumberPart1(splittedCardNo[0]);
		creditCardRequest.setCardNumberPart2(splittedCardNo[1]);
		return customerRequest;
	}

	private String[] splitEqually(String str) {
		int length = str.length(); // Get string length
		int whereToSplit; // store where will split
		if (length % 2 == 0)
			whereToSplit = length / 2; // if length number is pair then it'll split equal
		else
			whereToSplit = (length + 1) / 2; // else the first value will have one char more than the other
		return str.split("(?<=\\G.{" + whereToSplit + "})");// split the string
	}

	private CustomerResponse buildCustomerResponse(Customer customer, SplitCustomer splitCustomer) {
		List<Address> addresses = customer.getAddress();
		List<AddressResponse> addressResponses = new ArrayList<>();
		CreditCard card = customer.getCreditCard();
		SplitCreditCard splitCreditCard = splitCustomer.getCreditCard();
		addresses.forEach(address -> {
			addressResponses.add(AddressResponse.builder().houseNo(address.getHouseNo()).id(address.getId())
					.pinCode(address.getPinCode()).street(address.getStreet()).type(address.getType()).build());
		});
		String cardNo = card.getCardNumberPart1().concat(splitCreditCard.getCardNumberPart2());
		String pin = card.getPinPart1().concat(splitCreditCard.getPinPart2());
		String ssn = customer.getSsnPart1().concat(splitCustomer.getSsnPart2());
		CreditCardResponse cardResponse = CreditCardResponse.builder().id(card.getId()).cardNumber(cardNo).pin(pin)
				.cardType(card.getCardType()).cvv(card.getCvv()).expiry(card.getExpiry()).build();

		return CustomerResponse.builder().id(customer.getId()).address(addressResponses).creditCard(cardResponse)
				.age(customer.getAge()).name(customer.getName()).ssn(ssn).build();

	}

}
