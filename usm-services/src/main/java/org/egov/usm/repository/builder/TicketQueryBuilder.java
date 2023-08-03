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
		StringBuilder query = new StringBuilder("select ticket.id ,ticket.tenantid ,ticket.ticketno ,ticket.surveyanswerid ,ticket.questionid,ticket.ticketdescription, ticket.status ,ticket.ticketcreatedtime,ticket.ticketclosedtime ,ticket.createdtime ,ticket.createdby ,ticket.lastmodifiedtime ,ticket.lastmodifiedby   from eg_usm_survey_ticket ticket");  

        if(!ObjectUtils.isEmpty(searchCriteria.getTicketId())){
            addClauseIfRequired(query, preparedStmtList);
            query.append(" ticket.id = ? " );
            preparedStmtList.add(searchCriteria.getTicketId());
        }
        
        if(!ObjectUtils.isEmpty(searchCriteria.getTenantId())){
            addClauseIfRequired(query, preparedStmtList);
            query.append(" ticket.tenantid = ? " );
            preparedStmtList.add(searchCriteria.getTenantId());
        }
        if(!ObjectUtils.isEmpty(searchCriteria.getTicketNo())){
            addClauseIfRequired(query, preparedStmtList);
            query.append(" ticket.ticketno = ? " );
            preparedStmtList.add(searchCriteria.getTicketNo());
        }
        
        
        if(!ObjectUtils.isEmpty(searchCriteria.getStatus())){
            addClauseIfRequired(query, preparedStmtList);
            query.append(" ticket.status = ? " );
            preparedStmtList.add(searchCriteria.getStatus());
        }
        
      
        return query.toString();
	}
	
	
	

	
	
	
	
}
