package org.egov.report.repository.builder;

import java.util.Calendar;
import java.util.List;
import java.util.Set;

import org.egov.report.web.model.PropertyDetailsSearchCriteria;
import org.egov.report.web.model.WSReportSearchCriteria;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;



@Component
public class ReportQueryBuilder {
	
	private static final String SELECT = "SELECT ";
	private static final String INNER_JOIN = " INNER JOIN ";
	private static final String AND_QUERY = " AND ";
	private static final String connectionSelectValues = " ewc.tenantid,ewc.additionaldetails->>'ward' as ward,ewc.connectionno,ewc.oldconnectionno,";
	private static final String serviceSelectValues = " ews.connectiontype,ews.connectioncategory,ews.usagecategory,ews.connectionfacility,";
	private static final String userSelectValues = " ch.id ";
	
	
	
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

	private static final String FROM = "from ";
	private static final String WHERE = "where ";
	private static final String AND = "and ";
	private static final String ON = "on ";
	private static final String AS = "as ";
//	private static final String INNER_JOIN = "join ";
	private static final String DEMAND_TABLE_COLUMNS = "tenantid,edv.consumercode,to_char(to_timestamp(edv.taxperiodfrom / 1000), 'MM-YYYY') as monthYear ";
	private static final String ULB_AND_MONTH = "demand.TENANTID as ulb,demand.monthYear ";
	private static final String MONTH_YEAR_CLAUSE = "(to_char(to_timestamp(edv.taxperiodfrom / 1000), 'MM')= ? and to_char(to_timestamp(edv.taxperiodfrom / 1000), 'YYYY')= ? ) ";
	private static final String LEFT_OUTER_JOIN = "left outer join ";
	
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
			+ "epp.tenantid,epa.ward,epp.oldpropertyid,epp.propertyid,epo.userid,"
			+ "epa.doorno,epa.buildingname,epa.street,epa.city,epa.pincode "
			+ FROM
			+ "eg_pt_property epp "
			+ INNER_JOIN + "eg_pt_owner epo " +  ON  + "epo.propertyid = epp.id "
			+ LEFT_OUTER_JOIN + "eg_user eu on eu.uuid = epo.userid "
			+ INNER_JOIN + "eg_pt_address epa on epa.propertyid = epp.id "
			+ WHERE + "epp.status <> 'INACTIVE' "
			+ AND + "epp.tenantid = ? ";

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

}
