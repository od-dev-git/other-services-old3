package org.egov.report.web.model;

import java.util.Set;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SRReportSearchCriteria {
	
	@JsonProperty("tenantId")
	private String tenantId;
	
	@JsonProperty("fromDate")
	private Long fromDate;
	
	@JsonProperty("toDate")
	private Long toDate;
	
	@JsonProperty("serviceRequestId")
	private String serviceRequestId;
	
	@JsonProperty("service")
	private String service;
	
	@JsonProperty("status")
	private String status;
}

