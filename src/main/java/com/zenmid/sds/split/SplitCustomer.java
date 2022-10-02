package com.zenmid.sds.split;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "customer_part2")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SplitCustomer {

	@Id
	private Long id;
	
	@Column(name = "ssn_part2")
	private String ssnPart2;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "card_id", referencedColumnName = "id")
	private SplitCreditCard creditCard;

}
