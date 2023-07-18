package org.egov.usm.web.model;

import java.util.List;

import org.egov.usm.model.enums.Status;
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
public class QuestionDetail {

	@JsonProperty("id")
	private String id;
	
	@JsonProperty("surveyId")
	private String surveyId;
	
	@JsonProperty("surveyDetailsId")
	private String surveyDetailsId;
	
	@JsonProperty("questionStatement")
	private String questionStatement;
	
	@JsonProperty("category")
	private String category;
	
	@JsonProperty("options")
    private List<String> options;
	
	@JsonProperty("answer")
	private SurveyAnswer answer;
	
	@JsonProperty("auditDetail")
	private AuditDetails auditDetail;
	
	@JsonProperty("status")
    private Status status;

    @JsonProperty("required")
    private Boolean required;
    
    @JsonProperty("hasOpenTicket")
    private Boolean hasOpenTicket;
	
}
