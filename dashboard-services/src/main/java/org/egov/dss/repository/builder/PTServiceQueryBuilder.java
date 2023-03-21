package org.egov.dss.repository.builder;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.egov.dss.model.PropertySerarchCriteria;
import org.springframework.stereotype.Component;

@Component
public class PTServiceQueryBuilder {
	
	public static final String ASSESSED_PROPERTIES_SQL = " select count(distinct propertyid) from eg_pt_asmt_assessment epaa ";
    
	public static String getAccessedPropertiesCountQuery(PropertySerarchCriteria criteria,
			Map<String, Object> preparedStatementValues) {
		StringBuilder selectQuery = new StringBuilder(ASSESSED_PROPERTIES_SQL);
		return addWhereClause(selectQuery, preparedStatementValues, criteria);
	}
	
	
	private static void addClauseIfRequired(Map<String, Object> values, StringBuilder queryString) {
		if (values.isEmpty())
			queryString.append(" WHERE ");
		else {
			queryString.append(" AND");
		}
	}
	
	private static String addWhereClause(StringBuilder selectQuery, Map<String, Object> preparedStatementValues,
			PropertySerarchCriteria searchCriteria) {

		if (StringUtils.isNotBlank(searchCriteria.getTenantId())) {
			addClauseIfRequired(preparedStatementValues, selectQuery);
			if (searchCriteria.getTenantId().split("\\.").length > 1) {
				selectQuery.append(" epaa.tenantId =:tenantId");
				preparedStatementValues.put("tenantId", searchCriteria.getTenantId());
		} else {
				selectQuery.append(" epaa.tenantId LIKE :tenantId");
				preparedStatementValues.put("tenantId", searchCriteria.getTenantId() + "%");
			}

		}

       if (searchCriteria.getFromDate() != null) {
			addClauseIfRequired(preparedStatementValues, selectQuery);
			selectQuery.append(" epaa.createdtime >= :fromDate");
			preparedStatementValues.put("fromDate", searchCriteria.getFromDate());
		}

		if (searchCriteria.getToDate() != null) {
			addClauseIfRequired(preparedStatementValues, selectQuery);
			selectQuery.append(" epaa.createdtime <= :toDate");
			preparedStatementValues.put("toDate", searchCriteria.getToDate());
	    }
		
		if (searchCriteria.getExcludedTenantId() != null) {
			addClauseIfRequired(preparedStatementValues, selectQuery);
			selectQuery.append(" epaa.tenantId <> :excludedTenantId");
			preparedStatementValues.put("excludedTenantId", searchCriteria.getExcludedTenantId());
	    }
		
		return selectQuery.toString();
	
	}
	
}
