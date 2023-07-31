package org.egov.usm.repository.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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
            String surveydetailsId = rs.getString("surveydetailsid");
            SurveyDetails surveyDetails = surveyDetailsMap.get(surveydetailsId);
            
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
                        .id(rs.getString("surveydetailsid"))
                        .surveyId(rs.getString("surveyid"))
                        .surveyNo(rs.getString("surveysubmittedno"))
                        .tenantId(rs.getString("tenantid"))
                        .ward(rs.getString("ward"))
                        .slumCode(rs.getString("slumcode"))
                        .surveyTime(rs.getLong("surveytime"))
                        .auditDetails(auditdetails)
                        .build();
            }
            
            addSubmittedAnswersToSurveyDetails(rs, surveyDetails);
            
            List<SubmittedAnswer> submittedAnswers = surveyDetails.getSubmittedAnswers();
            if (!CollectionUtils.isEmpty(submittedAnswers)) {
            	Collections.sort(surveyDetails.getSubmittedAnswers(),
                        Comparator.comparing(SubmittedAnswer::getId));
            }
            surveyDetailsMap.put(surveydetailsId, surveyDetails);
        }
        
        return new ArrayList<>(surveyDetailsMap.values());
	}

	private void addSubmittedAnswersToSurveyDetails(ResultSet rs, SurveyDetails surveyDetails) throws SQLException {
		String answerId = rs.getString("answerid");
		String surveyDetailsId = rs.getString("surveydetailsid");

        if (answerId == null || surveyDetailsId == null ) {
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
				.surveySubmittedId(surveyDetailsId)
				.questionId(rs.getString("questionid"))
				.questionStatement(rs.getString("questionstatement"))
				.questionCategory(rs.getString("questioncategory"))
				.answer(SurveyAnswer.fromValue(rs.getString("answer")))
				.auditDetails(auditdetails)
				.build();

	
		surveyDetails.addSubmittedAnswer(submittedAnswer);
		
	}

}
