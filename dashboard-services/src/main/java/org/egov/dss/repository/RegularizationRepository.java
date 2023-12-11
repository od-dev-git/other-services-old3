package org.egov.dss.repository;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import org.egov.dss.config.ConfigurationLoader;
import org.egov.dss.model.RegularizationSearchCriteria;
import org.egov.dss.model.BpaSearchCriteria;
import org.egov.dss.model.Chart;
import org.egov.dss.repository.builder.RegularizationQueryBuilder;
import org.egov.dss.repository.rowmapper.RegularizationPerformanceRateRowMapper;
import org.egov.dss.repository.rowmapper.ChartRowMapper;
import org.egov.dss.repository.rowmapper.RateRowMapper;
import org.egov.dss.repository.rowmapper.TableChartRowMapper;
import org.egov.dss.repository.rowmapper.TenantWiseCollectionRowMapper;
import org.egov.dss.repository.rowmapper.ULBPerformanceRateRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.SingleColumnRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Repository
public class RegularizationRepository {
	
	@Autowired
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	
	@Autowired
	private RegularizationQueryBuilder regularizationQueryBuilder;
	
	@Autowired
	private ConfigurationLoader config;
	
	public HashMap<String, BigDecimal> getTenantWiseAvgDaysPermitIssued(RegularizationSearchCriteria regularizationSearchCriteria) {
	        Map<String, Object> preparedStatementValues = new HashMap<>();
	        String query = regularizationQueryBuilder.getTenantWiseAvgPermitIssue(regularizationSearchCriteria, preparedStatementValues);
	        log.info("query for Regularization Tenant Wise Permit Issued Avg Days : "+query);
	        HashMap<String, BigDecimal> result = namedParameterJdbcTemplate.query(query, preparedStatementValues, new TenantWiseCollectionRowMapper());
	        return result;
	    }
	
    public HashMap<String, BigDecimal> getTotalApplicationByServiceType(RegularizationSearchCriteria regularizationSearchCriteria) {
	        Map<String, Object> preparedStatementValues = new HashMap<>();
	        String query = regularizationQueryBuilder.getTotalApplicationByServiceType(regularizationSearchCriteria, preparedStatementValues);
	        log.info("query for Regularization Total Application by Service Type : "+query);
	        HashMap<String, BigDecimal> result = namedParameterJdbcTemplate.query(query, preparedStatementValues, new TenantWiseCollectionRowMapper());
	        return result;
	    }
	    
	public HashMap<String, BigDecimal> getApprovedApplicationByServiceType(RegularizationSearchCriteria regularizationSearchCriteria) {
	        Map<String, Object> preparedStatementValues = new HashMap<>();
	        String query = regularizationQueryBuilder.getApprovedApplicationByServiceType(regularizationSearchCriteria, preparedStatementValues);
	        log.info("query for Regularization Total Approved Application by Service Type : "+query);
	        HashMap<String, BigDecimal> result = namedParameterJdbcTemplate.query(query, preparedStatementValues, new TenantWiseCollectionRowMapper());
	        return result;
	    }
	    
	public HashMap<String, BigDecimal> getAvgDaysToIssuePermitByServiceType(RegularizationSearchCriteria regularizationSearchCriteria) {
	        Map<String, Object> preparedStatementValues = new HashMap<>();
	        String query = regularizationQueryBuilder.getAvgDaysToIssuePermitByServiceType(regularizationSearchCriteria, preparedStatementValues);
	        log.info("query for Regularization Avg Days to issue permit by Service Type : "+query);
	        HashMap<String, BigDecimal> result = namedParameterJdbcTemplate.query(query, preparedStatementValues, new TenantWiseCollectionRowMapper());
	        return result;
	    }

		
}
