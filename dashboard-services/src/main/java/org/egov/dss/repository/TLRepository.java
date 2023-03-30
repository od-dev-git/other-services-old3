package org.egov.dss.repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.egov.dss.config.ConfigurationLoader;
import org.egov.dss.constants.DashboardConstants;
import org.egov.dss.model.Chart;
import org.egov.dss.model.TLSearchCriteria;
import org.egov.dss.repository.builder.TradeLicenseQueryBuilder;
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
public class TLRepository {
	
	
	@Autowired
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	
	@Autowired
	private TradeLicenseQueryBuilder tlQueryBuilder;
	
	@Autowired
	private ConfigurationLoader config;
	
	public Object getTotalApplications(TLSearchCriteria tlSearchCriteria) {
        Map<String, Object> preparedStatementValues = new HashMap<>();
        String query = tlQueryBuilder.getTotalApplication(tlSearchCriteria, preparedStatementValues);
        log.info("query: "+query);
        List<Integer> result = namedParameterJdbcTemplate.query(query, preparedStatementValues, new SingleColumnRowMapper<>(Integer.class));
        return result.get(0);
    }
	
	public Object getSlaAchievedAppCount(TLSearchCriteria tlSearchCriteria) {
        Map<String, Object> preparedStatementValues = new HashMap<>();
        tlSearchCriteria.setSlaThreshold(config.getSlaTlThreshold());
        String query = tlQueryBuilder.getTotalApplication(tlSearchCriteria, preparedStatementValues);
        log.info("query: "+query);
        List<Integer> result = namedParameterJdbcTemplate.query(query, preparedStatementValues, new SingleColumnRowMapper<>(Integer.class));
        return result.get(0);
    }
	
	public Object getTotalActiveUlbs(TLSearchCriteria tlSearchCriteria) {
        Map<String, Object> preparedStatementValues = new HashMap<>();
        String query = tlQueryBuilder.getTotalActiveUlbs(tlSearchCriteria, preparedStatementValues);
        log.info("query: "+query);
        List<Integer> result = namedParameterJdbcTemplate.query(query, preparedStatementValues, new SingleColumnRowMapper<>(Integer.class));
        return result.get(0);
    }
	
	public List<Chart> getCumulativeLicenseIssued(TLSearchCriteria tlSearchCriteria) {
		Map<String, Object> preparedStatementValues = new HashMap<>();
        String query = tlQueryBuilder.getCumulativeLicenseIssued(tlSearchCriteria, preparedStatementValues);
        log.info("query for TL Cumulative License Issued : "+query);
        List<Chart> result = namedParameterJdbcTemplate.query(query, preparedStatementValues, new ChartRowMapper());
        return result;
	}
	
	public HashMap<String, Long> getTenantWiseTotalApplication(TLSearchCriteria tlSearchCriteria) {
		Map<String, Object> preparedStatementValues = new HashMap<>();
		tlSearchCriteria.setIsApplicationDate(Boolean.TRUE);
        String query = tlQueryBuilder.getTenantWiseTotalApplication(tlSearchCriteria, preparedStatementValues);
        log.info("TL Tenant Wise Total Application : "+query);
        HashMap<String, Long> result = namedParameterJdbcTemplate.query(query, preparedStatementValues, new ULBPerformanceRateRowMapper());
        return result;
	}

	
	public HashMap<String, Long> getTenantWiseLicenseIssued(TLSearchCriteria tlSearchCriteria) {
		Map<String, Object> preparedStatementValues = new HashMap<>();
		tlSearchCriteria.setStatus(DashboardConstants.STATUS_APPROVED);
        String query = tlQueryBuilder.getTenantWiseTotalApplication(tlSearchCriteria, preparedStatementValues);
        log.info("TL Tenant Wise License Issued : "+query);
        HashMap<String, Long> result = namedParameterJdbcTemplate.query(query, preparedStatementValues, new ULBPerformanceRateRowMapper());
        return result;
	}
	
	public List<Chart> getLicenseByStatus(TLSearchCriteria tlSearchCriteria) {
		Map<String, Object> preparedStatementValues = new HashMap<>();
		String query = tlQueryBuilder.getLicenseByStatus(tlSearchCriteria, preparedStatementValues);
        log.info("Trade license by application status : "+query);
        List<Chart> result = namedParameterJdbcTemplate.query(query, preparedStatementValues, new ChartRowMapper());
        return result;
	}
	
	public List<HashMap<String, Object>> getTlStatusByBoundary(TLSearchCriteria tlSearchCriteria) {
		Map<String, Object> preparedStatementValues = new HashMap<>();
        String query = tlQueryBuilder.getTlStatusByBoundary(tlSearchCriteria, preparedStatementValues);
        log.info("TL status by boundary table  : "+query);
        List<HashMap<String, Object>> result = namedParameterJdbcTemplate.query(query, preparedStatementValues, new TableChartRowMapper());
        return result;
	}


	
}
