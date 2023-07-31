package org.egov.usm.repository.builder;

import java.util.List;

import javax.validation.Valid;

import org.egov.usm.web.model.SurveyDetails;
import org.egov.usm.web.model.SurveySearchCriteria;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class SurveyResponseQueryBuilder {
	
	public String validateSurveyDetailsForCurrentDate(SurveyDetails surveyDetails, List<Object> preparedStmtList) {
		StringBuilder query = new StringBuilder("SELECT question.id, question.surveyid, question.questionstatement, question.category, "
				+ "question.options, answer.id as answerid, answer.answer , question.status, question.required, question.type, question.createdby, "
				+ "question.createdtime , question.lastmodifiedtime , question.lastmodifiedby, "
				+ "surveysubmitted.id as surveydetailsid, surveysubmitted.surveysubmittedno , "
				+ "surveysubmitted.tenantid , surveysubmitted.ward , surveysubmitted.slumcode , "
				+ "surveysubmitted.createdtime as surveycreatedtime, surveysubmitted.createdby as surveycreatedby, "
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
		query.append(" WHERE to_timestamp(surveysubmitted.createdtime/1000) :: date = now() :: date");

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
		
		if (!ObjectUtils.isEmpty(surveyDetails.getSurveyId())) {
			addClauseIfRequired(query, preparedStmtList);
			query.append(" question.surveyid = ?");
			preparedStmtList.add(surveyDetails.getSurveyId());
		}
		log.info("Query for get Questions ", query.toString());
		return query.toString();
	}
	


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


	public String searchQuestionInLookup(SurveyDetails surveyDetails, List<Object> preparedStmtList) {
		StringBuilder query = new StringBuilder("SELECT lookup.questionId FROM eg_usm_slum_question_lookup lookup");

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

	public String isSurveyExistsForToday(@Valid SurveySearchCriteria criteria, List<Object> preparedStmtList) {
		StringBuilder query = new StringBuilder("SELECT COUNT(*) FROM eg_usm_survey_submitted survey");
		query.append(" WHERE to_timestamp(survey.createdtime / 1000) :: date = now() :: date");
		if (!ObjectUtils.isEmpty(criteria.getSurveyId())) {
			query.append(" AND survey.id = ?");
			preparedStmtList.add(criteria.getSurveyId());
		}
		
		return query.toString();
	}



	public String searchSubmittedSurvey(@Valid SurveySearchCriteria searchCriteria, List<Object> preparedStmtList) {
		StringBuilder query = new StringBuilder("SELECT surveysubmitted.id as surveydetailsid, surveysubmitted.surveyid, surveysubmitted.surveysubmittedno , "
				+ "surveysubmitted.tenantid , surveysubmitted.ward , surveysubmitted.slumcode ,surveysubmitted.surveytime, surveysubmitted.createdtime as surveycreatedtime, "
				+ "surveysubmitted.createdby as surveycreatedby, surveysubmitted.lastmodifiedtime as surveymodifiedtime, surveysubmitted.lastmodifiedby as surveymodifiedby, "
				+ "answer.id as answerid, answer.questionid, question.questionstatement, answer.questioncategory, answer.answer, answer.createdtime, answer.createdby, "
				+ "answer.lastmodifiedtime, answer.lastmodifiedby from eg_usm_survey_submitted surveysubmitted");
		query.append(" LEFT OUTER JOIN eg_usm_survey_submitted_answer answer ON surveysubmitted.id = answer.surveysubmittedid");
		query.append(" LEFT OUTER JOIN eg_usm_question question ON answer.questionid = question.id");

		if (!ObjectUtils.isEmpty(searchCriteria.getSurveyId())) {
			addClauseIfRequired(query, preparedStmtList);
			query.append(" surveysubmitted.id = ?");
			preparedStmtList.add(searchCriteria.getSurveyId());
		}
		
		if (!ObjectUtils.isEmpty(searchCriteria.getTenantId())) {
			addClauseIfRequired(query, preparedStmtList);
			query.append(" surveysubmitted.tenantid = ?");
			preparedStmtList.add(searchCriteria.getTenantId());
		}

		if (!ObjectUtils.isEmpty(searchCriteria.getSlumCode())) {
			addClauseIfRequired(query, preparedStmtList);
			query.append(" surveysubmitted.slumcode = ?");
			preparedStmtList.add(searchCriteria.getSlumCode());
		}
		
		if (!ObjectUtils.isEmpty(searchCriteria.getWard())) {
			addClauseIfRequired(query, preparedStmtList);
			query.append(" surveysubmitted.ward = ?");
			preparedStmtList.add(searchCriteria.getWard());
		}
		
		if (!ObjectUtils.isEmpty(searchCriteria.getSurveyNo())) {
			addClauseIfRequired(query, preparedStmtList);
			query.append(" surveysubmitted.surveysubmittedno = ?");
			preparedStmtList.add(searchCriteria.getSurveyNo());
		}
		
		if (!ObjectUtils.isEmpty(searchCriteria.getSurveyDate())) {
			addClauseIfRequired(query, preparedStmtList);
			query.append(" to_timestamp(surveysubmitted.createdtime / 1000) :: date = to_timestamp(? / 1000) :: date");
			preparedStmtList.add(searchCriteria.getSurveyDate());
		}
		
		query.append(" ORDER BY surveycreatedtime DESC ");
		log.info("Query for get Questions ", query.toString());
		return query.toString();
	}

}
