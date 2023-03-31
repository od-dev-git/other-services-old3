package org.egov.dss.repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.egov.dss.config.ConfigurationLoader;
import org.egov.dss.model.Chart;
import org.egov.dss.model.MarriageSearchCriteria;
import org.egov.dss.repository.builder.MarriageQueryBuilder;
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
public class MRRepository {
	
	@Autowired
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	
	@Autowired
	private MarriageQueryBuilder mrQueryBuilder;
	
	@Autowired
	private ConfigurationLoader config;
	
	public Object getTotalApplications(MarriageSearchCriteria mrSearchCriteria) {
        Map<String, Object> preparedStatementValues = new HashMap<>();
        String query = mrQueryBuilder.getTotalApplication(mrSearchCriteria, preparedStatementValues);
        log.info("query: "+query);
        List<Integer> result = namedParameterJdbcTemplate.query(query, preparedStatementValues, new SingleColumnRowMapper<>(Integer.class));
        return result.get(0);
    }
	
	public Object getSlaAchievedAppCount(MarriageSearchCriteria mrSearchCriteria) {
        Map<String, Object> preparedStatementValues = new HashMap<>();
        mrSearchCriteria.setSlaThreshold(config.getSlaMrThreshold());
        String query = mrQueryBuilder.getTotalApplication(mrSearchCriteria, preparedStatementValues);
        log.info("query: "+query);
        List<Integer> result = namedParameterJdbcTemplate.query(query, preparedStatementValues, new SingleColumnRowMapper<>(Integer.class));
        return result.get(0);
    }
	
	public List<Chart> getCumulativeApplications(MarriageSearchCriteria mrSearchCriteria) {
		Map<String, Object> preparedStatementValues = new HashMap<>();
        String query = mrQueryBuilder.getCumulativeApplications(mrSearchCriteria, preparedStatementValues);
        log.info("query for MR Cumulative Applications : "+query);
        List<Chart> result = namedParameterJdbcTemplate.query(query, preparedStatementValues, new ChartRowMapper());
        return result;
	}
	
	public HashMap<String, Long> getTenantWiseTotalApplication(MarriageSearchCriteria mrSearchCriteria) {
		Map<String, Object> preparedStatementValues = new HashMap<>();
		String query = mrQueryBuilder.getTenantWiseTotalApplication(mrSearchCriteria, preparedStatementValues);
        log.info("MR Tenant Wise Total Application : "+query);
        HashMap<String, Long> result = namedParameterJdbcTemplate.query(query, preparedStatementValues, new ULBPerformanceRateRowMapper());
        return result;
	}
	
	public List<Chart> getApplicationsByStatus(MarriageSearchCriteria mrSearchCriteria) {
		Map<String, Object> preparedStatementValues = new HashMap<>();
		String query = mrQueryBuilder.getApplicationsByStatus(mrSearchCriteria, preparedStatementValues);
        log.info("MR applications by status : "+query);
        List<Chart> result = namedParameterJdbcTemplate.query(query, preparedStatementValues, new ChartRowMapper());
        return result;
	}
	
	public List<HashMap<String, Object>> getMrStatusByBoundary(MarriageSearchCriteria mrSearchCriteria) {
		Map<String, Object> preparedStatementValues = new HashMap<>();
        String query = mrQueryBuilder.getMrStatusByBoundary(mrSearchCriteria, preparedStatementValues);
        log.info("MR status by boundary table  : "+query);
        List<HashMap<String, Object>> result = namedParameterJdbcTemplate.query(query, preparedStatementValues, new TableChartRowMapper());
        return result;
	}



}
