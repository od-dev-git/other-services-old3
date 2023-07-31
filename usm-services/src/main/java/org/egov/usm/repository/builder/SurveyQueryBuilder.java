package org.egov.usm.repository.builder;

import java.util.List;

import org.egov.usm.web.model.SurveySearchCriteria;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class SurveyQueryBuilder {

	public String getSurveySearchQuery(SurveySearchCriteria searchCriteria, List<Object> preparedStmtList) {
		StringBuilder query = new StringBuilder("SELECT survey.id as sid, survey.tenantid as stenantid, survey.title as stitle, survey.description as sdescription, survey.status as sstatus, survey.startdate as sstartdate, survey.enddate as senddate, survey.collectcitizeninfo as scollectcitizeninfo, survey.postedby as spostedby, survey.createdtime as screatedtime, survey.createdby as screatedby, survey.lastmodifiedtime as slastmodifiedtime, survey.lastmodifiedby as slastmodifiedby, question.id, question.surveyid, question.questionstatement, question.category, question.options, question.status, question.type, question.required, question.createdby, question.lastmodifiedby, question.createdtime, question.lastmodifiedtime");
        query.append(" FROM eg_usm_survey survey left outer join eg_usm_question question on survey.id = question.surveyid");

        if(!ObjectUtils.isEmpty(searchCriteria.getSurveyId())){
            addClauseIfRequired(query, preparedStmtList);
            query.append(" survey.id = ? " );
            preparedStmtList.add(searchCriteria.getSurveyId());
        }

        if(!ObjectUtils.isEmpty(searchCriteria.getTenantId())){
            addClauseIfRequired(query, preparedStmtList);
            query.append(" survey.tenantid = ? ");
            preparedStmtList.add(searchCriteria.getTenantId());
        }
        
        if(!ObjectUtils.isEmpty(searchCriteria.getStatus())){
            addClauseIfRequired(query, preparedStmtList);
            query.append(" survey.status = ? ");
            preparedStmtList.add(searchCriteria.getStatus());
        }
        query.append(" ORDER BY screatedtime DESC ");
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
