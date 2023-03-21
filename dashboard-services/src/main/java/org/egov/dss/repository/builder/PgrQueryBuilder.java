package org.egov.dss.repository.builder;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.egov.dss.model.PgrSearchCriteria;
import org.springframework.stereotype.Component;

@Component
public class PgrQueryBuilder {
	
public static final String PGR_TOTAL_APPLICATION = " select count(*) from eg_pgr_service eps ";
	
	public static String getTotalApplication(PgrSearchCriteria criteria, Map<String, Object> preparedStatementValues) {
		StringBuilder selectQuery = new StringBuilder(PGR_TOTAL_APPLICATION);
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
			PgrSearchCriteria searchCriteria) {

		if (StringUtils.isNotBlank(searchCriteria.getTenantId())) {
			addClauseIfRequired(preparedStatementValues, selectQuery);
			if (searchCriteria.getTenantId().split("\\.").length > 1) {
				selectQuery.append(" eps.tenantId =:tenantId");
				preparedStatementValues.put("tenantId", searchCriteria.getTenantId());
			} else {
				selectQuery.append(" eps.tenantId LIKE :tenantId");
				preparedStatementValues.put("tenantId", searchCriteria.getTenantId() + "%");
			}

		}

		if (searchCriteria.getFromDate() != null) {
			addClauseIfRequired(preparedStatementValues, selectQuery);
			selectQuery.append(" eps.createdtime >= :fromDate");
			preparedStatementValues.put("fromDate", searchCriteria.getFromDate());
		}

		if (searchCriteria.getToDate() != null) {
			addClauseIfRequired(preparedStatementValues, selectQuery);
			selectQuery.append(" eps.createdtime <= :toDate");
			preparedStatementValues.put("toDate", searchCriteria.getToDate());
		}

		if (searchCriteria.getExcludedTenantId() != null) {
			addClauseIfRequired(preparedStatementValues, selectQuery);
			selectQuery.append(" eps.tenantId <> :excludedTenantId");
			preparedStatementValues.put("excludedTenantId", searchCriteria.getExcludedTenantId());
		}

		return selectQuery.toString();

	}

}
