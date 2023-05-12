package org.egov.dss.repository.builder;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.egov.dss.model.PropertySerarchCriteria;import org.egov.dss.model.CommonSearchCriteria;
import org.egov.dss.model.DemandPayload;
import org.egov.dss.model.PayloadDetails;
import org.egov.dss.web.model.ChartCriteria;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.jayway.jsonpath.Criteria;

@Component
public class CommonQueryBuilder {
	
	public static final String PAYLOAD_QUERY_SQL = " select edd.id, edd.visualizationcode, edd.modulelevel, edd.startdate, edd.enddate, edd.timeinterval,"
			+ "edd.charttype , edd.tenantid, edd.districtid, edd.city, edd.headername, edd.valuetype"
			+ " from state.{tableName} edd ";
	
    public static String RESPONSE_DATA_UPDATE_QUERY = "Update state.{tableName} set responsedata = ?, lastmodifiedtime = ?, startdate = ?, enddate = ? where id =? ";
    
    public static final String PROPERTY_QUERY = " select to_char(monthYear, 'Mon-YYYY') as name, sum(completionApplication) over (order by monthYear asc rows between unbounded preceding and current row) as value "
    		+ "from (select to_date(concat('01-',EXTRACT(MONTH FROM to_timestamp(lastmodifiedtime/1000)),'-' ,EXTRACT(YEAR FROM to_timestamp(lastmodifiedtime/1000))),'DD-MM-YYYY') as monthYear, "
    		+ "count(*) completionApplication from eg_pt_property ";
    
    public static final String PAYLOAD_DATA_INSERT_QUERY = " insert into state.{tableName} (id,visualizationcode,modulelevel,startdate,enddate,timeinterval,charttype, tenantid, "
    		                                             + " headername, valuetype) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?) ";
    
	public static final String GET_DEMAND_PAYLOAD_QUERY = " select edv.tenantid as tenantid,coalesce(sum(edv2.taxamount), 0) as taxamount, coalesce(sum(edv2.collectionamount), 0) as collectionamount "
                                                         + "from egbs_demand_v1 edv inner join egbs_demanddetail_v1 edv2 on "
			                                             + "edv.id = edv2.demandid ";
	
	public static final String DEMAND_UPDATE_QUERY = " update state.eg_dss_demand set amount = ? , collectionamount = ? , lastmodifiedtime = ? where tenantid = ? and businessservice = ? and financialyear = ? ";
			
    public static String fetchSchedulerPayloads(ChartCriteria criteria, Map<String, Object> preparedStatementValues) {
    	String finalQuery = PAYLOAD_QUERY_SQL.replace("{tableName}", criteria.getTableName());
		StringBuilder selectQuery = new StringBuilder(finalQuery);
		return addWhereClause(selectQuery, preparedStatementValues, criteria);
	}
    
	public static String fetchDemandData(DemandPayload criteria, Map<String, Object> preparedStatementValues) {
		StringBuilder selectQuery = new StringBuilder(GET_DEMAND_PAYLOAD_QUERY);
		addWhereClauseForDemand(selectQuery, preparedStatementValues, criteria);
		addGroupByClause(selectQuery, "edv.tenantid");
		return selectQuery.toString();
	}
    
    private static void addClauseIfRequired(Map<String, Object> values, StringBuilder queryString) {
		if (values.isEmpty())
			queryString.append(" WHERE ");
		else {
			queryString.append(" AND");
		}
	}
    
    private static void addGroupByClause(StringBuilder demandQueryBuilder,String columnName) {
        demandQueryBuilder.append(" GROUP BY " + columnName);
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
		
		if (searchCriteria.getTenantIds() != null && !CollectionUtils.isEmpty(searchCriteria.getTenantIds())) {
			addClauseIfRequired(preparedStatementValues, selectQuery);
			selectQuery.append(" tenantId IN ( :tenantId)");
			preparedStatementValues.put("tenantId", searchCriteria.getTenantIds());
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
    
	private static String addWhereClauseForDemand(StringBuilder selectQuery, Map<String, Object> preparedStatementValues,
			DemandPayload criteria) {

	    if (criteria.getTaxPeriodFrom() != null) {
			addClauseIfRequired(preparedStatementValues, selectQuery);
			selectQuery.append(" edv.taxperiodfrom >= :startDate");
			preparedStatementValues.put("startDate", criteria.getTaxPeriodFrom());
		}

	    if (criteria.getTaxPeriodTo() != null) {
			addClauseIfRequired(preparedStatementValues, selectQuery);
			selectQuery.append(" edv.taxperiodto <= :endDate");
			preparedStatementValues.put("endDate", criteria.getTaxPeriodTo());
		}
	    
	    if (criteria.getBusinessService() != null) {
			addClauseIfRequired(preparedStatementValues, selectQuery);
			selectQuery.append(" edv.businessservice = :businessService");
			preparedStatementValues.put("businessService", criteria.getBusinessService());
		}
	    
	    if (criteria.getTaxHeadCode() != null) {
			addClauseIfRequired(preparedStatementValues, selectQuery);
			selectQuery.append(" edv2.taxheadcode not in (:taxHeadCode)");
			preparedStatementValues.put("taxHeadCode", criteria.getTaxHeadCode());
		}
	    
	    if (criteria.getExcludedTenantId() != null) {
			addClauseIfRequired(preparedStatementValues, selectQuery);
			selectQuery.append(" edv.tenantid != :excludedTenantId");
			preparedStatementValues.put("excludedTenantId", criteria.getExcludedTenantId());
		}
		
		return selectQuery.toString();

	}
	
	

}
