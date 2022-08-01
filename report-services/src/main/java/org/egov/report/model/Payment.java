package org.egov.report.model;

import java.math.BigDecimal;
import java.util.List;

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
public class Payment {
	
	private String tenantId;
	
	private BigDecimal totalDue;
	
	private BigDecimal totalAmountPaid;
	
	private String transactionNumber;
	
	private Long transactionDate;
	
	private String paymentMode;
	
	private Long instrumentDate;
	
	private String instrumentStatus;
	
	private AuditDetails auditDetails;
	
	private List<PaymentDetails> paymentDetails;
	
}
