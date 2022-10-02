package com.zenmid.sds.dto;

import java.io.Serializable;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerResponse implements Serializable {

	private static final long serialVersionUID = 1L;
	private Long id;
	private String name;
	private int age;
	private String ssn;
	private List<AddressResponse> address;
	private CreditCardResponse creditCard;

}
