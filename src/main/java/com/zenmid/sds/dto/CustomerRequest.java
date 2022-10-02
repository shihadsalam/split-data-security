package com.zenmid.sds.dto;

import java.util.List;

import com.zenmid.sds.master.Address;

import lombok.Data;

@Data
public class CustomerRequest {

	private Long id;
	private String name;
	private int age;
	private String ssn;
	private String ssnPart1;
	private String ssnPart2;
	private List<Address> address;
	private CreditCardRequest creditCard;

}
