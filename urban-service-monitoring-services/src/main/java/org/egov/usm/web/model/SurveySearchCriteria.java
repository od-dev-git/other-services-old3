package org.egov.usm.web.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SurveySearchCriteria {
	@JsonProperty("tenantId")
	private String tenantId;
	
	@JsonProperty("surveyId")
	private String surveyId;
	
	@JsonProperty("ward")
	private String ward;
	
	@JsonProperty("slumCode")
	private String slumCode;
}
