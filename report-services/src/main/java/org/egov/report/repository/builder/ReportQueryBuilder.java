package org.egov.report.repository.builder;

import java.util.Calendar;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;



@Component
public class ReportQueryBuilder {
	
	private static final String HRMS_QUERY = "select id, tenantid from eg_hrms_employee ";
	private static final String SELECT = "select ";
	private static final String FROM = "from ";
	private static final String WHERE = "where ";
	private static final String AND = "and ";
	private static final String ON = "on ";
	private static final String AS = "as ";
	private static final String INNER_JOIN = "join ";
	private static final String DEMAND_TABLE_COLUMNS = "tenantid,edv.consumercode,to_char(to_timestamp(edv.taxperiodfrom / 1000), 'MM-YYYY') as monthYear ";
	private static final String ULB_AND_MONTH = "demand.TENANTID as ulb,demand.monthYear ";
	private static final String MONTH_YEAR_CLAUSE = "(to_char(to_timestamp(edv.taxperiodfrom / 1000), 'MM')= ? and to_char(to_timestamp(edv.taxperiodfrom / 1000), 'YYYY')= ? ) ";
	
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

}
