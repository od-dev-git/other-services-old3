package org.egov.report.repository.builder;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.egov.report.web.model.SRReportSearchCriteria;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class SRQueryBuilder {
	
	
	private static final String QUERY_TCIKET_DETAIL = " select ser.tenantid, ser.city, ser.servicerequestid, ser.createdtime, ser.lastmodifiedtime, ser.createdby, ser.firstname,ser.phone, ser.service, ser.servicetype, "
			+ "ser.description, ser.priority, ser.status from eg_sr_service ser  ";
	
	public String getTicketDetailsQuery(Map<String, Object> preparedStatement, SRReportSearchCriteria criteria) {
		
		StringBuilder query = new StringBuilder(QUERY_TCIKET_DETAIL);
		
		return addWhereClause(query, preparedStatement, criteria);

	}
	
	private static String addWhereClause(StringBuilder selectQuery, Map<String, Object> preparedStatementValues,
			SRReportSearchCriteria searchCriteria) {
		
		//Remove the comments if TenantId field to be kept in search
		/*if (!StringUtils.isEmpty(searchCriteria.getTenantId())) {
			addClauseIfRequired(preparedStatementValues, selectQuery);
			selectQuery.append(" ser.tenantid IN ( :tenantId )");
			preparedStatementValues.put("tenantId", searchCriteria.getTenantId());
		}*/
		
		if (!StringUtils.isEmpty(searchCriteria.getCity())) {
			addClauseIfRequired(preparedStatementValues, selectQuery);
			selectQuery.append(" ser.city IN ( :city )");
			preparedStatementValues.put("city", searchCriteria.getCity());
		}
		
		if (searchCriteria.getFromDate() != null) {
			addClauseIfRequired(preparedStatementValues, selectQuery);
			selectQuery.append(" ser.createdtime >= :fromDate ");
			preparedStatementValues.put("fromDate", searchCriteria.getFromDate());
		}
		
		if (searchCriteria.getToDate() != null) {
			addClauseIfRequired(preparedStatementValues, selectQuery);
			selectQuery.append(" ser.createdtime <= :toDate ");
			preparedStatementValues.put("toDate", searchCriteria.getToDate());
		}
		
		if (!StringUtils.isEmpty(searchCriteria.getServiceRequestId())) {
			addClauseIfRequired(preparedStatementValues, selectQuery);
			selectQuery.append(" ser.servicerequestid = :servicerequestid");
			preparedStatementValues.put("servicerequestid", searchCriteria.getServiceRequestId());
		}
		
		if (!StringUtils.isEmpty(searchCriteria.getService())) {
			addClauseIfRequired(preparedStatementValues, selectQuery);
			selectQuery.append(" ser.service = :service");
			preparedStatementValues.put("service", searchCriteria.getService());
		}
		
		if (!StringUtils.isEmpty(searchCriteria.getStatus())) {
			addClauseIfRequired(preparedStatementValues, selectQuery);
			selectQuery.append(" ser.status = :status");
			preparedStatementValues.put("status", searchCriteria.getStatus());
		}

		return selectQuery.toString();
	}
	
	private static void addClauseIfRequired(Map<String, Object> values, StringBuilder queryString) {
		if (values.isEmpty())
			queryString.append(" WHERE ");
		else {
			queryString.append(" AND");
		}
	}
	

}
