package org.egov.usm.web.model;

import java.util.List;

import org.egov.common.contract.response.ResponseInfo;

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
public class SurveyResponse {
	
	@JsonProperty("responseInfo")
	private ResponseInfo responseInfo;
	
	@JsonProperty("response")
	private List<SurveyResponse> response;

}
