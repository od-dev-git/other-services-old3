package org.egov.report.web.model;

import java.math.BigDecimal;

import org.egov.report.web.model.WaterNewConsumerMonthlyResponse.WaterNewConsumerMonthlyResponseBuilder;

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
public class ConsumerBillHistoryResponse {
	
	@JsonProperty("ulb")
	private String ulb;
	
	@JsonProperty("consumerCode")
	private String consumerCode;
	
	@JsonProperty("periodFrom")
	private String periodFrom;
	
	@JsonProperty("periodTo")
	private String periodTo;
	
	@JsonProperty("paymentCompleted")
	private String paymentCompleted;
	
	@JsonProperty("taxAmount")
	private BigDecimal taxAmount;
	
	@JsonProperty("collectedAmount")
	private BigDecimal collectedAmount;
	
	@JsonProperty("dueAmount")
	private BigDecimal dueAmount;
	
}
