package org.egov.dss.web.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;

@Getter
public class DSSFilter {
	
	@JsonProperty("tenantId")
	private String tenantId;
	
	@JsonProperty("startDate")
	private Long startDate;
	
	@JsonProperty("endDate")
	private Long endDate;
	
	@JsonProperty("districtid")
	private String districtid;
	
	@JsonProperty("city")
	private String city;
}
