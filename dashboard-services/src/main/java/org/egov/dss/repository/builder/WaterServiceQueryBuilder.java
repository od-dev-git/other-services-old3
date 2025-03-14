package org.egov.dss.repository.builder;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.egov.dss.model.WaterSearchCriteria;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

@Component
public class WaterServiceQueryBuilder {

	public static final String WATER_ACTIVE_CONNECTIONS_COUNT = " select count(*) from eg_ws_connection  conn "
			+ "inner join eg_ws_service ws on ws.connection_id = conn.id ";

	public static final String TOTAL_APPLICATIONS_COUNT = " select count(*) from eg_ws_connection  conn ";

	public static final String CUMULATIVE_CONNECTIONS_SQL = " select to_char(monthYear, 'Mon-YYYY') as name, sum(conCount) over (order by monthYear asc rows between unbounded preceding and current row) as value "
			+ "from "
			+ "(select to_date(concat('01-',EXTRACT(MONTH FROM to_timestamp(ws.connectionExecutionDate/1000)),'-' ,EXTRACT(YEAR FROM to_timestamp(ws.connectionExecutionDate/1000))),'DD-MM-YYYY') monthYear , "
			+ "count(conn.connectionno) conCount "
			+ "from eg_ws_connection conn inner join eg_ws_service ws on ws.connection_id = conn.id  ";

	public static final String WS_CONNECTIONS_BY_USAGE_TYPE = " select ws.usagecategory as name , count(*) as value from eg_ws_connection conn inner join eg_ws_service ws on ws.connection_id = conn.id  ";

	public static final String WS_CONNECTIONS_BY_TYPE = " select ws.connectiontype as name, count(*) as value from eg_ws_connection conn inner join eg_ws_service ws on ws.connection_id = conn.id ";

	public static final String WS_CONNECTIONS_AGEING_QUERY = " select tenantid, sum(pending_from_0_to_3_days) pending_from_0_to_3_days, "
			+ "sum(pending_from_3_to_7_days) pending_from_3_to_7_days, sum(pending_from_7_to_15_days) pending_from_7_to_15_days, sum(pending_from_more_than_15_days) pending_from_more_than_15_days, count(*) as total_pending_applications from "
			+ "(select tenantid, case when lastmodifiedtime - createdtime <= 259200000 then 1 else 0 end as pending_from_0_to_3_days, "
			+ "case when lastmodifiedtime - createdtime > 259200000 and lastmodifiedtime - createdtime <= 604800000 then 1 else 0 end as pending_from_3_to_7_days, "
			+ "case when lastmodifiedtime - createdtime > 604800000 and lastmodifiedtime - createdtime <= 1296000000 then 1 else 0 end as pending_from_7_to_15_days, "
			+ "case when lastmodifiedtime - createdtime > 1296000000 then 1 else 0 end as pending_from_more_than_15_days "
			+ "from eg_ws_connection conn ";
	
	public static final String TOTAL_COMPLETED_APPLICATIONS_COUNT = " select tenantid as name , count(*) as value from eg_ws_connection  conn ";
	
	public static final String WS_STATUS_BY_BOUNDARY = "select tenantid, coalesce(sum(docverifier),0) as pendingatdocverf, "
			+ "coalesce(sum(fi),0) as pendingatfi, "
			+ "coalesce(sum(approval),0) as pendingatapproval, "
			+ "coalesce(sum(payment),0) as pendingatpayment , "
			+ "coalesce(sum(actv),0) as pendingatactv "
			+ "from ( select tenantid, case when applicationstatus='PENDING_FOR_DOCUMENT_VERIFICATION' then 1 end as docverifier, "
			+ "case when applicationstatus='PENDING_FOR_FIELD_INSPECTION' then 1 end as fi, "
			+ "case when applicationstatus='PENDING_FOR_APPROVAL' then 1 end as approval, "
			+ "case when applicationstatus='PENDING_FOR_PAYMENT' then 1 end as payment, "
			+ "case when applicationstatus='PENDING_FOR_CONNECTION_ACTIVATION' then 1 end as actv "
			+ "from eg_ws_connection conn ";
	
	public static String getActiveConnectionCount(WaterSearchCriteria criteria,
			Map<String, Object> preparedStatementValues) {
		StringBuilder selectQuery = new StringBuilder(WATER_ACTIVE_CONNECTIONS_COUNT);
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
			WaterSearchCriteria searchCriteria) {
		
		if (searchCriteria.getTenantIds() != null && !CollectionUtils.isEmpty(searchCriteria.getTenantIds())) {
		    addClauseIfRequired(preparedStatementValues, selectQuery);
		    selectQuery.append(" conn.tenantId in (:tenantId)");
			preparedStatementValues.put("tenantId", searchCriteria.getTenantIds());
		}

		if (searchCriteria.getFromDate() != null) {
			addClauseIfRequired(preparedStatementValues, selectQuery);
			selectQuery.append(" ws.connectionExecutionDate >= :fromDate");
			preparedStatementValues.put("fromDate", searchCriteria.getFromDate());
		}

		if (searchCriteria.getToDate() != null) {
			addClauseIfRequired(preparedStatementValues, selectQuery);
			selectQuery.append(" ws.connectionExecutionDate <= :toDate");
			preparedStatementValues.put("toDate", searchCriteria.getToDate());
		}
		
		if (searchCriteria.getStatus() != null) {
			addClauseIfRequired(preparedStatementValues, selectQuery);
			selectQuery.append(" conn.applicationstatus = :status");
			preparedStatementValues.put("status", searchCriteria.getStatus());
		}
		
		if (searchCriteria.getIsOldApplication() != null) {
			addClauseIfRequired(preparedStatementValues, selectQuery);
			selectQuery.append(" conn.isoldapplication = :isoldapplication");
			preparedStatementValues.put("isoldapplication", searchCriteria.getIsOldApplication());
		}
		
		if (searchCriteria.getSlaThreshold() != null) {
			addClauseIfRequired(preparedStatementValues, selectQuery);
			selectQuery.append(" conn.lastmodifiedtime - conn.createdtime < " + searchCriteria.getSlaThreshold());
			
		}
		
		if (searchCriteria.getExcludedTenantId() != null) {
			addClauseIfRequired(preparedStatementValues, selectQuery);
			selectQuery.append(" conn.tenantId != :excludedTenantId");
			preparedStatementValues.put("excludedTenantId", searchCriteria.getExcludedTenantId());
		}
		
		if (searchCriteria.getConnectionType() != null) {
			addClauseIfRequired(preparedStatementValues, selectQuery);
			selectQuery.append(" ws.connectiontype = :connectiontype");
			preparedStatementValues.put("connectiontype", searchCriteria.getConnectionType());
		}

		if (searchCriteria.getConnectionFacility() != null) {
			addClauseIfRequired(preparedStatementValues, selectQuery);
			selectQuery.append(" ws.connectionfacility = :connectionfacility");
			preparedStatementValues.put("connectionfacility", searchCriteria.getConnectionFacility());
		}
		
		return selectQuery.toString();

	}
	
	public String getTotalActiveXConnectionTypeWaterConnectionsCount(WaterSearchCriteria waterSearchCriteria,
			Map<String, Object> preparedStatementValues) {
		StringBuilder selectQuery = new StringBuilder(WATER_ACTIVE_CONNECTIONS_COUNT);
		return addWhereClause(selectQuery, preparedStatementValues, waterSearchCriteria);
	}

	public String getTotalActiveXConnectionFacilityTypeConnectionsCount(WaterSearchCriteria waterSearchCriteria,
			Map<String, Object> preparedStatementValues) {
		StringBuilder selectQuery = new StringBuilder(WATER_ACTIVE_CONNECTIONS_COUNT);
		return addWhereClause(selectQuery, preparedStatementValues, waterSearchCriteria);
	}

	public String getCumulativeConnectionsQuery(WaterSearchCriteria waterSearchCriteria,
			Map<String, Object> preparedStatementValues) {
		StringBuilder selectQuery = new StringBuilder(CUMULATIVE_CONNECTIONS_SQL);
		addWhereClause(selectQuery, preparedStatementValues, waterSearchCriteria);
		addGroupByClause(selectQuery," to_date(concat('01-',EXTRACT(MONTH FROM to_timestamp(ws.connectionExecutionDate/1000)),'-' ,EXTRACT(YEAR FROM to_timestamp(ws.connectionExecutionDate/1000))),'DD-MM-YYYY') ");
		addOrderByClause(selectQuery," to_date(concat('01-',EXTRACT(MONTH FROM to_timestamp(ws.connectionExecutionDate/1000)),'-' ,EXTRACT(YEAR FROM to_timestamp(ws.connectionExecutionDate/1000))),'DD-MM-YYYY') asc) wsconn ");
		return selectQuery.toString();
	}

    private static void addGroupByClause(StringBuilder demandQueryBuilder,String columnName) {
        demandQueryBuilder.append(" GROUP BY " + columnName);
    }

    private static void addOrderByClause(StringBuilder demandQueryBuilder,String columnName) {
        demandQueryBuilder.append(" ORDER BY " + columnName);
    }

    private static String addWhereClauseWithCreatedTime(StringBuilder selectQuery, Map<String, Object> preparedStatementValues,
			WaterSearchCriteria searchCriteria ) {

    	if (searchCriteria.getTenantIds() != null && !CollectionUtils.isEmpty(searchCriteria.getTenantIds())) {
		    addClauseIfRequired(preparedStatementValues, selectQuery);
		    selectQuery.append(" conn.tenantId in (:tenantId)");
			preparedStatementValues.put("tenantId", searchCriteria.getTenantIds());
		}
    	
		if (searchCriteria.getFromDate() != null) {
			addClauseIfRequired(preparedStatementValues, selectQuery);
			selectQuery.append(" conn.createdtime >= :fromDate");
			preparedStatementValues.put("fromDate", searchCriteria.getFromDate());
		}

		if (searchCriteria.getToDate() != null) {
			addClauseIfRequired(preparedStatementValues, selectQuery);
			selectQuery.append(" conn.createdtime <= :toDate");
			preparedStatementValues.put("toDate", searchCriteria.getToDate());
		}

		if (searchCriteria.getStatus() != null) {
			addClauseIfRequired(preparedStatementValues, selectQuery);
			selectQuery.append(" conn.applicationstatus = :status");
			preparedStatementValues.put("status", searchCriteria.getStatus());
		}

		if (searchCriteria.getIsOldApplication() != null) {
			addClauseIfRequired(preparedStatementValues, selectQuery);
			selectQuery.append(" conn.isoldapplication = :isoldapplication");
			preparedStatementValues.put("isoldapplication", searchCriteria.getIsOldApplication());
		}
		
		if (searchCriteria.getSlaThreshold() != null) {
			addClauseIfRequired(preparedStatementValues, selectQuery);
			selectQuery.append(" conn.lastmodifiedtime - conn.createdtime < " + searchCriteria.getSlaThreshold());
			
		}
		
		if (searchCriteria.getApplicationType() != null) {
			addClauseIfRequired(preparedStatementValues, selectQuery);
			selectQuery.append(" conn.applicationtype in ( :applicationtype) ");
			preparedStatementValues.put("applicationtype", searchCriteria.getApplicationType());
		}
		
		if (searchCriteria.getApplicationStatusExclude() != null) {
			addClauseIfRequired(preparedStatementValues, selectQuery);
			selectQuery.append(" conn.applicationstatus not in ( :applicationStatusNotIn) ");
			preparedStatementValues.put("applicationStatusNotIn", searchCriteria.getApplicationStatusExclude());
		}
		
		if (searchCriteria.getExcludedTenantId() != null) {
			addClauseIfRequired(preparedStatementValues, selectQuery);
			selectQuery.append(" conn.tenantId != :excludedTenantId");
			preparedStatementValues.put("excludedTenantId", searchCriteria.getExcludedTenantId());
		}

		return selectQuery.toString();

	}
    
    public String getwsConnectionsByUsageTypeQuery(WaterSearchCriteria waterSearchCriteria,
			Map<String, Object> preparedStatementValues) {
		StringBuilder selectQuery = new StringBuilder(WS_CONNECTIONS_BY_USAGE_TYPE);
		addWhereClause(selectQuery, preparedStatementValues, waterSearchCriteria);
		selectQuery.append(" group by ws.usagecategory ");
		return selectQuery.toString();
	}

	public String getwsConnectionsByTypeQuery(WaterSearchCriteria waterSearchCriteria,
			Map<String, Object> preparedStatementValues) {
		StringBuilder selectQuery = new StringBuilder(WS_CONNECTIONS_BY_TYPE);
		addWhereClause(selectQuery, preparedStatementValues, waterSearchCriteria);
		selectQuery.append(" group by ws.connectiontype ");
		return selectQuery.toString();
	}

	public String getWSConnectionAgeingQuery(WaterSearchCriteria waterSearchCriteria,
			Map<String, Object> preparedStatementValues) {
		StringBuilder selectQuery = new StringBuilder(WS_CONNECTIONS_AGEING_QUERY);
		addClauseIfRequired(preparedStatementValues, selectQuery);
		selectQuery.append(" conn.tenantId != :excludedTenantId");
		preparedStatementValues.put("excludedTenantId", waterSearchCriteria.getExcludedTenantId());
		
		selectQuery.append(" and conn.createdtime >= :fromdate");
		preparedStatementValues.put("fromdate", waterSearchCriteria.getFromDate());
		
		selectQuery.append(" and conn.lastmodifiedtime <= :todate");
		preparedStatementValues.put("todate", waterSearchCriteria.getToDate());
		selectQuery.append(" and conn.applicationstatus not in ('CONNECTION_ACTIVATED', 'REJECTED', 'CONNECTION_DISCONNECTED','CONNECTION_CLOSED','INITIATED')  ");
		selectQuery.append( " ) tmp ");
		addGroupByClause(selectQuery," tenantid ");
		return selectQuery.toString();
	}

	public String getWsTotalApplicationsCount(WaterSearchCriteria waterSearchCriteria,
			Map<String, Object> preparedStatementValues) {
		StringBuilder selectQuery = new StringBuilder(TOTAL_APPLICATIONS_COUNT);
		return addWhereClauseWithCreatedTime(selectQuery, preparedStatementValues, waterSearchCriteria);
	}

	public String getTenantWiseTotalApplication(WaterSearchCriteria waterSearchCriteria,
			Map<String, Object> preparedStatementValues) {
		StringBuilder selectQuery = new StringBuilder(TOTAL_COMPLETED_APPLICATIONS_COUNT);
		addWhereClauseWithCreatedTime(selectQuery, preparedStatementValues, waterSearchCriteria);
	    addGroupByClause(selectQuery," conn.tenantid ");
		return selectQuery.toString();
	}

	public String getWSStatusByBoundary(WaterSearchCriteria criteria, Map<String, Object> preparedStatementValues) {
		StringBuilder selectQuery = new StringBuilder(WS_STATUS_BY_BOUNDARY);
		addWhereClauseWithCreatedTime(selectQuery, preparedStatementValues, criteria);
		selectQuery.append( " ) wsTmp ");
		addGroupByClause(selectQuery," tenantid ");
		return selectQuery.toString();
	}

}
