package com.zenmid.sds.split;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Table(name = "credit_card_part2")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SplitCreditCard {

	@Id
	private Long id;

	@Column(name = "card_number_part2")
	private String cardNumberPart2;

	@Column(name = "pin_part2")
	private String pinPart2;
}
