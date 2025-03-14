package org.egov.report.repository.builder;

import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.egov.report.repository.WSReportRepository;
import org.egov.report.util.WSReportUtils;
import org.egov.report.web.model.DemandCriteria;
import org.egov.report.web.model.PTAssessmentSearchCriteria;
import org.egov.report.web.model.PropertyDetailsSearchCriteria;
import org.egov.report.web.model.TradeLicenseSearchCriteria;
import org.egov.report.web.model.WSReportSearchCriteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import lombok.extern.slf4j.Slf4j;



@Component
@Slf4j
public class ReportQueryBuilder {
	
	@Autowired
	WSReportUtils wsReportUtils;
	
	private static final String SELECT = "SELECT ";
	private static final String INNER_JOIN = " INNER JOIN ";
	private static final String AND_QUERY = " AND ";
	private static final String GROUP_BY = " group by ";
	private static final String ORDER_BY = " order by ";
	private static final String connectionSelectValues = " ewc.tenantid,ewc.additionaldetails->>'ward' as ward,ewc.connectionno,ewc.oldconnectionno,";
	private static final String serviceSelectValues = " ews.connectiontype,ews.connectioncategory,ews.usagecategory,ews.connectionfacility,";
	private static final String userSelectValues = " ch.uuid ";
	
	private static final String connectionSelect = " ewc.tenantid,ewc.additionaldetails->>'ward' as ward,ewc.connectionno, ewc.applicationno, ewc.additionaldetails ->> 'estimationLetterDate' as estimationletterdate, ";
	private static final String serviceSelect = " ews.connectiontype, ews.connectionexecutiondate, ews.connectioncategory, ews.usagecategory, ews.connectionfacility, ";
	private static final String connectionHolderSelect = " holder.userid ";
	
	private static final String demandSelectValues = " d.tenantid ,d.consumercode, d.taxperiodfrom , d.taxperiodto ,d.ispaymentcompleted,";
	private static final String demanddetailSelect = " sum(dd.taxamount) as totalTax,sum(dd.collectionamount) as totalCollected ";
	
	private static final String detailsSelectValues = " demanddetails.taxheadcode, demanddetails.taxamount, demanddetails.collectionamount ";

	private static final String paymentSelectValues = " payment.transactiondate, payment.paymentmode, payment.totalamountpaid, ";
	private static final String paymentDetailsSelectValues = " paymentd.createdby, paymentd.receiptnumber ";
	
	private static final String HRMS_QUERY = "select id, tenantid from eg_hrms_employee ";
	
	private static final String QUERY_FOR_CONSUMER_MASTER_WS_REPORT = SELECT 
			+ connectionSelectValues
			+ serviceSelectValues
			+ userSelectValues
			+ "from eg_ws_connection ewc"
			+ INNER_JOIN + " eg_ws_service ews on ewc.tenantid = ? and ewc.id = ews.connection_id "
			+ INNER_JOIN + " eg_ws_connectionholder esc on esc.connectionid = ewc.id "
			+ INNER_JOIN + " eg_user ch on ch.uuid = esc.userid "
			+ " WHERE " + "ewc.applicationstatus = 'CONNECTION_ACTIVATED' and ewc.isoldapplication = false ";

	private static final String FROM = " from ";
	private static final String WHERE = "where ";
	private static final String AND = "and ";
	private static final String ON = "on ";
	private static final String AS = "as ";
	private static final String IN = "in ";
//	private static final String INNER_JOIN = "join ";
	private static final String DEMAND_TABLE_COLUMNS = "tenantid,edv.consumercode,to_char(to_timestamp(edv.taxperiodfrom / 1000), 'MM-YYYY') as monthYear ";
	private static final String ULB_AND_MONTH = "demand.TENANTID as ulb,demand.monthYear ";
	private static final String MONTH_YEAR_CLAUSE = "(to_char(to_timestamp(edv.taxperiodfrom / 1000), 'MM')= ? and to_char(to_timestamp(edv.taxperiodfrom / 1000), 'YYYY')= ? ) ";
	private static final String LEFT_OUTER_JOIN = "left outer join ";
	private static final String TENANT_ID = "ewc.tenantid ";
	private static final String demandsSelectValues = " d.tenantid ,d.consumercode, d.taxperiodfrom , d.taxperiodto";
    private static final String connectionsSelectValues = " ewc.additionaldetails->>'ward' as ward,ewc.oldconnectionno,";
	
	private static final String wsSelectValues = "connectionno";
	
	private static final String DEMAND_QUERY =  "edv.consumercode,edv.taxperiodfrom,edv.taxperiodto,"
			+ "edv.createdtime from egbs_demand_v1 edv ";
	
	private static final String WS_CONNECTION_DETAILS_QUERY = SELECT + wsSelectValues 
			+ FROM + " eg_ws_connection ewc "
			+ INNER_JOIN + " eg_ws_service ews on ewc.tenantid= ? "
			+ AND +" ewc.id = ews.connection_id "
			+ WHERE + " ews.connectiontype = 'Non Metered' and ewc.applicationstatus = 'CONNECTION_ACTIVATED' and ewc.isoldapplication = false ";
	
	private static final String SCHEDULER_GENERATED_DEMANDS_QUERY = SELECT 
			+ "ewc2.tenantid ,EWC2.ADDITIONALDETAILS->>'ward' as ward,"
			+ "ewc2.oldconnectionno,ews2.connectiontype, "
			+ DEMAND_QUERY 
			+ INNER_JOIN + " eg_ws_connection ewc2 on ewc2.tenantid= ? and EWC2.connectionno=edv.CONSUMERCODE and  ewc2.applicationstatus ='CONNECTION_ACTIVATED' and ewc2.isoldapplication = false "
			+ INNER_JOIN + " eg_ws_service ews2 on ewc2.id = ews2.connection_id "
			+ WHERE + " businessservice = ? "
			+ AND + "consumercode " + IN +" ( " + WS_CONNECTION_DETAILS_QUERY +" )"
			+ AND +" ( taxperiodfrom >= ? "
			+ AND + "taxperiodfrom <= ? ) ";
	
	private static final String BILL_SUMMARY_QUERY2 = SELECT
			+ ULB_AND_MONTH + FROM +"( "
			+ SELECT + DEMAND_TABLE_COLUMNS + FROM + " egbs_demand_v1 edv "
			+ INNER_JOIN
			+ "	(  select distinct ewc.connectionno"
			+ "	   from eg_ws_connection ewc "
			+ "	   inner join eg_ws_service ews on ewc.id = ews.connection_id "
			+ "	   where ews.connectiontype = 'Non Metered' "
			+ "	   and ewc.applicationstatus = 'CONNECTION_ACTIVATED' "
			+ "	   and ewc.isoldapplication = false "
			+ "			                 ) " + AS + " rtable "
			+ ON
			+ "rtable.connectionno = edv.consumercode " + WHERE + MONTH_YEAR_CLAUSE + ") "
			+ AS + "demand ";
	
	private static final String PROPERTY_DETAILS_SUMMARY_QUERY = SELECT
			+ "epp.tenantid,epa.ward,epp.oldpropertyid,epp.propertyid,epp.ddnno,epp.legacyholdingno,epp.ownershipcategory,epp.propertytype,epp.usagecategory,eu.uuid,"
			+ "epa.doorno,epa.buildingname,epa.street,epa.city,epa.pincode "
			+ FROM
			+ "eg_pt_property epp "
			+ INNER_JOIN + "eg_pt_owner epo " +  ON  + "epo.propertyid = epp.id and epo.status='ACTIVE' "
			+ LEFT_OUTER_JOIN + "eg_user eu on eu.uuid = epo.userid "
			+ INNER_JOIN + "eg_pt_address epa on epa.propertyid = epp.id "
			+ WHERE + "epp.status <> 'INACTIVE' "
			+ AND + "epp.tenantid = ? ";
	
	private static final String PROPERTY_DEMANDS_QUERY = SELECT 
			+ "consumercode,edv.id,payer ,edv.createdby ,taxperiodfrom ,taxperiodto,eu.uuid,"
			+ "edv.tenantid ,edv.status,edv2.taxamount ,edv2.collectionamount,epp.oldpropertyid,epp.ddnno,epp.legacyholdingno,epa.ward "
			+ FROM + " egbs_demand_v1 edv "
			+ INNER_JOIN + "eg_pt_property epp on edv.consumercode = epp.propertyid " +  AND + "epp.status = 'ACTIVE' "
			+ INNER_JOIN +" eg_pt_address epa on epp.id =epa.propertyid "
			+ INNER_JOIN + "eg_pt_owner epo " +  ON  + "epo.propertyid = epp.id " 
			+ LEFT_OUTER_JOIN + "eg_user eu on eu.uuid = epo.userid "
			+ INNER_JOIN + " egbs_demanddetail_v1 edv2 on edv.id=edv2.demandid  "
			+ WHERE + "edv.tenantid= ? " + AND + " edv2.tenantid= ? " + AND + " edv.status <> 'CANCELLED' "
			+ AND ;
	
	private static final String QUERY_FOR_WATER_NEW_CONSUMER = SELECT 
			+ connectionSelect
			+ serviceSelect 
			+ connectionHolderSelect
			+ FROM + " eg_ws_connection ewc "
			+ INNER_JOIN + " eg_ws_service ews on ewc.id = ews.connection_id "
			+ INNER_JOIN + " eg_ws_connectionholder holder on holder.connectionid = ewc.id "
			+ WHERE + " ewc.applicationstatus = 'CONNECTION_ACTIVATED' and ewc.applicationtype = 'NEW_CONNECTION' ";

	private static final String QUERY_FOR_CONSUMER_BILL_HISTORY = SELECT
			+ demandSelectValues 
			+ demanddetailSelect 
			+ FROM + " egbs_demand_v1 d "
			+ INNER_JOIN + " egbs_demanddetail_v1 dd on d.id = dd.demandid "
			+ WHERE ;
	
	private static final String DEMANDS_QUERY = SELECT 
			+ "consumercode,edv.id,payer ,edv.createdby ,taxperiodfrom ,taxperiodto,"
			+ "edv.tenantid ,status,edv2.taxamount ,edv2.collectionamount "
			+ FROM + " egbs_demand_v1 edv "
			+ INNER_JOIN + " egbs_demanddetail_v1 edv2 on edv.tenantid= ?  "
			+ AND + "edv2.tenantid= ? " + AND + " edv.id=edv2.demandid  " + AND + " edv.status <> 'CANCELLED' "
			+ AND ;
      
	private static final String QUERY_FOR_WATER_MONTHLY_DEMAND = SELECT 
			+ demandSelectValues + " d.id, "
			+ detailsSelectValues 
			+ FROM + " egbs_demand_v1 d "
			+ INNER_JOIN + " egbs_demanddetail_v1 demanddetails on d.id = demanddetails.demandid "
			+ INNER_JOIN + " eg_ws_connection ewc on ewc.connectionno = d.consumercode "
			+ WHERE + " d.businessservice = 'WS' " + AND_QUERY + " d.status = 'ACTIVE' "
			+ AND_QUERY + " ewc.isoldapplication = false " + AND_QUERY + " ewc.applicationstatus = 'CONNECTION_ACTIVATED' ";

	private static final String QUERY_FOR_WS_CONNECTION = SELECT 
			+ connectionSelectValues 
			+ " ews.connectiontype, ewc2.userid "
			+ FROM + " eg_ws_connection ewc "
			+ INNER_JOIN + " eg_ws_service ews on ewc.id = ews.connection_id "
			+ INNER_JOIN + " eg_ws_connectionholder ewc2 on ewc.id = ewc2.connectionid "
			+ WHERE + " ewc.isoldapplication = false " + AND_QUERY + " ewc.applicationstatus = 'CONNECTION_ACTIVATED' ";
	
	private static final String WS_CONNECTIONS_QUERY = SELECT 
			+ connectionSelectValues 
			+ " ews.connectiontype, ewc2.userid, ews.usagecategory "
			+ FROM + " eg_ws_connection ewc "
			+ INNER_JOIN + " eg_ws_service ews on ewc.id = ews.connection_id "
			+ INNER_JOIN + " eg_ws_connectionholder ewc2 on ewc.id = ewc2.connectionid "
			+ WHERE + " ewc.isoldapplication = false " + AND_QUERY + " ewc.applicationstatus = 'CONNECTION_ACTIVATED' ";
	
	private static final String QUERY_FOR_EMPLOYEE_WISE_WS = SELECT 
			+ connectionSelectValues
			+ paymentSelectValues 
			+ paymentDetailsSelectValues
			+ FROM + " eg_ws_connection ewc "
			+ INNER_JOIN + " egcl_bill bill on ewc.connectionno = bill.consumercode "
			+ INNER_JOIN + " egcl_paymentdetail paymentd on paymentd.billid = bill.id "
			+ INNER_JOIN + " egcl_payment payment on payment.id = paymentd.paymentid "
			+ WHERE + " ewc.isoldapplication = false "
			+ AND + " ewc.applicationstatus ='CONNECTION_ACTIVATED' ";
	
	private static final String WS_CONNECTIONS_ELEGIBLE_FOR_DEMAND_QUERY = SELECT
			+ "EWC.TENANTID ,EWC.ADDITIONALDETAILS->>'ward' as ward,count(ewc.connectionno) as connectionscount "
			+ FROM + " eg_ws_connection ewc "
			+ INNER_JOIN + " eg_ws_service ews  on ewc.id = ews.connection_id "
			+ WHERE + " ews.connectiontype ='Non Metered' "
			+ AND + " ewc.applicationstatus ='CONNECTION_ACTIVATED' "+ AND +" ewc.isoldapplication =false "+ AND +" ewc.tenantid = ?"
			+ GROUP_BY + " ewc.tenantid,EWC.ADDITIONALDETAILS->>'ward'; ";
	
	private static final String QUERY_TO_GET_DEMANDS =  SELECT + " distinct(consumercode)" 
			+ "from egbs_demand_v1 demand "
			+ "inner join eg_ws_connection ewc on demand.consumercode = ewc.connectionno "
			+ "inner join eg_ws_service ews on ews.connection_id =ewc.id ";
	
	private static final String QUERY_FOR_WS_CONNECTION_COUNT= SELECT 
			+ "count(distinct ewc.connectionno) "
			+ FROM + " eg_ws_connection ewc "
			+ INNER_JOIN + " eg_ws_service ews on ewc.id = ews.connection_id "
			+ INNER_JOIN + " eg_ws_connectionholder ewc2 on ewc.id = ewc2.connectionid "
			+ WHERE + " ewc.isoldapplication = false " + AND_QUERY + " ewc.applicationstatus = 'CONNECTION_ACTIVATED' ";
	
	private static final String QUERY_TO_GET_DEMANDS_COUNT =  SELECT + " count(distinct(consumercode))" 
			+ "from egbs_demand_v1 demand "
			+ "inner join eg_ws_connection ewc on demand.consumercode = ewc.connectionno "
			+ "inner join eg_ws_service ews on ews.connection_id =ewc.id ";
	
//	private static final String QUERY_FOR_WATER_CONNECTIONS = "select distinct ewc.connectionno ,ewc2.userid "
//            + "from eg_ws_connection ewc "
//            + "inner join eg_ws_connectionholder ewc2 on ewc.id = ewc2.connectionid  "
//            + "inner join eg_ws_service ews on ewc.id = ews.connection_id "
//            + "where ewc.isoldapplication = 'false' " + AND_QUERY+" EWC.APPLICATIONSTATUS = 'CONNECTION_ACTIVATED' "    ;
	
    private static final String QUERY_FOR_WATER_CONNECTIONS = "select distinct ewc.connectionno  "
            + "from eg_ws_connection ewc "
            + "inner join eg_ws_connectionholder ewc2 on ewc.id = ewc2.connectionid  "
            + "inner join eg_ws_service ews on ewc.id = ews.connection_id "
            + "where ewc.isoldapplication = 'false' " + AND_QUERY+" EWC.APPLICATIONSTATUS = 'CONNECTION_ACTIVATED' "    ;
    
    private static final String QUERY_FOR_WS_MISCELLANEOUSDETAILS = SELECT 
            + " ewc.additionaldetails->>'ward' as ward,ewc.connectionno,ewc.oldconnectionno " 
            + FROM + " eg_ws_connection ewc "
            + WHERE + " ewc.isoldapplication = false " + AND_QUERY + " ewc.applicationstatus = 'CONNECTION_ACTIVATED' ";
	
	private static final String QUERY_FOR_WATER_MONTHLY_DEMANDS2 = SELECT 
            + demandsSelectValues + ", d.id, "
            + detailsSelectValues +" , "
            + connectionsSelectValues 
            + " ews.connectiontype, ewc2.userid "
            + FROM + " egbs_demand_v1 d "
            + INNER_JOIN + " egbs_demanddetail_v1 demanddetails on d.id = demanddetails.demandid "
            + INNER_JOIN + " eg_ws_connection ewc on ewc.connectionno = d.consumercode "
            + INNER_JOIN + " eg_ws_service ews on ewc.id = ews.connection_id "
            + INNER_JOIN + " eg_ws_connectionholder ewc2 on ewc.id = ewc2.connectionid "
            + WHERE + " d.businessservice = 'WS' " + AND_QUERY + " d.status = 'ACTIVE' "
            + AND_QUERY + " ewc.isoldapplication = 'false' " + AND_QUERY + " ewc.applicationstatus = 'CONNECTION_ACTIVATED' ";
	
	private static final String PROPERTY_DETAILS_SUMMARY_QUERY_COUNT = SELECT
            + "count(epp.propertyid) "
            + FROM
            + "eg_pt_property epp "
            + INNER_JOIN + "eg_pt_owner epo " +  ON  + "epo.propertyid = epp.id "
            + LEFT_OUTER_JOIN + "eg_user eu on eu.uuid = epo.userid "
            + INNER_JOIN + "eg_pt_address epa on epa.propertyid = epp.id "
            + WHERE + "epp.status <> 'INACTIVE' "
            + AND + "epp.tenantid = ? ";
	
	private static final String PROPERTY_DEMANDS_COUNT_QUERY = SELECT 
            + "COUNT(consumercode) "
            + FROM + " egbs_demand_v1 edv "
            + INNER_JOIN + "eg_pt_property epp on edv.consumercode = epp.propertyid "
            + INNER_JOIN +" eg_pt_address epa on epp.id =epa.propertyid "
            + INNER_JOIN + "eg_pt_owner epo " +  ON  + "epo.propertyid = epp.id "
            + LEFT_OUTER_JOIN + "eg_user eu on eu.uuid = epo.userid "
            + INNER_JOIN + " egbs_demanddetail_v1 edv2 on edv.id=edv2.demandid  "
            + WHERE + "edv2.tenantid= ? " + AND + " edv2.tenantid= ? " + AND + " edv.status <> 'CANCELLED' "
            + AND ;
	
	private static final String PROPERTY_DETAILS_SUMMARY_COUNT_QUERY = SELECT
            + "COUNT(eu.uuid)"
            + FROM
            + "eg_pt_property epp "
            + INNER_JOIN + "eg_pt_owner epo " +  ON  + "epo.propertyid = epp.id "
            + LEFT_OUTER_JOIN + "eg_user eu on eu.uuid = epo.userid "
            + INNER_JOIN + "eg_pt_address epa on epa.propertyid = epp.id "
            + WHERE + "epp.status <> 'INACTIVE' "
            + AND + "epp.tenantid = ? ";
	
	private static final String CONSUMER_MASTER_WS_COUNT_QUERY = "select count(*) "
	        +" from eg_ws_connection ewc " 
	        +" inner join eg_ws_service ews on ewc.id = ews.connection_id " 
	        +" where ewc.applicationstatus = 'CONNECTION_ACTIVATED' " 
	        +" and ewc.isoldapplication = false " ;
	    
    public static final String DEMAND_QUERY_GROUP_BY_CLAUSE = "consumercode ,edv.id,payer,edv.createdby  ,taxperiodfrom ,taxperiodto ,eu.uuid,edv.tenantid ,edv.status ,epp.oldpropertyid,epa.ward";

    private static final String PROPERTY = FROM
            + " eg_pt_property epp "
            + INNER_JOIN + "eg_pt_address epa on epa.propertyid = epp.id "
            + WHERE + "epp.status <> 'INACTIVE' "
            + AND + " epp.tenantid = ? ";

    private static final String PROPERTY_COUNT_QUERY = SELECT + "count ( distinct epp.propertyid ) " + PROPERTY;

    private static final String PROPERTY_IDS = SELECT + " epp.propertyid " + PROPERTY;

    private static final String PROPERTY_DETAILS = SELECT
            + "epp.propertyid,epp.tenantid,epa.ward,epp.oldpropertyid,epp.ddnno,epp.legacyholdingno,epp.propertytype,epp.usagecategory,eu.uuid,"
            + "epa.doorno,epa.buildingname,epa.street,epa.city,epa.pincode "
            + FROM
            + "eg_pt_property epp "
            + INNER_JOIN + "eg_pt_owner epo " + ON + "epo.propertyid = epp.id "
            + LEFT_OUTER_JOIN + "eg_user eu on eu.uuid = epo.userid "
            + INNER_JOIN + "eg_pt_address epa on epa.propertyid = epp.id "
            + WHERE + "epp.status <> 'INACTIVE' ";
    
    private static final String OLDPROPERTY_IDS_DETAILS = SELECT
            + " epp.propertyid,epp.oldpropertyid,epp.ddnno,epp.legacyholdingno "
            + FROM
            + "eg_pt_property epp "
            + WHERE + "epp.status <> 'INACTIVE' ";
    
    private static final String PT_ASSESSMENT_REPORT_QUERY = "select distinct on (asmt.assessmentnumber) initcap(split_part(asmt.tenantid, '.', 2)) as ulb , "
			+ "	asmt.propertyid as propertyid , "
			+ "	asmt.assessmentnumber as asmtnumber, "
			+ "	asmt.status as status, "
			+ "	address.ward as ward, "
			+ "	address.city as city, "
			+ "	own.userid as name, "
			+ "	asmt.financialyear as finyear, "
			+ "	to_char(to_timestamp(assessmentdate / 1000)::date, 'DD-MM-YYYY') as asmtdate, "
			+ "	case "
			+ "		when asmt.status = 'ACTIVE' then to_char(to_timestamp(asmt.lastmodifiedtime / 1000)::date, 'DD-MM-YYYY') "
			+ "		else null "
			+ "	end as approvaldate, "
			+ "	coalesce(asmt.additionaldetails->>'lightTax', '0') as lighttax, "
			+ "	coalesce(asmt.additionaldetails->>'waterTax', '0') as watertax, "
			+ "	coalesce(asmt.additionaldetails->>'holdingTax', '0') as holdingtax, "
			+ "	coalesce(asmt.additionaldetails->>'latrineTax', '0') as latrinetax, "
			+ "	coalesce(asmt.additionaldetails->>'parkingTax', '0') as parkingtax, "
			+ "	coalesce(asmt.additionaldetails->>'serviceTax', '0') as servicetax, "
			+ "	coalesce(asmt.additionaldetails->>'drainageTax', '0') as drainagetax, "
			+ "	coalesce(asmt.additionaldetails->>'usageExemption', '0') as usageexemption, "
			+ "	coalesce(asmt.additionaldetails->>'ownershipExemption', '0') as ownershipexemption, "
			+ "	coalesce(asmt.additionaldetails->>'solidWasteUserCharges', '0') as solidwasteusercharges, "
			+ "	coalesce(asmt.additionaldetails->>'otherDues', '0') as otherdues, "
			+ "	case "
			+ "		when hrms.code is null then 'SYSTEM' "
			+ "		else 'MANUAL' "
			+ "	end as triggeredby "
			+ "from "
			+ "	eg_pt_asmt_assessment asmt "
			+ "inner join eg_pt_property prop on "
			+ "	prop.propertyid = asmt.propertyid "
			+ "inner join eg_pt_address address on "
			+ "	address.propertyid = prop.id "
			+ "	and prop.status = 'ACTIVE' "
			+ "inner join eg_pt_owner own on "
			+ "	own.propertyid = prop.id and own.status = 'ACTIVE' "
//			+ "inner join eg_user usr on "
//			+ "	usr.uuid = own.userid "
			+ "left outer join eg_hrms_employee hrms on "
			+ "	hrms.uuid = asmt.createdby "
			+ "where "
			+ "	1 = 1 ";

	private static final String TL_AUTOESCALLATION_DETAILS_QUERY = "with td as ( "
			+ "select distinct on (ewpv.businessid) ewpv.businessid,ett.tenantid,ett.licensetype ,ett.applicationtype,ett.status,ewpv.comment,ewpv.createdtime,ewpv.createdby,ewav.assignee  from eg_tl_tradelicense ett  "
			+ "inner join eg_wf_processinstance_v2 ewpv on ewpv.businessid = ett.applicationnumber and modulename='tl-services' "
			+ "left outer join eg_wf_assignee_v2 ewav on ewpv.id = ewav.processinstanceid  "
			+ "where ett.businessservice ='TL' "
			+ "and ett.tenantid != 'od.testing' and ett.licensetype = '$licensetype' "
			+ "and ( case 	when '$ulb' = 'od' then ( ett.tenantid in ( 	select 	distinct tenantid 	from 	eg_tl_tradelicense ett2 ) ) 	else ett.tenantid = '$ulb' "
			+ "end ) "
			+ "and ett.status not in ('INITIATED', 'APPLIED', 'PENDINGPAYMENT', 'DELETED','APPROVED','INACTIVE','EXPIRED') "
			+ "order by ewpv.businessid , ewpv.lastmodifiedtime  desc	 "
			+ ") "
			+ " select	initcap(split_part(td.tenantid, '.', 2)) as tenantid, "
			+ "		td.licensetype , "
			+ "	td.applicationtype, "
			+  " td.businessid , "
			+ "	td.status, "
			+ "	td.comment, "
			+ "	td.createdtime ,"
			+ "	td.createdby, "
			+ "	td.assignee, "
			+ "	td.createdtime  as escallationdate, "
			+ "	ewpv.createdtime  as submissiondate "
			+ "from td inner join eg_wf_processinstance_v2 ewpv on td.businessid = ewpv.businessid and ewpv.modulename='tl-services' "
			+ "and td.comment in 	('Auto Escalated to Approver', 'Auto Escalated to Approver From Citizen', 'Auto Escalated to Document Verifier From Citizen', 'Auto Escalated to Field Inspection', 'Auto Escalated to Field Inspection From Citizen' 	) and  "
			+ "case  when td.applicationtype in ('NEW','RENEWAL') then ewpv.\"action\" ='PAY' "
			+ "	when td.applicationtype in ('CORRECTION','OWNERSHIPTRANSFER') then ewpv.\"action\" ='APPLY' "
			+ "end ";
	
	private static final String PT_DDN_DETAILS_QUERY = "select distinct INITCAP(SPLIT_PART(epp.tenantid, '.', 2)) as ulb,"
			+ " epp.propertyid as propertyid, epp.createdtime , eu.uuid as name, epp.ddnno as ddnno"
			+ " from eg_pt_property epp"
			+ " inner join eg_pt_owner epo on epp.id = epo.propertyid"
			+ " and epp.tenantid != 'od.testing' and epp.tenantid not like '%deleted%' and epo.status = 'ACTIVE'"
			+ " and ( case when '$ulb' = 'od' then ( epp.tenantid in ( select distinct tenantid from eg_pt_property ) )"
			+ " else epp.tenantid = '$ulb' end ) inner join eg_user eu on epo.userid = eu.uuid"
			+ " order by ulb, epp.propertyid, epp.createdtime desc";

	private void addClauseIfRequired(List<Object> values, StringBuilder queryString) {
		if (values.isEmpty())
			queryString.append(" WHERE ");
		else {
			queryString.append(" AND");
		}
	}
	
	private String createQuery(List<Long> ids) {
		StringBuilder builder = new StringBuilder();
		int length = ids.size();
		for (int i = 0; i < length; i++) {
			builder.append(" ?");
			if (i != length - 1)
				builder.append(",");
		}
		return builder.toString();
	}
	
	private void addToPreparedStatement(List<Object> preparedStatement, List<Long> ids) {
		preparedStatement.addAll(ids);
	}
	
	public String getEmployeeBaseTenantQuery(List<Long> userIds, List<Object> preparedStatement) {
		StringBuilder builder = new StringBuilder(HRMS_QUERY);
		
		String userId = " id in (";
		addClauseIfRequired(preparedStatement, builder);
		builder.append(userId).append(createQuery(userIds)).append(" )");
		addToPreparedStatement(preparedStatement, userIds);
		
		return builder.toString();
	}
	
	public String getQueryForConsumerMasterWSReport(List<Object> preparedStatement, WSReportSearchCriteria criteria) {
		
		StringBuilder query = new StringBuilder(QUERY_FOR_CONSUMER_MASTER_WS_REPORT);
		
		preparedStatement.add(criteria.getTenantId());
		
		query.append(AND_QUERY);
		query.append("ewc.tenantid = ?");
		preparedStatement.add(criteria.getTenantId());
		
		if(StringUtils.hasText(criteria.getWard()) && !criteria.getWard().equalsIgnoreCase("nil")) {
			query.append(AND_QUERY);
			query.append("ewc.additionaldetails->> 'ward' = ? ");
			preparedStatement.add(criteria.getWard());
		}
		
		if(StringUtils.hasText(criteria.getConnectionType())) {
			query.append(AND_QUERY);
			query.append("ews.connectiontype = ? ");
			preparedStatement.add(criteria.getConnectionType());
		}
		
		addPaginationIfRequired(query,criteria.getLimit(),criteria.getOffset(),preparedStatement);
		
		return query.toString();
	}
	
	public String getBillSummaryDetailsQuery(WSReportSearchCriteria criteria, List<Object> preparedStmtList) {

        StringBuilder query = new StringBuilder(BILL_SUMMARY_QUERY2);

        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(criteria.getMonthYear());

        String month = Integer.toString(c.get(Calendar.MONTH) + 1);
        if (month.length() == 1)
            month = "0" + month;
        String year = Integer.toString(c.get(Calendar.YEAR));

        preparedStmtList.add(month);
        preparedStmtList.add(year);

        if (StringUtils.hasText(criteria.getTenantId())) {
            query.append(WHERE);
            query.append(" demand.tenantid = '").append(criteria.getTenantId()).append("'");
        }
        log.info(query.toString());
        return query.toString();
	}

	public String getPropertyDetailsQuery(PropertyDetailsSearchCriteria criteria, List<Object> preparedPropStmtList) {

		 StringBuilder query = new StringBuilder(PROPERTY_DETAILS_SUMMARY_QUERY);

	     preparedPropStmtList.add(criteria.getUlbName());
	     if(StringUtils.hasText(criteria.getWardNo())) {
				query.append(AND);
				query.append(" epa.ward = '").append(criteria.getWardNo()).append("'");
	     }

	     addPaginationIfRequired(query,criteria.getLimit(),criteria.getOffset(),preparedPropStmtList);

	     return query.toString();
	}

	public String getWaterNewConsumerQuery(WSReportSearchCriteria criteria, List<Object> preparedStmtList) {
		
		StringBuilder query = new StringBuilder(QUERY_FOR_WATER_NEW_CONSUMER);
		
		if(StringUtils.hasText(criteria.getWard()) && !criteria.getWard().equalsIgnoreCase("nil")) {
			query.append(AND_QUERY);
			query.append("ewc.additionaldetails->> 'ward' = ? ");
			preparedStmtList.add(criteria.getWard());
		}
		
		query.append(AND_QUERY).append(" ewc.tenantid = ? ");
		preparedStmtList.add(criteria.getTenantId());
		
		query.append(AND_QUERY).append(" ews.connectionexecutiondate >= ? ");
		preparedStmtList.add(wsReportUtils.getStartingTimeOfFirstDayOfMonthYear(criteria.getMonthYear()));
		
		query.append(AND_QUERY).append(" ews.connectionexecutiondate <= ? ");
		preparedStmtList.add(wsReportUtils.addOneMonth(criteria.getMonthYear()));
		
		return query.toString();
	}
	
	public String getConsumerBillHistoryQuery(WSReportSearchCriteria criteria, List<Object> preparedStmtList) {
		
		StringBuilder query = new StringBuilder(QUERY_FOR_CONSUMER_BILL_HISTORY);
		
		query.append(" d.businessservice ='WS' and d.status !='CANCELLED' ");
		query.append(AND_QUERY).append(" d.consumercode = ? ");
		preparedStmtList.add(criteria.getConsumerCode());
		
		query.append(GROUP_BY).append(demandSelectValues.substring(0,demandSelectValues.length()-1));
		query.append(ORDER_BY).append(" d.taxperiodto ");
		
		return query.toString();
	}

	public String getPropertyDemandQuery(PropertyDetailsSearchCriteria searchCriteria,List<Object> preparedPropStmtList) {
		
StringBuilder query = new StringBuilder(DEMANDS_QUERY);
		
		query.append(" edv.businessservice ='PT' ;");
		preparedPropStmtList.add(searchCriteria.getUlbName());
		preparedPropStmtList.add(searchCriteria.getUlbName());
	
		return query.toString();
	}

	public String getPropertyWiseDemandQuery(PropertyDetailsSearchCriteria searchCriteria,
			List<Object> preparedPropStmtList) {
		
StringBuilder query = new StringBuilder(PROPERTY_DEMANDS_QUERY);
		
        query.append(" edv.businessservice ='PT' ");
		preparedPropStmtList.add(searchCriteria.getUlbName());
		preparedPropStmtList.add(searchCriteria.getUlbName());
		if(StringUtils.hasText(searchCriteria.getPropertyId())) {
			query.append(AND_QUERY).append(" edv.consumercode = ? ");
			preparedPropStmtList.add(searchCriteria.getPropertyId());
		}

		if(StringUtils.hasText(searchCriteria.getOldPropertyId())) {
			query.append(AND_QUERY).append(" epp.oldpropertyid = ? ");
			preparedPropStmtList.add(searchCriteria.getOldPropertyId());
		}

		if(StringUtils.hasText(searchCriteria.getDdnNo())){
			query.append(AND_QUERY).append(" epp.ddnno = ? ");
			preparedPropStmtList.add(searchCriteria.getDdnNo());
		}

		if(StringUtils.hasText(searchCriteria.getLegacyHoldingNo())){
			query.append(AND_QUERY).append(" epp.legacyholdingno = ? ");
			preparedPropStmtList.add(searchCriteria.getLegacyHoldingNo());
		}
		
		if(StringUtils.hasText(searchCriteria.getWardNo())) {
			query.append(AND_QUERY).append(" epa.ward  = ? ");
			preparedPropStmtList.add(searchCriteria.getWardNo());
		}
		
		addPaginationIfRequired(query,searchCriteria.getLimit(),searchCriteria.getOffset(),preparedPropStmtList);
	
		return query.toString();
	}
	
	public String getWaterMonthlyDemandQuery(WSReportSearchCriteria criteria, List<Object> preparedStmtList) {

		StringBuilder query = new StringBuilder(QUERY_FOR_WATER_MONTHLY_DEMAND);
		query.append(AND_QUERY).append(" d.taxperiodto >= ? ");
		preparedStmtList.add(criteria.getFromDate());
		query.append(AND_QUERY).append(" d.taxperiodto  <= ? ");
		preparedStmtList.add(criteria.getToDate());
		query.append(AND_QUERY).append(" d.tenantid = ? ");
		preparedStmtList.add(criteria.getTenantId());

		return query.toString();
	}

	public String getWaterMonthlyDemandConnectionQuery(WSReportSearchCriteria criteria, List<Object> preparedStmtList) {

		StringBuilder query = new StringBuilder(QUERY_FOR_WS_CONNECTION);
		
		if(StringUtils.hasText(criteria.getTenantId())) {
			query.append(AND_QUERY).append(" ewc.tenantid = ? ");
			preparedStmtList.add(criteria.getTenantId());
		}

		if(StringUtils.hasText(criteria.getWard())) {
			query.append(AND_QUERY).append(" ewc.additionaldetails->> 'ward' = ? ");
			preparedStmtList.add(criteria.getWard());
		}

		if(StringUtils.hasText(criteria.getConnectionType())) {
			query.append(AND_QUERY).append(" ews.connectiontype = ? ");
			preparedStmtList.add(criteria.getConnectionType());
		}
		
		if(StringUtils.hasText(criteria.getOldConnectionNo())) {
			query.append(AND_QUERY).append(" ewc.oldconnectionno = ? ");
			preparedStmtList.add(criteria.getOldConnectionNo());
		}

		return query.toString();

	}

	public String getPropertiesDetail(PropertyDetailsSearchCriteria searchCriteria, List<Object> preparedPropStmtList) {


		 StringBuilder query = new StringBuilder(PROPERTY_DETAILS_SUMMARY_QUERY);

	     preparedPropStmtList.add(searchCriteria.getUlbName());

	     if(StringUtils.hasText(searchCriteria.getWardNo())) {
				query.append(AND).append(" epa.ward = ? ");
				preparedPropStmtList.add(searchCriteria.getWardNo());
	     }

		if(StringUtils.hasText(searchCriteria.getPropertyId())) {
			query.append(AND_QUERY).append(" epp.propertyid = ? ");
			preparedPropStmtList.add(searchCriteria.getPropertyId());
		}
		
		if(StringUtils.hasText(searchCriteria.getOldPropertyId())) {
			query.append(AND_QUERY).append(" epp.oldpropertyid = ? ");
			preparedPropStmtList.add(searchCriteria.getOldPropertyId());
		}

		if(StringUtils.hasText(searchCriteria.getDdnNo())){
			query.append(AND_QUERY).append(" epp.ddnno = ? ");
			preparedPropStmtList.add(searchCriteria.getDdnNo());
		}

		if(StringUtils.hasText(searchCriteria.getLegacyHoldingNo())){
			query.append(AND_QUERY).append(" epp.legacyholdingno = ? ");
			preparedPropStmtList.add(searchCriteria.getLegacyHoldingNo());
		}
		
		addPaginationIfRequired(query,searchCriteria.getLimit(),searchCriteria.getOffset(),preparedPropStmtList);

		return query.toString();
	}

	public String getEmployeeWiseWSCollectionQuery(WSReportSearchCriteria searchCriteria,
			List<Object> preparedStmtList) {
		
		StringBuilder query =  new StringBuilder(QUERY_FOR_EMPLOYEE_WISE_WS);
		
		query.append(AND_QUERY).append(" ewc.tenantid = ? ");
		preparedStmtList.add(searchCriteria.getTenantId());
		
		query.append(AND_QUERY).append(" payment.transactiondate >= ? ");
		preparedStmtList.add(searchCriteria.getFromDate());
		
		query.append(AND_QUERY).append(" payment.transactiondate <= ? ");
		preparedStmtList.add(searchCriteria.getToDate());
		
		if(StringUtils.hasText(searchCriteria.getWard())) {
			query.append(AND).append(" ewc.additionaldetails->> 'ward' = ? ");
			preparedStmtList.add(searchCriteria.getWard());
		}
		
		return query.toString();
	}
	
	public String getElegibleWSConnectionsQuery(WSReportSearchCriteria searchCriteria, List<Object> preparedStmtList) {

		StringBuilder query = new StringBuilder(WS_CONNECTIONS_ELEGIBLE_FOR_DEMAND_QUERY);//change itt
		preparedStmtList.add(searchCriteria.getTenantId());
		
		log.info(" Prepared Statement : " + preparedStmtList.toString());
		
		return query.toString();
	}
	
	public String getDemandsQuery(WSReportSearchCriteria searchCriteria, List<Object> preparedStmtList) {

		StringBuilder query = new StringBuilder(QUERY_TO_GET_DEMANDS);
		
		query.append(AND_QUERY).append("ews.connectiontype = ? ");
		preparedStmtList.add("Non Metered");

		Long firstDate = wsReportUtils.getFirstDayOfMonthYear(searchCriteria.getFromDate());

		query.append(WHERE).append(" taxperiodfrom >= ? and taxperiodfrom <= ? ");
		preparedStmtList.add(firstDate);
		preparedStmtList.add(wsReportUtils.addOneMonth(firstDate));

		query.append(AND_QUERY).append(" businessservice = ?");
		preparedStmtList.add("WS");

		addPaginationIfRequired(query,searchCriteria.getLimit(),searchCriteria.getOffset(),preparedStmtList);

		return query.toString();

	}
	
	public String getWaterConnectionQuery(WSReportSearchCriteria criteria, List<Object> preparedStmtList) {

		StringBuilder query = new StringBuilder(QUERY_FOR_WS_CONNECTION);
		
		if(criteria.getFromDate() != null) {
			
			if(StringUtils.hasText(criteria.getConnectionType())) {
				query.append(AND_QUERY).append(" ews.connectiontype = ? ");
				preparedStmtList.add(criteria.getConnectionType());
			}
			
			Long firstDate = wsReportUtils.getFirstDayOfMonthYear(criteria.getFromDate());
			query.append(AND_QUERY).append(" ews.connectionexecutiondate <= ? ");
			preparedStmtList.add(wsReportUtils.addOneMonth(firstDate));
		}
		
		addPaginationIfRequired(query,criteria.getLimit(),criteria.getOffset(),preparedStmtList);
		
		return query.toString();

	}
	
	
	
	public String getSchedulerGeneratedDemandQuery(WSReportSearchCriteria searchCriteria,
			List<Object> preparedStmtList) {

		log.info("inside query");
		StringBuilder query = new StringBuilder(SCHEDULER_GENERATED_DEMANDS_QUERY);
		log.info("building query");
		preparedStmtList.add(searchCriteria.getTenantId());
		preparedStmtList.add("WS");
		preparedStmtList.add(searchCriteria.getTenantId());
		Long firstDay = wsReportUtils.getFirstDayOfMonthYear(searchCriteria.getMonthYear());
		preparedStmtList.add(wsReportUtils.getFirstDayOfMonthYear(searchCriteria.getMonthYear()));
		Long lastDay = wsReportUtils.addOneMonth(firstDay);
		preparedStmtList.add(lastDay);

		if(StringUtils.hasText(searchCriteria.getWard())) {
			query.append(AND).append(" EWC2.ADDITIONALDETAILS ->> 'ward' = ? ");
			preparedStmtList.add(searchCriteria.getWard());
		}
		
		addPaginationIfRequired(query,searchCriteria.getLimit(),searchCriteria.getOffset(),preparedStmtList);
		
		log.info(query.toString());
		log.info("returning query from query builder");
		return query.toString();
	}

	public String getConsumerMasterReportCount(List<Object> preparedStatement, WSReportSearchCriteria criteria) {
		
		StringBuilder query = new StringBuilder(CONSUMER_MASTER_WS_COUNT_QUERY );
		
		query.append(AND_QUERY);
		query.append("ewc.tenantid = ?");
		preparedStatement.add(criteria.getTenantId());
		
		if(StringUtils.hasText(criteria.getWard()) && !criteria.getWard().equalsIgnoreCase("nil")) {
			query.append(AND_QUERY);
			query.append("ewc.additionaldetails->> 'ward' = ? ");
			preparedStatement.add(criteria.getWard());
		}
		
		if(StringUtils.hasText(criteria.getConnectionType())) {
			query.append(AND_QUERY);
			query.append("ews.connectiontype = ? ");
			preparedStatement.add(criteria.getConnectionType());
		}
		
		return query.toString();
	}

	public String getSchedulerBasedWSDemandCount(List<Object> preparedStatement,
			WSReportSearchCriteria searchCriteria) {
		
		String countForSchedularBasedDemands = "select count(*) from egbs_demand_v1 edv "
				+ INNER_JOIN + " eg_ws_connection ewc2 on ewc2.tenantid= ? and EWC2.connectionno=edv.CONSUMERCODE "
				+ INNER_JOIN + " eg_ws_service ews2 on ewc2.id = ews2.connection_id "
				+ WHERE + " businessservice = ? "
				+ AND + "consumercode " + IN +" ( " + WS_CONNECTION_DETAILS_QUERY +" )"
				+ AND +" ( taxperiodfrom >= ? "
				+ AND + "taxperiodfrom <= ? ) ";
		
		StringBuilder query = new StringBuilder(countForSchedularBasedDemands);
		
		preparedStatement.add(searchCriteria.getTenantId());
		preparedStatement.add("WS");
		preparedStatement.add(searchCriteria.getTenantId());
		Long firstDay = wsReportUtils.getFirstDayOfMonthYear(searchCriteria.getMonthYear());
		preparedStatement.add(wsReportUtils.getFirstDayOfMonthYear(searchCriteria.getMonthYear()));
		Long lastDay = wsReportUtils.addOneMonth(firstDay);
		preparedStatement.add(lastDay);

		if(StringUtils.hasText(searchCriteria.getWard())) {
			query.append(AND).append(" EWC2.ADDITIONALDETAILS ->> 'ward' = ? ");
			preparedStatement.add(searchCriteria.getWard());
		}
		
		return query.toString();
	}
	
	public String getWaterConnectionCountQuery(WSReportSearchCriteria searchCriteria, List<Object> preparedStmtList) {

		StringBuilder query = new StringBuilder(QUERY_FOR_WS_CONNECTION_COUNT);

		if (searchCriteria.getFromDate() != null) {

			if (StringUtils.hasText(searchCriteria.getConnectionType())) {
				query.append(AND_QUERY).append(" ews.connectiontype = ? ");
				preparedStmtList.add(searchCriteria.getConnectionType());
			}

			Long firstDate = wsReportUtils.getFirstDayOfMonthYear(searchCriteria.getFromDate());
			query.append(AND_QUERY).append(" ews.connectionexecutiondate <= ? ");
			preparedStmtList.add(wsReportUtils.addOneMonth(firstDate));
		}

		return query.toString();
	}
	
	public String getDemandsQueryCount(WSReportSearchCriteria searchCriteria, List<Object> preparedStmtList) {
		StringBuilder query = new StringBuilder(QUERY_TO_GET_DEMANDS_COUNT);

		query.append(AND_QUERY).append("ews.connectiontype = ? ");
		preparedStmtList.add("Non Metered");

		Long firstDate = wsReportUtils.getFirstDayOfMonthYear(searchCriteria.getFromDate());

		query.append(WHERE).append(" taxperiodfrom >= ? and taxperiodfrom <= ? ");
		preparedStmtList.add(firstDate);
		preparedStmtList.add(wsReportUtils.addOneMonth(firstDate));

		query.append(AND_QUERY).append(" businessservice = ?");
		preparedStmtList.add("WS");

		return query.toString();
	}
	
	public String getWaterConnectionsCountQuery(WSReportSearchCriteria searchCriteria, List<Object> preparedStmtList) {
        StringBuilder query = new StringBuilder(QUERY_FOR_WS_CONNECTION_COUNT);

        query.append(AND_QUERY).append(" ewc.tenantid = ? ");
        preparedStmtList.add(searchCriteria.getTenantId());
        if(StringUtils.hasText(searchCriteria.getWard())) {
            query.append(AND_QUERY).append(" ewc.additionaldetails->> 'ward' = ? ");
            preparedStmtList.add(searchCriteria.getWard());
        }

        if(StringUtils.hasText(searchCriteria.getConnectionType())) {
            query.append(AND_QUERY).append(" ews.connectiontype = ? ");
            preparedStmtList.add(searchCriteria.getConnectionType());
        }
        

        return query.toString();
    }
	
	public String getWaterConnectionsQuery(WSReportSearchCriteria searchCriteria, List<Object> preparedStmtList) {
        StringBuilder query = new StringBuilder(QUERY_FOR_WATER_CONNECTIONS);

        query.append(AND_QUERY).append(" ewc.tenantid = ? ");
        preparedStmtList.add(searchCriteria.getTenantId());
        if(StringUtils.hasText(searchCriteria.getWard())) {
            query.append(AND_QUERY).append(" ewc.additionaldetails->> 'ward' = ? ");
            preparedStmtList.add(searchCriteria.getWard());
        }

        if(StringUtils.hasText(searchCriteria.getConnectionType())) {
            query.append(AND_QUERY).append(" ews.connectiontype = ? ");
            preparedStmtList.add(searchCriteria.getConnectionType());
        }

        addPaginationIfRequired(query,searchCriteria.getLimit(),searchCriteria.getOffset(),preparedStmtList);
        
        return query.toString();
    }
	
	public String getWaterMonthlyDemandQuery2(WSReportSearchCriteria searchCriteria, Map<String ,Object> preparedStmtList ) {
      StringBuilder query = new StringBuilder(QUERY_FOR_WATER_MONTHLY_DEMANDS2);
      query.append(" and d.taxperiodto <= :to ");
      preparedStmtList.put("to", searchCriteria.getToDate());
      query.append("and d.consumercode IN  (:id)  ");
      preparedStmtList.put("id", searchCriteria.getConsumerNumbers());
      query.append(ORDER_BY).append(" d.taxperiodto ");
      query.append(" desc ");
	    
        return query.toString();
    }

    public String getPropertyDetailsQueryCount(PropertyDetailsSearchCriteria searchCriteria,
            List<Object> preparedPropStmtList) {
        
        StringBuilder query = new StringBuilder(PROPERTY_DETAILS_SUMMARY_QUERY_COUNT);
        
        preparedPropStmtList.add(searchCriteria.getUlbName());
        if(StringUtils.hasText(searchCriteria.getWardNo())) {
               query.append(AND);
               query.append(" epa.ward = '").append(searchCriteria.getWardNo()).append("'");
        }

        return query.toString();
    }

    public String getPropertyWiseDemandCountQuery(PropertyDetailsSearchCriteria searchCriteria,
            List<Object> preparedPropStmtList) {
        
        StringBuilder query = new StringBuilder(PROPERTY_DEMANDS_COUNT_QUERY);
                
                query.append(" edv.businessservice ='PT' ");
                preparedPropStmtList.add(searchCriteria.getUlbName());
                preparedPropStmtList.add(searchCriteria.getUlbName());
                if(StringUtils.hasText(searchCriteria.getPropertyId())) {
                    query.append(AND_QUERY).append(" edv.consumercode = ? ");
                    preparedPropStmtList.add(searchCriteria.getPropertyId());
                }

                if(StringUtils.hasText(searchCriteria.getOldPropertyId())) {
                    query.append(AND_QUERY).append(" epp.oldpropertyid = ? ");
                    preparedPropStmtList.add(searchCriteria.getOldPropertyId());
                }
                
                if(StringUtils.hasText(searchCriteria.getWardNo())) {
                    query.append(AND_QUERY).append(" epa.ward  = ? ");
                    preparedPropStmtList.add(searchCriteria.getWardNo());
                }
                
                return query.toString();
    }

    public String getPropertiesDetailCount(PropertyDetailsSearchCriteria searchCriteria,
            List<Object> preparedPropStmtList) {
        StringBuilder query = new StringBuilder(PROPERTY_DETAILS_SUMMARY_COUNT_QUERY);

        preparedPropStmtList.add(searchCriteria.getUlbName());

        if(StringUtils.hasText(searchCriteria.getWardNo())) {
               query.append(AND).append(" epa.ward = ? ");
               preparedPropStmtList.add(searchCriteria.getWardNo());
        }

       if(StringUtils.hasText(searchCriteria.getPropertyId())) {
           query.append(AND_QUERY).append(" epp.propertyid = ? ");
           preparedPropStmtList.add(searchCriteria.getPropertyId());
       }
       
       if(StringUtils.hasText(searchCriteria.getOldPropertyId())) {
           query.append(AND_QUERY).append(" epp.oldpropertyid = ? ");
           preparedPropStmtList.add(searchCriteria.getOldPropertyId());
       }

		if(StringUtils.hasText(searchCriteria.getDdnNo())){
			query.append(AND_QUERY).append(" epp.ddnno = ? ");
			preparedPropStmtList.add(searchCriteria.getDdnNo());
		}

		if(StringUtils.hasText(searchCriteria.getLegacyHoldingNo())){
			query.append(AND_QUERY).append(" epp.legacyholdingno = ? ");
			preparedPropStmtList.add(searchCriteria.getLegacyHoldingNo());
		}

       return query.toString();
    }
    
    private void addPaginationIfRequired(StringBuilder query , Integer limit , Integer offset ,List<Object> preparedStmtList) {
        
        if(limit != null && offset != null) {
            query.append(" limit ? ");
            preparedStmtList.add(limit);
            
            query.append(" offset ? ");
            preparedStmtList.add(offset);
        }

    }
    
    public String getPropertiesCount(PropertyDetailsSearchCriteria searchCriteria, List<Object> preparedPropStmtList) {
        StringBuilder query = new StringBuilder(PROPERTY_COUNT_QUERY);

        preparedPropStmtList.add(searchCriteria.getUlbName());

        if(StringUtils.hasText(searchCriteria.getWardNo())) {
               query.append(AND).append(" epa.ward = ? ");
               preparedPropStmtList.add(searchCriteria.getWardNo());
        }

       if(StringUtils.hasText(searchCriteria.getPropertyId())) {
           query.append(AND_QUERY).append(" epp.propertyid = ? ");
           preparedPropStmtList.add(searchCriteria.getPropertyId());
       }
       
       if(StringUtils.hasText(searchCriteria.getOldPropertyId())) {
           query.append(AND_QUERY).append(" epp.oldpropertyid = ? ");
           preparedPropStmtList.add(searchCriteria.getOldPropertyId());
       }

		if(StringUtils.hasText(searchCriteria.getDdnNo())){
			query.append(AND_QUERY).append(" epp.ddnno = ? ");
			preparedPropStmtList.add(searchCriteria.getDdnNo());
		}

		if(StringUtils.hasText(searchCriteria.getLegacyHoldingNo())){
			query.append(AND_QUERY).append(" epp.legacyholdingno = ? ");
			preparedPropStmtList.add(searchCriteria.getLegacyHoldingNo());
		}

       return query.toString();
    }

    public String getPropertyDetailQuery(PropertyDetailsSearchCriteria searchCriteria,
            List<Object> preparedPropStmtList) {
        StringBuilder query = new StringBuilder(PROPERTY_DETAILS);
        
        if (!CollectionUtils.isEmpty(searchCriteria.getPropertyIds()) ) {
            addAndClause(query);
            query.append("epp.propertyid IN ("
            + getIdQueryForStrings(searchCriteria.getPropertyIds()));
        }

        return query.toString();
    }
    
    private static void addGroupByClause(StringBuilder demandQueryBuilder,String columnName) {
        demandQueryBuilder.append(" GROUP BY " + columnName);
    }
    
    private static boolean addAndClause(StringBuilder queryString) {
        queryString.append(" AND ");
        return true;
    }
    
    private static String getIdQueryForStrings(Set<String> idList) {

        StringBuilder query = new StringBuilder();
        if (!idList.isEmpty()) {

            String[] list = idList.toArray(new String[idList.size()]);
            query.append("'"+list[0]+"'");
            for (int i = 1; i < idList.size(); i++) {
                query.append("," + "'"+list[i]+"'");
            }
        }
        return query.append(")").toString();
    }

    public String getPropertyIds(PropertyDetailsSearchCriteria searchCriteria, List<Object> preparedPropStmtList) {
                
        StringBuilder query = new StringBuilder(PROPERTY_IDS);

        preparedPropStmtList.add(searchCriteria.getUlbName());

        if(StringUtils.hasText(searchCriteria.getWardNo())) {
               query.append(AND);
               query.append(" epa.ward = '").append(searchCriteria.getWardNo()).append("'");
        }
        
        if(StringUtils.hasText(searchCriteria.getPropertyId())) {
            query.append(AND_QUERY).append(" epp.propertyid = ? ");
            preparedPropStmtList.add(searchCriteria.getPropertyId());
        }
        
        if(StringUtils.hasText(searchCriteria.getOldPropertyId())) {
            query.append(AND_QUERY).append(" epp.oldpropertyid = ? ");
            preparedPropStmtList.add(searchCriteria.getOldPropertyId());
        }

		if(StringUtils.hasText(searchCriteria.getDdnNo())){
			query.append(AND_QUERY).append(" epp.ddnno = ? ");
			preparedPropStmtList.add(searchCriteria.getDdnNo());
		}

		if(StringUtils.hasText(searchCriteria.getLegacyHoldingNo())){
			query.append(AND_QUERY).append(" epp.legacyholdingno = ? ");
			preparedPropStmtList.add(searchCriteria.getLegacyHoldingNo());
		}
        
        return query.toString();
    }

    public String getOldPropertyIdsQuery(PropertyDetailsSearchCriteria searchCriteria,
            List<Object> preparedPropStmtList) {
        StringBuilder query = new StringBuilder(OLDPROPERTY_IDS_DETAILS);

        if (!CollectionUtils.isEmpty(searchCriteria.getPropertyIds())) {
            addAndClause(query);
            query.append("epp.propertyid IN ("
                    + getIdQueryForStrings(searchCriteria.getPropertyIds()));
        }

        return query.toString();
    }
    
    public String getWaterConnectionDetailsQuery(WSReportSearchCriteria criteria, List<Object> preparedStmtList) {

        StringBuilder query = new StringBuilder(WS_CONNECTIONS_QUERY);

        if (criteria.getConsumerNumbers() != null && !criteria.getConsumerNumbers().isEmpty()) {
            addAndClause(query);
            query.append("ewc.connectionno IN ("
            + getIdQueryForStrings(criteria.getConsumerNumbers()));
        }

        addPaginationIfRequired(query,criteria.getLimit(),criteria.getOffset(),preparedStmtList);

        return query.toString();

    }

    public String getMiscellaneousWaterDetailsQuery(WSReportSearchCriteria searchCriteria,
            List<Object> preparedStmtList) {
        StringBuilder query = new StringBuilder(QUERY_FOR_WS_MISCELLANEOUSDETAILS);

        if (searchCriteria.getConsumerNumbers() != null && !searchCriteria.getConsumerNumbers().isEmpty()) {
            addAndClause(query);
            query.append("ewc.connectionno IN ("
            + getIdQueryForStrings(searchCriteria.getConsumerNumbers()));
        }

        addPaginationIfRequired(query,searchCriteria.getLimit(),searchCriteria.getOffset(),preparedStmtList);

        return query.toString();
    }

    public String getMiscellaneousWaterConnectionDetailsQuery(WSReportSearchCriteria searchCriteria,
            List<Object> preparedStmtList) {
        StringBuilder query = new StringBuilder(QUERY_FOR_WS_MISCELLANEOUSDETAILS);

        query.append(AND_QUERY).append(" ewc.tenantid = ? ");
        preparedStmtList.add(searchCriteria.getTenantId());
        if(StringUtils.hasText(searchCriteria.getWard())) {
            query.append(AND_QUERY).append(" ewc.additionaldetails->> 'ward' = ? ");
            preparedStmtList.add(searchCriteria.getWard());
        }

        addPaginationIfRequired(query,searchCriteria.getLimit(),searchCriteria.getOffset(),preparedStmtList);

        return query.toString();
    }

	public String getCreatePTAssessmentReportQuery(@Valid PTAssessmentSearchCriteria ptAssessmentSearchCriteria,
			String tenantid) {
		StringBuilder query =  new StringBuilder(PT_ASSESSMENT_REPORT_QUERY);
	    
		if (!StringUtils.isEmpty(tenantid)) {
			query.append(" and asmt.tenantid = '" + tenantid + "'");
		}
		
		if (!StringUtils.isEmpty(ptAssessmentSearchCriteria.getFinancialYear())) {
			query.append(" and financialyear = '" + ptAssessmentSearchCriteria.getFinancialYear() + "'");
		}
		
		query.append(" order by asmt.assessmentnumber,asmt.propertyid, assessmentdate desc ");

	    log.info("Query for Payment Report search: " + query);
	    
	    return query.toString();

	}

	public String getTLAutoEscallationReport(TradeLicenseSearchCriteria searchCriteria,
	        List<Object> preparedPropStmtList) {
	    // Initialize the query with the base query string
	    StringBuilder query = new StringBuilder(TL_AUTOESCALLATION_DETAILS_QUERY);
	    log.info("Initial Query: {}", query);

	    // Replace all occurrences of $ulb with the provided tenantId or default to 'od'
	    String tenantIdReplacement = StringUtils.hasText(searchCriteria.getTenant()) 
	                                ? searchCriteria.getTenant() : "'od'";
	    log.info("Tenant ID Replacement: {}", tenantIdReplacement);
	    replacePlaceholder(query, "$ulb", tenantIdReplacement);

	    // Replace all occurrences of $licensetype with the provided licenseType if available
	    if (StringUtils.hasText(searchCriteria.getLicenseType())) {
	        String licenseTypeReplacement = searchCriteria.getLicenseType();
	        log.info("License Type Replacement: {}", licenseTypeReplacement);
	        replacePlaceholder(query, "$licensetype", licenseTypeReplacement);
	    } else {
	        // Remove the license type condition from the query if licenseType is not provided
	        removeLicenseTypeCondition(query);
	    }

	    log.info("Final Query: {}", query);
	    return query.toString();
	}

	private void replacePlaceholder(StringBuilder query, String placeholder, String replacement) {
	    int index = query.indexOf(placeholder);
	    while (index != -1) {
	        query.replace(index, index + placeholder.length(), replacement);
	        index = query.indexOf(placeholder, index + replacement.length());
	    }
	}

	private void removeLicenseTypeCondition(StringBuilder query) {
	    String conditionToRemove = "and ett.licensetype = '$licensetype'";
	    int index = query.indexOf(conditionToRemove);
	    if (index != -1) {
	        query.delete(index, index + conditionToRemove.length());
	    }
	}

	public String createptDDNReport(@Valid PTAssessmentSearchCriteria searchCriteria, String tenantId) {
		StringBuilder query = new StringBuilder(PT_DDN_DETAILS_QUERY);

		log.info("Initial Query: {}", query);

		// Replace all occurrences of $ulb with the provided tenantId or default to 'od'
		String tenantIdReplacement = StringUtils.hasText(tenantId) ? tenantId : "'od'";
		log.info("Tenant ID Replacement: {}", tenantIdReplacement);
		replacePlaceholder(query, "$ulb", tenantIdReplacement);

		log.info("Final Query: {}", query);
		return query.toString();

	}
}
