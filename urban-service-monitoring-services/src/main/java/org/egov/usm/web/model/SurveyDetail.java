package org.egov.usm.web.model;

import org.egov.usm.model.enums.SurveyAnswer;

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
public class SurveyDetail {

	@JsonProperty("id")
	private String id;
	
	@JsonProperty("surveyId")
	private String surveyId;
	
	@JsonProperty("questionCode")
	private String questionCode;
	
	@JsonProperty("answer")
	private SurveyAnswer answer;
	
	@JsonProperty("additionalDetail")
	private String additionalDetail;
	
	@JsonProperty("auditDetail")
	private AuditDetails auditDetail;
	
}
