package org.egov.dss.web.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;

@Getter
public class ChartCriteria {
	
	@JsonProperty("moduleLevel")
	private String moduleLevel;
	
	@JsonProperty("visualizationCode")
	private String visualizationCode;
	
	@JsonProperty("filters")
	private DSSFilter filter;

}
