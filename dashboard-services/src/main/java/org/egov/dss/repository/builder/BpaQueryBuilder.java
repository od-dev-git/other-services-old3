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
		+ "select to_date(concat('01-',EXTRACT(MONTH FROM to_timestamp(approvaldate/1000)),'-' ,EXTRACT(YEAR FROM to_timestamp(approvaldate/1000))),'DD-MM-YYYY') monthYear from eg_bpa_buildingplan bpa  ";

public static final String BPA_TENANT_WISE_TOTAL_APPLICATIONS = " select bpa.tenantid as tenantid, count(bpa.applicationno) as totalamt from eg_bpa_buildingplan bpa  ";

public static final String BPA_TENANT_WISE_AVG_DAYS_PERMIT_ISSUED = " select bpa.tenantid as tenantid , avg((bpa.approvaldate-bpa.applicationdate)/86400000) as totalamt from eg_bpa_buildingplan bpa  ";

public static final String BPA_TOTAL_APPLICATION_RECEIVED_BY_SERVICETYPE = " select tmp.servicetype as tenantid,count(tmp.applicationno) as totalamt"
		+ " from ( select ebe.applicationno, case when ebe.alterationsubservice = '' "
		+ "	and ebe.servicetype = 'New Construction' then ebe.servicetype when ebe.alterationsubservice != '' "
		+ "	and ebe.servicetype = 'New Construction' then concat('Addition & Alteration ', '(', ebe.alterationsubservice, ')')"
		+ "	else ebe.servicetype end as servicetype from eg_bpa_buildingplan bpa"
		+ "	inner join eg_bpa_edcrdata ebe on ebe.applicationno = bpa.applicationno ";

public static final String BPA_SLA_COMPLIENCE_APPLICATIONS = " select count(bpa.applicationno) from eg_bpa_buildingplan bpa inner join eg_bpa_edcrdata ebe on "
		                                                   + " ebe.applicationno = bpa.applicationno ";

public static final String BPA_APPL_BREAKDOWN_1 = "select INITCAP(SPLIT_PART(internal.tenantid, '.', 2)) as ulb, sum(case when internal.status = 'Pending at Document Verification' then internal.internalcount else 0 end)"
		+ " pendingDocVerif, sum(case when internal.status = 'Pending at Field Inspection' then internal.internalcount else 0 end) pendingFieldInspection, sum(case when internal.status = 'Pending at Planning Assistant' "
		+ "then internal.internalcount else 0 end) pendingAtPlanningAssistant, sum(case when internal.status = 'Pending at Planning Officer' then internal.internalcount else 0 end) pendingAtPlanningOfficer, sum(case when "
		+ "internal.status = 'Pending at Planning Member' then internal.internalcount else 0 end) pendingAtPlanningMember, sum(case when internal.status = 'Pending at DPBP Committee' then internal.internalcount else 0 end) pendingAtDpbp,"
		+ " sum(case when internal.status = 'Pending for Citizen Action' then internal.internalcount else 0 end) pendingForCitizenAction, sum(case when internal.status = 'Pending for Permit Fee Payment' then internal.internalcount else 0 end)"
		+ " pendingSancFeePayment , sum(case when internal.status = 'Pending at Accrediated Officer' then internal.internalcount else 0 end) pendingataccrediatedofficer, sum(case when internal.status in ( 'Pending at Document Verification', "
		+ "'Pending at Field Inspection', 'Pending at Planning Assistant', 'Pending at Planning Officer', 'Pending at Planning Member', 'Pending at DPBP Committee', 'Pending for Citizen Action', 'Pending for Permit Fee Payment', 'Pending at Accrediated Officer')"
		+ " then internal.internalcount else 0 end) totalApplicationReciceved from ( select bpa.tenantid as tenantid, case when bpa.status = 'APPROVED' then 'Approved' when bpa.status = 'REJECTED' then 'Rejected' when bpa.status = 'DOC_VERIFICATION_INPROGRESS' then"
		+ " 'Pending at Document Verification' when bpa.status = 'FIELDINSPECTION_INPROGRESS' then 'Pending at Field Inspection' when bpa.status = 'APP_L1_VERIFICATION_INPROGRESS' then 'Pending at Planning Assistant' when bpa.status = 'APPROVAL_INPROGRESS' and "
		+ "bpa.businessservice in ('BPA1', 'BPA6') then 'Pending at Planning Assistant' when bpa.status = 'APP_L2_VERIFICATION_INPROGRESS' then 'Pending at Planning Officer' when bpa.status = 'APPROVAL_INPROGRESS' and bpa.businessservice = 'BPA2' then 'Pending at Planning Officer'"
		+ " when bpa.status = 'APP_L3_VERIFICATION_INPROGRESS' then 'Pending at Planning Member' when bpa.status = 'APPROVAL_INPROGRESS' and bpa.businessservice = 'BPA3' then 'Pending at Planning Member' when bpa.status = 'APP_L4_VERIFICATION_INPROGRESS' then 'Pending at DPBP Committee'"
		+ " when bpa.status = 'APPROVAL_INPROGRESS' and bpa.businessservice = 'BPA4' then 'Pending at DPBP Committee' when bpa.status in ('CITIZEN_ACTION_PENDING_AT_DOC_VERIF', 'CITIZEN_ACTION_PENDING_AT_APP_L1_VERIF', 'CITIZEN_ACTION_PENDING_AT_APP_L2_VERIF', 'CITIZEN_ACTION_PENDING_AT_APP_L3_VERIF',"
		+ " 'CITIZEN_ACTION_PENDING_AT_APPROVAL') then 'Pending for Citizen Action' when bpa.status = 'PENDING_ARCHITECT_ACTION_FOR_REWORK' then 'Pending for Drawing Rework' when bpa.status = 'PENDING_SANC_FEE_PAYMENT' then 'Pending for Permit Fee Payment' when bpa.status = 'APPROVAL_INPROGRESS'"
		+ " and bpa.businessservice = 'BPA5' then 'Pending at Accrediated Officer' end as status, count(bpa.applicationno) as internalcount from eg_bpa_buildingplan bpa ";

public static final String BPA_APPL_BREAKDOWN_2 = "group by bpa.status,bpa.tenantid,bpa.businessservice having bpa.businessservice in "
		+ "('BPA1', 'BPA2', 'BPA3', 'BPA4', 'BPA5', 'BPA6')	and bpa.status not in('DELETED', 'INITIATED', 'CITIZEN_APPROVAL_INPROCESS', 'PENDING_APPL_FEE', 'INPROGRESS', "
		+ "'PENDING_FORWARD', 'CONSTRUCT_START_INTIMATED', 'PLINTH_VERIFICATION_INPROGRESS', 'GROUNDFLR_VERIFICATION_INPROGRESS', 'TOPFLR_VERIFICATION_INPROGRESS', "
		+ "'TOPFLR_VERIFICATION_COMPLETED') ) internal group by internal.tenantid";

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
		
		if (searchCriteria.getRiskType() != null) {
			addClauseIfRequired(preparedStatementValues, selectQuery);
			selectQuery.append(" ebe.risktype = :riskType ");
			preparedStatementValues.put("riskType", searchCriteria.getRiskType());
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
		
		StringBuilder selectQuery = new StringBuilder(BPA_SLA_COMPLIENCE_APPLICATIONS);
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
		addGroupByClause(selectQuery," to_date(concat('01-',EXTRACT(MONTH FROM to_timestamp(approvaldate/1000)),'-' ,EXTRACT(YEAR FROM to_timestamp(approvaldate/1000))),'DD-MM-YYYY') ");
		addOrderByClause(selectQuery," to_date(concat('01-',EXTRACT(MONTH FROM to_timestamp(approvaldate/1000)),'-' ,EXTRACT(YEAR FROM to_timestamp(approvaldate/1000))),'DD-MM-YYYY') asc) bpa_tmp ");
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
	        selectQuery.append(" ) tmp ");
	        addGroupByClause(selectQuery," tmp.servicetype ");
	        return selectQuery.toString();
	    }
	    
	    public String getApprovedApplicationByServiceType(BpaSearchCriteria bpaSearchCriteria,
	            Map<String, Object> preparedStatementValues) {
	        StringBuilder selectQuery = new StringBuilder(BPA_TOTAL_APPLICATION_RECEIVED_BY_SERVICETYPE);
	        addWhereClause(selectQuery, preparedStatementValues, bpaSearchCriteria);
	        selectQuery.append(" ) tmp ");
	        addGroupByClause(selectQuery," tmp.servicetype ");
	        return selectQuery.toString();
	    }

		public String getApplicationsBreakdownQuery(BpaSearchCriteria criteria,
				Map<String, Object> preparedStatementValues) {
			StringBuilder selectQuery = new StringBuilder(BPA_APPL_BREAKDOWN_1);
			addWhereClauseforPendingApplication(selectQuery, preparedStatementValues, criteria);
			selectQuery.append(BPA_APPL_BREAKDOWN_2);
			return selectQuery.toString();
		}


}
