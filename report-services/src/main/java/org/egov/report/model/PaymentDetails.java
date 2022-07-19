package org.egov.report.model;

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
public class PaymentDetails {
	
	private String receiptNumber;
	
	private String receiptType;
	
	private Long receiptDate;
	
	private String businessService;
	
	private Bill bill;
	
}
