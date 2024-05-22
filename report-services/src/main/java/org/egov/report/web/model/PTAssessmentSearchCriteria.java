package org.egov.report.web.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PTAssessmentSearchCriteria {
	@JsonProperty("financialYear")
	private String financialYear;
	
	@JsonProperty("tenantIds")
	private List<String> tenantIds; 
}