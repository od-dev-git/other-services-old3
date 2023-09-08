package org.egov.usm.repository.builder;

import java.util.List;

import org.egov.usm.web.model.SurveyDetails;
import org.egov.usm.web.model.SurveySearchCriteria;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class SurveyQueryBuilder {

	/**
	 * @param searchCriteria
	 * @param preparedStmtList
	 * @return Query String
	 */
	public String getSurveySearchQuery(SurveySearchCriteria searchCriteria, List<Object> preparedStmtList) {
		StringBuilder query = new StringBuilder("SELECT survey.id as sid, survey.tenantid as stenantid, survey.title as stitle, survey.description as sdescription, survey.status as sstatus, survey.startdate as sstartdate, survey.enddate as senddate, survey.collectcitizeninfo as scollectcitizeninfo, survey.postedby as spostedby, survey.createdtime as screatedtime, survey.createdby as screatedby, survey.lastmodifiedtime as slastmodifiedtime, survey.lastmodifiedby as slastmodifiedby, question.id, question.surveyid, question.questionstatement, question.questionorder, question.category, question.options, question.status, question.type, question.required, question.createdby, question.lastmodifiedby, question.createdtime, question.lastmodifiedtime");
        query.append(" FROM eg_usm_survey survey left outer join eg_usm_question question on survey.id = question.surveyid");

        if(!ObjectUtils.isEmpty(searchCriteria.getSurveyId())){
            addClauseIfRequired(query, preparedStmtList);
            query.append(" survey.id = ? " );
            preparedStmtList.add(searchCriteria.getSurveyId());
        }

        if(!ObjectUtils.isEmpty(searchCriteria.getTenant())){
            addClauseIfRequired(query, preparedStmtList);
            query.append(" survey.tenantid = ? ");
            preparedStmtList.add(searchCriteria.getTenant());
        }
        
        if(!ObjectUtils.isEmpty(searchCriteria.getStatus())){
            addClauseIfRequired(query, preparedStmtList);
            query.append(" survey.status = ? ");
            preparedStmtList.add(searchCriteria.getStatus());
        }
        
        query.append(" ORDER BY screatedtime DESC, question.questionorder ASC");
        return query.toString();
	}

	/**
	 * @param surveyDetails
	 * @param preparedStmtList
	 * @return Query String
	 */
	public String getQuestionDetails(SurveyDetails surveyDetails, List<Object> preparedStmtList) {
		StringBuilder query = new StringBuilder("SELECT question.id, question.surveyid, question.questionstatement, question.category, question.options, question.status, question.required, question.type, question.createdby, question.createdtime, question.lastmodifiedby, question.lastmodifiedtime, false as hasopenticket FROM eg_usm_question question");
		
		if (!ObjectUtils.isEmpty(surveyDetails.getSurveyId())) {
			query.append(" WHERE question.surveyid = ?");
			preparedStmtList.add(surveyDetails.getSurveyId());
		}
		
		log.info("Query for get Questions ", query.toString());
		return query.toString();
	}

	
	
	private void addClauseIfRequired(StringBuilder query, List<Object> preparedStmtList) {
		if (preparedStmtList.isEmpty()) {
			query.append(" WHERE ");
		} else {
			query.append(" AND ");
		}
	}

	/**
	 * @param searchCriteria
	 * @param preparedStmtList
	 * @return Query String
	 */
	public String isSurveyExistsQuery(SurveySearchCriteria searchCriteria, List<Object> preparedStmtList) {
		StringBuilder query = new StringBuilder("SELECT DISTINCT(survey.id) FROM eg_usm_survey survey");
       
        if(!ObjectUtils.isEmpty(searchCriteria.getSurveyId())){
            addClauseIfRequired(query, preparedStmtList);
            query.append(" survey.id = ? ");
            preparedStmtList.add(searchCriteria.getSurveyId());
        }
		return query.toString();
	}

}
