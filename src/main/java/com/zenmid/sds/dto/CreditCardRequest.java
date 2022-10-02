package com.zenmid.sds.dto;

import lombok.Data;

@Data
public class CreditCardRequest {

	private Long id;
	private String cardType;
	private String cardNumber;
	private String cardNumberPart1;
	private String cardNumberPart2;
	private String pin;
	private String pinPart1;
	private String pinPart2;
	private String cvv;
	private String expiry;

}
