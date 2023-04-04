package org.egov.dss.repository.builder;

import java.util.Map;


import org.egov.dss.web.model.ChartCriteria;
import org.springframework.stereotype.Component;

@Component
public class CommonQueryBuilder {
	
	public static final String PAYLOAD_QUERY_SQL = " select edd.id, edd.visualizationcode, edd.modulelevel, edd.startdate, edd.enddate, edd.timeinterval,"
			+ "edd.charttype , edd.tenantid, edd.districtid, edd.city, edd.headername, edd.valuetype"
			+ " from eg_dss_response edd ";
	
    public static final String RESPONSE_DATA_UPDATE_QUERY = "Update eg_dss_response set responsedata = ?, lastmodifiedtime = ?, startdate = ?, enddate = ? where id =? ";
    
    public static String fetchSchedulerPayloads(ChartCriteria criteria, Map<String, Object> preparedStatementValues) {
		StringBuilder selectQuery = new StringBuilder(PAYLOAD_QUERY_SQL);
		return addWhereClause(selectQuery, preparedStatementValues, criteria);
	}
    
    private static void addClauseIfRequired(Map<String, Object> values, StringBuilder queryString) {
		if (values.isEmpty())
			queryString.append(" WHERE ");
		else {
			queryString.append(" AND");
		}
	}

	private static String addWhereClause(StringBuilder selectQuery, Map<String, Object> preparedStatementValues,
			ChartCriteria criteria) {

	    if (criteria.getStartDate() != null) {
			addClauseIfRequired(preparedStatementValues, selectQuery);
			selectQuery.append(" edd.startdate >= :startDate");
			preparedStatementValues.put("startDate", criteria.getStartDate());
		}

	    if (criteria.getEndDate() != null) {
			addClauseIfRequired(preparedStatementValues, selectQuery);
			selectQuery.append(" edd.enddate <= :endDate");
			preparedStatementValues.put("endDate", criteria.getEndDate());
		}
		
		

		return selectQuery.toString();

	}

}
