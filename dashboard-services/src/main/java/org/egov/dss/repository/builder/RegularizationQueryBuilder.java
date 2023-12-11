package org.egov.dss.repository.builder;

import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.egov.dss.model.BpaSearchCriteria;
import org.egov.dss.model.RegularizationSearchCriteria;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

@Component
public class RegularizationQueryBuilder {
	
	public static final String REGULARIZATION_TOTAL_APPLICATIONS = " select count(ebra.applicationno) from eg_bpa_regularization_application ebra  ";
	public static final String REGULARIZATION_AVG_DAYS = " select avg((approvaldate-applicationdate)/86400000) from eg_bpa_regularization_application ebra  ";
	public static final String REGULARIZATION_MIN_DAYS = " select min((approvaldate-applicationdate)/86400000) from eg_bpa_regularization_application ebra  ";
	public static final String REGULARIZATION_MAX_DAYS = " select max((approvaldate-applicationdate)/86400000) from eg_bpa_regularization_application ebra  ";
	
	private static String addWhereClauseForApplicationReceived(StringBuilder selectQuery, Map<String, Object> preparedStatementValues,
			RegularizationSearchCriteria searchCriteria) {
        

		if (searchCriteria.getTenantIds() != null && !CollectionUtils.isEmpty(searchCriteria.getTenantIds())) {
			addClauseIfRequired(preparedStatementValues, selectQuery);
			selectQuery.append(" ebra.tenantid IN ( :tenantId )");
			preparedStatementValues.put("tenantId",searchCriteria.getTenantIds());
		}

		if (searchCriteria.getFromDate() != null) {
			addClauseIfRequired(preparedStatementValues, selectQuery);
			selectQuery.append(" ebra.applicationdate >= :fromDate");
			preparedStatementValues.put("fromDate", searchCriteria.getFromDate());
		}

		if (searchCriteria.getToDate() != null) {
			addClauseIfRequired(preparedStatementValues, selectQuery);
			selectQuery.append(" ebra.applicationdate <= :toDate");
			preparedStatementValues.put("toDate", searchCriteria.getToDate());
		}
		
		if (searchCriteria.getStatus() != null) {
			addClauseIfRequired(preparedStatementValues, selectQuery);
			selectQuery.append(" ebra.status IN (:status) ");
			preparedStatementValues.put("status", searchCriteria.getStatus());
		}
		
		if (searchCriteria.getSlaThreshold() != null) {
			addClauseIfRequired(preparedStatementValues, selectQuery);
			selectQuery.append(" ebra.lastmodifiedtime - ebra.createdtime < " + searchCriteria.getSlaThreshold());
		}

		if (searchCriteria.getExcludedTenantId() != null) {
			addClauseIfRequired(preparedStatementValues, selectQuery);
			selectQuery.append(" ebra.tenantId != :excludedTenantId");
			preparedStatementValues.put("excludedTenantId", searchCriteria.getExcludedTenantId());
		}
		

		if (searchCriteria.getStatusNotIn() != null) {
			addClauseIfRequired(preparedStatementValues, selectQuery);
			selectQuery.append(" ebra.status NOT IN (:status) ");
			preparedStatementValues.put("status", searchCriteria.getStatusNotIn());
		}
		
		if (searchCriteria.getBusinessServices() != null) {
			addClauseIfRequired(preparedStatementValues, selectQuery);
			selectQuery.append(" ebra.businessservice IN (:businessservice) ");
			preparedStatementValues.put("businessservice", searchCriteria.getBusinessServices());
		}
		

		return selectQuery.toString();

	}
	
	private static void addClauseIfRequired(Map<String, Object> values, StringBuilder queryString) {
		if (values.isEmpty())
			queryString.append(" WHERE ");
		else {
			queryString.append(" AND");
		}
	}
	
	private static String addWhereClause(StringBuilder selectQuery, Map<String, Object> preparedStatementValues,
			RegularizationSearchCriteria searchCriteria) {
        

		if (searchCriteria.getTenantIds() != null && !CollectionUtils.isEmpty(searchCriteria.getTenantIds())) {
			addClauseIfRequired(preparedStatementValues, selectQuery);
			selectQuery.append(" ebra.tenantid IN ( :tenantId )");
			preparedStatementValues.put("tenantId",searchCriteria.getTenantIds());
		}

		if (searchCriteria.getFromDate() != null) {
			addClauseIfRequired(preparedStatementValues, selectQuery);
			selectQuery.append(" ebra.approvaldate >= :fromDate");
			preparedStatementValues.put("fromDate", searchCriteria.getFromDate());
		}

		if (searchCriteria.getToDate() != null) {
			addClauseIfRequired(preparedStatementValues, selectQuery);
			selectQuery.append(" ebra.approvaldate <= :toDate");
			preparedStatementValues.put("toDate", searchCriteria.getToDate());
		}
		
		if (searchCriteria.getStatus() != null) {
			addClauseIfRequired(preparedStatementValues, selectQuery);
			selectQuery.append(" ebra.status IN (:status) ");
			preparedStatementValues.put("status", searchCriteria.getStatus());
		}
		
		if (searchCriteria.getSlaThreshold() != null) {
			addClauseIfRequired(preparedStatementValues, selectQuery);
			selectQuery.append(" ebra.approvaldate - ebra.applicationdate <= " + searchCriteria.getSlaThreshold());
		}

		if (searchCriteria.getExcludedTenantId() != null) {
			addClauseIfRequired(preparedStatementValues, selectQuery);
			selectQuery.append(" ebra.tenantId != :excludedTenantId");
			preparedStatementValues.put("excludedTenantId", searchCriteria.getExcludedTenantId());
		}
		

		if (searchCriteria.getStatusNotIn() != null) {
			addClauseIfRequired(preparedStatementValues, selectQuery);
			selectQuery.append(" ebra.status NOT IN (:status) ");
			preparedStatementValues.put("status", searchCriteria.getStatusNotIn());
		}
		
		if (searchCriteria.getBusinessServices() != null) {
			addClauseIfRequired(preparedStatementValues, selectQuery);
			selectQuery.append(" ebra.businessservice IN (:businessservice) ");
			preparedStatementValues.put("businessservice", searchCriteria.getBusinessServices());
		}
		
		if (searchCriteria.getRiskType() != null) {
			addClauseIfRequired(preparedStatementValues, selectQuery);
			selectQuery.append(" ebe.risktype = :riskType ");
			preparedStatementValues.put("riskType", searchCriteria.getRiskType());
		}
		

		return selectQuery.toString();

	}
	
	private static String addWhereClauseforPendingApplication(StringBuilder selectQuery, Map<String, Object> preparedStatementValues,
			RegularizationSearchCriteria searchCriteria) {
        

		if (searchCriteria.getTenantIds() != null && !CollectionUtils.isEmpty(searchCriteria.getTenantIds())) {
			addClauseIfRequired(preparedStatementValues, selectQuery);
			selectQuery.append(" ebra.tenantid IN ( :tenantId )");
			preparedStatementValues.put("tenantId",searchCriteria.getTenantIds());
		}

		if (searchCriteria.getFromDate() != null) {
			addClauseIfRequired(preparedStatementValues, selectQuery);
			selectQuery.append(" ebra.lastmodifiedtime >= :fromDate");
			preparedStatementValues.put("fromDate", searchCriteria.getFromDate());
		}

		if (searchCriteria.getToDate() != null) {
			addClauseIfRequired(preparedStatementValues, selectQuery);
			selectQuery.append(" ebra.lastmodifiedtime <= :toDate");
			preparedStatementValues.put("toDate", searchCriteria.getToDate());
		}
		
		if (searchCriteria.getStatus() != null) {
			addClauseIfRequired(preparedStatementValues, selectQuery);
			selectQuery.append(" ebra.status IN (:status) ");
			preparedStatementValues.put("status", searchCriteria.getStatus());
		}
		
		if (searchCriteria.getSlaThreshold() != null) {
			addClauseIfRequired(preparedStatementValues, selectQuery);
			selectQuery.append(" ebra.lastmodifiedtime - ebra.createdtime < " + searchCriteria.getSlaThreshold());
		}

		if (searchCriteria.getExcludedTenantId() != null) {
			addClauseIfRequired(preparedStatementValues, selectQuery);
			selectQuery.append(" ebra.tenantId != :excludedTenantId");
			preparedStatementValues.put("excludedTenantId", searchCriteria.getExcludedTenantId());
		}
		

		if (searchCriteria.getStatusNotIn() != null) {
			addClauseIfRequired(preparedStatementValues, selectQuery);
			selectQuery.append(" ebra.status NOT IN (:status) ");
			preparedStatementValues.put("status", searchCriteria.getStatusNotIn());
		}
		
		if (searchCriteria.getDeleteStatus() != null) {
			addClauseIfRequired(preparedStatementValues, selectQuery);
			selectQuery.append(" ebra.status != :deletedStatus");
			preparedStatementValues.put("deletedStatus", searchCriteria.getDeleteStatus());
		}
		
		if (searchCriteria.getBusinessServices() != null) {
			addClauseIfRequired(preparedStatementValues, selectQuery);
			selectQuery.append(" ebra.businessservice IN (:businessservice) ");
			preparedStatementValues.put("businessservice", searchCriteria.getBusinessServices());
		}
		

		return selectQuery.toString();

	}
    
	public String getGeneralQuery(RegularizationSearchCriteria regularizationSearchCriteria, Map<String, Object> preparedStatementValues) {
		
		StringBuilder selectQuery = new StringBuilder(REGULARIZATION_TOTAL_APPLICATIONS);
		addWhereClauseForApplicationReceived(selectQuery, preparedStatementValues, regularizationSearchCriteria);

		return selectQuery.toString();
	}
	
	public static String getTotalRegularizationCertificateIssued(RegularizationSearchCriteria criteria, Map<String, Object> preparedStatementValues) {
		StringBuilder selectQuery = new StringBuilder(REGULARIZATION_TOTAL_APPLICATIONS);
		return addWhereClause(selectQuery, preparedStatementValues, criteria); 
	}
	
	public String getPendingApplicationQuery(RegularizationSearchCriteria regularizationSearchCriteria,
			Map<String, Object> preparedStatementValues) {
		StringBuilder selectQuery = new StringBuilder(REGULARIZATION_TOTAL_APPLICATIONS);
		addWhereClauseforPendingApplication(selectQuery, preparedStatementValues, regularizationSearchCriteria);
		return selectQuery.toString();
	}
	
	public String getAvgDaysToIssueCertificateQuery(RegularizationSearchCriteria regularizationSearchCriteria,
			Map<String, Object> preparedStatementValues) {
		
		StringBuilder selectQuery = new StringBuilder(REGULARIZATION_AVG_DAYS);
		addWhereClause(selectQuery, preparedStatementValues, regularizationSearchCriteria);

		return selectQuery.toString();
	}
	
	public String getMinDaysToIssueCertificateQuery(RegularizationSearchCriteria regularizationSearchCriteria,
			Map<String, Object> preparedStatementValues) {
		
		StringBuilder selectQuery = new StringBuilder(REGULARIZATION_MIN_DAYS);
		addWhereClause(selectQuery, preparedStatementValues, regularizationSearchCriteria);

		return selectQuery.toString();
	}
	
	public String getMaxDaysToIssueCertificateQuery(RegularizationSearchCriteria regularizationSearchCriteria,
			Map<String, Object> preparedStatementValues) {
		
		StringBuilder selectQuery = new StringBuilder(REGULARIZATION_MAX_DAYS);
		addWhereClause(selectQuery, preparedStatementValues, regularizationSearchCriteria);

		return selectQuery.toString();
	}
}

