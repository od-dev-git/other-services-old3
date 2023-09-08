package org.egov.usm.repository.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.egov.usm.model.enums.Status;
import org.egov.usm.web.model.AuditDetails;
import org.egov.usm.web.model.QuestionDetail;
import org.egov.usm.web.model.Survey;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

@Component
public class SurveyRowMapper implements ResultSetExtractor<List<Survey>> {
	
	@Override
	public List<Survey> extractData(ResultSet rs) throws SQLException, DataAccessException {
		Map<String,Survey> surveyMap = new LinkedHashMap<>();

        while (rs.next()){
            String id = rs.getString("sid");
            Survey survey = surveyMap.get(id);
            
            if(survey == null) {
                Long lastModifiedTime = rs.getLong("slastmodifiedtime");
                if (rs.wasNull()) {
                    lastModifiedTime = null;
                }

                AuditDetails auditdetails = AuditDetails.builder()
                        .createdBy(rs.getString("screatedby"))
                        .createdTime(rs.getLong("screatedtime"))
                        .lastModifiedBy(rs.getString("slastmodifiedby"))
                        .lastModifiedTime(lastModifiedTime)
                        .build();

                survey = Survey.builder()
                        .id(rs.getString("sid"))
                        .tenant(rs.getString("stenantid"))
                        .title(rs.getString("stitle"))
                        .status(Status.fromValue(rs.getString("sstatus")))
                        .description(rs.getString("sdescription"))
                        .startDate(rs.getLong("sstartdate"))
                        .endDate(rs.getLong("senddate"))
                        .collectCitizenInfo(rs.getBoolean("scollectcitizeninfo"))
                        .postedBy(rs.getString("spostedby"))
                        .auditDetails(auditdetails)
                        .build();
            }
            addQuestionsToSurvey(rs, survey);
            surveyMap.put(id, survey);
        }
        return new ArrayList<>(surveyMap.values());
	}

	
	private void addQuestionsToSurvey(ResultSet rs, Survey survey) throws SQLException {
		String questionId = rs.getString("id");
		String surveyId = rs.getString("sid");

        if (questionId == null || surveyId == null ) {
        	survey.addQuestionsItem(null);
        	return;
        }
           
        
        List<QuestionDetail> questions = survey.getQuestionDetails();
        
        if (!CollectionUtils.isEmpty(questions))
            for (QuestionDetail question : questions) {
                if (question.getId().equals(questionId))
                    return;
            }

        AuditDetails auditdetails = AuditDetails.builder()
                .createdBy(rs.getString("createdby"))
                .createdTime(rs.getLong("createdtime"))
                .lastModifiedBy(rs.getString("lastmodifiedby"))
                .lastModifiedTime(rs.getLong("lastmodifiedtime"))
                .build();

        QuestionDetail question =  QuestionDetail.builder()
                .id(rs.getString("id"))
                .surveyId(rs.getString("surveyid"))
                .questionStatement(rs.getString("questionstatement"))
                .category(rs.getString("category"))
                .options(rs.getString("options"))
                .status(Status.fromValue(rs.getString("status")))
                .required(rs.getBoolean("required"))
                .type(rs.getString("type"))
                .questionOrder(rs.getInt("questionorder"))
                .auditDetails(auditdetails)
                .build();

        survey.addQuestionsItem(question);
	}

}
