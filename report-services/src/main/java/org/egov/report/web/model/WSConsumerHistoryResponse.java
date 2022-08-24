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
	@Builder.Default
	private BigDecimal currentDemand = BigDecimal.ZERO;

	@JsonProperty("collectionAmt")
	@Builder.Default
	private BigDecimal collectionAmt = BigDecimal.ZERO;
	
	@JsonProperty("rebateAmt")
	@Builder.Default
	private BigDecimal rebateAmt = BigDecimal.ZERO;
	
	@JsonProperty("penalty")
	@Builder.Default
	private BigDecimal penalty = BigDecimal.ZERO;
	
	@JsonProperty("advance")
	@Builder.Default
	private BigDecimal advance = BigDecimal.ZERO;
	
	@JsonProperty("arrear")
	@Builder.Default
	private BigDecimal arrear = BigDecimal.ZERO;
	
	@JsonProperty("totalDue")
	@Builder.Default
	private BigDecimal totalDue = BigDecimal.ZERO;
	
	@JsonProperty("paymentDate")
	private String paymentDate;
	
	@JsonProperty("paymentMode")
	private String paymentMode;
	
	@JsonProperty("receiptNo")
	private String receiptNo;
	
}
