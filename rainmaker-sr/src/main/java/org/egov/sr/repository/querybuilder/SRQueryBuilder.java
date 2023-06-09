package org.egov.sr.repository.querybuilder;

import java.util.Map;

import org.egov.sr.contract.ServiceReqSearchCriteria;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

@Component
public class SRQueryBuilder {
	
	public static final String INNER_JOIN = " inner join ";
	public static final String SR_APPLICATIONS_QUERY = "select * from eg_pgr_service pgr " + INNER_JOIN
			+ " eg_pgr_address add on add.uuid = pgr.addressid ";
	
	public static String getSRApplications(ServiceReqSearchCriteria criteria, Map<String, Object> preparedStatementValues) {
		StringBuilder selectQuery = new StringBuilder(SR_APPLICATIONS_QUERY);
		return addWhereClause(selectQuery, preparedStatementValues, criteria);
	}


	private static String addWhereClause(StringBuilder selectQuery, Map<String, Object> preparedStatementValues,
			ServiceReqSearchCriteria searchCriteria) {
		if (!StringUtils.isEmpty(searchCriteria.getTenantId())) {
			addClauseIfRequired(preparedStatementValues, selectQuery);
			selectQuery.append(" pgr.tenantid IN ( :tenantId )");
			preparedStatementValues.put("tenantId",searchCriteria.getTenantId());
		}

		if (!StringUtils.isEmpty(searchCriteria.getServiceRequestId())) {
			addClauseIfRequired(preparedStatementValues, selectQuery);
			selectQuery.append(" pgr.servicerequestid in (:serviceRequestId)");
			preparedStatementValues.put("serviceRequestId", searchCriteria.getServiceRequestId());
		}

		if (!StringUtils.isEmpty(searchCriteria.getPhone())) {
			addClauseIfRequired(preparedStatementValues, selectQuery);
			selectQuery.append(" pgr.phone = :phone");
			preparedStatementValues.put("phone", searchCriteria.getPhone());
		}
		
		if (searchCriteria.getStatus() != null) {
			addClauseIfRequired(preparedStatementValues, selectQuery);
			selectQuery.append(" pgr.status IN (:status) ");
			preparedStatementValues.put("status", searchCriteria.getStatus());
		}
		selectQuery.append(" order by servicerequestid ");
		
		return selectQuery.toString();
	}
	
	private static void addClauseIfRequired(Map<String, Object> values, StringBuilder queryString) {
		if (values.isEmpty())
			queryString.append(" WHERE ");
		else {
			queryString.append(" AND");
		}
	}

}
