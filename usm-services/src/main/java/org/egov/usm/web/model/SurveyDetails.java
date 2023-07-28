package org.egov.usm.web.model;

import java.util.ArrayList;
import java.util.List;

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
public class SurveyDetails {

	@JsonProperty("id")
	private String id;
	
	@JsonProperty("surveyNo")
	private String surveyNo;
	
	@JsonProperty("surveyId")
	private String surveyId;
	
	@JsonProperty("tenantId")
	private String tenantId;
	
	@JsonProperty("ward")
	private String ward;
	
	@JsonProperty("slumCode")
	private String slumCode;
	
	@JsonProperty("surveyTime")
	private Long surveyTime;
	
	@JsonProperty("isClosed")
	private Boolean isClosed;
	
	@JsonProperty("additionalDetail")
	private Object additionalDetail;
	
	@JsonProperty("auditDetails")
	private AuditDetails auditDetails;
	
	@JsonProperty("questionDetails")
	private List<QuestionDetail> questionDetails;
	
	public SurveyDetails addQuestionsItem(QuestionDetail questionDetails) {
        if (this.questionDetails == null) {
            this.questionDetails = new ArrayList<>();
        }

        if (null != questionDetails)
            this.questionDetails.add(questionDetails);
        return this;
    }
	
	@JsonProperty("submittedAnswers")
	List<SubmittedAnswer> submittedAnswers  = new ArrayList<>();
	
	@JsonProperty("surveyTickets")
	private List<SurveyTicket> surveyTickets = new ArrayList<>();
	
	@JsonProperty("saveQuestionLookup")
	private List<QuestionLookup> saveQuestionLookup = new ArrayList<>();
	
	@JsonProperty("updateQuestionLookup")
	private List<QuestionLookup> updateQuestionLookup = new ArrayList<>();
	
}
