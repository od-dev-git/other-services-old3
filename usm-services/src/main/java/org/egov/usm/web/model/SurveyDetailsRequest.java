package org.egov.usm.web.model;

import org.egov.common.contract.request.RequestInfo;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SurveyDetailsRequest {

	@JsonProperty("requestInfo")
	private RequestInfo requestInfo;
	
	@JsonProperty("surveyDetails")
	private SurveyDetails surveyDetails;
}
