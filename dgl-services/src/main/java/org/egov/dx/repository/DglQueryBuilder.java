package org.egov.dx.repository;

import java.util.Map;

import org.egov.dx.web.models.DGLSearchCriteria;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class DglQueryBuilder {
	
	private static final String QUERY = " select * from eg_dgl_reference_data dgl";
	
	
	public static String getDGLDataQuery(DGLSearchCriteria criteria, Map<String, Object> preparedStatementValues) {
		StringBuilder selectQuery = new StringBuilder(QUERY);
		return addWhereClause(selectQuery, preparedStatementValues, criteria);
	}
	
	private static String addWhereClause(StringBuilder selectQuery, Map<String, Object> preparedStatementValues,
			DGLSearchCriteria criteria) {
		if (!StringUtils.isEmpty(criteria.getConsumerCode())) {
			addClauseIfRequired(preparedStatementValues, selectQuery);
			selectQuery.append(" dgl.consumercode = :consumerCode ");
			preparedStatementValues.put("consumerCode",criteria.getConsumerCode());
		}
		if (!StringUtils.isEmpty(criteria.getMaskedId())) {
			addClauseIfRequired(preparedStatementValues, selectQuery);
			selectQuery.append(" dgl.maskedid = :maskedId ");
			preparedStatementValues.put("maskedId",criteria.getMaskedId());
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
