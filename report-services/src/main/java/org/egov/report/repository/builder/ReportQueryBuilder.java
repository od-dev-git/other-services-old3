package org.egov.report.repository.builder;

import java.util.Calendar;
import java.util.List;
import java.util.Set;

import org.egov.report.repository.WSReportRepository;
import org.egov.report.util.WSReportUtils;
import org.egov.report.web.model.PropertyDetailsSearchCriteria;
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
			+ INNER_JOIN + " eg_ws_connection ewc2 on ewc2.tenantid= ? and EWC2.connectionno=edv.CONSUMERCODE "
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
			+ "epp.tenantid,epa.ward,epp.oldpropertyid,epp.propertyid,eu.uuid,"
			+ "epa.doorno,epa.buildingname,epa.street,epa.city,epa.pincode "
			+ FROM
			+ "eg_pt_property epp "
			+ INNER_JOIN + "eg_pt_owner epo " +  ON  + "epo.propertyid = epp.id "
			+ LEFT_OUTER_JOIN + "eg_user eu on eu.uuid = epo.userid "
			+ INNER_JOIN + "eg_pt_address epa on epa.propertyid = epp.id "
			+ WHERE + "epp.status <> 'INACTIVE' "
			+ AND + "epp.tenantid = ? ";
	
	private static final String PROPERTY_DEMANDS_QUERY = SELECT 
			+ "consumercode,edv.id,payer ,edv.createdby ,taxperiodfrom ,taxperiodto,eu.uuid,"
			+ "edv.tenantid ,edv.status,edv2.taxamount ,edv2.collectionamount,epp.oldpropertyid,epa.ward "
			+ FROM + " egbs_demand_v1 edv "
			+ INNER_JOIN + "eg_pt_property epp on edv.consumercode = epp.propertyid "
			+ INNER_JOIN +" eg_pt_address epa on epp.id =epa.propertyid "
			+ INNER_JOIN + "eg_pt_owner epo " +  ON  + "epo.propertyid = epp.id "
			+ LEFT_OUTER_JOIN + "eg_user eu on eu.uuid = epo.userid "
			+ INNER_JOIN + " egbs_demanddetail_v1 edv2 on edv.id=edv2.demandid  "
			+ WHERE + "edv2.tenantid= ? " + AND + " edv2.tenantid= ? " + AND + " edv.status <> 'CANCELLED' "
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
	
	private static final String QUERY_FOR_EMPLOYEE_WISE_WS = SELECT 
			+ connectionSelectValues
			+ paymentSelectValues 
			+ paymentDetailsSelectValues
			+ FROM + " eg_ws_connection ewc "
			+ INNER_JOIN + " egcl_bill bill on ewc.connectionno = bill.consumercode "
			+ INNER_JOIN + " egcl_paymentdetail paymentd on paymentd.billid = bill.id "
			+ INNER_JOIN + " egcl_payment payment on payment.id = paymentd.paymentid "
			+ WHERE + " ewc.isoldapplication = false ";
	
	private static final String WS_CONNECTIONS_ELEGIBLE_FOR_DEMAND_QUERY = SELECT
			+ "EWC.TENANTID ,EWC.ADDITIONALDETAILS->>'ward' as ward,count(distinct ewc.connectionno) as connectionscount "
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
			+ "count(ewc2.userid) "
			+ FROM + " eg_ws_connection ewc "
			+ INNER_JOIN + " eg_ws_service ews on ewc.id = ews.connection_id "
			+ INNER_JOIN + " eg_ws_connectionholder ewc2 on ewc.id = ewc2.connectionid "
			+ WHERE + " ewc.isoldapplication = false " + AND_QUERY + " ewc.applicationstatus = 'CONNECTION_ACTIVATED' ";
	
	private static final String QUERY_TO_GET_DEMANDS_COUNT =  SELECT + " count(distinct(consumercode))" 
			+ "from egbs_demand_v1 demand "
			+ "inner join eg_ws_connection ewc on demand.consumercode = ewc.connectionno "
			+ "inner join eg_ws_service ews on ews.connection_id =ewc.id ";
	
	private static final String QUERY_FOR_WATER_CONNECTIONS = "select distinct ewc.connectionno ,ewc2.userid "
            + "from eg_ws_connection ewc "
            + "inner join eg_ws_connectionholder ewc2 on ewc.id = ewc2.connectionid  "
            + "inner join eg_ws_service ews on ewc.id = ews.connection_id "
            + "where ewc.isoldapplication = 'false' " + AND_QUERY+" EWC.APPLICATIONSTATUS = 'CONNECTION_ACTIVATED' "    ;
	
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
	
	public String getQueryForConsumerMasterWSReport(List<Object> preparedStatement, WSReportSearchCriteria criteria,
			Integer limit, Integer offset) {
		
		StringBuilder query = new StringBuilder(QUERY_FOR_CONSUMER_MASTER_WS_REPORT);
		
		preparedStatement.add(criteria.getTenantId());
		
		query.append(AND_QUERY);
		query.append("ewc.tenantid = ?");
		preparedStatement.add(criteria.getTenantId());
		
		if(criteria.getWard() != null && !criteria.getWard().equalsIgnoreCase("nil")) {
			query.append(AND_QUERY);
			query.append("ewc.additionaldetails->> 'ward' = ? ");
			preparedStatement.add(criteria.getWard());
		}
		
		if(criteria.getConnectionType() != null) {
			query.append(AND_QUERY);
			query.append("ews.connectiontype = ? ");
			preparedStatement.add(criteria.getConnectionType());
		}
		
		query.append(" limit ? ");
		preparedStatement.add(limit);
		
		query.append(" offset ? ");
		preparedStatement.add(offset);
		
		return query.toString();
	}
	
	public String getBillSummaryDetailsQuery(WSReportSearchCriteria criteria, List<Object> preparedStmtList) {
		
       StringBuilder query = new StringBuilder(BILL_SUMMARY_QUERY2);
       
       Calendar c = Calendar.getInstance(); 
     c.setTimeInMillis(criteria.getMonthYear());
     
     String mMonth = Integer.toString(c.get(Calendar.MONTH)+1);
     if(mMonth.length()==1)
    	 mMonth="0"+mMonth;
     String mYear =   Integer.toString(c.get(Calendar.YEAR));
      

     preparedStmtList.add(mMonth);
     preparedStmtList.add(mYear);
     
     if(criteria.getTenantId() != null) {
			query.append(WHERE);
			query.append(" demand.tenantid = '").append(criteria.getTenantId()).append("'");
     }
     
     return query.toString();
	}

	public String getPropertyDetailsQuery(PropertyDetailsSearchCriteria criteria, List<Object> preparedPropStmtList) {

		 StringBuilder query = new StringBuilder(PROPERTY_DETAILS_SUMMARY_QUERY);

	     preparedPropStmtList.add(criteria.getUlbName());

	     if(criteria.getWardNo() != null) {
				query.append(AND);
				query.append(" epa.ward = '").append(criteria.getWardNo()).append("'");
	     }

	     return query.toString();
	}

	public String getWaterNewConsumerQuery(WSReportSearchCriteria criteria, List<Object> preparedStmtList) {
		
		StringBuilder query = new StringBuilder(QUERY_FOR_WATER_NEW_CONSUMER);
		
		if(criteria.getWard() != null && !criteria.getWard().equalsIgnoreCase("nil")) {
			query.append(AND_QUERY);
			query.append("ewc.additionaldetails->> 'ward' = ? ");
			preparedStmtList.add(criteria.getWard());
		}
		
		query.append(AND_QUERY).append(" ewc.tenantid = ? ");
		preparedStmtList.add(criteria.getTenantId());
		
		query.append(AND_QUERY).append(" ews.connectionexecutiondate >= ? ");
		preparedStmtList.add(criteria.getMonthYear());
		
		query.append(AND_QUERY).append(" ews.connectionexecutiondate <= ? ");
		preparedStmtList.add(wsReportUtils.addOneMonth(criteria.getMonthYear()));
		
		return query.toString();
	}
	
	public String getConsumerBillHistoryQuery(WSReportSearchCriteria criteria, List<Object> preparedStmtList) {
		
		StringBuilder query = new StringBuilder(QUERY_FOR_CONSUMER_BILL_HISTORY);
		
		query.append(" d.businessservice ='WS' ");
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
		if(searchCriteria.getPropertyId() != null) {
			query.append(AND_QUERY).append(" edv.consumercode = ? ");
			preparedPropStmtList.add(searchCriteria.getPropertyId());
		}

		if(searchCriteria.getOldPropertyId() != null) {
			query.append(AND_QUERY).append(" epp.oldpropertyid = ? ");
			preparedPropStmtList.add(searchCriteria.getOldPropertyId());
		}
		
		if(searchCriteria.getWardNo() != null) {
			query.append(AND_QUERY).append(" epa.ward  = ? ");
			preparedPropStmtList.add(searchCriteria.getWardNo());
		}
		
	
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
		
		if(criteria.getTenantId() != null) {
			query.append(AND_QUERY).append(" ewc.tenantid = ? ");
			preparedStmtList.add(criteria.getTenantId());
		}

		if(criteria.getWard() != null) {
			query.append(AND_QUERY).append(" ewc.additionaldetails->> 'ward' = ? ");
			preparedStmtList.add(criteria.getWard());
		}

		if(criteria.getConnectionType() != null) {
			query.append(AND_QUERY).append(" ews.connectiontype = ? ");
			preparedStmtList.add(criteria.getConnectionType());
		}
		
		if(criteria.getOldConnectionNo() != null) {
			query.append(AND_QUERY).append(" ewc.oldconnectionno = ? ");
			preparedStmtList.add(criteria.getOldConnectionNo());
		}

		return query.toString();

	}

	public String getPropertiesDetail(PropertyDetailsSearchCriteria searchCriteria, List<Object> preparedPropStmtList) {


		 StringBuilder query = new StringBuilder(PROPERTY_DETAILS_SUMMARY_QUERY);

	     preparedPropStmtList.add(searchCriteria.getUlbName());

	     if(searchCriteria.getWardNo() != null) {
				query.append(AND).append(" epa.ward = ? ");
				preparedPropStmtList.add(searchCriteria.getWardNo());
	     }

		if(searchCriteria.getPropertyId() != null) {
			query.append(AND_QUERY).append(" epp.propertyid = ? ");
			preparedPropStmtList.add(searchCriteria.getPropertyId());
		}
		
		if(searchCriteria.getOldPropertyId() != null) {
			query.append(AND_QUERY).append(" epp.oldpropertyid = ? ");
			preparedPropStmtList.add(searchCriteria.getOldPropertyId());
		}

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
		
		if(searchCriteria.getWard() != null) {
			query.append(AND).append(" ewc.additionaldetails->> 'ward' = ? ");
			preparedStmtList.add(searchCriteria.getWard());
		}
		
		return query.toString();
	}
	
	public String getElegibleWSConnectionsQuery(WSReportSearchCriteria searchCriteria, List<Object> preparedStmtList) {

		StringBuilder query = new StringBuilder(WS_CONNECTIONS_ELEGIBLE_FOR_DEMAND_QUERY);

		preparedStmtList.add(searchCriteria.getTenantId());

		return query.toString();
	}
	
	public String getDemandsQuery(WSReportSearchCriteria searchCriteria, List<Object> preparedStmtList, Integer limit,
			Integer offset) {

		StringBuilder query = new StringBuilder(QUERY_TO_GET_DEMANDS);
		
		query.append(AND_QUERY).append("ews.connectiontype = ? ");
		preparedStmtList.add("Non Metered");

		Long firstDate = wsReportUtils.getFirstDayOfMonthYear(searchCriteria.getFromDate());

		query.append(WHERE).append(" taxperiodfrom >= ? and taxperiodfrom <= ? ");
		preparedStmtList.add(firstDate);
		preparedStmtList.add(wsReportUtils.addOneMonth(firstDate));

		query.append(AND_QUERY).append(" businessservice = ?");
		preparedStmtList.add("WS");

		query.append(" limit ? ");
		preparedStmtList.add(limit);

		query.append(" offset ? ");
		preparedStmtList.add(offset);

		return query.toString();

	}
	
	public String getWaterConnectionQuery(WSReportSearchCriteria criteria, List<Object> preparedStmtList, Integer limit, Integer offset) {

		StringBuilder query = new StringBuilder(QUERY_FOR_WS_CONNECTION);
		
		if(criteria.getFromDate() != null) {
			
			if(criteria.getConnectionType() != null) {
				query.append(AND_QUERY).append(" ews.connectiontype = ? ");
				preparedStmtList.add(criteria.getConnectionType());
			}
			
			Long firstDate = wsReportUtils.getFirstDayOfMonthYear(criteria.getFromDate());
			query.append(AND_QUERY).append(" ews.connectionexecutiondate <= ? ");
			preparedStmtList.add(wsReportUtils.addOneMonth(firstDate));
		}
		
		query.append(" limit ? ");
		preparedStmtList.add(limit);
		
		query.append(" offset ? ");
		preparedStmtList.add(offset);
		
		
		return query.toString();

	}
	
	
	
	public String getSchedulerGeneratedDemandQuery(WSReportSearchCriteria searchCriteria,
			List<Object> preparedStmtList, Integer limit, Integer offset) {

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

		if(searchCriteria.getWard() != null) {
			query.append(AND).append(" EWC2.ADDITIONALDETAILS ->> 'ward' = ? ");
			preparedStmtList.add(searchCriteria.getWard());
		}
		
		query.append(" limit ? ");
		preparedStmtList.add(limit);
		
		query.append(" offset ? ");
		preparedStmtList.add(offset);
		
		log.info(query.toString());
		log.info("returning query from query builder");
		return query.toString();
	}

	public String getConsumerMasterReportCount(List<Object> preparedStatement, WSReportSearchCriteria criteria) {
		
		StringBuilder query = new StringBuilder("select count(*) "+ 
				"from eg_ws_connection ewc " + 
				"inner join eg_ws_service ews on ewc.id = ews.connection_id " + 
				"where ewc.applicationstatus = 'CONNECTION_ACTIVATED' " + 
				"and ewc.isoldapplication = false " );
		
		query.append(AND_QUERY);
		query.append("ewc.tenantid = ?");
		preparedStatement.add(criteria.getTenantId());
		
		if(criteria.getWard() != null && !criteria.getWard().equalsIgnoreCase("nil")) {
			query.append(AND_QUERY);
			query.append("ewc.additionaldetails->> 'ward' = ? ");
			preparedStatement.add(criteria.getWard());
		}
		
		if(criteria.getConnectionType() != null) {
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

		if(searchCriteria.getWard() != null) {
			query.append(AND).append(" EWC2.ADDITIONALDETAILS ->> 'ward' = ? ");
			preparedStatement.add(searchCriteria.getWard());
		}
		
		return query.toString();
	}
	
	public String getWaterConnectionCountQuery(WSReportSearchCriteria searchCriteria, List<Object> preparedStmtList) {

		StringBuilder query = new StringBuilder(QUERY_FOR_WS_CONNECTION_COUNT);

		if (searchCriteria.getFromDate() != null) {

			if (searchCriteria.getConnectionType() != null) {
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
        if(searchCriteria.getWard() != null) {
            query.append(AND_QUERY).append(" ewc.additionaldetails->> 'ward' = ? ");
            preparedStmtList.add(searchCriteria.getWard());
        }

        if(searchCriteria.getConnectionType() != null) {
            query.append(AND_QUERY).append(" ews.connectiontype = ? ");
            preparedStmtList.add(searchCriteria.getConnectionType());
        }
        

        return query.toString();
    }
	
	public String getWaterConnectionsQuery(WSReportSearchCriteria searchCriteria, List<Object> preparedStmtList,Integer limit , Integer offset) {
        StringBuilder query = new StringBuilder(QUERY_FOR_WATER_CONNECTIONS);

        query.append(AND_QUERY).append(" ewc.tenantid = ? ");
        preparedStmtList.add(searchCriteria.getTenantId());
        if(searchCriteria.getWard() != null) {
            query.append(AND_QUERY).append(" ewc.additionaldetails->> 'ward' = ? ");
            preparedStmtList.add(searchCriteria.getWard());
        }

        if(searchCriteria.getConnectionType() != null) {
            query.append(AND_QUERY).append(" ews.connectiontype = ? ");
            preparedStmtList.add(searchCriteria.getConnectionType());
        }
        query.append(" limit ? ");
        preparedStmtList.add(limit);
        
        query.append(" offset ? ");
        preparedStmtList.add(offset);

        return query.toString();
    }
	
	public String getWaterMonthlyDemandQuery2(WSReportSearchCriteria searchCriteria, List<Object> preparedStmtList ,List<String> keySet) {
        StringBuilder query = new StringBuilder(QUERY_FOR_WATER_MONTHLY_DEMANDS2);
        query.append(AND_QUERY).append(" d.taxperiodto >= ? ");
        preparedStmtList.add(searchCriteria.getFromDate());
        query.append(AND_QUERY).append(" d.taxperiodto  <= ? ");
        preparedStmtList.add(searchCriteria.getToDate());
        query.append(AND_QUERY).append(" d.consumercode IN ( ? ) ");
        preparedStmtList.add(String.join(" , ", keySet));
    
        return query.toString();
    }
}
