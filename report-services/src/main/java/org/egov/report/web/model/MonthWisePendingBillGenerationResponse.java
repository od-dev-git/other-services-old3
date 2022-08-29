package org.egov.report.web.model;

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
public class MonthWisePendingBillGenerationResponse {
	
	@JsonProperty("ulb")
	private String ulb;
	
	@JsonProperty("tenantId")
	private String tenantId;
	
	@JsonProperty("consumerCode")
	private String consumerCode;

}
