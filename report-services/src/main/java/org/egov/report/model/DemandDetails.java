package org.egov.report.model;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DemandDetails {
	
	private String consumerCode;
	
	private String demandId;

	private BigDecimal rebateAmount = BigDecimal.ZERO;

	private BigDecimal taxAmount = BigDecimal.ZERO;

	private BigDecimal penaltyAmount = BigDecimal.ZERO;

	private BigDecimal advance = BigDecimal.ZERO;

	private BigDecimal collectedAmt = BigDecimal.ZERO;

	private BigDecimal arrears = BigDecimal.ZERO;

	private BigDecimal arrearsCollectedAmount = BigDecimal.ZERO;

	private BigDecimal amountBeforeDueDate = BigDecimal.ZERO;

	private BigDecimal amountAfterDueDate = BigDecimal.ZERO;

	private BigDecimal totalDue = BigDecimal.ZERO;

	private String demandPeriodFrom;

	private String demandPeriodTo;

}
