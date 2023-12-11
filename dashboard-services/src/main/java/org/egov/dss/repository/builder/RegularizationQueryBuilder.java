package org.egov.dss.repository.builder;

import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.egov.dss.model.BpaSearchCriteria;
import org.egov.dss.model.RegularizationSearchCriteria;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

@Component
public class RegularizationQueryBuilder {


	public static final String REGULARIZATION_TENANT_WISE_AVG_DAYS_PERMIT_ISSUED = " select ebra.tenantid as tenantid , avg((ebra.approvaldate-ebra.applicationdate)/86400000) as totalamt from eg_bpa_regularization_application ebra ";
	
    public static final String REGULARIZATION_TOTAL_APPLICATION_RECEIVED_BY_SERVICETYPE = "select apptype  as tenantid,\r\n"
    		+ "count(ebra.applicationno) as totalamt\r\n"
    		+ "from\r\n"
    		+ "		eg_bpa_regularization_application ebra ";
	public static final String REGULARIZATION_AVG_DAYS_PERMIT_ISSUE_BY_SERVICETYPE = "select apptype as tenantid,\r\n"
			+ "	avg((ebra.approvaldate-ebra.applicationdate)/ 86400000) as totalamt\r\n"
			+ "	from\r\n"
			+ "		eg_bpa_regularization_application ebra\r\n";

	public static final String REGULARIZATION_TENANT_WISE_TOTAL_APPLICATIONS = " select ebra.tenantid as tenantid, count(ebra.applicationno) as totalamt from eg_bpa_regularization_application ebra  ";


	public static final String REGULARIZATION_TOTAL_APPLICATIONS = " select count(ebra.applicationno) from eg_bpa_regularization_application ebra ";
			
	public static final String REGULARIZATION_AVG_DAYS = " select avg((approvaldate-applicationdate)/86400000) from eg_bpa_regularization_application ebra  ";
	public static final String REGULARIZATION_MIN_DAYS = " select min((approvaldate-applicationdate)/86400000) from eg_bpa_regularization_application ebra  ";
	public static final String REGULARIZATION_MAX_DAYS = " select max((approvaldate-applicationdate)/86400000) from eg_bpa_regularization_application ebra  ";

	public static final String REG_APPL_BREAKDOWN_1 = "select INITCAP(SPLIT_PART(internal.tenantid,'.', 2)) as ulb,sum(case when internal.status = 'Pending at Document Verification' then internal.internalcount else 0 end)\r\n"
			+ "			 pendingDocVerif,sum(case when internal.status = 'Pending at Field Inspection' then internal.internalcount else 0 end) pendingFieldInspection,\r\n"
			+ "			 sum(case when internal.status = 'Pending at Planning Assistant' then internal.internalcount else 0 end) pendingAtPlanningAssistant,\r\n"
			+ "			 sum(case when internal.status = 'Pending at Planning Officer' then internal.internalcount else 0 end) pendingAtPlanningOfficer,\r\n"
			+ "			 sum(case when internal.status = 'Pending at Planning Member' then internal.internalcount else 0 end) pendingAtPlanningMember,\r\n"
			+ "			 sum(case when internal.status = 'Pending at DPBP Committee' then internal.internalcount else 0 end) pendingAtDpbp,\r\n"
			+ "			 sum(case when internal.status = 'Pending for Citizen Action' then internal.internalcount else 0 end) pendingForCitizenAction,\r\n"
			+ "			 sum(case when internal.status = 'Pending for Permit Fee Payment' then internal.internalcount else 0 end)pendingSancFeePayment,\r\n"
			+ "			 sum(case when internal.status in ( 'Pending at Document Verification', 'Pending at Field Inspection', 'Pending at Planning Assistant', 'Pending at Planning Officer',\r\n"
			+ "			 'Pending at Planning Member', 'Pending at DPBP Committee', 'Pending for Citizen Action', 'Pending for Permit Fee Payment') then internal.internalcount else 0 end) totalApplicationReciceved\r\n"
			+ "			 from(select ebra.tenantid as tenantid,case when ebra.status = 'APPROVED' then 'Approved' when ebra.status = 'REJECTED' then 'Rejected'\r\n"
			+ "			 when ebra.status = 'DOC_VERIFICATION_INPROGRESS' then 'Pending at Document Verification' when ebra.status = 'FIELDINSPECTION_INPROGRESS' then 'Pending at Field Inspection'\r\n"
			+ "			 when ebra.status = 'APP_L1_VERIFICATION_INPROGRESS' then 'Pending at Planning Assistant' when ebra.status = 'APPROVAL_INPROGRESS'\r\n"
			+ "			 and ebra.businessservice in ('LR1', 'BLR1') then 'Pending at Planning Assistant'when ebra.status = 'APP_L2_VERIFICATION_INPROGRESS' then 'Pending at Planning Officer'\r\n"
			+ "			 when ebra.status = 'APPROVAL_INPROGRESS'and ebra.businessservice in ('LR2', 'BLR3') then 'Pending at Planning Officer'\r\n"
			+ "			 when ebra.status = 'APP_L3_VERIFICATION_INPROGRESS' then 'Pending at Planning Member'when ebra.status = 'APPROVAL_INPROGRESS'\r\n"
			+ "			 and ebra.businessservice in ('LR3','BLR3') then 'Pending at Planning Member'when ebra.status = 'APP_L4_VERIFICATION_INPROGRESS' then 'Pending at DPBP Committee'\r\n"
			+ "			 when ebra.status = 'APPROVAL_INPROGRESS'and ebra.businessservice in ('LR4', 'BLR4') then 'Pending at DPBP Committee'\r\n"
			+ "			 when ebra.status in ('CITIZEN_ACTION_PENDING_AT_DOC_VERIF', 'CITIZEN_ACTION_PENDING_AT_APP_L1_VERIF', 'CITIZEN_ACTION_PENDING_AT_APP_L2_VERIF',\r\n"
			+ "			 'CITIZEN_ACTION_PENDING_AT_APP_L3_VERIF', 'CITIZEN_ACTION_PENDING_AT_APPROVAL') then 'Pending for Citizen Action'\r\n"
			+ "			 when ebra.status = 'PENDING_SANC_FEE_PAYMENT' then 'Pending for Permit Fee Payment'end as status,count(ebra.applicationno) as internalcount\r\n"
			+ "			 from eg_bpa_regularization_application ebra";
	public static final String REG_APPL_BREAKDOWN_2 = " group by ebra.status, ebra.tenantid,ebra.businessservice having ebra.businessservice \r\n"
			+ "			 in ('LR1', 'LR2', 'LR3', 'LR4', 'BLR1', 'BLR2', 'BLR3', 'BLR4')and ebra.status not in('DELETED', 'INITIATED', 'CITIZEN_APPROVAL_INPROCESS',\r\n"
			+ "			'PENDING_APPL_FEE', 'INPROGRESS', 'PENDING_FORWARD', 'CONSTRUCT_START_INTIMATED', 'PLINTH_VERIFICATION_INPROGRESS', 'GROUNDFLR_VERIFICATION_INPROGRESS',\r\n"
			+ "			'TOPFLR_VERIFICATION_INPROGRESS', 'TOPFLR_VERIFICATION_COMPLETED') ) internal group by internal.tenantid\r\n"
			+ "			 " + " ";

	
	
	
	 
	public String getTotalApplicationByServiceType(RegularizationSearchCriteria regularizationSearchCriteria,
	            Map<String, Object> preparedStatementValues) {
	        StringBuilder selectQuery = new StringBuilder(REGULARIZATION_TOTAL_APPLICATION_RECEIVED_BY_SERVICETYPE);
	        addWhereClauseForApplicationReceived(selectQuery, preparedStatementValues, regularizationSearchCriteria);
	        addGroupByClause(selectQuery," apptype ");
	        return selectQuery.toString();
	    }
	    
	public String getApprovedApplicationByServiceType(RegularizationSearchCriteria regularizationSearchCriteria,
	            Map<String, Object> preparedStatementValues) {
	        StringBuilder selectQuery = new StringBuilder(REGULARIZATION_TOTAL_APPLICATION_RECEIVED_BY_SERVICETYPE);
	        addWhereClause(selectQuery, preparedStatementValues, regularizationSearchCriteria);
	        addGroupByClause(selectQuery," apptype ");
	        return selectQuery.toString();
	    }
	    
	public String getAvgDaysToIssuePermitByServiceType(RegularizationSearchCriteria regularizationSearchCriteria,
	            Map<String, Object> preparedStatementValues) {
	        StringBuilder selectQuery = new StringBuilder(REGULARIZATION_AVG_DAYS_PERMIT_ISSUE_BY_SERVICETYPE);
	        addWhereClause(selectQuery, preparedStatementValues, regularizationSearchCriteria);
	        addGroupByClause(selectQuery," ebra.apptype ");
	        return selectQuery.toString();
	    }

	
	private static String addWhereClause(StringBuilder selectQuery, Map<String, Object> preparedStatementValues,
			 RegularizationSearchCriteria searchCriteria) {
	        

			if (searchCriteria.getTenantIds() != null && !CollectionUtils.isEmpty(searchCriteria.getTenantIds())) {
				addClauseIfRequired(preparedStatementValues, selectQuery);
				selectQuery.append(" ebra.tenantid IN ( :tenantId )");
				preparedStatementValues.put("tenantId",searchCriteria.getTenantIds());
			}

			if (searchCriteria.getFromDate() != null) {
				addClauseIfRequired(preparedStatementValues, selectQuery);
				selectQuery.append(" ebra.approvaldate >= :fromDate");
				preparedStatementValues.put("fromDate", searchCriteria.getFromDate());
			}

			if (searchCriteria.getToDate() != null) {
				addClauseIfRequired(preparedStatementValues, selectQuery);
				selectQuery.append(" ebra.approvaldate <= :toDate");
				preparedStatementValues.put("toDate", searchCriteria.getToDate());
			}
			
			if (searchCriteria.getStatus() != null) {
				addClauseIfRequired(preparedStatementValues, selectQuery);
				selectQuery.append(" ebra.status IN (:status) ");
				preparedStatementValues.put("status", searchCriteria.getStatus());
			}
			
			if (searchCriteria.getSlaThreshold() != null) {
				addClauseIfRequired(preparedStatementValues, selectQuery);
				selectQuery.append(" ebra.approvaldate - ebra.applicationdate <= " + searchCriteria.getSlaThreshold());
			}

			if (searchCriteria.getExcludedTenantId() != null) {
				addClauseIfRequired(preparedStatementValues, selectQuery);
				selectQuery.append(" ebra.tenantId != :excludedTenantId");
				preparedStatementValues.put("excludedTenantId", searchCriteria.getExcludedTenantId());
			}
			

			if (searchCriteria.getStatusNotIn() != null) {
				addClauseIfRequired(preparedStatementValues, selectQuery);
				selectQuery.append(" ebra.status NOT IN (:status) ");
				preparedStatementValues.put("status", searchCriteria.getStatusNotIn());
			}
			
			if (searchCriteria.getBusinessServices() != null) {
				addClauseIfRequired(preparedStatementValues, selectQuery);
				selectQuery.append(" ebra.businessservice IN (:businessservice) ");
				preparedStatementValues.put("businessservice", searchCriteria.getBusinessServices());
			}
			
			if (searchCriteria.getRiskType() != null) {
				addClauseIfRequired(preparedStatementValues, selectQuery);
				selectQuery.append(" ebe.risktype = :riskType ");
				preparedStatementValues.put("riskType", searchCriteria.getRiskType());
			}
			

			return selectQuery.toString();

		}
	 
	private static void addGroupByClause(StringBuilder demandQueryBuilder,String columnName) {
	        demandQueryBuilder.append(" GROUP BY " + columnName);
	    }
	 
	private static void addClauseIfRequired(Map<String, Object> values, StringBuilder queryString) {
			if (values.isEmpty())
				queryString.append(" WHERE ");
			else {
				queryString.append(" AND");
			}
		}
	 
	private static String addWhereClauseForApplicationReceived(StringBuilder selectQuery, Map<String, Object> preparedStatementValues,
				RegularizationSearchCriteria searchCriteria) {
	        

			if (searchCriteria.getTenantIds() != null && !CollectionUtils.isEmpty(searchCriteria.getTenantIds())) {
				addClauseIfRequired(preparedStatementValues, selectQuery);
				selectQuery.append(" ebra.tenantid IN ( :tenantId )");
				preparedStatementValues.put("tenantId",searchCriteria.getTenantIds());
			}

			if (searchCriteria.getFromDate() != null) {
				addClauseIfRequired(preparedStatementValues, selectQuery);
				selectQuery.append(" ebra.applicationdate >= :fromDate");
				preparedStatementValues.put("fromDate", searchCriteria.getFromDate());
			}

			if (searchCriteria.getToDate() != null) {
				addClauseIfRequired(preparedStatementValues, selectQuery);
				selectQuery.append(" ebra.applicationdate <= :toDate");
				preparedStatementValues.put("toDate", searchCriteria.getToDate());
			}
			
			if (searchCriteria.getStatus() != null) {
				addClauseIfRequired(preparedStatementValues, selectQuery);
				selectQuery.append(" ebra.status IN (:status) ");
				preparedStatementValues.put("status", searchCriteria.getStatus());
			}
			
			if (searchCriteria.getSlaThreshold() != null) {
				addClauseIfRequired(preparedStatementValues, selectQuery);
				selectQuery.append(" ebra.lastmodifiedtime - ebra.createdtime < " + searchCriteria.getSlaThreshold());
			}

			if (searchCriteria.getExcludedTenantId() != null) {
				addClauseIfRequired(preparedStatementValues, selectQuery);
				selectQuery.append(" ebra.tenantId != :excludedTenantId");
				preparedStatementValues.put("excludedTenantId", searchCriteria.getExcludedTenantId());
			}
			

			if (searchCriteria.getStatusNotIn() != null) {
				addClauseIfRequired(preparedStatementValues, selectQuery);
				selectQuery.append(" ebra.status NOT IN (:status) ");
				preparedStatementValues.put("status", searchCriteria.getStatusNotIn());
			}
			
			if (searchCriteria.getBusinessServices() != null) {
				addClauseIfRequired(preparedStatementValues, selectQuery);
				selectQuery.append(" ebra.businessservice IN (:businessservice) ");
				preparedStatementValues.put("businessservice", searchCriteria.getBusinessServices());
			}
			
			

			return selectQuery.toString();

		}
	 
	private static String addWhereClauseforPendingApplication(StringBuilder selectQuery, Map<String, Object> preparedStatementValues,
			RegularizationSearchCriteria searchCriteria) {
        

		if (searchCriteria.getTenantIds() != null && !CollectionUtils.isEmpty(searchCriteria.getTenantIds())) {
			addClauseIfRequired(preparedStatementValues, selectQuery);
			selectQuery.append(" ebra.tenantid IN ( :tenantId )");
			preparedStatementValues.put("tenantId",searchCriteria.getTenantIds());
		}

		if (searchCriteria.getFromDate() != null) {
			addClauseIfRequired(preparedStatementValues, selectQuery);
			selectQuery.append(" ebra.lastmodifiedtime >= :fromDate");
			preparedStatementValues.put("fromDate", searchCriteria.getFromDate());
		}

		if (searchCriteria.getToDate() != null) {
			addClauseIfRequired(preparedStatementValues, selectQuery);
			selectQuery.append(" ebra.lastmodifiedtime <= :toDate");
			preparedStatementValues.put("toDate", searchCriteria.getToDate());
		}
		
		if (searchCriteria.getStatus() != null) {
			addClauseIfRequired(preparedStatementValues, selectQuery);
			selectQuery.append(" ebra.status IN (:status) ");
			preparedStatementValues.put("status", searchCriteria.getStatus());
		}
		
		if (searchCriteria.getSlaThreshold() != null) {
			addClauseIfRequired(preparedStatementValues, selectQuery);
			selectQuery.append(" ebra.lastmodifiedtime - ebra.createdtime < " + searchCriteria.getSlaThreshold());
		}

		if (searchCriteria.getExcludedTenantId() != null) {
			addClauseIfRequired(preparedStatementValues, selectQuery);
			selectQuery.append(" ebra.tenantId != :excludedTenantId");
			preparedStatementValues.put("excludedTenantId", searchCriteria.getExcludedTenantId());
		}
		

		if (searchCriteria.getStatusNotIn() != null) {
			addClauseIfRequired(preparedStatementValues, selectQuery);
			selectQuery.append(" ebra.status NOT IN (:status) ");
			preparedStatementValues.put("status", searchCriteria.getStatusNotIn());
		}
		
		if (searchCriteria.getDeleteStatus() != null) {
			addClauseIfRequired(preparedStatementValues, selectQuery);
			selectQuery.append(" ebra.status != :deletedStatus");
			preparedStatementValues.put("deletedStatus", searchCriteria.getDeleteStatus());
		}
		
		if (searchCriteria.getBusinessServices() != null) {
			addClauseIfRequired(preparedStatementValues, selectQuery);
			selectQuery.append(" ebra.businessservice IN (:businessservice) ");
			preparedStatementValues.put("businessservice", searchCriteria.getBusinessServices());
		}
		

		return selectQuery.toString();

	}
    
	public String getGeneralQuery(RegularizationSearchCriteria regularizationSearchCriteria, Map<String, Object> preparedStatementValues) {
		
		StringBuilder selectQuery = new StringBuilder(REGULARIZATION_TOTAL_APPLICATIONS);
		addWhereClauseForApplicationReceived(selectQuery, preparedStatementValues, regularizationSearchCriteria);

		return selectQuery.toString();
	}
	
	public static String getTotalRegularizationCertificateIssued(RegularizationSearchCriteria criteria, Map<String, Object> preparedStatementValues) {
		StringBuilder selectQuery = new StringBuilder(REGULARIZATION_TOTAL_APPLICATIONS);
		return addWhereClause(selectQuery, preparedStatementValues, criteria); 
	}
	
	public String getPendingApplicationQuery(RegularizationSearchCriteria regularizationSearchCriteria,
			Map<String, Object> preparedStatementValues) {
		StringBuilder selectQuery = new StringBuilder(REGULARIZATION_TOTAL_APPLICATIONS);
		addWhereClauseforPendingApplication(selectQuery, preparedStatementValues, regularizationSearchCriteria);
		return selectQuery.toString();
	}
	
	public String getAvgDaysToIssueCertificateQuery(RegularizationSearchCriteria regularizationSearchCriteria,
			Map<String, Object> preparedStatementValues) {
		
		StringBuilder selectQuery = new StringBuilder(REGULARIZATION_AVG_DAYS);
		addWhereClause(selectQuery, preparedStatementValues, regularizationSearchCriteria);

		return selectQuery.toString();
	}
	
	public String getMinDaysToIssueCertificateQuery(RegularizationSearchCriteria regularizationSearchCriteria,
			Map<String, Object> preparedStatementValues) {
		
		StringBuilder selectQuery = new StringBuilder(REGULARIZATION_MIN_DAYS);
		addWhereClause(selectQuery, preparedStatementValues, regularizationSearchCriteria);

		return selectQuery.toString();
	}
	
	public String getMaxDaysToIssueCertificateQuery(RegularizationSearchCriteria regularizationSearchCriteria,
			Map<String, Object> preparedStatementValues) {
		
		StringBuilder selectQuery = new StringBuilder(REGULARIZATION_MAX_DAYS);
		addWhereClause(selectQuery, preparedStatementValues, regularizationSearchCriteria);

		return selectQuery.toString();
	}


	public String getTenantWiseRegularizationApplicationQuery(RegularizationSearchCriteria regularizationSearchCriteria,
            Map<String, Object> preparedStatementValues) {
        StringBuilder selectQuery = new StringBuilder(REGULARIZATION_TENANT_WISE_TOTAL_APPLICATIONS);
        addWhereClauseForApplicationReceived(selectQuery, preparedStatementValues, regularizationSearchCriteria);
        addGroupByClause(selectQuery," ebra.tenantid ");
        return selectQuery.toString();
    }
   
   

public String getTenantWisePermitIssuedQuery(RegularizationSearchCriteria regularizationSearchCriteria,
            Map<String, Object> preparedStatementValues) {
        StringBuilder selectQuery = new StringBuilder(REGULARIZATION_TENANT_WISE_TOTAL_APPLICATIONS);
        addWhereClause(selectQuery, preparedStatementValues, regularizationSearchCriteria);
        addGroupByClause(selectQuery," ebra.tenantid ");
        return selectQuery.toString();
    }
   
    
    public String getTenantWiseAvgPermitIssue(RegularizationSearchCriteria regularizationSearchCriteria,
            Map<String, Object> preparedStatementValues) {
        StringBuilder selectQuery = new StringBuilder(REGULARIZATION_TENANT_WISE_AVG_DAYS_PERMIT_ISSUED);
        addWhereClause(selectQuery, preparedStatementValues, regularizationSearchCriteria);
        addGroupByClause(selectQuery," ebra.tenantid ");
        return selectQuery.toString();
    }
    
    public String getTenantWiseRegularizationPendingApplication(RegularizationSearchCriteria regularizationSearchCriteria,
            Map<String, Object> preparedStatementValues) {
        StringBuilder selectQuery = new StringBuilder(REGULARIZATION_TENANT_WISE_TOTAL_APPLICATIONS);
        addWhereClauseforPendingApplication(selectQuery, preparedStatementValues, regularizationSearchCriteria);
        addGroupByClause(selectQuery,"ebra.tenantid ");
        return selectQuery.toString();
    }

	public String getApplicationsBreakdownQuery(RegularizationSearchCriteria criteria,
			Map<String, Object> preparedStatementValues) {
		StringBuilder selectQuery = new StringBuilder(REG_APPL_BREAKDOWN_1);
		addWhereClauseforPendingApplication(selectQuery, preparedStatementValues, criteria);
		selectQuery.append(REG_APPL_BREAKDOWN_2);
		return selectQuery.toString();
	}

}

