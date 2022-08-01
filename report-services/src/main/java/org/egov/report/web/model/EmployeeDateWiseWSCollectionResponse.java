package org.egov.report.web.model;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class EmployeeDateWiseWSCollectionResponse {
	
	private String tenantId;
	
	private String ulb;
	
	private String employeeId;
	
	private String employeeName;
	
	private String businessService;
	
	private String head;
	
	private Long transactionDate;
	
	private String paymentMode;
	
	private String consumerCode;
	
	private String receiptNo;
	
	private BigDecimal collectedAmount;
}
