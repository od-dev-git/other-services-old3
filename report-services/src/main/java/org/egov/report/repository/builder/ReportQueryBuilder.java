package org.egov.report.repository.builder;

import java.util.List;
import java.util.Set;

import org.egov.report.web.model.WSReportSearchCriteria;
import org.springframework.stereotype.Component;

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
	
	
	
	
	
	
	

}
