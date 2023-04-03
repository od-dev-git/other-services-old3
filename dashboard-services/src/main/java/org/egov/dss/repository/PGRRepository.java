package org.egov.dss.repository;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.egov.dss.config.ConfigurationLoader;
import org.egov.dss.model.Chart;
import org.egov.dss.model.PgrSearchCriteria;
import org.egov.dss.repository.builder.PgrQueryBuilder;
import org.egov.dss.repository.rowmapper.BPAPerformanceRateRowMapper;
import org.egov.dss.repository.rowmapper.ChartRowMapper;
import org.egov.dss.repository.rowmapper.TenantWiseConnectionsRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.SingleColumnRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Repository
public class PGRRepository {
	
	@Autowired
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	
	@Autowired
	private PgrQueryBuilder pgrQueryBuilder;
	
    @Autowired
	private ConfigurationLoader config;
	
	public Object getTotalApplications(PgrSearchCriteria pgrSearchCriteria) {
        Map<String, Object> preparedStatementValues = new HashMap<>();
        String query = pgrQueryBuilder.getTotalApplication(pgrSearchCriteria, preparedStatementValues);
        log.info("query: "+query);
        List<Integer> result = namedParameterJdbcTemplate.query(query, preparedStatementValues, new SingleColumnRowMapper<>(Integer.class));
        return result.get(0);
    }
	
	public Object getSlaAchievedAppCount(PgrSearchCriteria pgrSearchCriteria) {
        Map<String, Object> preparedStatementValues = new HashMap<>();
        pgrSearchCriteria.setSlaThreshold(config.getSlaPgrThreshold());
        String query = pgrQueryBuilder.getTotalApplication(pgrSearchCriteria, preparedStatementValues);
        log.info("query: "+query);
        List<Integer> result = namedParameterJdbcTemplate.query(query, preparedStatementValues, new SingleColumnRowMapper<>(Integer.class));
        return result.get(0);
    }

	public List<Chart> getTopFiveComplaints(PgrSearchCriteria criteria) {
		
		Map<String, Object> preparedStatementValues = new HashMap<>();
		String query = pgrQueryBuilder.getTopFiveComplaintsQuery(criteria, preparedStatementValues);
		log.info("query for get Top Five Complaints: " + query);
		return namedParameterJdbcTemplate.query(query, preparedStatementValues,new ChartRowMapper());
	}

	public HashMap<String, BigDecimal> getTenantWiseApplications(PgrSearchCriteria criteria) {	
		Map<String, Object> preparedStatementValues = new HashMap<>();
		String query = pgrQueryBuilder.getTenantWiseApplicationsQuery(criteria, preparedStatementValues);
		log.info("query for Tenant Wise Complaints: " + query);
		return namedParameterJdbcTemplate.query(query, preparedStatementValues,new TenantWiseConnectionsRowMapper());
	}

	public HashMap<String, BigDecimal> getTenantWiseSlaAchieved(PgrSearchCriteria criteria) {
		Map<String, Object> preparedStatementValues = new HashMap<>();
		criteria.setSlaThreshold(config.getSlaPgrThreshold());
		String query = pgrQueryBuilder.getTenantWiseApplicationsQuery(criteria, preparedStatementValues);
		log.info("query for tenant Wise SLA Achieved: " + query);
		return  namedParameterJdbcTemplate.query(query, preparedStatementValues, new TenantWiseConnectionsRowMapper());
	}

	public List<Chart> getCumulativeComplaints(PgrSearchCriteria criteria) {
		Map<String, Object> preparedStatementValues = new HashMap<>();
        String query = pgrQueryBuilder.getCumulativeComplaintsQuery(criteria, preparedStatementValues);
        log.info("query for Cumulative Complaints : "+query);
        List<Chart> result = namedParameterJdbcTemplate.query(query, preparedStatementValues, new ChartRowMapper());
        return result;
	}

	public List<Chart> getMonthYearData(PgrSearchCriteria criteria) {
		Map<String, Object> preparedStatementValues = new HashMap<>();
        String query = pgrQueryBuilder.getMonthYearDataQuery(criteria, preparedStatementValues);
        log.info("query for Month Year Query : "+query);
        return namedParameterJdbcTemplate.query(query, preparedStatementValues, new ChartRowMapper());
	}


}
