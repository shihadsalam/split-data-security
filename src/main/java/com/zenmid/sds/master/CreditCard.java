package com.zenmid.sds.master;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Table(name = "credit_card")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreditCard {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@Column(name = "card_type")
	private String cardType;

	@Column(name = "card_number_part1")
	private String cardNumberPart1;

	@Column(name = "pin_part1")
	private String pinPart1;

	private String cvv;
	private String expiry;

}
