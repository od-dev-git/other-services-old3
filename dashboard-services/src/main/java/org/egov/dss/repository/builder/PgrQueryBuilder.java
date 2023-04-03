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

}
