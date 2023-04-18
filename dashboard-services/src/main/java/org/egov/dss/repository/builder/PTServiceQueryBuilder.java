package org.egov.dss.repository.builder;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.egov.dss.model.PropertySerarchCriteria;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

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
	public static final String TOTAL_PROPERTY_ASSESSMENTS_SQL = SELECT_SQL + " count(assessmentnumber) as totalAsmt from eg_pt_asmt_assessment epaa ";
	public static final String TOTAL_PROPERTY_NEW_ASSESSMENTS_SQL = SELECT_SQL + " count(*) as newAsmt  from eg_pt_property epaa ";
	public static final String CUMULATIVE_PROPERTIES_ASSESSED_SQL = " select to_char(monthYear, 'Mon-YYYY') as name, sum(assessedProperties) over (order by monthYear asc rows between unbounded preceding and current row) as value from (select to_date(concat('01-',EXTRACT(MONTH FROM to_timestamp(lastmodifiedtime/1000)),'-' ,EXTRACT(YEAR FROM to_timestamp(lastmodifiedtime/1000))),'DD-MM-YYYY') monthYear ,count(distinct propertyid) assessedProperties from eg_pt_asmt_assessment epaa ";
	public static final String PROPERTIES_BY_USAGETYPE_SQL = " select usagecategory as name , count(*) as value from eg_pt_property epaa";
	public static final String APPLICATIONS_TENANT_WISE_SQL = " select tenantid as name , count(*) as value from eg_pt_property epaa";
	public static final String TOTAL_PROPERTY_ASSESSMENTS_TENANTWISE_SQL = SELECT_SQL + TENANTID_SQL +" as name , count(assessmentnumber) as value from eg_pt_asmt_assessment epaa ";
	public static final String TOTAL_PROPERTY_NEW_ASSESSMENTS_TENANTWISE_SQL = SELECT_SQL + TENANTID_SQL + " as name ,  count(*) as value  from eg_pt_property epaa ";
	public static final String PROPERTIES_BY_FINANCIAL_YEAR_SQL = "    select epaa.tenantid, "
			+ "    case when extract(month from to_timestamp(epaa.createdtime/1000))>3 then concat(extract(year from to_timestamp(epaa.createdtime/1000)),'-',extract(year from to_timestamp(epaa.createdtime/1000))+1) "
			+ "    when extract(month from to_timestamp(epaa.createdtime/1000))<=3 then concat(extract(year from to_timestamp(epaa.createdtime/1000))-1,'-',extract(year from to_timestamp(epaa.createdtime/1000))) "
			+ "    end createdFinYear, count(epaa.propertyid) propertyCount "
			+ "    from eg_pt_property epaa";
	public static final String GROUP_BY_CLAUSE_FOR_PROPERTIES_BY_FINANCIAL_YEAR_SQL = " epaa.tenantid, case when extract(month from to_timestamp(epaa.createdtime/1000))>3 then concat(extract(year from to_timestamp(epaa.createdtime/1000)),'-',extract(year from to_timestamp(epaa.createdtime/1000))+1) "
			+ "    when extract(month from to_timestamp(epaa.createdtime/1000))<=3 then concat(extract(year from to_timestamp(epaa.createdtime/1000))-1,'-',extract(year from to_timestamp(epaa.createdtime/1000))) "
			+ "    end ";
	
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

		if (searchCriteria.getTenantIds() != null && !CollectionUtils.isEmpty(searchCriteria.getTenantIds())) {
			addClauseIfRequired(preparedStatementValues, selectQuery);
			selectQuery.append(" epaa.tenantId in ( :tenantIds )");
			preparedStatementValues.put("tenantIds",searchCriteria.getTenantIds());
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
		
		if (searchCriteria.getStatus() != null) {
			addClauseIfRequired(preparedStatementValues, selectQuery);
			selectQuery.append(" epaa.status = :status");
			preparedStatementValues.put("status", searchCriteria.getStatus());
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
		selectQuery.append(" creationreason not in ('UPDATE','MUTATION') ");
		preparedStatementValues.put("creationReason", "UPDATE");
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

		if (propertySearchCriteria.getTenantIds() != null && !CollectionUtils.isEmpty(propertySearchCriteria.getTenantIds())) {
			addClauseIfRequired(preparedStatementValues, selectQuery);
			selectQuery.append(" pay.tenantId in ( :tenantId )");
			preparedStatementValues.put("tenantId", propertySearchCriteria.getTenantIds());
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
		StringBuilder selectQuery = new StringBuilder(TOTAL_PROPERTY_ASSESSMENTS_SQL);
		selectQuery.append(" where epaa.status = :active ");
		preparedStatementValues.put("active","ACTIVE");
		return addWhereClause(selectQuery, preparedStatementValues, propertySearchCriteria,true);
	}


	public String getPtTotalNewAssessmentsCountQuery(PropertySerarchCriteria propertySearchCriteria,
			Map<String, Object> preparedStatementValues) {
		StringBuilder selectQuery = new StringBuilder(TOTAL_PROPERTY_NEW_ASSESSMENTS_SQL);
		selectQuery.append(" where epaa.status = :active ");
		preparedStatementValues.put("active","ACTIVE");
		selectQuery.append(" and epaa.creationreason ='CREATE' ");
		preparedStatementValues.put("creationReason", "CREATE");
		return addWhereClauseWithLastModifiedTime(selectQuery, preparedStatementValues, propertySearchCriteria);
	}
	
	public String getCumulativePropertiesAssessedQuery(PropertySerarchCriteria propertySearchCriteria,
			Map<String, Object> preparedStatementValues) {
		StringBuilder selectQuery = new StringBuilder(CUMULATIVE_PROPERTIES_ASSESSED_SQL);
		addWhereClauseWithLastModifiedTime(selectQuery, preparedStatementValues, propertySearchCriteria);
		addGroupByClause(selectQuery,"to_date(concat('01-',EXTRACT(MONTH FROM to_timestamp(lastmodifiedtime/1000)),'-' ,EXTRACT(YEAR FROM to_timestamp(lastmodifiedtime/1000))),'DD-MM-YYYY')");
		addOrderByClause(selectQuery,"to_date(concat('01-',EXTRACT(MONTH FROM to_timestamp(lastmodifiedtime/1000)),'-' ,EXTRACT(YEAR FROM to_timestamp(lastmodifiedtime/1000))),'DD-MM-YYYY') ASC) asmt ");
		return selectQuery.toString();
	}
	
	public String getpropertiesByUsageTypeQuery(PropertySerarchCriteria propertySearchCriteria,
			Map<String, Object> preparedStatementValues) {
		StringBuilder selectQuery = new StringBuilder(PROPERTIES_BY_USAGETYPE_SQL);
		addWhereClauseWithLastModifiedTime(selectQuery, preparedStatementValues, propertySearchCriteria);
		selectQuery.append(" group  by usagecategory ");
		return selectQuery.toString();
	}
	
	public String getSlaCompletionCountListQuery(PropertySerarchCriteria propertySearchCriteria,
			Map<String, Object> preparedStatementValues) {
		StringBuilder selectQuery = new StringBuilder(APPLICATIONS_TENANT_WISE_SQL);
		addWhereClause(selectQuery, preparedStatementValues, propertySearchCriteria ,true);
		selectQuery.append(" and status='ACTIVE' ");
		addGroupByClause(selectQuery," tenantid ");
		addOrderByClause(selectQuery," count(*) ASC ");
		return selectQuery.toString();
	}
	
	public String getPtTotalAssessmentsTenantwiseCountQuery(PropertySerarchCriteria propertySearchCriteria,
			Map<String, Object> preparedStatementValues) {
		StringBuilder selectQuery = new StringBuilder(TOTAL_PROPERTY_ASSESSMENTS_TENANTWISE_SQL);
		selectQuery.append(" where epaa.status = :active ");
		preparedStatementValues.put("active","ACTIVE");
		addWhereClause(selectQuery, preparedStatementValues, propertySearchCriteria,true);
		addGroupByClause(selectQuery," tenantid ");
		return selectQuery.toString();
	}
	
	public String getPtTotalNewAssessmentsTenantwiseCount(PropertySerarchCriteria propertySearchCriteria,
			Map<String, Object> preparedStatementValues) {
		StringBuilder selectQuery = new StringBuilder(TOTAL_PROPERTY_NEW_ASSESSMENTS_TENANTWISE_SQL);
		selectQuery.append(" where epaa.status = :active ");
		preparedStatementValues.put("active","ACTIVE");
		addWhereClause(selectQuery, preparedStatementValues, propertySearchCriteria,true);
		selectQuery.append(" and epaa.creationreason = :reason ");
		preparedStatementValues.put("reason", "CREATE");
		addGroupByClause(selectQuery," tenantid ");
		return selectQuery.toString();
	}

	public String getTotalApplicationCompletionCountListQuery(PropertySerarchCriteria propertySearchCriteria,
			Map<String, Object> preparedStatementValues) {
		StringBuilder selectQuery = new StringBuilder(APPLICATIONS_TENANT_WISE_SQL);
		addWhereClause(selectQuery, preparedStatementValues, propertySearchCriteria ,true);
		selectQuery.append(" and status='ACTIVE' ");
		addGroupByClause(selectQuery," tenantid ");
		addOrderByClause(selectQuery," count(*) ASC ");
		return selectQuery.toString();
	}
	
	private static String addWhereClauseWithLastModifiedTime(StringBuilder selectQuery,
			Map<String, Object> preparedStatementValues, PropertySerarchCriteria searchCriteria) {
		
		if (searchCriteria.getTenantIds() != null && !CollectionUtils.isEmpty(searchCriteria.getTenantIds())) {
		    addClauseIfRequired(preparedStatementValues, selectQuery);
		    selectQuery.append(" epaa.tenantId in (:tenantId)");
			preparedStatementValues.put("tenantId", searchCriteria.getTenantIds());
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

	public String getgetPropertiesByFinancialYearListQuery(PropertySerarchCriteria propertySearchCriteria,
			Map<String, Object> preparedStatementValues) {
		StringBuilder selectQuery = new StringBuilder(PROPERTIES_BY_FINANCIAL_YEAR_SQL);
		selectQuery.append(" where epaa.status = :status ");
		preparedStatementValues.put("status","ACTIVE");
		selectQuery.append(" and epaa.creationreason not in ('UPDATE','MUTATION') ");
		preparedStatementValues.put("creationReason", "UPDATE");
		selectQuery.append(" and epaa.tenantid != :tenant ");
		preparedStatementValues.put("tenant", "od.testing");
		addGroupByClause(selectQuery,GROUP_BY_CLAUSE_FOR_PROPERTIES_BY_FINANCIAL_YEAR_SQL);
		return selectQuery.toString();
	}


	public String getTotalApplicationCountListQuery(PropertySerarchCriteria propertySearchCriteria,
			Map<String, Object> preparedStatementValues) {
		StringBuilder selectQuery = new StringBuilder(APPLICATIONS_TENANT_WISE_SQL);
		addWhereClause(selectQuery, preparedStatementValues, propertySearchCriteria ,true);
		addGroupByClause(selectQuery," tenantid ");
		return selectQuery.toString();
	}

}
