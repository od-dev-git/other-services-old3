package org.egov.dss.repository;

import static java.util.Collections.reverseOrder;

import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.egov.dss.config.ConfigurationLoader;
import org.egov.dss.model.Chart;
import org.egov.dss.model.PropertySerarchCriteria;
import org.egov.dss.repository.builder.PTServiceQueryBuilder;
import org.egov.dss.repository.rowmapper.ChartRowMapper;
import org.egov.dss.repository.rowmapper.TableChartRowMapper;
import org.egov.dss.repository.rowmapper.ULBPerformanceRateRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.SingleColumnRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;


import lombok.extern.slf4j.Slf4j;

@Slf4j
@Repository
public class PTRepository {
	
	@Autowired
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	
	@Autowired
	private PTServiceQueryBuilder ptServiceQueryBuilder;
	
	@Autowired
	private ConfigurationLoader config;
	
	
	public Object getAssessedPropertiesCount(PropertySerarchCriteria propertySearchCriteria) {
        Map<String, Object> preparedStatementValues = new HashMap<>();
        String query = ptServiceQueryBuilder.getAccessedPropertiesCountQuery(propertySearchCriteria, preparedStatementValues);
        log.info("query: "+query);
        List<Integer> result = namedParameterJdbcTemplate.query(query, preparedStatementValues, new SingleColumnRowMapper<>(Integer.class));
        return result.get(0);
    }


	public Integer getTotalPropertiesCount(PropertySerarchCriteria propertySearchCriteria) {
		Map<String, Object> preparedStatementValues = new HashMap<>();
		String query = ptServiceQueryBuilder.getTotalPropertiesCountQuery(propertySearchCriteria, preparedStatementValues);
		log.info("query for total properties: "+ query);
		List<Integer> result = namedParameterJdbcTemplate.query(query, preparedStatementValues, new SingleColumnRowMapper<>(Integer.class));
		return result.get(0);
	}
	
	public Integer getTotalApplicationsCount(PropertySerarchCriteria propertySearchCriteria) {
		Map<String, Object> preparedStatementValues = new HashMap<>();
		String query = ptServiceQueryBuilder.getTotalApplicationsCountQuery(propertySearchCriteria, preparedStatementValues);
		log.info("query for total applications: "+ query);
		List<Integer> result = namedParameterJdbcTemplate.query(query, preparedStatementValues, new SingleColumnRowMapper<>(Integer.class));
		return result.get(0);
	}
	
	public Object getSlaAchievedAppCount(PropertySerarchCriteria propertySearchCriteria) {
        Map<String, Object> preparedStatementValues = new HashMap<>();
        propertySearchCriteria.setSlaThreshold(config.getSlaPtThreshold());
        String query = ptServiceQueryBuilder.getAccessedPropertiesCountQuery(propertySearchCriteria, preparedStatementValues);
        log.info("query: "+query);
        List<Integer> result = namedParameterJdbcTemplate.query(query, preparedStatementValues, new SingleColumnRowMapper<>(Integer.class));
        return result.get(0);
    }
	
	public Object getActivePRopertyULBs(PropertySerarchCriteria propertySearchCriteria) {
		Map<String, Object> preparedStatementValues = new HashMap<>();
        String query = ptServiceQueryBuilder.getActiveULBsQuery(propertySearchCriteria, preparedStatementValues);
        log.info("query for active property ULBs : "+query);
        List<Integer> result = namedParameterJdbcTemplate.query(query, preparedStatementValues, new SingleColumnRowMapper<>(Integer.class));
        return result.get(0);
	}


	public Integer getTotalProperties(PropertySerarchCriteria propertySearchCriteria) {
		Map<String, Object> preparedStatementValues = new HashMap<>();
        String query = ptServiceQueryBuilder.getTotalPropertiesQuery(propertySearchCriteria, preparedStatementValues);
        log.info("query for total Properties : "+query);
        List<Integer> result = namedParameterJdbcTemplate.query(query, preparedStatementValues, new SingleColumnRowMapper<>(Integer.class));
        return result.get(0);
	}


	public Integer getTotalPropertiesPaid(PropertySerarchCriteria propertySearchCriteria) {
		Map<String, Object> preparedStatementValues = new HashMap<>();
        String query = ptServiceQueryBuilder.getTotalPropertiesPaidQuery(propertySearchCriteria, preparedStatementValues);
        log.info("query for Total Properties Paid : "+query);
        List<Integer> result = namedParameterJdbcTemplate.query(query, preparedStatementValues, new SingleColumnRowMapper<>(Integer.class));
        return result.get(0);
	}


	public Integer getTotalMutationPropertiesCount(PropertySerarchCriteria propertySearchCriteria) {
		Map<String, Object> preparedStatementValues = new HashMap<>();
        String query = ptServiceQueryBuilder.getTotalMutationPropertiesCountQuery(propertySearchCriteria, preparedStatementValues);
        log.info("query for Total Mutation Property Count : "+query);
        List<Integer> result = namedParameterJdbcTemplate.query(query, preparedStatementValues, new SingleColumnRowMapper<>(Integer.class));
        return result.get(0);
	}
	
	public Integer getPtTotalAssessmentsCount(PropertySerarchCriteria propertySearchCriteria) {
		Map<String, Object> preparedStatementValues = new HashMap<>();
        String query = ptServiceQueryBuilder.getPtTotalAssessmentsCountQuery(propertySearchCriteria, preparedStatementValues);
        log.info("query for PT Total Assessment Count : "+query);
        List<Integer> result = namedParameterJdbcTemplate.query(query, preparedStatementValues, new SingleColumnRowMapper<>(Integer.class));
        return result.get(0);
	}
	
	public Integer getPtTotalNewAssessmentsCount(PropertySerarchCriteria propertySearchCriteria) {
		Map<String, Object> preparedStatementValues = new HashMap<>();
        String query = ptServiceQueryBuilder.getPtTotalNewAssessmentsCountQuery(propertySearchCriteria, preparedStatementValues);
        log.info("query for PT New Assessment Count : "+query);
        List<Integer> result = namedParameterJdbcTemplate.query(query, preparedStatementValues, new SingleColumnRowMapper<>(Integer.class));
        return result.get(0);
	}
	
	public HashMap<String, Long> getPtTotalAssessmentsTenantwiseCount(PropertySerarchCriteria propertySearchCriteria) {
		Map<String, Object> preparedStatementValues = new HashMap<>();
        String query = ptServiceQueryBuilder.getPtTotalAssessmentsTenantwiseCountQuery(propertySearchCriteria, preparedStatementValues);
        log.info("query for PT Total Assessment Tenantwise Count : "+query);
        HashMap<String, Long> result = namedParameterJdbcTemplate.query(query, preparedStatementValues, new ULBPerformanceRateRowMapper());
        return result;
	}
	
	public HashMap<String, Long> getPtTotalNewAssessmentsTenantwiseCount(PropertySerarchCriteria propertySearchCriteria) {
		Map<String, Object> preparedStatementValues = new HashMap<>();
        String query = ptServiceQueryBuilder.getPtTotalNewAssessmentsTenantwiseCount(propertySearchCriteria, preparedStatementValues);
        log.info("query for PT Total New Assessment Tenantwise Count : "+query);
        HashMap<String, Long> result = namedParameterJdbcTemplate.query(query, preparedStatementValues, new ULBPerformanceRateRowMapper());
        return result;
	}
	
	public List<Chart> getCumulativePropertiesAssessed(PropertySerarchCriteria propertySearchCriteria) {
		Map<String, Object> preparedStatementValues = new HashMap<>();
        String query = ptServiceQueryBuilder.getCumulativePropertiesAssessedQuery(propertySearchCriteria, preparedStatementValues);
        log.info("query for Cumulative Properties Assessed Count : "+query);
        List<Chart> result = namedParameterJdbcTemplate.query(query, preparedStatementValues, new ChartRowMapper());
        return result;
	}
	
	public List<Chart> getpropertiesByUsageType(PropertySerarchCriteria propertySearchCriteria) {
		Map<String, Object> preparedStatementValues = new HashMap<>();
        String query = ptServiceQueryBuilder.getpropertiesByUsageTypeQuery(propertySearchCriteria, preparedStatementValues);
        log.info("query getpropertiesByUsageType : "+query);
        List<Chart> result = namedParameterJdbcTemplate.query(query, preparedStatementValues, new ChartRowMapper());
        return result;
	}
	
	public HashMap<String, Long> getSlaCompletionCountList(PropertySerarchCriteria propertySearchCriteria) {
		Map<String, Object> preparedStatementValues = new HashMap<>();
		propertySearchCriteria.setSlaThreshold(config.getSlaPtThreshold());
        String query = ptServiceQueryBuilder.getSlaCompletionCountListQuery(propertySearchCriteria, preparedStatementValues);
        log.info("query getSlaCompletionCountList : "+query);
        HashMap<String, Long> result = namedParameterJdbcTemplate.query(query, preparedStatementValues, new ULBPerformanceRateRowMapper());
        propertySearchCriteria.setSlaThreshold(null);
        return result;
	}

	public HashMap<String, Long> getTotalApplicationCompletionCountList(PropertySerarchCriteria propertySearchCriteria) {
		Map<String, Object> preparedStatementValues = new HashMap<>();
        String query = ptServiceQueryBuilder.getTotalApplicationCompletionCountListQuery(propertySearchCriteria, preparedStatementValues);
        log.info("query getTotalApplicationCompletionCountList : "+query);
        HashMap<String, Long> result = namedParameterJdbcTemplate.query(query, preparedStatementValues, new ULBPerformanceRateRowMapper());
        return result;
	}


	public List<HashMap<String, Object>> getPropertiesByFinancialYear(PropertySerarchCriteria propertySearchCriteria) {
		Map<String, Object> preparedStatementValues = new HashMap<>();
        String query = ptServiceQueryBuilder.getgetPropertiesByFinancialYearListQuery(propertySearchCriteria, preparedStatementValues);
        log.info("query getTotalApplicationCompletionCountList : "+query);
        List<HashMap<String, Object>> result = namedParameterJdbcTemplate.query(query, preparedStatementValues, new TableChartRowMapper());
        return result;
	}
}
