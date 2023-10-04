package org.egov.usm.web.model;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
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

	@JsonProperty("surveySubmittedId")
	private String surveySubmittedId;
	
	@JsonProperty("surveyNo")
	private String surveyNo;
	
	@JsonProperty("surveyId")
	private String surveyId;
	
	@JsonProperty("surveyTitle")
	private String surveyTitle;
	
	@JsonProperty("tenantId")
	private String tenantId;
	
	@JsonProperty("ward")
	private String ward;
	
	@JsonProperty("slumCode")
	private String slumCode;
	
	@JsonProperty("surveyTime")
	private Long surveyTime;
	
	@JsonProperty("surveyStartTime")
	private String surveyStartTime;
	
	@JsonProperty("surveyEndTime")
	private String surveyEndTime;
	
	@JsonIgnore
	private Object additionalDetail;
	
	@JsonProperty("auditDetails")
	private AuditDetails auditDetails;
	
	@JsonProperty("questionDetails")
	private List<QuestionDetail> questionDetails;
	
	@JsonProperty("submittedAnswers")
	private List<SubmittedAnswer> submittedAnswers;
	
	@JsonProperty("surveyTickets")
	private List<SurveyTicket> surveyTickets = new ArrayList<>();
	
	@JsonProperty("saveQuestionLookup")
	private List<QuestionLookup> saveQuestionLookup = new ArrayList<>();
	
	@JsonProperty("updateQuestionLookup")
	private List<QuestionLookup> updateQuestionLookup = new ArrayList<>();
	
	public SurveyDetails addQuestionsItem(QuestionDetail questionDetails) {
        if (this.questionDetails == null) {
            this.questionDetails = new ArrayList<>();
        }

        if (null != questionDetails)
            this.questionDetails.add(questionDetails);
        return this;
    }
	
	public SurveyDetails addSubmittedAnswer(SubmittedAnswer submittedAnswer) {
        if (this.submittedAnswers == null) {
            this.submittedAnswers = new ArrayList<>();
        }

        if (null != submittedAnswers)
            this.submittedAnswers.add(submittedAnswer);
        return this;
    }
	
}
