package org.egov.report.web.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class WSReportSearchCriteria {
	
	@JsonProperty("tenantId")
	private String tenantId;

	@JsonProperty("module")
	private String module;
	
	@JsonProperty("fromDate")
	private Long fromDate;
	
	@JsonProperty("toDate")
	private Long toDate;
	
	@JsonProperty("collectionDate")
	private Long collectionDate;
	
	@JsonProperty("paymentMode")
	private String paymentMode;
	
	@JsonProperty("monthYear")
	private Long monthYear;
}
