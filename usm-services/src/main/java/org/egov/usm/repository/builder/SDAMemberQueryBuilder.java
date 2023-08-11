package org.egov.usm.repository.builder;

import java.util.List;

import org.egov.usm.web.model.MemberSearchCriteria;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

@Component
public class SDAMemberQueryBuilder {
	
	/**
	 * @param searchCriteria
	 * @param preparedStmtList
	 * @return final query String
	 */
	public String getMemberSearchQuery(MemberSearchCriteria searchCriteria, List<Object> preparedStmtList) {
		StringBuilder query = new StringBuilder("SELECT sda.id, sda.userid, sda.tenantid, sda.ward, sda.slumcode, sda.active, sda.createdtime, sda.createdby, sda.lastmodifiedtime, sda.lastmodifiedby FROM eg_usm_sda_mapping sda");

        if(!ObjectUtils.isEmpty(searchCriteria.getId())){
            addClauseIfRequired(query, preparedStmtList);
            query.append(" sda.id = ? " );
            preparedStmtList.add(searchCriteria.getId());
        }

        if(!ObjectUtils.isEmpty(searchCriteria.getTenantId())){
            addClauseIfRequired(query, preparedStmtList);
            query.append(" sda.tenantid = ? ");
            preparedStmtList.add(searchCriteria.getTenantId());
        }
        
        if (!ObjectUtils.isEmpty(searchCriteria.getWard())) {
			addClauseIfRequired(query, preparedStmtList);
			query.append(" sda.ward = ?");
			preparedStmtList.add(searchCriteria.getWard());
		}
        
        if (!ObjectUtils.isEmpty(searchCriteria.getSlumCode())) {
			addClauseIfRequired(query, preparedStmtList);
			query.append(" sda.slumcode = ?");
			preparedStmtList.add(searchCriteria.getSlumCode());
		}
		
        if(!ObjectUtils.isEmpty(searchCriteria.isActive())){
            addClauseIfRequired(query, preparedStmtList);
            query.append(" sda.active = ? ");
            preparedStmtList.add(searchCriteria.isActive());
        }
        
        query.append(" ORDER BY sda.createdtime DESC ");
        return query.toString();
	}
	
	
	private void addClauseIfRequired(StringBuilder query, List<Object> preparedStmtList) {
		if (preparedStmtList.isEmpty()) {
			query.append(" WHERE ");
		} else {
			query.append(" AND ");
		}
	}
	

}
