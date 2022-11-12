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

	@Builder.Default
	private BigDecimal rebateAmount = BigDecimal.ZERO;

	@Builder.Default
	private BigDecimal taxAmount = BigDecimal.ZERO;

	@Builder.Default
	private BigDecimal penaltyAmount = BigDecimal.ZERO;

	@Builder.Default
	private BigDecimal advance = BigDecimal.ZERO;

	@Builder.Default
	private BigDecimal collectedAmt = BigDecimal.ZERO;

	@Builder.Default
	private BigDecimal arrears = BigDecimal.ZERO;

	@Builder.Default
	private BigDecimal arrearsCollectedAmount = BigDecimal.ZERO;

	@Builder.Default
	private BigDecimal amountBeforeDueDate = BigDecimal.ZERO;

	@Builder.Default
	private BigDecimal amountAfterDueDate = BigDecimal.ZERO;

	@Builder.Default
	private BigDecimal totalDue = BigDecimal.ZERO;

	private String demandPeriodFrom;

	private String demandPeriodTo;

}
