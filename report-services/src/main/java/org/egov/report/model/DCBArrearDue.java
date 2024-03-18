package org.egov.report.model;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DCBArrearDue {
	
	private String consumerCode;
	
	private BigDecimal taxAmount;
	
	private BigDecimal collectionAmount;
	
	private BigDecimal due;

}
