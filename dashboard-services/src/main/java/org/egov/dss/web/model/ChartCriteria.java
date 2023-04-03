package org.egov.dss.web.model;

import javax.validation.constraints.Pattern;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ChartCriteria {
	
//	@JsonProperty("moduleLevel")
//	private String moduleLevel;
//	
//	@JsonProperty("visualizationCode")
//	private String visualizationCode;
//	
//	@JsonProperty("filters")
//	private DSSFilter filter;
	

	@JsonProperty("financialYear")
	private String financialYear;
	
	@JsonProperty("startDate")
	private Long startDate;
	
	@JsonProperty("endDate")
	private Long endDate;

}
