package com.zenmid.sds.dto;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreditCardResponse implements Serializable {

	private static final long serialVersionUID = 1L;
	private Long id;
	private String cardType;
	private String cardNumber;
	private String pin;
	private String cvv;
	private String expiry;

}
