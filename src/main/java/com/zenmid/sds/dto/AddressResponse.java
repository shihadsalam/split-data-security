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
public class AddressResponse implements Serializable {

	private static final long serialVersionUID = 1L;
	private Long id;
	private String type;
	private String street;
	private String houseNo;
	private Integer pinCode;

}
