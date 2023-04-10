package org.egov.dss.repository.builder;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.egov.dss.model.CommonSearchCriteria;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

@Component
public class CommonServiceQueryBuilder {
	
	public static final String CITIZENS_REGISTERED_QUERY_SQL = " select count(distinct mobilenumber) from eg_user where type ='CITIZEN' ";
	public static final String PT_TOTAL_APPLICATIONS_QUERY_SQL = " select count(*)  from eg_pt_property  ";
	public static final String WS_TOTAL_APPLICATIONS_QUERY_SQL = " select count(*)  from eg_ws_connection  ";
	public static final String BPA_TOTAL_APPLICATIONS_QUERY_SQL = " 	select count(applicationno) from eg_bpa_buildingplan bpa  ";
	public static final String TL_TOTAL_APPLICATIONS_QUERY_SQL = " 	select count(applicationnumber) as licenceIssued from eg_tl_tradelicense tl ";
	public static final String MR_TOTAL_APPLICATIONS_QUERY_SQL = " 	select count(applicationnumber) noOfApplication from eg_mr_application mr  ";
	public static final String PGR_TOTAL_APPLICATIONS_QUERY_SQL = " 	select count(servicerequestid) slaCompletionCount from eg_pgr_service  ";
	public static final String TL_TOTAL_COMP_APPLICATIONS_QUERY_SQL = " select tenantid as name , count(applicationnumber) as value from eg_tl_tradelicense tl ";

    private static void addClauseIfRequired(Map<String, Object> values, StringBuilder queryString) {
		if (values.isEmpty())
			queryString.append(" WHERE ");
		else {
			queryString.append(" AND");
		}
	}

	private static String addWhereClause(StringBuilder selectQuery, Map<String, Object> preparedStatementValues,
			CommonSearchCriteria searchCriteria) {


		if (searchCriteria.getTenantIds() != null && !CollectionUtils.isEmpty(searchCriteria.getTenantIds())) {
			addClauseIfRequired(preparedStatementValues, selectQuery);
			selectQuery.append("  tenantid IN ( :tenantId )");
			preparedStatementValues.put("tenantId",searchCriteria.getTenantIds());
		}

		if (searchCriteria.getFromDate() != null) {
			addClauseIfRequired(preparedStatementValues, selectQuery);
			selectQuery.append("  lastmodifiedtime >= :fromDate");
			preparedStatementValues.put("fromDate", searchCriteria.getFromDate());
		}

		if (searchCriteria.getToDate() != null) {
			addClauseIfRequired(preparedStatementValues, selectQuery);
			selectQuery.append("  lastmodifiedtime <= :toDate");
			preparedStatementValues.put("toDate", searchCriteria.getToDate());
		}
		
		if (searchCriteria.getStatus() != null) {
			addClauseIfRequired(preparedStatementValues, selectQuery);
			selectQuery.append("  status IN (:status) ");
			preparedStatementValues.put("status", searchCriteria.getStatus());
		}
		
		if (searchCriteria.getSlaThreshold() != null) {
			addClauseIfRequired(preparedStatementValues, selectQuery);
			selectQuery.append("  lastmodifiedtime -  createdtime < " + searchCriteria.getSlaThreshold());
		}

		if (searchCriteria.getExcludedTenantId() != null) {
			addClauseIfRequired(preparedStatementValues, selectQuery);
			selectQuery.append("  tenantId != :excludedTenantId");
			preparedStatementValues.put("excludedTenantId", searchCriteria.getExcludedTenantId());
		}
		

		if (searchCriteria.getStatusNotIn() != null) {
			addClauseIfRequired(preparedStatementValues, selectQuery);
			selectQuery.append("  status NOT IN (:status) ");
			preparedStatementValues.put("status", searchCriteria.getStatusNotIn());
		}
		
		if (searchCriteria.getBusinessServices() != null) {
			addClauseIfRequired(preparedStatementValues, selectQuery);
			selectQuery.append("  businessservice IN (:businessservice) ");
			preparedStatementValues.put("businessservice", searchCriteria.getBusinessServices());
		}
		

		return selectQuery.toString();

	}

	public String getTotalCitizensRegisteredQuery(CommonSearchCriteria criteria,
			Map<String, Object> preparedStatementValues) {
		StringBuilder selectQuery = new StringBuilder(CITIZENS_REGISTERED_QUERY_SQL);
		selectQuery.append(" and createddate >= to_timestamp(:usergtCreatedDate/1000) ");
		preparedStatementValues.put("usergtCreatedDate", criteria.getFromDate());
		selectQuery.append("and createddate <= to_timestamp(:userltCreatedDate/1000) ");
		preparedStatementValues.put("userltCreatedDate", criteria.getToDate());
		return selectQuery.toString();
	}

	public String ptTotalCompletionCount(CommonSearchCriteria criteria, Map<String, Object> preparedStatementValues) {
		StringBuilder selectQuery = new StringBuilder(PT_TOTAL_APPLICATIONS_QUERY_SQL);
		addWhereClause(selectQuery, preparedStatementValues, criteria);
		return selectQuery.toString();
	}

	public String wsTotalCompletionCount(CommonSearchCriteria criteria, Map<String, Object> preparedStatementValues) {
		StringBuilder selectQuery = new StringBuilder(WS_TOTAL_APPLICATIONS_QUERY_SQL);
		addWhereClause(selectQuery, preparedStatementValues, criteria);
		return selectQuery.toString();
	}

	public String bpaTotalCompletionCount(CommonSearchCriteria criteria,
			Map<String, Object> preparedStatementValues) {
		StringBuilder selectQuery = new StringBuilder(BPA_TOTAL_APPLICATIONS_QUERY_SQL);
		addWhereClause(selectQuery, preparedStatementValues, criteria);
		return selectQuery.toString();
	}

	public String tlTotalCompletionCount(CommonSearchCriteria criteria, Map<String, Object> preparedStatementValues) {
		StringBuilder selectQuery = new StringBuilder(TL_TOTAL_APPLICATIONS_QUERY_SQL);
		addWhereClause(selectQuery, preparedStatementValues, criteria);
		return selectQuery.toString();
	}

	public String mrTotalCompletionCount(CommonSearchCriteria criteria, Map<String, Object> preparedStatementValues) {
		StringBuilder selectQuery = new StringBuilder(MR_TOTAL_APPLICATIONS_QUERY_SQL);
		addWhereClause(selectQuery, preparedStatementValues, criteria);
		return selectQuery.toString();
	}

	public String pgrTotalCompletionCount(CommonSearchCriteria criteria, Map<String, Object> preparedStatementValues) {
		StringBuilder selectQuery = new StringBuilder(PGR_TOTAL_APPLICATIONS_QUERY_SQL);
		addWhereClause(selectQuery, preparedStatementValues, criteria);
		return selectQuery.toString();
	}

	public String pgrTotalApplicationsTenantWise(CommonSearchCriteria criteria,
			Map<String, Object> preparedStatementValues) {
		StringBuilder selectQuery = new StringBuilder(PGR_TOTAL_APPLICATIONS_QUERY_SQL);
		addWhereClause(selectQuery, preparedStatementValues, criteria);
		addGroupByClause(selectQuery, " tenantid ");
		return selectQuery.toString();
	}
	
	private static void addGroupByClause(StringBuilder demandQueryBuilder, String columnName) {
		demandQueryBuilder.append(" GROUP BY " + columnName);
	}

	public String tlTotalCompletedApplicationsTenantWise(CommonSearchCriteria criteria,
			Map<String, Object> preparedStatementValues) {
		StringBuilder selectQuery = new StringBuilder(TL_TOTAL_COMP_APPLICATIONS_QUERY_SQL);
		addWhereClause(selectQuery, preparedStatementValues, criteria);
		addGroupByClause(selectQuery, " tenantid ");
		return selectQuery.toString();
	}

}
