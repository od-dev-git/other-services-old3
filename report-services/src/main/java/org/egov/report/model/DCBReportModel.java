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
public class DCBReportModel {
	
	private String propertyId;
	
	private String oldPropertyId;
	
	private String ward;
	
	private BigDecimal currentDemand;
	
	private BigDecimal arrearDemand;
	
	private BigDecimal totalDemand;
	
	private BigDecimal currentPayment;

}