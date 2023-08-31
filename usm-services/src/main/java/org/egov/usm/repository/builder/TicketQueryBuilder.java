package org.egov.usm.repository.builder;

import java.util.List;

import org.egov.usm.web.model.SurveyDetails;
import org.egov.usm.web.model.TicketSearchCriteria;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

@Component
public class TicketQueryBuilder {

	public String searchQuestionsInTicket(SurveyDetails surveyDetails, List<Object> preparedStmtList) {
		StringBuilder query = new StringBuilder("SELECT ticket.questionid FROM eg_usm_survey_ticket ticket");
		query.append(" LEFT OUTER JOIN eg_usm_survey_submitted_answer answer ON ticket.surveyanswerid = answer.id");
		if (!ObjectUtils.isEmpty(surveyDetails.getSurveySubmittedId())) {
			query.append(" WHERE answer.surveysubmittedid = ?");
			preparedStmtList.add(surveyDetails.getSurveySubmittedId());
		}

		return query.toString();
	}

	private void addClauseIfRequired(StringBuilder query, List<Object> preparedStmtList) {
		if (preparedStmtList.isEmpty()) {
			query.append(" WHERE ");
		} else {
			query.append(" AND ");
		}
	}

	public String getSurveyTicketSearchQuery(TicketSearchCriteria searchCriteria, List<Object> preparedStmtList) {

		StringBuilder query = new StringBuilder(
				"select ticket.id, ticket.tenantid, answer.questioncategory ,  ticket.ticketno , ticket.surveyanswerid, "
						+ "ticket.questionid,ticket.ticketdescription, ticket.status ,submit.slumcode , submit.ward,"
						+ "ticket.ticketcreatedtime,ticket.ticketclosedtime ,ticket.unattended,"
						+ "ticket.createdtime ,ticket.createdby ,ticket.lastmodifiedtime ,ticket.issatisfied,"
						+ "ticket.lastmodifiedby FROM eg_usm_survey_ticket ticket ");
		query.append(" LEFT OUTER JOIN eg_usm_survey_submitted_answer answer on ticket.surveyanswerid =answer.id");
		query.append(" LEFT OUTER JOIN eg_usm_survey_submitted submit ON answer.surveysubmittedid =submit.id");

		if (searchCriteria.getIsOfficial() == Boolean.TRUE) {
			query.append(
					" JOIN eg_usm_dept_mapping dept ON  submit.tenantid = dept.tenantid and submit.ward = dept.ward and submit.slumcode = dept.slumcode and answer.questioncategory = dept.category");

		}

		if (!ObjectUtils.isEmpty(searchCriteria.getTicketId())) {
			addClauseIfRequired(query, preparedStmtList);
			query.append(" ticket.id = ? ");
			preparedStmtList.add(searchCriteria.getTicketId());
		}

		if (!ObjectUtils.isEmpty(searchCriteria.getTenantId())) {
			addClauseIfRequired(query, preparedStmtList);
			query.append(" ticket.tenantid = ? ");
			preparedStmtList.add(searchCriteria.getTenantId());
		}
		if (!ObjectUtils.isEmpty(searchCriteria.getTicketNo())) {
			addClauseIfRequired(query, preparedStmtList);
			query.append(" ticket.ticketno = ? ");
			preparedStmtList.add(searchCriteria.getTicketNo());
		}

		if (!ObjectUtils.isEmpty(searchCriteria.getStatus())) {
			addClauseIfRequired(query, preparedStmtList);
			query.append(" ticket.status = ? ");
			preparedStmtList.add(searchCriteria.getStatus());
		}

		if (!ObjectUtils.isEmpty(searchCriteria.getWard())) {
			addClauseIfRequired(query, preparedStmtList);
			query.append(" submit.ward = ? ");
			preparedStmtList.add(searchCriteria.getWard());
		}
		if (!ObjectUtils.isEmpty(searchCriteria.getSlumCode())) {
			addClauseIfRequired(query, preparedStmtList);
			query.append(" submit.slumcode = ? ");
			preparedStmtList.add(searchCriteria.getSlumCode());
		}

		if (!ObjectUtils.isEmpty(searchCriteria.getCategory())) {
			addClauseIfRequired(query, preparedStmtList);
			query.append(" answer.questioncategory  = ? ");
			preparedStmtList.add(searchCriteria.getCategory());
		}

		if (!ObjectUtils.isEmpty(searchCriteria.getCreatedBy())) {
			addClauseIfRequired(query, preparedStmtList);
			query.append(" ticket.createdby = ? ");
			preparedStmtList.add(searchCriteria.getCreatedBy());
		}
		if (!ObjectUtils.isEmpty(searchCriteria.getTicketDate())) {
			addClauseIfRequired(query, preparedStmtList);
			query.append(" to_timestamp(ticket.createdtime / 1000) :: date = to_timestamp(? / 1000) :: date");
			preparedStmtList.add(searchCriteria.getTicketDate());
		}
		if (!ObjectUtils.isEmpty(searchCriteria.getUnAttended())) {
			addClauseIfRequired(query, preparedStmtList);
			query.append(" ticket.unattended = ? ");
			preparedStmtList.add(searchCriteria.getUnAttended());
		}
		query.append(" ORDER BY ticket.createdtime  ");

		return query.toString();
	}

}
