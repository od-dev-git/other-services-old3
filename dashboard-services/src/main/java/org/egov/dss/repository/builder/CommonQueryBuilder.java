package org.egov.dss.repository.builder;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.egov.dss.model.PropertySerarchCriteria;
import org.egov.dss.web.model.ChartCriteria;
import org.springframework.stereotype.Component;

import com.jayway.jsonpath.Criteria;

@Component
public class CommonQueryBuilder {
	
	public static final String PAYLOAD_QUERY_SQL = " select edd.id, edd.visualizationcode, edd.modulelevel, edd.startdate, edd.enddate, edd.timeinterval,"
			+ "edd.charttype , edd.tenantid, edd.districtid, edd.city, edd.headername, edd.valuetype"
			+ " from eg_dss_response edd ";
	
    public static final String RESPONSE_DATA_UPDATE_QUERY = "Update eg_dss_response set responsedata = ?, lastmodifiedtime = ?, startdate = ?, enddate = ? where id =? ";
    
    public static final String PROPERTY_QUERY = " select to_char(monthYear, 'Mon-YYYY') as name, sum(completionApplication) over (order by monthYear asc rows between unbounded preceding and current row) as value "
    		+ "from (select to_date(concat('01-',EXTRACT(MONTH FROM to_timestamp(lastmodifiedtime/1000)),'-' ,EXTRACT(YEAR FROM to_timestamp(lastmodifiedtime/1000))),'DD-MM-YYYY') as monthYear, "
    		+ "count(*) completionApplication from eg_pt_property ";
    
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
	
	private String addWhereClauseForProperty(StringBuilder selectQuery, Map<String, Object> preparedStatementValues,
			PropertySerarchCriteria searchCriteria) {
		if (StringUtils.isNotBlank(searchCriteria.getTenantId())) {
			addClauseIfRequired(preparedStatementValues, selectQuery);
			if (searchCriteria.getTenantId().split("\\.").length > 1) {
				selectQuery.append(" tenantId =:tenantId");
				preparedStatementValues.put("tenantId", searchCriteria.getTenantId());
		} else {
				selectQuery.append(" tenantId LIKE :tenantId");
				preparedStatementValues.put("tenantId", searchCriteria.getTenantId() + "%");
			}

		}

       if (searchCriteria.getFromDate() != null) {
			addClauseIfRequired(preparedStatementValues, selectQuery);
			selectQuery.append(" lastmodifiedtime >= :fromDate");
			preparedStatementValues.put("fromDate", searchCriteria.getFromDate());
		}

		if (searchCriteria.getToDate() != null) {
			addClauseIfRequired(preparedStatementValues, selectQuery);
			selectQuery.append(" lastmodifiedtime <= :toDate");	
			preparedStatementValues.put("toDate", searchCriteria.getToDate());
	    }
		
		if (searchCriteria.getSlaThreshold() != null) {
			addClauseIfRequired(preparedStatementValues, selectQuery);
			selectQuery.append(" lastmodifiedtime - epaa.createdtime < " + searchCriteria.getSlaThreshold());
		}
		
		if (searchCriteria.getExcludedTenantId() != null) {
			addClauseIfRequired(preparedStatementValues, selectQuery);
			selectQuery.append(" tenantId != :excludedTenantId");
			preparedStatementValues.put("excludedTenantId", searchCriteria.getExcludedTenantId());
	    }
		
		if (!StringUtils.isEmpty(searchCriteria.getStatus())) {
			addClauseIfRequired(preparedStatementValues, selectQuery);
			selectQuery.append(" status = :status");
			preparedStatementValues.put("status", searchCriteria.getStatus());
	    }
		
		return selectQuery.toString();
	}

	public String getTotalPropertiesQuery(PropertySerarchCriteria criteriaProperty,
			Map<String, Object> preparedStatementValues) {
		
		StringBuilder selectQuery = new StringBuilder(PROPERTY_QUERY);
		addWhereClauseForProperty(selectQuery, preparedStatementValues, criteriaProperty);
		selectQuery.append(" group by to_date(concat('01-',EXTRACT(MONTH FROM to_timestamp(lastmodifiedtime/1000)),'-' ,EXTRACT(YEAR FROM to_timestamp(lastmodifiedtime/1000))),'DD-MM-YYYY') ");
		selectQuery.append(" order by to_date(concat('01-',EXTRACT(MONTH FROM to_timestamp(lastmodifiedtime/1000)),'-' ,EXTRACT(YEAR FROM to_timestamp(lastmodifiedtime/1000))),'DD-MM-YYYY')) tmp ");
		return selectQuery.toString();
	}

	

}
