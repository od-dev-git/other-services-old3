package org.egov.usm.repository.builder;

import java.util.List;

import org.egov.usm.config.USMConfiguration;
import org.egov.usm.web.model.SurveyDetails;
import org.egov.usm.web.model.SurveyTicket;
import org.egov.usm.web.model.TicketSearchCriteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

@Component
public class TicketQueryBuilder {
	
	@Autowired
	private USMConfiguration config;

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
						+ "ticket.questionid, ticket.ticketdescription, question.questionstatement_odia, ticket.status ,comment.id as commentid,comment.ticketid,comment.comment ,submit.slumcode , submit.ward,"
						+ "ticket.ticketcreatedtime,ticket.ticketclosedtime ,ticket.unattended,"
						+ "ticket.createdtime ,ticket.createdby ,ticket.lastmodifiedtime ,ticket.issatisfied,"
						+ "ticket.lastmodifiedby, ticket.escalatedid, ticket.escalatedtime FROM eg_usm_survey_ticket ticket ");
		query.append(" LEFT OUTER JOIN eg_usm_question question on ticket.questionid =question.id");
		query.append(" LEFT OUTER JOIN eg_usm_survey_submitted_answer answer on ticket.surveyanswerid =answer.id");
		query.append(" LEFT OUTER JOIN eg_usm_survey_submitted submit ON answer.surveysubmittedid =submit.id");
		query.append(" LEFT OUTER JOIN eg_usm_survey_ticket_comment comment ON comment.ticketid=ticket.id");

		if (!ObjectUtils.isEmpty(searchCriteria.getOfficialRole())) {
			query.append(" JOIN eg_usm_dept_mapping dept ON  submit.tenantid = dept.tenantid and submit.ward = dept.ward and submit.slumcode = dept.slumcode and UPPER(answer.questioncategory) = UPPER(dept.category)");
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
			query.append(" UPPER(answer.questioncategory)  = UPPER(?) ");
			preparedStmtList.add(searchCriteria.getCategory().toUpperCase());
		}

		if (!ObjectUtils.isEmpty(searchCriteria.getCreatedBy())) {
			addClauseIfRequired(query, preparedStmtList);
			query.append(" ticket.createdby = ? ");
			preparedStmtList.add(searchCriteria.getCreatedBy());
		}
		
		if (searchCriteria.getIsEscalationOfficer() == Boolean.TRUE && !ObjectUtils.isEmpty(searchCriteria.getTicketDate())) {
			addClauseIfRequired(query, preparedStmtList);
			query.append(" ticket.createdtime >=(? - (" + config.getTicketTimeLimitEscalationOfficer() + ")) and ticket.createdtime <= ?");
			preparedStmtList.add(searchCriteria.getTicketDate());
			preparedStmtList.add(searchCriteria.getTicketDate());
		}
		
		if (searchCriteria.getIsNodalOfficer() == Boolean.TRUE && !ObjectUtils.isEmpty(searchCriteria.getTicketDate())) {
			addClauseIfRequired(query, preparedStmtList);
			query.append(" ticket.createdtime >=(? - (" + config.getTicketTimeLimitNodalOfficer() + ")) and ticket.createdtime <= ?");
			preparedStmtList.add(searchCriteria.getTicketDate());
			preparedStmtList.add(searchCriteria.getTicketDate());
		}
		
		if (!ObjectUtils.isEmpty(searchCriteria.getTicketDate())
				&& ObjectUtils.isEmpty(searchCriteria.getIsEscalationOfficer()) && ObjectUtils.isEmpty(searchCriteria.getIsNodalOfficer())) {
			addClauseIfRequired(query, preparedStmtList);
			query.append(" to_timestamp(ticket.createdtime / 1000) :: date at time zone 'Asia/Kolkata' = to_timestamp(? / 1000) :: date at time zone 'Asia/Kolkata'");
			preparedStmtList.add(searchCriteria.getTicketDate());
		}
		
		if (!ObjectUtils.isEmpty(searchCriteria.getUnAttended())) {
			addClauseIfRequired(query, preparedStmtList);
			query.append(" ticket.unattended = ? ");
			preparedStmtList.add(searchCriteria.getUnAttended());
		}
		
		if (!ObjectUtils.isEmpty(searchCriteria.getIsAutoEscalated())) {
			addClauseIfRequired(query, preparedStmtList);
			if(searchCriteria.getIsAutoEscalated() == Boolean.FALSE) {
				query.append(" ticket.escalatedid IS NULL ");
			} else {
				query.append(" ticket.escalatedid IS NOT NULL ");
			}
		}
		
		if (!ObjectUtils.isEmpty(searchCriteria.getIsSatisfied())) {
			addClauseIfRequired(query, preparedStmtList);
			query.append(" ticket.issatisfied = ? ");
			preparedStmtList.add(searchCriteria.getIsSatisfied());
		}
		
		if (!ObjectUtils.isEmpty(searchCriteria.getOfficialRole()) && !ObjectUtils.isEmpty(searchCriteria.getAssigned())) {
			addClauseIfRequired(query, preparedStmtList);
			query.append("dept.role = ? AND  dept.assigned = ? ");
			preparedStmtList.add(searchCriteria.getOfficialRole());
			preparedStmtList.add(searchCriteria.getAssigned());
		}
		query.append(" ORDER BY ticket.createdtime DESC");

		return query.toString();
	}

	public String getUpdateDailyTicketQuery() {
		StringBuilder query = new StringBuilder("UPDATE public.eg_usm_survey_ticket SET unattended = true, lastmodifiedtime = extract(epoch from now()) * 1000 WHERE status = 'OPEN' ;");
		query.append(" UPDATE eg_usm_slum_question_lookup SET hasopenticket = false, ticketid = '' WHERE hasopenticket = true ");

		return query.toString();
	}

	public String updateAutoEscalatedTickets(SurveyTicket escalatedTicket) {
		StringBuilder query = new StringBuilder("UPDATE public.eg_usm_survey_ticket SET escalatedid = '"+escalatedTicket.getEscalatedId()+"', escalatedtime = "+escalatedTicket.getEscalatedTime()+" WHERE id = '"+escalatedTicket.getId()+"' ;");
		return query.toString();
	}
	

}
