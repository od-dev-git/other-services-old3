package org.egov.usm.repository.builder;

import java.util.List;

import org.egov.usm.web.model.SurveySearchCriteria;
import org.egov.usm.web.model.TicketSearchCriteria;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

@Component
public class TicketQueryBuilder {
	
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
