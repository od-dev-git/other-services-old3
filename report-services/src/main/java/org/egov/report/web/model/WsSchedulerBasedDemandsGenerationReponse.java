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
public class WsSchedulerBasedDemandsGenerationReponse {

	@JsonProperty("ulb")
	private String ulb;
	
	@JsonProperty("tenantId")
	private String tenantId;
	
	@JsonProperty("consumerCode")
	private String consumerCode;
	
	@JsonProperty("ward")
	private String ward;
	
	@JsonProperty("oldConnectionNo")
	private String oldConnectionNo;
	
	@JsonProperty("connectionType")
	private String connectionType;
	
	@JsonProperty("demandGenerationDate")
	private String demandGenerationDate;
	
	@JsonProperty("taxPeriodFrom")
	private String taxPeriodFrom;
	
	@JsonProperty("taxPeriodto")
	private String taxPeriodto;
	
}
