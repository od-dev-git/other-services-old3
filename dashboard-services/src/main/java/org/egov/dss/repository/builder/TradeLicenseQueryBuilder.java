package org.egov.dss.repository.builder;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.egov.dss.model.TLSearchCriteria;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

@Component
public class TradeLicenseQueryBuilder {

	public static final String TL_TOTAL_APPLICATION = " select count(ett.applicationnumber) from eg_tl_tradelicense ett ";
	
	public static final String TL_ACTIVE_ULBS = " select count(distinct ett.tenantid) from eg_tl_tradelicense ett ";
	
	public static final String TL_CUMULATIVE_LICENSE_ISSUED = " select to_char(monthYear, 'Mon-YYYY') as name, sum(licenseCount) over (order by monthYear asc rows between unbounded preceding and current row) as value from "
		    + "(select to_date(concat('01-',EXTRACT(MONTH FROM to_timestamp(lastmodifiedtime/1000)),'-' ,EXTRACT(YEAR FROM to_timestamp(lastmodifiedtime/1000))),'DD-MM-YYYY') monthYear , "
			+ "count(applicationnumber) licenseCount from eg_tl_tradelicense ett ";
	
	public static final String TL_TENAT_WISE_TOTAL_APPLICATION = " select ett.tenantid as name, count(ett.applicationnumber) as value from eg_tl_tradelicense ett ";
	
	public static final String TL_LICENSE_ISSUED_BY_STATUS = " select ett.status as name, count(ett.applicationnumber) as value from eg_tl_tradelicense ett ";
	
	public static final String TL_STATUS_BY_BOUNDARY = " select tenantid, "
			+ "coalesce(sum(approved),0) as approvedCnt, "
			+ "coalesce(sum(cancelled),0) as cancelledCnt, "
			+ "coalesce(sum(citizenActionPending),0) as citizenActionPendingCnt, "
			+ "coalesce(sum(docVerificationPending),0) as docVerificationPendingCnt, "
			+ "coalesce(sum(expired),0) as expiredCnt, "
			+ "coalesce(sum(fieldInspectionPending),0) as fieldInspectionPendingCnt, "
			+ "coalesce(sum(initiated),0) as initiatedCnt, "
			+ "coalesce(sum(approvalPending),0) as approvalPendingCnt, "
			+ "coalesce(sum(PaymentPending),0) as PaymentPendingCnt, "
			+ "coalesce(sum(rejected),0) as rejectedCnt "
			+ "from ( "
			+ "select tenantid,  "
			+ "case when status='APPROVED' then 1 end as approved, "
			+ "case when status='CANCELLED' then 1 end as cancelled, "
			+ "case when status in ('CITIZENACTIONPENDINGATAPPROVER','CITIZENACTIONPENDINGATDOCVERIFICATION','CITIZENACTIONPENDINGATFIVERIFICATION','CITIZENACTIONREQUIRED') then 1 end as citizenActionPending, "
			+ "case when status='DOCVERIFICATION' then 1 end as docVerificationPending, "
			+ "case when status='EXPIRED' then 1 end as expired, "
			+ "case when status='FIELDINSPECTION' then 1 end as fieldInspectionPending, "
			+ "case when status='INITIATED' then 1 end as initiated, "
			+ "case when status='PENDINGAPPROVAL' then 1 end as approvalPending, "
			+ "case when status='PENDINGPAYMENT' then 1 end as PaymentPending, "
			+ "case when status='REJECTED' then 1 end as rejected "
			+ "from eg_tl_tradelicense ett ";

	public static String getTotalApplication(TLSearchCriteria criteria, Map<String, Object> preparedStatementValues) {
		StringBuilder selectQuery = new StringBuilder(TL_TOTAL_APPLICATION);
		return addWhereClause(selectQuery, preparedStatementValues, criteria);
	}
	
	public static String getTotalActiveUlbs(TLSearchCriteria criteria, Map<String, Object> preparedStatementValues) {
		StringBuilder selectQuery = new StringBuilder(TL_ACTIVE_ULBS);
		return addWhereClause(selectQuery, preparedStatementValues, criteria);
	}
	
	public static String getCumulativeLicenseIssued(TLSearchCriteria criteria, Map<String, Object> preparedStatementValues) {
		StringBuilder selectQuery = new StringBuilder(TL_CUMULATIVE_LICENSE_ISSUED);
	    addWhereClause(selectQuery, preparedStatementValues, criteria);
	    addGroupByClause(selectQuery," to_date(concat('01-',EXTRACT(MONTH FROM to_timestamp(lastmodifiedtime/1000)),'-' ,EXTRACT(YEAR FROM to_timestamp(lastmodifiedtime/1000))),'DD-MM-YYYY') ");
		addOrderByClause(selectQuery," to_date(concat('01-',EXTRACT(MONTH FROM to_timestamp(lastmodifiedtime/1000)),'-' ,EXTRACT(YEAR FROM to_timestamp(lastmodifiedtime/1000))),'DD-MM-YYYY') asc) tlTmp ");
		return selectQuery.toString();
	}
	
	public static String getTenantWiseTotalApplication(TLSearchCriteria criteria, Map<String, Object> preparedStatementValues) {
		StringBuilder selectQuery = new StringBuilder(TL_TENAT_WISE_TOTAL_APPLICATION);
	    addWhereClause(selectQuery, preparedStatementValues, criteria);
	    addGroupByClause(selectQuery," ett.tenantid ");
		addOrderByClause(selectQuery," count(ett.applicationnumber) ASC ");
		return selectQuery.toString();
	}
	
	public static String getLicenseByStatus(TLSearchCriteria criteria, Map<String, Object> preparedStatementValues) {
		StringBuilder selectQuery = new StringBuilder(TL_LICENSE_ISSUED_BY_STATUS);
	    addWhereClause(selectQuery, preparedStatementValues, criteria);
	    addGroupByClause(selectQuery," ett.status ");
		return selectQuery.toString();
	}
	
	public String getTlStatusByBoundary(TLSearchCriteria criteria,
			Map<String, Object> preparedStatementValues) {
		StringBuilder selectQuery = new StringBuilder(TL_STATUS_BY_BOUNDARY);
		addWhereClause(selectQuery, preparedStatementValues, criteria);
		selectQuery.append( " ) tlTmp ");
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
			TLSearchCriteria searchCriteria) {
        
		if (searchCriteria.getTenantIds() != null && !CollectionUtils.isEmpty(searchCriteria.getTenantIds())) {
			addClauseIfRequired(preparedStatementValues, selectQuery);
			selectQuery.append(" ett.tenantid in (:tenantId)");
			preparedStatementValues.put("tenantId", searchCriteria.getTenantIds());
		}
		
		if (searchCriteria.getFromDate() != null) {
			addClauseIfRequired(preparedStatementValues, selectQuery);
			if (searchCriteria.getIsApplicationDate() == Boolean.TRUE) {
				selectQuery.append(" ett.applicationdate >= :fromDate");
			} else {
				selectQuery.append(" ett.lastmodifiedtime >= :fromDate");
			}
			preparedStatementValues.put("fromDate", searchCriteria.getFromDate());
		}

		if (searchCriteria.getToDate() != null) {
			addClauseIfRequired(preparedStatementValues, selectQuery);
			if (searchCriteria.getIsApplicationDate() == Boolean.TRUE) {
				selectQuery.append(" ett.applicationdate <= :toDate");
			} else {
				selectQuery.append(" ett.lastmodifiedtime <= :toDate");
			}
			preparedStatementValues.put("toDate", searchCriteria.getToDate());
		}
		
		if (searchCriteria.getSlaThreshold() != null) {
			addClauseIfRequired(preparedStatementValues, selectQuery);
			selectQuery.append(" ett.lastmodifiedtime - ett.createdtime < " + searchCriteria.getSlaThreshold());
		}
		
		if (searchCriteria.getApplicationType() != null) {
			addClauseIfRequired(preparedStatementValues, selectQuery);
			selectQuery.append(" ett.applicationtype = :applicationType");
			preparedStatementValues.put("applicationType", searchCriteria.getApplicationType());
		}
		
		if (searchCriteria.getStatus() != null) {
			addClauseIfRequired(preparedStatementValues, selectQuery);
			selectQuery.append(" ett.status = :status");
			preparedStatementValues.put("status", searchCriteria.getStatus());
		}
		
		if (searchCriteria.getBusinessServices() != null) {
			addClauseIfRequired(preparedStatementValues, selectQuery);
			selectQuery.append(" ett.businessservice in (:businessService)");
			preparedStatementValues.put("businessService", searchCriteria.getBusinessServices());
		}

		if (searchCriteria.getExcludedTenantId() != null) {
			addClauseIfRequired(preparedStatementValues, selectQuery);
			selectQuery.append(" ett.tenantId != :excludedTenantId");
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
