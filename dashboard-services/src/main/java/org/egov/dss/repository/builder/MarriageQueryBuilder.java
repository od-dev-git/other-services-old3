package org.egov.dss.repository.builder;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.egov.dss.model.MarriageSearchCriteria;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

@Component
public class MarriageQueryBuilder {
	
	public static final String MR_TOTAL_APPLICATION = " select count(ema.applicationnumber) from eg_mr_application ema ";
	
	public static final String MR_CUMULATIVE_APPLICATIONS = " select to_char(monthYear, 'Mon-YYYY') as name, sum(noOfApplication) over (order by monthYear asc rows between unbounded preceding and current row) as value "
			+ "from (select to_date(concat('01-',EXTRACT(MONTH FROM to_timestamp(lastmodifiedtime/1000)),'-' ,EXTRACT(YEAR FROM to_timestamp(lastmodifiedtime/1000))),'DD-MM-YYYY') monthYear , "
			+ "count(applicationnumber) noOfApplication from eg_mr_application ema ";
	
	public static final String MR_TENANT_WISE_TOTAL_APPLICATION = " select ema.tenantid as name, count(ema.applicationnumber) as value from eg_mr_application ema ";
	
	public static final String MR_TOTAL_APPLICATION_BY_STATUS = " select ema.status as name, count(ema.applicationnumber) as value from eg_mr_application ema ";
	
	public static final String MR_STATUS_BY_BOUNDARY = " select tenantid, "
			+ "coalesce(sum(approved),0) as approvedCnt, "
			+ "coalesce(sum(cancelled),0) as cancelledCnt, "
			+ "coalesce(sum(backFromApproverToDocVerifier),0) as backFromApproverToDocVerifierCnt, "
			+ "coalesce(sum(backFromScheduleToDocVerifier),0) as backFromScheduleToDocVerifierCnt, "
			+ "coalesce(sum(citizenActionPending),0) as citizenActionPendingCnt, "
			+ "coalesce(sum(docVerificationPending),0) as docVerificationPendingCnt, "
			+ "coalesce(sum(fieldInspectionPending),0) as fieldInspectionPendingCnt, "
			+ "coalesce(sum(initiated),0) as initiatedCnt, "
			+ "coalesce(sum(approvalPending),0) as approvalPendingCnt, "
			+ "coalesce(sum(paymentPending),0) as paymentPendingCnt, "
			+ "coalesce(sum(schedulePending),0) as schedulePendingCnt, "
			+ "coalesce(sum(rejected),0) as rejectedCnt "
			+ "from ( "
			+ "select tenantid,  "
			+ "case when status='APPROVED' then 1 end as approved, "
			+ "case when status='CANCELLED' then 1 end as cancelled, "
			+ "case when status in ('CITIZENACTIONPENDINGATAPPROVER','CITIZENACTIONPENDINGATDOCVERIFICATION','CITIZENACTIONPENDINGATSCHEDULE') then 1 end as citizenActionPending, "
			+ "case when status='BACKFROMAPPROVALTODOCVERIFIER' then 1 end as backFromApproverToDocVerifier, "
			+ "case when status='BACKFROMSHEDULETODOCVERIFIER' then 1 end as backFromScheduleToDocVerifier, "
			+ "case when status='DOCVERIFICATION' then 1 end as docVerificationPending, "
			+ "case when status='FIELDINSPECTION' then 1 end as fieldInspectionPending, "
			+ "case when status='INITIATED' then 1 end as initiated, "
			+ "case when status='PENDINGAPPROVAL' then 1 end as approvalPending, "
			+ "case when status='PENDINGPAYMENT' then 1 end as paymentPending, "
			+ "case when status='PENDINGSCHEDULE' then 1 end as schedulePending, "
			+ "case when status='REJECTED' then 1 end as rejected "
			+ "from eg_mr_application ema ";
	
	public static String getTotalApplication(MarriageSearchCriteria criteria, Map<String, Object> preparedStatementValues) {
		StringBuilder selectQuery = new StringBuilder(MR_TOTAL_APPLICATION);
		return addWhereClause(selectQuery, preparedStatementValues, criteria);
	}
	
	public static String getCumulativeApplications(MarriageSearchCriteria criteria, Map<String, Object> preparedStatementValues) {
		StringBuilder selectQuery = new StringBuilder(MR_CUMULATIVE_APPLICATIONS);
	    addWhereClause(selectQuery, preparedStatementValues, criteria);
	    addGroupByClause(selectQuery," to_date(concat('01-',EXTRACT(MONTH FROM to_timestamp(lastmodifiedtime/1000)),'-' ,EXTRACT(YEAR FROM to_timestamp(lastmodifiedtime/1000))),'DD-MM-YYYY') ");
		addOrderByClause(selectQuery," to_date(concat('01-',EXTRACT(MONTH FROM to_timestamp(lastmodifiedtime/1000)),'-' ,EXTRACT(YEAR FROM to_timestamp(lastmodifiedtime/1000))),'DD-MM-YYYY') asc) mrTmp ");
		return selectQuery.toString();
	}
	
	public static String getTenantWiseTotalApplication(MarriageSearchCriteria criteria, Map<String, Object> preparedStatementValues) {
		StringBuilder selectQuery = new StringBuilder(MR_TENANT_WISE_TOTAL_APPLICATION);
	    addWhereClause(selectQuery, preparedStatementValues, criteria);
	    addGroupByClause(selectQuery," ema.tenantid ");
		addOrderByClause(selectQuery," count(ema.applicationnumber) ASC ");
		return selectQuery.toString();
	}
	
	public static String getApplicationsByStatus(MarriageSearchCriteria criteria, Map<String, Object> preparedStatementValues) {
		StringBuilder selectQuery = new StringBuilder(MR_TOTAL_APPLICATION_BY_STATUS);
	    addWhereClause(selectQuery, preparedStatementValues, criteria);
	    addGroupByClause(selectQuery," ema.status ");
		return selectQuery.toString();
	}
	
	public String getMrStatusByBoundary(MarriageSearchCriteria criteria,
			Map<String, Object> preparedStatementValues) {
		StringBuilder selectQuery = new StringBuilder(MR_STATUS_BY_BOUNDARY);
		addWhereClause(selectQuery, preparedStatementValues, criteria);
		selectQuery.append( " ) mrTmp ");
		addGroupByClause(selectQuery," tenantid ");
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
			MarriageSearchCriteria searchCriteria) {

		if (searchCriteria.getTenantIds() != null && !CollectionUtils.isEmpty(searchCriteria.getTenantIds())) {
			addClauseIfRequired(preparedStatementValues, selectQuery);
			selectQuery.append(" ema.tenantId IN ( :tenantId )");
			preparedStatementValues.put("tenantId", searchCriteria.getTenantIds());
		}


		if (searchCriteria.getFromDate() != null) {
			addClauseIfRequired(preparedStatementValues, selectQuery);
			if (searchCriteria.getIsApplicationDate() == Boolean.TRUE) {
				selectQuery.append(" ema.applicationdate >= :fromDate");
			} else {
				selectQuery.append(" ema.lastmodifiedtime >= :fromDate");
			}
			preparedStatementValues.put("fromDate", searchCriteria.getFromDate());
		}

		if (searchCriteria.getToDate() != null) {
			addClauseIfRequired(preparedStatementValues, selectQuery);
			if (searchCriteria.getIsApplicationDate() == Boolean.TRUE) {
				selectQuery.append(" ema.applicationdate <= :toDate");
			} else {
				selectQuery.append(" ema.lastmodifiedtime <= :toDate");
			}
			preparedStatementValues.put("toDate", searchCriteria.getToDate());
		}
		
		if (searchCriteria.getSlaThreshold() != null) {
			addClauseIfRequired(preparedStatementValues, selectQuery);
			selectQuery.append(" ema.lastmodifiedtime - ema.createdtime < " + searchCriteria.getSlaThreshold());
		}
		
		if (searchCriteria.getApplicationType() != null) {
			addClauseIfRequired(preparedStatementValues, selectQuery);
			selectQuery.append(" ema.applicationtype = :applicationType");
			preparedStatementValues.put("applicationType", searchCriteria.getApplicationType());
		}
		
		if (searchCriteria.getStatus() != null) {
			addClauseIfRequired(preparedStatementValues, selectQuery);
			selectQuery.append(" ema.status = :status");
			preparedStatementValues.put("status", searchCriteria.getStatus());
		}
		
		if (!CollectionUtils.isEmpty(searchCriteria.getStatusNotIn())) {
			addClauseIfRequired(preparedStatementValues, selectQuery);
			selectQuery.append(" ema.status not in (:statusNotIn) ");
			preparedStatementValues.put("statusNotIn", searchCriteria.getStatusNotIn());
		}
		
		if (searchCriteria.getIsTatkalApplication() == Boolean.TRUE) {
			addClauseIfRequired(preparedStatementValues, selectQuery);
			selectQuery.append(" ema.istatkalapplication = true ");			
		}

		if (searchCriteria.getExcludedTenantId() != null) {
			addClauseIfRequired(preparedStatementValues, selectQuery);
			selectQuery.append(" ema.tenantId != :excludedTenantId");
			preparedStatementValues.put("excludedTenantId", searchCriteria.getExcludedTenantId());
		}

		return selectQuery.toString();

	}
	
	private static void addGroupByClause(StringBuilder demandQueryBuilder, String columnName) {
		demandQueryBuilder.append(" GROUP BY " + columnName);
	}

	private static void addOrderByClause(StringBuilder demandQueryBuilder, String columnName) {
		demandQueryBuilder.append(" ORDER BY " + columnName);
	}


}
