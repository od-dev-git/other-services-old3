package org.egov.dss.repository.builder;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.egov.dss.config.ConfigurationLoader;
import org.egov.dss.model.PgrSearchCriteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PgrQueryBuilder {
	
	@Autowired
	private ConfigurationLoader configurationLoader;
	
	public static final String PGR_TOTAL_APPLICATION = " select count(servicerequestid) from eg_pgr_service eps ";

	public static final String PGR_TOP_COMPLAINTS = " select servicecode as name , count(servicerequestid) as value from eg_pgr_service eps ";

	public static final String TENANT_WISE_APPLICATIONS = " select tenantid, count(servicerequestid) as connections from eg_pgr_service eps ";
	
	public static final String CUMULATIVE_COMPLAINTS = "select to_char(monthYear, 'Mon-YYYY') as name, sum(closedComplaints) over (order by monthYear asc rows between unbounded preceding and current row) as value "
			+ "from "
			+ "(select to_date(concat('01-',EXTRACT(MONTH FROM to_timestamp(lastmodifiedtime/1000)),'-' ,EXTRACT(YEAR FROM to_timestamp(lastmodifiedtime/1000))),'DD-MM-YYYY') monthYear , "
			+ "count(servicerequestid) closedComplaints from eg_pgr_service eps";
	
	public static final String MONTH_YEAR_QUERY = " select to_char(monthYear, 'Mon-YYYY') as name , 0 as value "
			+ "from ( "
			+ "select to_date(concat('01-',EXTRACT(MONTH FROM to_timestamp(lastmodifiedtime/1000)),'-' ,EXTRACT(YEAR FROM to_timestamp(lastmodifiedtime/1000))),'DD-MM-YYYY') monthYear from eg_ws_connection eps  ";
	
	public static final String PGR_COMPLAINTS_BY_STATUS = " select status as name, count(servicerequestid) as value from eg_pgr_service eps ";
	public static final String PGR_COMPLAINTS_BY_DEPARTMENT = " select servicecode as name, count(servicerequestid) as value from eg_pgr_service eps ";
	public static final String PGR_COMPLAINTS_BY_CHANNEL = " select source as name, count(servicerequestid) as value from eg_pgr_service eps ";
	public static final String PGR_EVENT_DURATION_GRAPH = " select to_char(monthYear, 'Mon-YYYY') as name, "
			+ "avg(procesTime) as value "
			+ "from ( "
			+ "select to_date(concat('01-',EXTRACT(MONTH FROM to_timestamp(lastmodifiedtime/1000)),'-' ,EXTRACT(YEAR FROM to_timestamp(lastmodifiedtime/1000))),'DD-MM-YYYY') monthYear, "
			+ "(slaendtime-createdtime)/3600000 as procesTime "
			+ "from eg_pgr_service eps ";

	public static final String PGR_UNIQUE_CITIZENS = " select to_char(to_date(concat('01-',EXTRACT(MONTH FROM to_timestamp(eps.lastmodifiedtime/1000)),'-' ,EXTRACT(YEAR FROM to_timestamp(eps.lastmodifiedtime/1000))),'DD-MM-YYYY'), 'Mon-YYYY') as name, "
			+ "count(distinct eps.accountid) as value from eg_pgr_service eps ";


	public static final String PGR_MONTH_YEAR = " select to_char(monthYear, 'Mon-YYYY') as name ";
	public static final String PGR_CLOSED_STATUS_COUNT = " coalesce(sum(closed),0) as value ";
	public static final String PGR_OPEN_STATUS_COUNT = " coalesce(sum(open),0) as value ";
	public static final String PGR_TOTAL_COMPLAINTS_BY_STATUS = " from  "
			+ "(select to_date(concat('01-',EXTRACT(MONTH FROM to_timestamp(lastmodifiedtime/1000)),'-' ,EXTRACT(YEAR FROM to_timestamp(lastmodifiedtime/1000))),'DD-MM-YYYY') monthYear, "
			+ "case when status in ('closed','resolved','rejected') then 1 end as closed, "
			+ "case when status not in ('closed','resolved','rejected') then 1 end as open "
			+ "from eg_pgr_service eps ";
	public static final String PGR_CLOSED_STATUS_MONTHWISE_COUNT = PGR_MONTH_YEAR +" , "+PGR_CLOSED_STATUS_COUNT+PGR_TOTAL_COMPLAINTS_BY_STATUS;
	public static final String PGR_OPEN_STATUS_MONTHWISE_COUNT = PGR_MONTH_YEAR +" , "+PGR_OPEN_STATUS_COUNT+PGR_TOTAL_COMPLAINTS_BY_STATUS;
	
	public static final String PGR_WHATSAPP_COUNT = " coalesce(sum(whatsapp),0) as value ";
	public static final String PGR_IVR_COUNT = " coalesce(sum(ivr),0) as value ";
	public static final String PGR_WEB_COUNT = " coalesce(sum(web),0) as value ";
	public static final String PGR_MOBILEAPP_COUNT = " coalesce(sum(mobileapp),0) as value ";
	public static final String PGR_TOTAL_COMPLAINTS_BY_SOURCE = " from "
			+ "(select to_date(concat('01-',EXTRACT(MONTH FROM to_timestamp(eps.lastmodifiedtime/1000)),'-' ,EXTRACT(YEAR FROM to_timestamp(eps.lastmodifiedtime/1000))),'DD-MM-YYYY') monthYear, "
			+ "case when  source ='mobileapp' then 1 end as mobileapp, case when  source ='ivr' then 1 end as ivr, "
			+ "case when  source ='web' then 1 end as web, case when  source ='whatsapp' then 1 end as whatsapp "
			+ "from eg_pgr_service eps ";
	public static final String PGR_WHATSAPP_COMPLAINT_MONTHWISE_COUNT = PGR_MONTH_YEAR +" , "+PGR_WHATSAPP_COUNT+PGR_TOTAL_COMPLAINTS_BY_SOURCE;
	public static final String PGR_IVR_COMPLAINT_MONTHWISE_COUNT = PGR_MONTH_YEAR +" , "+PGR_IVR_COUNT+PGR_TOTAL_COMPLAINTS_BY_SOURCE;
	public static final String PGR_WEB_COMPLAINT_MONTHWISE_COUNT = PGR_MONTH_YEAR +" , "+PGR_WEB_COUNT+PGR_TOTAL_COMPLAINTS_BY_SOURCE;
	public static final String PGR_MOBILEAPP_COMPLAINT_MONTHWISE_COUNT = PGR_MONTH_YEAR +" , "+PGR_MOBILEAPP_COUNT+PGR_TOTAL_COMPLAINTS_BY_SOURCE;
	public static final String PGR_COMPLAINTS_BY_TENANT = " select TENANTID as name, count(servicerequestid) as value from eg_pgr_service eps ";

	
	
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
		
		if (searchCriteria.getSlaThreshold() != null) {
			addClauseIfRequired(preparedStatementValues, selectQuery);
			selectQuery.append(" eps.lastmodifiedtime - eps.createdtime < " + searchCriteria.getSlaThreshold());
		}

		if (searchCriteria.getExcludedTenantId() != null) {
			addClauseIfRequired(preparedStatementValues, selectQuery);
			selectQuery.append(" eps.tenantId != :excludedTenantId");
			preparedStatementValues.put("excludedTenantId", searchCriteria.getExcludedTenantId());
		}
		
		if (searchCriteria.getStatus() != null) {
			addClauseIfRequired(preparedStatementValues, selectQuery);
			selectQuery.append(" eps.status in (:status)");
			preparedStatementValues.put("status", searchCriteria.getStatus());
		}
		if (searchCriteria.getStatusNotIn() != null) {
			addClauseIfRequired(preparedStatementValues, selectQuery);
			selectQuery.append(" eps.status not in (:statusNotIn)");
			preparedStatementValues.put("statusNotIn", searchCriteria.getStatusNotIn());
		}
		
		return selectQuery.toString();

	}
	
	public void addGroupByClause(StringBuilder selectQuery, String columnName) {
		selectQuery.append(" GROUP BY " + columnName);
	}
	
	public void addOrderByClause(StringBuilder selectQuery, String columnName, String orderBy) {
		selectQuery.append(" ORDER BY " + columnName + " "+ orderBy);
	}

	public String getTopFiveComplaintsQuery(PgrSearchCriteria criteria, Map<String, Object> preparedStatementValues) {
		StringBuilder selectQuery = new StringBuilder(PGR_TOP_COMPLAINTS);
		addWhereClause(selectQuery, preparedStatementValues, criteria);
		addGroupByClause(selectQuery, " name ");
		addOrderByClause(selectQuery, " value ", "desc");
		selectQuery.append(" limit :limit ");
		preparedStatementValues.put("limit", configurationLoader.getPgrTopComplaintsLimit());
		return selectQuery.toString();
	}

	public String getTenantWiseApplicationsQuery(PgrSearchCriteria criteria,
			Map<String, Object> preparedStatementValues) {
		StringBuilder selectQuery = new StringBuilder(TENANT_WISE_APPLICATIONS);
		addWhereClause(selectQuery, preparedStatementValues, criteria);
		addGroupByClause(selectQuery, "tenantid");
		return selectQuery.toString();
	}

	public String getCumulativeComplaintsQuery(PgrSearchCriteria criteria,
			Map<String, Object> preparedStatementValues) {
		StringBuilder selectQuery = new StringBuilder(CUMULATIVE_COMPLAINTS);
		addWhereClause(selectQuery, preparedStatementValues, criteria);
		addGroupByClause(selectQuery, " to_date(concat('01-',EXTRACT(MONTH FROM to_timestamp(lastmodifiedtime/1000)),'-' ,EXTRACT(YEAR FROM to_timestamp(lastmodifiedtime/1000))),'DD-MM-YYYY') ");
		addOrderByClause(selectQuery, " to_date(concat('01-',EXTRACT(MONTH FROM to_timestamp(lastmodifiedtime/1000)),'-' ,EXTRACT(YEAR FROM to_timestamp(lastmodifiedtime/1000))),'DD-MM-YYYY') ", "asc");
		selectQuery.append(" ) tlTmp ");
		return selectQuery.toString();
	}

	public String getMonthYearDataQuery(PgrSearchCriteria criteria, Map<String, Object> preparedStatementValues) {
		
		StringBuilder selectQuery = new StringBuilder(MONTH_YEAR_QUERY);
		addWhereClause(selectQuery, preparedStatementValues, criteria);
		addGroupByClause(selectQuery," to_date(concat('01-',EXTRACT(MONTH FROM to_timestamp(lastmodifiedtime/1000)),'-' ,EXTRACT(YEAR FROM to_timestamp(lastmodifiedtime/1000))),'DD-MM-YYYY') ");
		addOrderByClause(selectQuery," to_date(concat('01-',EXTRACT(MONTH FROM to_timestamp(lastmodifiedtime/1000)),'-' ,EXTRACT(YEAR FROM to_timestamp(lastmodifiedtime/1000))),'DD-MM-YYYY') ", "asc");
		selectQuery.append(" ) tlTmp ");
		return selectQuery.toString();
	}
	
	public String getComplaintsByStatusCriteriaQuery(PgrSearchCriteria pgrSearchCriteria,
			Map<String, Object> preparedStatementValues) {
		StringBuilder selectQuery = new StringBuilder(PGR_COMPLAINTS_BY_STATUS);
		addWhereClause(selectQuery, preparedStatementValues, pgrSearchCriteria);
		addGroupByClause(selectQuery," status ");
		return selectQuery.toString();

	}

	public String getComplaintsByChannelCriteriaQuery(PgrSearchCriteria pgrSearchCriteria,
			Map<String, Object> preparedStatementValues) {
		StringBuilder selectQuery = new StringBuilder(PGR_COMPLAINTS_BY_CHANNEL);
		addWhereClause(selectQuery, preparedStatementValues, pgrSearchCriteria);
		addGroupByClause(selectQuery," source ");
		return selectQuery.toString();
	}

	public String getComplaintsByDepartmentCriteriaQuery(PgrSearchCriteria pgrSearchCriteria,
			Map<String, Object> preparedStatementValues) {
		StringBuilder selectQuery = new StringBuilder(PGR_COMPLAINTS_BY_DEPARTMENT);
		addWhereClause(selectQuery, preparedStatementValues, pgrSearchCriteria);
		addGroupByClause(selectQuery," servicecode ");
		return selectQuery.toString();
	}

    private static void addOrderByClause(StringBuilder demandQueryBuilder,String columnName) {
        demandQueryBuilder.append(" ORDER BY " + columnName);
    }

	public String getEventDurationGraphQuery(PgrSearchCriteria pgrSearchCriteria,
			Map<String, Object> preparedStatementValues) {
		StringBuilder selectQuery = new StringBuilder(PGR_EVENT_DURATION_GRAPH);
		addWhereClause(selectQuery, preparedStatementValues, pgrSearchCriteria);
		selectQuery.append(" ) tmpPgr ");
		addGroupByClause(selectQuery," tmpPgr.monthYear ");
		addOrderByClause(selectQuery," tmpPgr.monthYear asc ");
		return selectQuery.toString();
	}

	public String getUniqueCitizensQuery(PgrSearchCriteria pgrSearchCriteria,
			Map<String, Object> preparedStatementValues) {
		StringBuilder selectQuery = new StringBuilder(PGR_UNIQUE_CITIZENS);
		addWhereClause(selectQuery, preparedStatementValues, pgrSearchCriteria);
		addGroupByClause(selectQuery," to_date(concat('01-',EXTRACT(MONTH FROM to_timestamp(eps.lastmodifiedtime/1000)),'-' ,EXTRACT(YEAR FROM to_timestamp(eps.lastmodifiedtime/1000))),'DD-MM-YYYY') ");
		addOrderByClause(selectQuery," to_date(concat('01-',EXTRACT(MONTH FROM to_timestamp(eps.lastmodifiedtime/1000)),'-' ,EXTRACT(YEAR FROM to_timestamp(eps.lastmodifiedtime/1000))),'DD-MM-YYYY') ");
		return selectQuery.toString();
	}

	public String getTotalClosedComplaintsMonthWise(PgrSearchCriteria pgrSearchCriteria,
			Map<String, Object> preparedStatementValues) {
		StringBuilder selectQuery = new StringBuilder(PGR_CLOSED_STATUS_MONTHWISE_COUNT);
		addWhereClause(selectQuery, preparedStatementValues, pgrSearchCriteria);
		selectQuery.append(" ) tmpPgr ");
		addGroupByClause(selectQuery," tmpPgr.monthYear ");
		addOrderByClause(selectQuery," tmpPgr.monthYear asc ");
		return selectQuery.toString();
	}

	public String getTotalOpenedComplaintsMonthWise(PgrSearchCriteria pgrSearchCriteria,
			Map<String, Object> preparedStatementValues) {
		StringBuilder selectQuery = new StringBuilder(PGR_OPEN_STATUS_MONTHWISE_COUNT);
		addWhereClause(selectQuery, preparedStatementValues, pgrSearchCriteria);
		selectQuery.append(" ) tmpPgr ");
		addGroupByClause(selectQuery," tmpPgr.monthYear ");
		addOrderByClause(selectQuery," tmpPgr.monthYear asc ");
		return selectQuery.toString();
	}

	public String getTotalComplaintsByWhatsapp(PgrSearchCriteria pgrSearchCriteria,
			Map<String, Object> preparedStatementValues) {
		StringBuilder selectQuery = new StringBuilder(PGR_WHATSAPP_COMPLAINT_MONTHWISE_COUNT);
		addWhereClause(selectQuery, preparedStatementValues, pgrSearchCriteria);
		selectQuery.append(" ) tmpPgr ");
		addGroupByClause(selectQuery," tmpPgr.monthYear ");
		addOrderByClause(selectQuery," tmpPgr.monthYear asc ");
		return selectQuery.toString();
	}

	public String getTotalComplaintsByIvr(PgrSearchCriteria pgrSearchCriteria,
			Map<String, Object> preparedStatementValues) {
		StringBuilder selectQuery = new StringBuilder(PGR_IVR_COMPLAINT_MONTHWISE_COUNT);
		addWhereClause(selectQuery, preparedStatementValues, pgrSearchCriteria);
		selectQuery.append(" ) tmpPgr ");
		addGroupByClause(selectQuery," tmpPgr.monthYear ");
		addOrderByClause(selectQuery," tmpPgr.monthYear asc ");
		return selectQuery.toString();
	}

	public String getTotalComplaintsByWeb(PgrSearchCriteria pgrSearchCriteria,
			Map<String, Object> preparedStatementValues) {
		StringBuilder selectQuery = new StringBuilder(PGR_WEB_COMPLAINT_MONTHWISE_COUNT);
		addWhereClause(selectQuery, preparedStatementValues, pgrSearchCriteria);
		selectQuery.append(" ) tmpPgr ");
		addGroupByClause(selectQuery," tmpPgr.monthYear ");
		addOrderByClause(selectQuery," tmpPgr.monthYear asc ");
		return selectQuery.toString();
	}

	public String getTotalComplaintsByMobileApp(PgrSearchCriteria pgrSearchCriteria,
			Map<String, Object> preparedStatementValues) {
		StringBuilder selectQuery = new StringBuilder(PGR_MOBILEAPP_COMPLAINT_MONTHWISE_COUNT);
		addWhereClause(selectQuery, preparedStatementValues, pgrSearchCriteria);
		selectQuery.append(" ) tmpPgr ");
		addGroupByClause(selectQuery," tmpPgr.monthYear ");
		addOrderByClause(selectQuery," tmpPgr.monthYear asc ");
		return selectQuery.toString();
	}

	public String getTenantWiseTotalApplication(PgrSearchCriteria pgrSearchCriteria,
			Map<String, Object> preparedStatementValues) {
		StringBuilder selectQuery = new StringBuilder(PGR_COMPLAINTS_BY_TENANT);
		addWhereClause(selectQuery, preparedStatementValues, pgrSearchCriteria);
	    addGroupByClause(selectQuery," eps.tenantid ");
		return selectQuery.toString();
	}

}
