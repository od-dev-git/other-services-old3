package org.egov.dss.repository.builder;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.egov.dss.model.PropertySerarchCriteria;
import org.springframework.stereotype.Component;

@Component
public class PTServiceQueryBuilder {
	
	public static final String ASSESSED_PROPERTIES_SQL = " select count(distinct propertyid) from eg_pt_asmt_assessment epaa ";
    public static final String TOTAL_PROPERTIES_SQL = " select count(distinct propertyid) from eg_pt_property epaa ";
    public static final String TOTAL_APPLICATIONS_SQL = " select count(id) from eg_pt_property epaa ";
	public static final String ACTIVE_ULBS_SQL = " select count(distinct tenantid) from eg_pt_property epaa  ";
	public static final String TOTAL_PROPERTIES_NEW_SQL = " select count(*) noOfNewProperties  from eg_pt_property epaa ";
	public static final String TOTAL_PROPERTIES_PAID_SQL = " select count(distinct bill.consumercode) from egcl_payment pay inner join egcl_paymentdetail pdtl on pdtl.paymentid = pay.id inner join egcl_bill bill on bill.id=pdtl.billid  ";
	public static final String TOTAL_PROPERTY_ID_SQL = " select count(id) from eg_pt_property epaa";
	public static final String SELECT_SQL = "  select ";
	public static final String TENANTID_SQL = " tenantid ";
	public static final String TOTAL_PROPERTY_ASSESSMENTS_TENANTWISE_SQL = SELECT_SQL + " count(assessmentnumber) as totalAsmt from eg_pt_asmt_assessment epaa ";
	public static final String TOTAL_PROPERTY_NEW_ASSESSMENTS_TENANTWISE_SQL = SELECT_SQL + " count(*) as newAsmt  from eg_pt_property epaa ";
	
	public static String getAccessedPropertiesCountQuery(PropertySerarchCriteria criteria,
			Map<String, Object> preparedStatementValues) {
		StringBuilder selectQuery = new StringBuilder(ASSESSED_PROPERTIES_SQL);
		return addWhereClause(selectQuery, preparedStatementValues, criteria,false);
	}
	
	
	private static void addClauseIfRequired(Map<String, Object> values, StringBuilder queryString) {
		if (values.isEmpty())
			queryString.append(" WHERE ");
		else {
			queryString.append(" AND");
		}
	}
	
	private static String addWhereClause(StringBuilder selectQuery, Map<String, Object> preparedStatementValues,
			PropertySerarchCriteria searchCriteria,boolean isULBPerformance) {

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
			if(isULBPerformance = true) {
				selectQuery.append(" epaa.lastmodifiedtime <= :toDate");
			}else {
				selectQuery.append(" epaa.createdtime <= :toDate");	
			}
			preparedStatementValues.put("toDate", searchCriteria.getToDate());
	    }
		
		if (searchCriteria.getSlaThreshold() != null) {
			addClauseIfRequired(preparedStatementValues, selectQuery);
			selectQuery.append(" epaa.lastmodifiedtime - epaa.createdtime < " + searchCriteria.getSlaThreshold());
		}
		
		if (searchCriteria.getExcludedTenantId() != null) {
			addClauseIfRequired(preparedStatementValues, selectQuery);
			selectQuery.append(" epaa.tenantId != :excludedTenantId");
			preparedStatementValues.put("excludedTenantId", searchCriteria.getExcludedTenantId());
	    }
		
		return selectQuery.toString();
	
	}


	public String getTotalPropertiesCountQuery(PropertySerarchCriteria propertySearchCriteria,
			Map<String, Object> preparedStatementValues) {
		StringBuilder selectQuery = new StringBuilder(TOTAL_PROPERTIES_SQL);
		selectQuery.append(" where epaa.status = :active ");
		preparedStatementValues.put("active","ACTIVE");
		return addWhereClause(selectQuery, preparedStatementValues, propertySearchCriteria,false);
	}
	
	public String getTotalApplicationsCountQuery(PropertySerarchCriteria propertySearchCriteria,
			Map<String, Object> preparedStatementValues) {
		StringBuilder selectQuery = new StringBuilder(TOTAL_APPLICATIONS_SQL);
		return addWhereClause(selectQuery, preparedStatementValues, propertySearchCriteria,false);
	}
	
	public static String getActiveULBsQuery(PropertySerarchCriteria criteria,
			Map<String, Object> preparedStatementValues) {
		StringBuilder selectQuery = new StringBuilder(ACTIVE_ULBS_SQL);
		return addWhereClauseWithLastModifiedTime(selectQuery, preparedStatementValues, criteria);
	}

	public String getTotalPropertiesQuery(PropertySerarchCriteria propertySearchCriteria,
			Map<String, Object> preparedStatementValues) {
		StringBuilder selectQuery = new StringBuilder(TOTAL_PROPERTIES_NEW_SQL);
		addClauseIfRequired(preparedStatementValues, selectQuery);
		selectQuery.append(" creationreason ='CREATE' ");
		preparedStatementValues.put("creationReason", "CREATE");
		return addWhereClause(selectQuery, preparedStatementValues, propertySearchCriteria,false);
	}


	public String getTotalMutationPropertiesCountQuery(PropertySerarchCriteria propertySearchCriteria,
			Map<String, Object> preparedStatementValues) {
		StringBuilder selectQuery = new StringBuilder(TOTAL_PROPERTY_ID_SQL);
		addClauseIfRequired(preparedStatementValues, selectQuery);
		selectQuery.append(" creationreason ='MUTATION' ");
		preparedStatementValues.put("creationReason", "MUTATION");
		return addWhereClauseWithLastModifiedTime(selectQuery, preparedStatementValues, propertySearchCriteria);
	}
	
	public String getTotalPropertiesPaidQuery(PropertySerarchCriteria propertySearchCriteria,
			Map<String, Object> preparedStatementValues) {
		StringBuilder selectQuery = new StringBuilder(TOTAL_PROPERTIES_PAID_SQL);

		addClauseIfRequired(preparedStatementValues, selectQuery);
		selectQuery.append(" pay.paymentstatus != 'CANCELLED' ");
		preparedStatementValues.put("paymentstatus", "CANCELLED");

		if (StringUtils.isNotBlank(propertySearchCriteria.getTenantId())) {
			addClauseIfRequired(preparedStatementValues, selectQuery);
			if (propertySearchCriteria.getTenantId().split("\\.").length > 1) {
				selectQuery.append(" pay.tenantId =:tenantId");
				preparedStatementValues.put("tenantId", propertySearchCriteria.getTenantId());
			} else {
				selectQuery.append(" pay.tenantId LIKE :tenantId");
				preparedStatementValues.put("tenantId", propertySearchCriteria.getTenantId() + "%");
			}

		}

		if (propertySearchCriteria.getFromDate() != null) {
			addClauseIfRequired(preparedStatementValues, selectQuery);
			selectQuery.append(" pay.transactiondate >= :fromDate");
			preparedStatementValues.put("fromDate", propertySearchCriteria.getFromDate());
		}

		if (propertySearchCriteria.getToDate() != null) {
			addClauseIfRequired(preparedStatementValues, selectQuery);
			selectQuery.append(" pay.transactiondate <= :toDate");
			preparedStatementValues.put("toDate", propertySearchCriteria.getToDate());
		}

		if (propertySearchCriteria.getExcludedTenantId() != null) {
			addClauseIfRequired(preparedStatementValues, selectQuery);
			selectQuery.append(" pay.tenantId <> :excludedTenantId");
			preparedStatementValues.put("excludedTenantId", propertySearchCriteria.getExcludedTenantId());
		}

		return selectQuery.toString();
	}
	
	public String getPtTotalAssessmentsCountQuery(PropertySerarchCriteria propertySearchCriteria,
			Map<String, Object> preparedStatementValues) {
		StringBuilder selectQuery = new StringBuilder(TOTAL_PROPERTY_ASSESSMENTS_TENANTWISE_SQL);
		selectQuery.append(" where epaa.status = :active ");
		preparedStatementValues.put("active","ACTIVE");
		return addWhereClause(selectQuery, preparedStatementValues, propertySearchCriteria,true);
	}


	public String getPtTotalNewAssessmentsCountQuery(PropertySerarchCriteria propertySearchCriteria,
			Map<String, Object> preparedStatementValues) {
		StringBuilder selectQuery = new StringBuilder(TOTAL_PROPERTY_NEW_ASSESSMENTS_TENANTWISE_SQL);
		selectQuery.append(" where epaa.status = :active ");
		preparedStatementValues.put("active","ACTIVE");
		selectQuery.append(" and epaa.creationreason ='CREATE' ");
		preparedStatementValues.put("creationReason", "CREATE");
		return addWhereClauseWithLastModifiedTime(selectQuery, preparedStatementValues, propertySearchCriteria);
	}
	
	
	private static String addWhereClauseWithLastModifiedTime(StringBuilder selectQuery,
			Map<String, Object> preparedStatementValues, PropertySerarchCriteria searchCriteria) {

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
			selectQuery.append(" epaa.lastmodifiedtime >= :fromDate");
			preparedStatementValues.put("fromDate", searchCriteria.getFromDate());
		}

		if (searchCriteria.getToDate() != null) {
			addClauseIfRequired(preparedStatementValues, selectQuery);
			selectQuery.append(" epaa.lastmodifiedtime <= :toDate");
			preparedStatementValues.put("toDate", searchCriteria.getToDate());
		}

		if (searchCriteria.getExcludedTenantId() != null) {
			addClauseIfRequired(preparedStatementValues, selectQuery);
			selectQuery.append(" epaa.tenantId <> :excludedTenantId");
			preparedStatementValues.put("excludedTenantId", searchCriteria.getExcludedTenantId());
		}

		return selectQuery.toString();

	}
	
    private static void addGroupByClause(StringBuilder demandQueryBuilder,String columnName) {
        demandQueryBuilder.append(" GROUP BY " + columnName);
    }

    private static void addOrderByClause(StringBuilder demandQueryBuilder,String columnName) {
        demandQueryBuilder.append(" ORDER BY " + columnName);
    }
	
}
