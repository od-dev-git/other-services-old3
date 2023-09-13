package org.egov.usm.repository.builder;

import java.util.List;

import javax.validation.Valid;

import org.egov.usm.web.model.USMOfficialSearchCriteria;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

@Component
public class USMOfficcialQueryBuilder {

	private void addClauseIfRequired(StringBuilder query, List<Object> preparedStmtList) {
		if (preparedStmtList.isEmpty()) {
			query.append(" WHERE ");
		} else {
			query.append(" AND ");
		}
	}

	public String getUSMOfficialSearchQuery(@Valid USMOfficialSearchCriteria searchCriteria,
			List<Object> preparedStmtList) {
		StringBuilder query = new StringBuilder("SELECT department.id ,department.role, department.category,department.tenantid,department.ward,department.slumcode,department.assigned,department.createdtime,department.createdby,department.lastmodifiedtime,department.lastmodifiedby FROM eg_usm_dept_mapping department ");

		if(!ObjectUtils.isEmpty(searchCriteria.getTicketId())){
            query.append(" LEFT OUTER JOIN eg_usm_survey_submitted surveysubmitted ON department.tenantid = surveysubmitted.tenantid AND department.ward = surveysubmitted.ward AND department.slumcode = surveysubmitted.slumcode ");
            query.append(" LEFT OUTER JOIN eg_usm_survey_submitted_answer answer ON surveysubmitted.id = answer.surveysubmittedid ");
            query.append(" LEFT OUTER JOIN eg_usm_survey_ticket ticket ON answer.id  = ticket.surveyanswerid ");
        }
		
		if (!ObjectUtils.isEmpty(searchCriteria.getTenantId())) {
			addClauseIfRequired(query, preparedStmtList);
			query.append(" department.tenantid = ? ");
			preparedStmtList.add(searchCriteria.getTenantId());
		}
		
		if (!ObjectUtils.isEmpty(searchCriteria.getWard())) {
			addClauseIfRequired(query, preparedStmtList);
			query.append(" department.ward = ? ");
			preparedStmtList.add(searchCriteria.getWard());
		}
		
		if (!ObjectUtils.isEmpty(searchCriteria.getSlumcode())) {
			addClauseIfRequired(query, preparedStmtList);
			query.append(" department.slumcode = ? ");
			preparedStmtList.add(searchCriteria.getSlumcode());
		}
		
		if (!ObjectUtils.isEmpty(searchCriteria.getRole())) {
			addClauseIfRequired(query, preparedStmtList);
			query.append(" department.role = ? ");
			preparedStmtList.add(searchCriteria.getRole());
		}

		if(!ObjectUtils.isEmpty(searchCriteria.getTicketId())){
            addClauseIfRequired(query, preparedStmtList);
            query.append(" ticket.id = ?");
            preparedStmtList.add(searchCriteria.getTicketId());
        }
		
		query.append(" ORDER BY department.createdtime DESC ");
		return query.toString();
	}

	
	
	public String getUuidOfUSMOfficials(@Valid USMOfficialSearchCriteria searchCriteria, List<Object> preparedStmtList) {
		StringBuilder query = new StringBuilder("SELECT dept.assigned FROM eg_usm_dept_mapping dept "
				+ "JOIN eg_usm_survey_submitted survey ON dept.tenantid = survey.tenantid AND dept.ward = survey.ward AND dept.slumcode = survey.slumcode "
				+ "JOIN eg_usm_survey_submitted_answer answer ON survey.id = answer.surveysubmittedid AND dept.category = answer.questioncategory "
				+ "JOIN eg_usm_survey_ticket ticket ON answer.id = ticket.surveyanswerid ");
		
		if (!ObjectUtils.isEmpty(searchCriteria.getTicketId())) {
			addClauseIfRequired(query, preparedStmtList);
			query.append(" ticket.id = ? ");
			preparedStmtList.add(searchCriteria.getTicketId());
		}
		
		if (!ObjectUtils.isEmpty(searchCriteria.getRole())) {
			addClauseIfRequired(query, preparedStmtList);
			query.append(" dept.role = ? ");
			preparedStmtList.add(searchCriteria.getRole());
		}

		return query.toString();
	}

}
