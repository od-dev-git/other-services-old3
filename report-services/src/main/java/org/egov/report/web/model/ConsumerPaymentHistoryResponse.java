package org.egov.report.web.model;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonProperty;

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
public class ConsumerPaymentHistoryResponse {
	
	@JsonProperty("tenantId")
	private String tenantId;
	
	@JsonProperty("ulb")
	private String ulb;
	
	@JsonProperty("wardNo")
	private String ward;
	
	@JsonProperty("consumerNo")
	private String consumerCode;
	
	@JsonProperty("consumerName")
	private String conumerName;
	
	@JsonProperty("consumerAddress")
	private String consumerAddress;
	
	@JsonProperty("head")
	private String head;
	
	@JsonProperty("dateOfTransaction")
	private Long transactionDate;
	
	@JsonProperty("MonthYear")
	private String monthYear;
	
	@JsonProperty("paidAmount")
	private BigDecimal paidAmount;

	@JsonProperty("paymentMode")
	private String paymentMode;
	
	@JsonProperty("transactionId")
	private String transactionId;
	
	@JsonProperty("employeeId")
	private String employeeId;
	
	@JsonProperty("employeeName")
	private String employeeName;
}
