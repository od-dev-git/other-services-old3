package org.egov.dss.repository.builder;

import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.egov.dss.model.BpaSearchCriteria;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

@Component
public class BpaQueryBuilder {
	
public static final String BPA_TOTAL_APPLICATIONS = " select count(bpa.applicationno) from eg_bpa_buildingplan bpa  ";
public static final String BPA_AVG_DAYS = " select avg((approvaldate-applicationdate)/86400000) from eg_bpa_buildingplan bpa  ";
public static final String BPA_MIN_DAYS = " select min((approvaldate-applicationdate)/86400000) from eg_bpa_buildingplan bpa  ";
public static final String BPA_MAX_DAYS = " select max((approvaldate-applicationdate)/86400000) from eg_bpa_buildingplan bpa  ";
public static final String TENANTWISE_PERMITS_ISSUED_LIST_SQL = " select tenantid as name , count(applicationno) as value from eg_bpa_buildingplan bpa  ";
public static final String TOTAL_PERMIITS_ISSUED_VS_TOTAL_OC_ISSUED_VS_TOTAL_OC_SUBMITTED_QUERY = " select to_char(monthYear, 'Mon-YYYY') as name, sum(conCount) over (order by monthYear asc rows between unbounded preceding and current row) as value "
		+ "	from "
		+ "	(select to_date(concat('01-',EXTRACT(MONTH FROM to_timestamp(approvaldate/1000)),'-' ,EXTRACT(YEAR FROM to_timestamp(approvaldate/1000))),'DD-MM-YYYY') monthYear , "
		+ "	count(applicationno) conCount from eg_bpa_buildingplan bpa  ";

public static final String MONTH_YEAR_QUERY = " select to_char(monthYear, 'Mon-YYYY') as name , 0 as value "
		+ "from ( "
		+ "select to_date(concat('01-',EXTRACT(MONTH FROM to_timestamp(lastmodifiedtime/1000)),'-' ,EXTRACT(YEAR FROM to_timestamp(lastmodifiedtime/1000))),'DD-MM-YYYY') monthYear from eg_bpa_buildingplan bpa  ";

public static final String BPA_TENANT_WISE_TOTAL_APPLICATIONS = " select bpa.tenantid as tenantid, count(bpa.applicationno) as totalamt from eg_bpa_buildingplan bpa  ";

public static final String BPA_TENANT_WISE_AVG_DAYS_PERMIT_ISSUED = " select bpa.tenantid as tenantid , avg((bpa.approvaldate-bpa.applicationdate)/86400000) as totalamt from eg_bpa_buildingplan bpa  ";

public static final String BPA_TOTAL_APPLICATION_RECEIVED_BY_SERVICETYPE = " select ebe.servicetype as tenantid, count(bpa.applicationno) as totalamt from eg_bpa_buildingplan bpa "
		                                                                  + " inner join eg_bpa_edcrdata ebe on ebe.applicationno = bpa.applicationno  ";

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

	private static String addWhereClauseforPendingApplication(StringBuilder selectQuery, Map<String, Object> preparedStatementValues,
			BpaSearchCriteria searchCriteria) {
        

		if (searchCriteria.getTenantIds() != null && !CollectionUtils.isEmpty(searchCriteria.getTenantIds())) {
			addClauseIfRequired(preparedStatementValues, selectQuery);
			selectQuery.append(" bpa.tenantid IN ( :tenantId )");
			preparedStatementValues.put("tenantId",searchCriteria.getTenantIds());
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
		
		if (searchCriteria.getDeleteStatus() != null) {
			addClauseIfRequired(preparedStatementValues, selectQuery);
			selectQuery.append(" bpa.status != :deletedStatus");
			preparedStatementValues.put("deletedStatus", searchCriteria.getDeleteStatus());
		}
		
		if (searchCriteria.getBusinessServices() != null) {
			addClauseIfRequired(preparedStatementValues, selectQuery);
			selectQuery.append(" bpa.businessservice IN (:businessservice) ");
			preparedStatementValues.put("businessservice", searchCriteria.getBusinessServices());
		}
		

		return selectQuery.toString();

	}
	
	private static String addWhereClauseForApplicationReceived(StringBuilder selectQuery, Map<String, Object> preparedStatementValues,
			BpaSearchCriteria searchCriteria) {
        

		if (searchCriteria.getTenantIds() != null && !CollectionUtils.isEmpty(searchCriteria.getTenantIds())) {
			addClauseIfRequired(preparedStatementValues, selectQuery);
			selectQuery.append(" bpa.tenantid IN ( :tenantId )");
			preparedStatementValues.put("tenantId",searchCriteria.getTenantIds());
		}

		if (searchCriteria.getFromDate() != null) {
			addClauseIfRequired(preparedStatementValues, selectQuery);
			selectQuery.append(" bpa.applicationdate >= :fromDate");
			preparedStatementValues.put("fromDate", searchCriteria.getFromDate());
		}

		if (searchCriteria.getToDate() != null) {
			addClauseIfRequired(preparedStatementValues, selectQuery);
			selectQuery.append(" bpa.applicationdate <= :toDate");
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
	
	private static String addWhereClause(StringBuilder selectQuery, Map<String, Object> preparedStatementValues,
			BpaSearchCriteria searchCriteria) {
        

		if (searchCriteria.getTenantIds() != null && !CollectionUtils.isEmpty(searchCriteria.getTenantIds())) {
			addClauseIfRequired(preparedStatementValues, selectQuery);
			selectQuery.append(" bpa.tenantid IN ( :tenantId )");
			preparedStatementValues.put("tenantId",searchCriteria.getTenantIds());
		}

		if (searchCriteria.getFromDate() != null) {
			addClauseIfRequired(preparedStatementValues, selectQuery);
			selectQuery.append(" bpa.approvaldate >= :fromDate");
			preparedStatementValues.put("fromDate", searchCriteria.getFromDate());
		}

		if (searchCriteria.getToDate() != null) {
			addClauseIfRequired(preparedStatementValues, selectQuery);
			selectQuery.append(" bpa.approvaldate <= :toDate");
			preparedStatementValues.put("toDate", searchCriteria.getToDate());
		}
		
		if (searchCriteria.getStatus() != null) {
			addClauseIfRequired(preparedStatementValues, selectQuery);
			selectQuery.append(" bpa.status IN (:status) ");
			preparedStatementValues.put("status", searchCriteria.getStatus());
		}
		
		if (searchCriteria.getSlaThreshold() != null) {
			addClauseIfRequired(preparedStatementValues, selectQuery);
			selectQuery.append(" bpa.approvaldate - bpa.applicationdate <= " + searchCriteria.getSlaThreshold());
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
    
	public String getGeneralQuery(BpaSearchCriteria bpaSearchCriteria,
			Map<String, Object> preparedStatementValues) {
		
		StringBuilder selectQuery = new StringBuilder(BPA_TOTAL_APPLICATIONS);
		addWhereClauseForApplicationReceived(selectQuery, preparedStatementValues, bpaSearchCriteria);

		return selectQuery.toString();
	}
	
	public String getPendingApplicationQuery(BpaSearchCriteria bpaSearchCriteria,
			Map<String, Object> preparedStatementValues) {
		StringBuilder selectQuery = new StringBuilder(BPA_TOTAL_APPLICATIONS);
		addWhereClauseforPendingApplication(selectQuery, preparedStatementValues, bpaSearchCriteria);
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
		addWhereClauseForApplicationReceived(selectQuery, preparedStatementValues, bpaSearchCriteria);
		addGroupByClause(selectQuery," tenantid ");
		return selectQuery.toString();
	}

	public String getTotalPermitsIssuedVsTotalOcIssuedVsTotalOcSubmittedQuery(BpaSearchCriteria bpaSearchCriteria,
			Map<String, Object> preparedStatementValues) {
		StringBuilder selectQuery = new StringBuilder(TOTAL_PERMIITS_ISSUED_VS_TOTAL_OC_ISSUED_VS_TOTAL_OC_SUBMITTED_QUERY);
		addWhereClause(selectQuery, preparedStatementValues, bpaSearchCriteria);
		addGroupByClause(selectQuery," to_date(concat('01-',EXTRACT(MONTH FROM to_timestamp(approvaldate/1000)),'-' ,EXTRACT(YEAR FROM to_timestamp(approvaldate/1000))),'DD-MM-YYYY') ");
		addOrderByClause(selectQuery," to_date(concat('01-',EXTRACT(MONTH FROM to_timestamp(approvaldate/1000)),'-' ,EXTRACT(YEAR FROM to_timestamp(approvaldate/1000))),'DD-MM-YYYY') asc) bpa_tmp ");
		return selectQuery.toString();
	}
	
    private static void addOrderByClause(StringBuilder demandQueryBuilder,String columnName) {
        demandQueryBuilder.append(" ORDER BY " + columnName);
    }

	public String getMonthYearDataQuery(BpaSearchCriteria bpaSearchCriteria,
			Map<String, Object> preparedStatementValues) {
		StringBuilder selectQuery = new StringBuilder(MONTH_YEAR_QUERY);
		addWhereClause(selectQuery, preparedStatementValues, bpaSearchCriteria);
		addGroupByClause(selectQuery," to_date(concat('01-',EXTRACT(MONTH FROM to_timestamp(lastmodifiedtime/1000)),'-' ,EXTRACT(YEAR FROM to_timestamp(lastmodifiedtime/1000))),'DD-MM-YYYY') ");
		addOrderByClause(selectQuery," to_date(concat('01-',EXTRACT(MONTH FROM to_timestamp(lastmodifiedtime/1000)),'-' ,EXTRACT(YEAR FROM to_timestamp(lastmodifiedtime/1000))),'DD-MM-YYYY') asc) bpa_tmp ");
		return selectQuery.toString();
	}


	   public String getTenantWiseBpaApplicationQuery(BpaSearchCriteria bpaSearchCriteria,
	            Map<String, Object> preparedStatementValues) {
	        StringBuilder selectQuery = new StringBuilder(BPA_TENANT_WISE_TOTAL_APPLICATIONS);
	        addWhereClauseForApplicationReceived(selectQuery, preparedStatementValues, bpaSearchCriteria);
	        addGroupByClause(selectQuery," bpa.tenantid ");
	        return selectQuery.toString();
	    }
	   
	   public String getTenantWisePermitIssuedQuery(BpaSearchCriteria bpaSearchCriteria,
	            Map<String, Object> preparedStatementValues) {
	        StringBuilder selectQuery = new StringBuilder(BPA_TENANT_WISE_TOTAL_APPLICATIONS);
	        addWhereClause(selectQuery, preparedStatementValues, bpaSearchCriteria);
	        addGroupByClause(selectQuery," bpa.tenantid ");
	        return selectQuery.toString();
	    }
	   
	    
	    public String getTenantWiseAvgPermitIssue(BpaSearchCriteria bpaSearchCriteria,
	            Map<String, Object> preparedStatementValues) {
	        StringBuilder selectQuery = new StringBuilder(BPA_TENANT_WISE_AVG_DAYS_PERMIT_ISSUED);
	        addWhereClause(selectQuery, preparedStatementValues, bpaSearchCriteria);
	        addGroupByClause(selectQuery," bpa.tenantid ");
	        return selectQuery.toString();
	    }
	    
	    public String getTenantWiseBpaPendingApplication(BpaSearchCriteria bpaSearchCriteria,
	            Map<String, Object> preparedStatementValues) {
	        StringBuilder selectQuery = new StringBuilder(BPA_TENANT_WISE_TOTAL_APPLICATIONS);
	        addWhereClauseforPendingApplication(selectQuery, preparedStatementValues, bpaSearchCriteria);
	        addGroupByClause(selectQuery," bpa.tenantid ");
	        return selectQuery.toString();
	    }

	    public String getTotalApplicationByServiceType(BpaSearchCriteria bpaSearchCriteria,
	            Map<String, Object> preparedStatementValues) {
	        StringBuilder selectQuery = new StringBuilder(BPA_TOTAL_APPLICATION_RECEIVED_BY_SERVICETYPE);
	        addWhereClauseForApplicationReceived(selectQuery, preparedStatementValues, bpaSearchCriteria);
	        addGroupByClause(selectQuery," ebe.servicetype ");
	        return selectQuery.toString();
	    }
	    
	    public String getApprovedApplicationByServiceType(BpaSearchCriteria bpaSearchCriteria,
	            Map<String, Object> preparedStatementValues) {
	        StringBuilder selectQuery = new StringBuilder(BPA_TOTAL_APPLICATION_RECEIVED_BY_SERVICETYPE);
	        addWhereClause(selectQuery, preparedStatementValues, bpaSearchCriteria);
	        addGroupByClause(selectQuery," ebe.servicetype ");
	        return selectQuery.toString();
	    }


}
