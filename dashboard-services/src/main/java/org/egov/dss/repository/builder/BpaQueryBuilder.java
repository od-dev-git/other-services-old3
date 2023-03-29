package org.egov.dss.repository.builder;

import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.egov.dss.model.BpaSearchCriteria;
import org.springframework.stereotype.Component;

@Component
public class BpaQueryBuilder {
	
public static final String BPA_TOTAL_APPLICATIONS = " select count(bpa.applicationno) from eg_bpa_buildingplan bpa  ";
public static final String BPA_AVG_DAYS = " select avg((lastmodifiedtime-createdtime)/86400000) from eg_bpa_buildingplan bpa  ";
public static final String BPA_MIN_DAYS = " select min((lastmodifiedtime-createdtime)/86400000) from eg_bpa_buildingplan bpa  ";
public static final String BPA_MAX_DAYS = " select max((lastmodifiedtime-createdtime)/86400000) from eg_bpa_buildingplan bpa  ";
public static final String TENANTWISE_PERMITS_ISSUED_LIST_SQL = " select tenantid as name , count(applicationno) as value from eg_bpa_buildingplan bpa  ";


	
	public static String getTotalPermitIssued(BpaSearchCriteria criteria, Map<String, Object> preparedStatementValues) {
		StringBuilder selectQuery = new StringBuilder(BPA_TOTAL_APPLICATIONS);
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
			BpaSearchCriteria searchCriteria) {

		if (StringUtils.isNotBlank(searchCriteria.getTenantId())) {
			addClauseIfRequired(preparedStatementValues, selectQuery);
			if (searchCriteria.getTenantId().split("\\.").length > 1) {
				selectQuery.append(" bpa.tenantId =:tenantId");
				preparedStatementValues.put("tenantId", searchCriteria.getTenantId());
			} else {
				selectQuery.append(" bpa.tenantId LIKE :tenantId");
				preparedStatementValues.put("tenantId", searchCriteria.getTenantId() + "%");
			}

		}

		if (searchCriteria.getFromDate() != null) {
			addClauseIfRequired(preparedStatementValues, selectQuery);
			selectQuery.append(" bpa.lastmodifiedtime >= :fromDate");
			preparedStatementValues.put("fromDate", searchCriteria.getFromDate());
		}

		if (searchCriteria.getToDate() != null) {
			addClauseIfRequired(preparedStatementValues, selectQuery);
			selectQuery.append(" bpa.lastmodifiedtime <= :toDate");
			preparedStatementValues.put("toDate", searchCriteria.getToDate());
		}
		
		if (searchCriteria.getStatus() != null) {
			addClauseIfRequired(preparedStatementValues, selectQuery);
			selectQuery.append(" bpa.status IN (:status) ");
			preparedStatementValues.put("status", searchCriteria.getStatus());
		}
		
		if (searchCriteria.getSlaThreshold() != null) {
			addClauseIfRequired(preparedStatementValues, selectQuery);
			selectQuery.append(" bpa.lastmodifiedtime - bpa.createdtime < " + searchCriteria.getSlaThreshold());
		}

		if (searchCriteria.getExcludedTenantId() != null) {
			addClauseIfRequired(preparedStatementValues, selectQuery);
			selectQuery.append(" bpa.tenantId != :excludedTenantId");
			preparedStatementValues.put("excludedTenantId", searchCriteria.getExcludedTenantId());
		}
		

		if (searchCriteria.getStatusNotIn() != null) {
			addClauseIfRequired(preparedStatementValues, selectQuery);
			selectQuery.append(" bpa.status NOT IN (:status) ");
			preparedStatementValues.put("status", searchCriteria.getStatusNotIn());
		}
		
		if (searchCriteria.getBusinessServices() != null) {
			addClauseIfRequired(preparedStatementValues, selectQuery);
			selectQuery.append(" bpa.businessservice IN (:businessservice) ");
			preparedStatementValues.put("businessservice", searchCriteria.getBusinessServices());
		}
		

		return selectQuery.toString();

	}
	
    private static String getForINClause(Set<String> listSet) {

        StringBuilder query = new StringBuilder();
        if (!listSet.isEmpty()) {

            String[] list = listSet.toArray(new String[listSet.size()]);
            query.append("'"+list[0]+"'");
            for (int i = 1; i < listSet.size(); i++) {
                query.append("," + "'"+list[i]+"'");
            }
        }
        return query.append(")").toString();
    }
    
    private static void addAndClause(StringBuilder queryString) {
        queryString.append(" AND ");
    }

	public String getGeneralQuery(BpaSearchCriteria bpaSearchCriteria,
			Map<String, Object> preparedStatementValues) {
		
		StringBuilder selectQuery = new StringBuilder(BPA_TOTAL_APPLICATIONS);
		addWhereClause(selectQuery, preparedStatementValues, bpaSearchCriteria);

		return selectQuery.toString();
	}

	public String getMaxDaysToIssuePermitQuery(BpaSearchCriteria bpaSearchCriteria,
			Map<String, Object> preparedStatementValues) {
		
		StringBuilder selectQuery = new StringBuilder(BPA_MAX_DAYS);
		addWhereClause(selectQuery, preparedStatementValues, bpaSearchCriteria);

		return selectQuery.toString();
	}

	public String getMinDaysToIssuePermitQuery(BpaSearchCriteria bpaSearchCriteria,
			Map<String, Object> preparedStatementValues) {
		
		StringBuilder selectQuery = new StringBuilder(BPA_MIN_DAYS);
		addWhereClause(selectQuery, preparedStatementValues, bpaSearchCriteria);

		return selectQuery.toString();
	}

	public String getAvgDaysToIssuePermitQuery(BpaSearchCriteria bpaSearchCriteria,
			Map<String, Object> preparedStatementValues) {
		
		StringBuilder selectQuery = new StringBuilder(BPA_AVG_DAYS);
		addWhereClause(selectQuery, preparedStatementValues, bpaSearchCriteria);

		return selectQuery.toString();
	}

	public String getSLAComplianceGeneralQuery(BpaSearchCriteria bpaSearchCriteria,
			Map<String, Object> preparedStatementValues) {
		
		StringBuilder selectQuery = new StringBuilder(BPA_TOTAL_APPLICATIONS);
		addWhereClause(selectQuery, preparedStatementValues, bpaSearchCriteria);

		return selectQuery.toString();
	}
	
    private static void addGroupByClause(StringBuilder demandQueryBuilder,String columnName) {
        demandQueryBuilder.append(" GROUP BY " + columnName);
    }

	public String getTenantWisePermitsIssuedListQuery(BpaSearchCriteria bpaSearchCriteria,
			Map<String, Object> preparedStatementValues) {
		StringBuilder selectQuery = new StringBuilder(TENANTWISE_PERMITS_ISSUED_LIST_SQL);
		addWhereClause(selectQuery, preparedStatementValues, bpaSearchCriteria);
		addGroupByClause(selectQuery," tenantid ");
		return selectQuery.toString();
	}

	public String getTenantWiseApplicationsReceivedListQuery(BpaSearchCriteria bpaSearchCriteria,
			Map<String, Object> preparedStatementValues) {
		StringBuilder selectQuery = new StringBuilder(TENANTWISE_PERMITS_ISSUED_LIST_SQL);
		addWhereClause(selectQuery, preparedStatementValues, bpaSearchCriteria);
		addGroupByClause(selectQuery," tenantid ");
		return selectQuery.toString();
	}


}
