package org.egov.report.web.model;

import java.math.BigDecimal;

import org.egov.report.web.model.ConsumerPaymentHistoryResponse.ConsumerPaymentHistoryResponseBuilder;

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
public class WSConsumerHistoryResponse {
	
	@JsonProperty("ulb")
	private String ulb;
	
	@JsonProperty("ward")
	private String ward;
	
	@JsonProperty("consumerNo")
	private String consumerNo;
	
	@JsonProperty("oldConnectionNo")
	private String oldConnectionNo;
	
	@JsonProperty("month")
	private String month;
	
	@JsonProperty("connectionType")
	private String connectionType;
	
	@JsonProperty("currentDemand")
	private String currentDemand;

	@JsonProperty("collectionAmt")
	private String collectionAmt;
	
	@JsonProperty("rebateAmt")
	private String rebateAmt;
	
	@JsonProperty("penalty")
	private String penalty;
	
	@JsonProperty("advance")
	private String advance;
	
	@JsonProperty("arrear")
	private String arrear;
	
	@JsonProperty("totalDue")
	private String totalDue;
	
	@JsonProperty("paymentDate")
	private String paymentDate;
	
	@JsonProperty("paymentMode")
	private String paymentMode;
	
	@JsonProperty("receiptNo")
	private String receiptNo;
	
}
