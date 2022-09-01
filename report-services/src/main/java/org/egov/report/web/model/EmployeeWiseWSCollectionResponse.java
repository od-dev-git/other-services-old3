package org.egov.report.web.model;

import java.math.BigDecimal;

import org.egov.report.web.model.BillSummaryResponses.BillSummaryResponsesBuilder;

import com.fasterxml.jackson.annotation.JsonProperty;

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
public class EmployeeWiseWSCollectionResponse {
	
	@JsonProperty("ulb")
	private String ulb;
	
	@JsonProperty("tenantId")
	private String tenantId;
	
	@JsonProperty("ward")
	private String ward;

	@JsonProperty("employeeId")
	private String employeeId;
	
	@JsonProperty("employeeName")
	private String employeeName;
	
	@JsonProperty("head")
	private String head;
	
	@JsonProperty("paymentDate")
	private Long paymentDate;
	
	@JsonProperty("paymentMode")
	private String paymentMode;
	
	@JsonProperty("receiptNo")
	private String receiptNo;
	
	@JsonProperty("consumerCode")
	private String consumerCode;
	
	@JsonProperty("amount")
	private BigDecimal amount;
	
	@JsonProperty("oldConsumerNo")
	private String oldConsumerNo;
}
