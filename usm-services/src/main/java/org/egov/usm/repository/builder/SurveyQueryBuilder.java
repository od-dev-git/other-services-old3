package org.egov.usm.repository.builder;

import java.util.List;

import org.egov.usm.web.model.SurveyDetails;
import org.egov.usm.web.model.SurveySearchCriteria;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
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
        return query.toString();
	}

	public String validateSurveyDetailsForCurrentDate(SurveyDetails surveyDetails, List<Object> preparedStmtList) {
		StringBuilder query = new StringBuilder("SELECT question.id, question.surveyid, question.questionstatement, question.category, "
				+ "question.options, answer.answer , question.status, question.required, question.createdby, "
				+ "question.createdtime , question.lastmodifiedtime , question.lastmodifiedby, "
				+ "surveysubmitted.id as surveydetailsid, surveysubmitted.surveysubmittedno , "
				+ "surveysubmitted.tenantid , surveysubmitted.ward , surveysubmitted.slumcode , "
				+ "surveysubmitted.isclosed , surveysubmitted.createdtime as surveycreatedtime, surveysubmitted.createdby as surveycreatedby, "
				+ "surveysubmitted.lastmodifiedtime as surveymodifiedtime, surveysubmitted.lastmodifiedby as surveymodifiedby");
		query.append(", exists(select hasopenticket from eg_usm_slum_question_lookup lookup WHERE question.id = lookup.questionid");

		if (!ObjectUtils.isEmpty(surveyDetails.getTenantId())) {
			query.append(" AND lookup.tenantid = ?");
			preparedStmtList.add(surveyDetails.getTenantId());
		}

		if (!ObjectUtils.isEmpty(surveyDetails.getSlumCode())) {
			query.append(" AND lookup.slumcode = ? ");
			preparedStmtList.add(surveyDetails.getSlumCode());
		}
		query.append(" ) as hasopenticket FROM eg_usm_question question");
		query.append(" LEFT OUTER JOIN eg_usm_survey_submitted_answer answer ON answer.questionid = question.id");
		query.append(" LEFT OUTER JOIN eg_usm_survey_submitted surveysubmitted ON surveysubmitted.id = answer.surveysubmittedid");
		query.append(" WHERE to_timestamp(surveysubmitted.createdtime) :: date = now() :: date");

		if (!ObjectUtils.isEmpty(surveyDetails.getTenantId())) {
			addClauseIfRequired(query, preparedStmtList);
			query.append(" surveysubmitted.tenantid = ?");
			preparedStmtList.add(surveyDetails.getTenantId());
		}

		if (!ObjectUtils.isEmpty(surveyDetails.getSlumCode())) {
			addClauseIfRequired(query, preparedStmtList);
			query.append(" surveysubmitted.slumcode = ?");
			preparedStmtList.add(surveyDetails.getSlumCode());
		}
		log.info("Query for get Questions ", query.toString());
		return query.toString();
	}

	public String getQuestionDetails(SurveyDetails surveyDetails, List<Object> preparedStmtList) {
		StringBuilder query = new StringBuilder("SELECT question.id, question.surveyid, question.questionstatement, question.category, question.options, question.status, question.required, question.createdby, question.createdtime, question.lastmodifiedby, question.lastmodifiedtime");
		query.append(", exists(select hasopenticket from eg_usm_slum_question_lookup lookup WHERE question.id = lookup.questionid");

		if (!ObjectUtils.isEmpty(surveyDetails.getTenantId())) {
			query.append(" AND lookup.tenantid = ?");
			preparedStmtList.add(surveyDetails.getTenantId());
		}

		if (!ObjectUtils.isEmpty(surveyDetails.getSlumCode())) {
			query.append(" AND lookup.slumcode = ?");
			preparedStmtList.add(surveyDetails.getSlumCode());
		}
		
		query.append(") as hasopenticket FROM eg_usm_question question");
		
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

	public String isPresentInQuestionLookup(SurveyDetails surveyDetails, List<Object> preparedStmtList) {
		StringBuilder query = new StringBuilder("SELECT COUNT(*) FROM eg_usm_slum_question_lookup lookup");

		if (!ObjectUtils.isEmpty(surveyDetails.getTenantId())) {
			addClauseIfRequired(query, preparedStmtList);
			query.append(" lookup.tenantid = ?");
			preparedStmtList.add(surveyDetails.getTenantId());
		}

		if (!ObjectUtils.isEmpty(surveyDetails.getSlumCode())) {
			addClauseIfRequired(query, preparedStmtList);
			query.append(" lookup.slumcode = ?");
			preparedStmtList.add(surveyDetails.getSlumCode());
		}
		return query.toString();
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
