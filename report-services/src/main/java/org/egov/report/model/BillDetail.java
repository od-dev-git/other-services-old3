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
public class BillDetail {
	
	private BigDecimal amount;
	
	private BigDecimal amountPaid;
	
	private Long fromPeriod;
	
	private Long toPeriod;
}
