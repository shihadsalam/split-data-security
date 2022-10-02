package com.zenmid.sds.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.zenmid.sds.dto.CardPinUpdateRequest;
import com.zenmid.sds.dto.CustomerRequest;
import com.zenmid.sds.dto.CustomerResponse;
import com.zenmid.sds.service.CustomerService;

@RestController
public class SplitDataController {

	@Autowired
	private CustomerService customerService;

	@PostMapping("/customer")
	public String createCustomer(@RequestBody CustomerRequest customerRequest) {
		return customerService.createCustomer(customerRequest);
	}

	@GetMapping("/customers")
	public List<CustomerResponse> getAllCustomers() {
		return customerService.getAllCustomers();
	}

	@GetMapping("/customer/{name}")
	public CustomerResponse getCustomerByName(@PathVariable String name) {
		return customerService.getCustomerByName(name);
	}

	@PostMapping("/customer/update-card-pin")
	// @PreAuthorize("hasAnyAuthority('ROLE_USER')")
	public String upateCreditCardPIN(@RequestBody CardPinUpdateRequest cardPinUpdateRequest) {
		return customerService.upateCreditCardPIN(cardPinUpdateRequest.getId(), cardPinUpdateRequest.getCardPin());
	}
	
	@GetMapping("/clear-cache")
	public String clearAllCache() {
		return customerService.evictAllCache();
	}

}
