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
public class SubmittedAnswer {

	@JsonProperty("id")
	private String id;
	
	@JsonProperty("surveySubmittedId")
	private String surveySubmittedId;
	
	@JsonProperty("questionId")
	private String questionId;
	
	@JsonProperty("questionStatement")
	private String questionStatement;
	
	@JsonProperty("questionCategory")
	private String questionCategory;
	
	@JsonProperty("answer")
	private SurveyAnswer answer;
	
    @JsonProperty("hasOpenTicket")
    private Boolean hasOpenTicket = false;
	
	@JsonProperty("auditDetails")
	private AuditDetails auditDetails;

}
