package org.egov.report.repository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.egov.report.config.ReportServiceConfiguration;
import org.egov.report.model.UtilityReportDetails;
import org.egov.report.model.UtilityReportSearchCriteria;
import org.egov.report.producer.KafkaProducer;
import org.egov.report.repository.rowmapper.UtilityReportDetailsRowMapper;
import org.egov.report.web.model.UtilityReportRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.util.ObjectUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Repository
public class BPAReportRepository {
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Autowired
	private KafkaProducer producer;
	
	@Autowired
	private ReportServiceConfiguration config;
	
	@Autowired
	private UtilityReportDetailsRowMapper rowMapper;
	
	
	/**
	 * Repository for get All Payments Report from db
	 * @param searchCriteria 
	 * 
	 * @return List of Payments Report
	 */
	public List<Map<String, Object>> getAllPaymentsReport(UtilityReportSearchCriteria searchCriteria) {
		log.info("Search Criteria : " + searchCriteria.toString());
		StringBuilder query =  new StringBuilder("select INITCAP(SPLIT_PART(ebb.tenantid,'.',2)) as ulb,ebb.applicationno as applicationno,eu.name as name,eu.mobilenumber as mobilenumber,ebb.status as currentstatus,case when eb.businessservice in ('BPA.NC_OC_SAN_FEE','BPA.NC_SAN_FEE') then 'Permit Fee' when eb.businessservice in ('BPA.NC_OC_APP_FEE','BPA.NC_APP_FEE') then 'Application Fee' end as feetype,ep.totalamountpaid as amount,to_char(to_timestamp(epd.receiptdate / 1000), 'YYYY-MM-DD') as paymentdate,ep.transactionnumber as receiptno "
	    		+ "from egcl_payment ep inner join egcl_paymentdetail epd on ep.id=epd.paymentid "
	    		+ "inner join egcl_bill eb on epd.billid=eb.id "
	    		+ "left outer join eg_bpa_buildingplan ebb on eb.consumercode=ebb.applicationno "
	    		+ "inner join eg_land_ownerinfo elo on ebb.landid=elo.landinfoid and (elo.isprimaryowner = 'true' or elo.isprimaryowner is null) "
	    		+ "inner join eg_user eu on elo.uuid=eu.uuid "
	    		+ "where ep.totalamountpaid <> 0 and ebb.tenantid != 'od.testing' and ebb.status != 'DELETED'  and ep.paymentstatus not in ('CANCELLED', 'DISHONOURED')");
	    
		if (!ObjectUtils.isEmpty(searchCriteria.getTenantId())) {
			query.append(" and ebb.tenantid = '" + searchCriteria.getTenantId() + "'");
		}
		
		if (!ObjectUtils.isEmpty(searchCriteria.getFromDate())) {
			query.append(" and to_timestamp(ebb.createdtime / 1000) :: date at time zone 'Asia/Kolkata' >= to_timestamp(" + searchCriteria.getFromDate() + "/ 1000) :: date at time zone 'Asia/Kolkata'");
		}
		
		if (!ObjectUtils.isEmpty(searchCriteria.getToDate())) {
			query.append(" and to_timestamp(ebb.createdtime / 1000) :: date at time zone 'Asia/Kolkata' < to_timestamp(" + searchCriteria.getToDate() + " / 1000) :: date at time zone 'Asia/Kolkata'");
		}
	    log.info("Query for Payment Report search: " + query);
	    
	    List<Map<String, Object>> paymentsDetailsList =  jdbcTemplate.queryForList(query.toString());
	    
	    if(paymentsDetailsList.isEmpty())
			return Collections.emptyList();
		return paymentsDetailsList;
	}

	
	
	/**
	 * Repository for get All Applications Report from db
	 * @param searchCriteria 
	 * 
	 * @return List of Applications Report
	 */
	public List<Map<String, Object>> getAllApplicationsReport(UtilityReportSearchCriteria searchCriteria) {
		
		StringBuilder query =  new StringBuilder("select  distinct on (ebb.applicationno) INITCAP(SPLIT_PART(ebb.tenantid, '.', 2)) as ULB,   ebb.applicationno as applicationno, "
				+ "case when ebb.status = 'APPROVED' then 'Approved'   "
				+ "when ebb.status = 'REJECTED' then 'Rejected'   "
				+ "when ebb.status = 'DOC_VERIFICATION_INPROGRESS' then 'Pending For Document Verification' "
				+ "when ebb.status = 'FIELDINSPECTION_INPROGRESS' then 'Pending at Field Inspection'  "
				+ "when ebb.status = 'APP_L1_VERIFICATION_INPROGRESS' then 'L1 Verification In-Progress'  "
				+ "when ebb.status = 'APPROVAL_INPROGRESS' then 'Approval In-Progress'  "
				+ "when ebb.status = 'APP_L2_VERIFICATION_INPROGRESS' then 'L2 Verification In-Progress'  "
				+ "when ebb.status = 'APP_L3_VERIFICATION_INPROGRESS' then 'L3 Verification In-Progress'  "
				+ "when ebb.status = 'APP_L4_VERIFICATION_INPROGRESS' then 'L4 Verification In-Progress'  "
				+ "when ebb.status = 'CITIZEN_ACTION_PENDING_AT_APPROVAL' then 'Citizen Action Pending At Approver'  "
				+ "when ebb.status = 'CITIZEN_ACTION_PENDING_AT_DOC_VERIF' then 'Citizen Action Pending at Document Verifier' "
				+ "when ebb.status = 'CITIZEN_ACTION_PENDING_AT_APP_L1_VERIF' then 'Citizen Action Pending at L1 verifier'    "
				+ "when ebb.status = 'CITIZEN_ACTION_PENDING_AT_APP_L2_VERIF' then 'Citizen Action Pending at L2 verifier'    "
				+ "when ebb.status = 'CITIZEN_ACTION_PENDING_AT_APP_L3_VERIF' then 'Citizen Action Pending at L3 verifier'    "
				+ "when ebb.status = 'PENDING_ARCHITECT_ACTION_FOR_REWORK' then 'Pending at Architect For Rework' "
				+ "when ebb.status = 'PENDING_SANC_FEE_PAYMENT' then 'Pending for Permit fee payment'  "
				+ "when ebb.status = 'PERMIT_REVOKED' then 'PERMIT REVOKED'  "
				+ "when ebb.status = 'SHOW_CAUSE_ISSUED' then 'SHOW CAUSE ISSUED' "
				+ "when ebb.status = 'SHOW_CAUSE_REPLY_VERIFICATION_PENDING' then 'SHOW CAUSE REPLY VERIFICATION PENDING'   "
				+ "when ebb.status = 'APP_L2_PENDING_DEPT_VERIFICATION_INPROGRESS' then 'L2 Pending Dept Verification In-Progress'   "
				+ "when ebb.status = 'APP_L2_PENDING_DEPT_VERIFICATION_INPROGRESS' then 'L3 Pending Dept Verification In-Progress'   "
				+ "when ebb.status = 'APP_L2_PENDING_DEPT_VERIFICATION_INPROGRESS' then 'L4 Pending Dept Verification In-Progress' end as status, eu.name as applicantname,   eu2.name as approvalName,   ebb.approvalno as permitnumber, eu.mobilenumber as applicantmobilenumber,   ebed.mauza as mauza, "
				+ "case when ebed.risktype = 'HIGH' then 'OTHER THAN LOW'       else 'LOW'  end as risktype,  "
				+ "case when ebed.alterationsubservice  = 'ALTERATION_SERVICE_A' then 'Addition & Alteration (ALTERATION_SERVICE_A)'        else ebed.servicetype   end as servicetype, ebed.plotnumber as plotnumber,  ebed.workflowname as Workflow,  "
				+ "case when ebed.isbuaabove500 = true then 'YES'     when ebed.isbuaabove500 = false then 'NO'   end as isbuaabove500,   euarchitect.name as architectname,  euarchitect.mobilenumber as architectmobilenumber,  ebb.status as currentstatus,    "
				+ "case when ebb.status = 'APPROVED' then null when ebb.status = 'REJECTED' then null else ceiling(((extract(EPOCH from (select NOW())) * 1000)-ewpcs.createdtime)/ cast(86400000 as float)) end as pendingdayssincecurrentstatus, eu1.name as officernameatpending, "
				+ "case when ebb.applicationdate<>0 then to_char(to_timestamp(ebb.applicationdate / 1000), 'YYYY-MM-DD') end as submissiondate,    "
				+ "case when ebb.status = 'REJECTED' then ceiling( (ebb.lastmodifiedtime - ebb.applicationdate)/ cast(86400000 as float) ) when ( ebb.status = 'APPROVED' and ebb.approvaldate != 0 and ebb.approvaldate is not null ) then ceiling( (ebb.approvaldate - ebb.applicationdate)/ cast(86400000 as float) )  else ceiling( ( ( extract( EPOCH from ( select NOW() ) ) * 1000 )- ewp2.createdtime )/ cast(86400000 as float)) end as pendingdayssincesubmission, "
				+ "case when ebb.status = 'APPROVED' and ebb.approvaldate = 0 then null when ebb.status = 'APPROVED' then to_char(to_timestamp(ebb.approvaldate / 1000), 'YYYY-MM-DD')  else null   end as approvaldate,    ebed.totalbuiltuparea as totalbuiltuparea,  ebed.totalplotarea as totalplotarea,    ebed.maxbuildingheight as maxbuildingheight,    ebed.alterationsubservice as alterationsubservice "
				+ "from eg_bpa_buildingplan ebb inner join eg_land_ownerinfo elo on  ebb.landid = elo.landinfoid and ebb.businessservice in ('BPA1', 'BPA2', 'BPA3', 'BPA4', 'BPA5', 'BPA6') and ebb.status != 'DELETED' "
				+ "inner join eg_user eu on    elo.uuid = eu.uuid "
				+ "inner join eg_land_landinfo ell on   ebb.landid = ell.id "
				+ "inner join eg_land_address ela on ell.id = ela.landinfoid "
				+ "inner join eg_user euarchitect on ebb.accountid = euarchitect.uuid "
				+ "inner join (select distinct on (businessid) *  from    eg_wf_processinstance_v2 order by businessid,   createdtime desc) as ewpcs on   ewpcs.businessid = ebb.applicationno "
				+ "left outer join eg_wf_processinstance_v2 ewp2 on ewp2.businessid = ebb.applicationno   and ewp2.action = 'APPLY' "
				+ "left outer join eg_wf_assignee_v2 ewa1 on ewpcs.id = ewa1.processinstanceid "
				+ "left outer join eg_bpa_dscdetails ebd on ebb.applicationno = ebd.applicationno "
				+ "left outer join eg_user eu1 on   ewa1.assignee = eu1.uuid "
				+ "left outer join eg_user eu2 on ebd.approvedby = eu2.uuid "
				+ "left outer join eg_bpa_edcrdata ebed on ebb.applicationno = ebed.applicationno "
				+ "where ebb.tenantid != 'od.testing' "
				+ "AND ebb.status not IN ('INITIATED','CITIZEN_APPROVAL_INPROCESS','PENDING_APPL_FEE','INPROGRESS','PENDING_FORWARD','CONSTRUCT_START_INTIMATED','PLINTH_VERIFICATION_INPROGRESS','GROUNDFLR_VERIFICATION_INPROGRESS','TOPFLR_VERIFICATION_INPROGRESS','TOPFLR_VERIFICATION_COMPLETED')") ;
	    
		if (!ObjectUtils.isEmpty(searchCriteria.getTenantId())) {
			query.append(" and ebb.tenantid = " + searchCriteria.getTenantId());
		}
		
		if (!ObjectUtils.isEmpty(searchCriteria.getFromDate())) {
			query.append(" and to_timestamp(ebb.createdtime / 1000) :: date at time zone 'Asia/Kolkata' >= to_timestamp(" + searchCriteria.getFromDate() + "/ 1000) :: date at time zone 'Asia/Kolkata'");
		}
		
		if (!ObjectUtils.isEmpty(searchCriteria.getToDate())) {
			query.append(" and to_timestamp(ebb.createdtime / 1000) :: date at time zone 'Asia/Kolkata' < to_timestamp(" + searchCriteria.getToDate() + " / 1000) :: date at time zone 'Asia/Kolkata'");
		}
		log.info("Query for Applications Report search: " + query);
		
	    List<Map<String, Object>> applicationsDetailsList =  jdbcTemplate.queryForList(query.toString());
	    
	    if(applicationsDetailsList.isEmpty())
			return Collections.emptyList();
		return applicationsDetailsList;
	}



	public List<UtilityReportDetails> isReportExist(String reportType) {
		String query = "SELECT * FROM eg_bpa_utility_reports WHERE reporttype = '" + reportType +"' ORDER BY lastmodifiedtime DESC;";
	    log.info("Query for Applications Report search:", query);
	    
	    List<UtilityReportDetails> reportDetailsList =  jdbcTemplate.query(query, rowMapper);
	    if(reportDetailsList.isEmpty())
			return new ArrayList<>();
	    return reportDetailsList;
	}



	public void saveReportDetails(UtilityReportRequest utilityReportRequest) {
		producer.push(config.getSaveUtilityReportTopic(), utilityReportRequest);
	}



	public void updateReportDetails(UtilityReportRequest utilityReportRequest) {
		producer.push(config.getUpdateUtilityReportTopic(), utilityReportRequest);
	}
	
}
