package org.egov.usm.repository.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.egov.usm.model.enums.SurveyAnswer;
import org.egov.usm.web.model.AuditDetails;
import org.egov.usm.web.model.SubmittedAnswer;
import org.egov.usm.web.model.SurveyDetails;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

@Component
public class SurveyAnswerDetailsRowMapper implements ResultSetExtractor<List<SurveyDetails>>{
	
	@Override
	public List<SurveyDetails> extractData(ResultSet rs) throws SQLException, DataAccessException {
		
		Map<String,SurveyDetails> surveyDetailsMap = new LinkedHashMap<>();
		
        while (rs.next()) {
            String surveySubmittedId = rs.getString("surveysubmittedid");
            SurveyDetails surveyDetails = surveyDetailsMap.get(surveySubmittedId);
            
            if(surveyDetails == null) {
            	Long lastModifiedTime = rs.getLong("surveymodifiedtime");
            	if (rs.wasNull()) {
                    lastModifiedTime = null;
                }

                AuditDetails auditdetails = AuditDetails.builder()
                        .createdBy(rs.getString("surveycreatedby"))
                        .createdTime(rs.getLong("surveycreatedtime"))
                        .lastModifiedBy(rs.getString("surveymodifiedby"))
                        .lastModifiedTime(lastModifiedTime)
                        .build();

                surveyDetails =  SurveyDetails.builder()
                        .surveySubmittedId(rs.getString("surveysubmittedid"))
                        .surveyId(rs.getString("surveyid"))
                        .surveyTitle(rs.getString("title"))
                        .surveyNo(rs.getString("surveysubmittedno"))
                        .tenantId(rs.getString("tenantid"))
                        .ward(rs.getString("ward"))
                        .slumCode(rs.getString("slumcode"))
                        .surveyTime(rs.getLong("surveytime"))
                        .auditDetails(auditdetails)
                        .build();
            }
            
            addSubmittedAnswersToSurveyDetails(rs, surveyDetails);
            surveyDetailsMap.put(surveySubmittedId, surveyDetails);
        }
        
        return new ArrayList<>(surveyDetailsMap.values());
	}

	private void addSubmittedAnswersToSurveyDetails(ResultSet rs, SurveyDetails surveyDetails) throws SQLException {
		String answerId = rs.getString("answerid");
		String surveySubmittedId = rs.getString("surveysubmittedid");

        if (answerId == null || surveySubmittedId == null ) {
        	surveyDetails.addQuestionsItem(null);
        	return;
        }
           
        List<SubmittedAnswer> submittedAnswers = surveyDetails.getSubmittedAnswers();
        
        if (!CollectionUtils.isEmpty(submittedAnswers))
            for (SubmittedAnswer answer : submittedAnswers) {
                if (answer.getId().equals(answerId))
                    return;
            }
        
		AuditDetails auditdetails = AuditDetails.builder()
                .createdBy(rs.getString("createdby"))
                .createdTime(rs.getLong("createdtime"))
                .lastModifiedBy(rs.getString("lastmodifiedby"))
                .lastModifiedTime(rs.getLong("lastmodifiedtime"))
                .build();
		
		SubmittedAnswer submittedAnswer = SubmittedAnswer.builder()
				.id(answerId)	
				.surveySubmittedId(surveySubmittedId)
				.questionId(rs.getString("questionid"))
				.questionStatement(rs.getString("questionstatement"))
				.questionStatementOdia(rs.getString("questionstatement_odia"))
				.questionCategory(rs.getString("questioncategory"))
				.answer(SurveyAnswer.fromValue(rs.getString("answer")))
				.hasOpenTicket(rs.getBoolean("hasopenticket"))
				.auditDetails(auditdetails)
				.build();

	
		surveyDetails.addSubmittedAnswer(submittedAnswer);
		
	}

}
