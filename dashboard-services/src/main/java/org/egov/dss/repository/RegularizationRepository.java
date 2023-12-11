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
import java.util.List;
import java.util.Map;

import org.egov.dss.config.ConfigurationLoader;
import org.egov.dss.model.BpaSearchCriteria;
import org.egov.dss.model.RegularizationSearchCriteria;
import org.egov.dss.repository.builder.BpaQueryBuilder;
import org.egov.dss.repository.builder.RegularizationQueryBuilder;
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

		

	public Integer totalApplicationsReceived(RegularizationSearchCriteria regularizationSearchCriteria) {
        Map<String, Object> preparedStatementValues = new HashMap<>();
        String query = regularizationQueryBuilder.getGeneralQuery(regularizationSearchCriteria, preparedStatementValues);
        log.info("query for totalApplicationsReceived: "+query);
        List<Integer> result = namedParameterJdbcTemplate.query(query, preparedStatementValues, new SingleColumnRowMapper<>(Integer.class));
        return result.get(0);
	}    
	
	public Object getTotalRegularizationCertificateIssued(RegularizationSearchCriteria regularizationSearchCriteria) {
        Map<String, Object> preparedStatementValues = new HashMap<>();
        String query = regularizationQueryBuilder.getTotalRegularizationCertificateIssued(regularizationSearchCriteria, preparedStatementValues);
        log.info("query for getTotalRegularizationCertificateIssued: "+query);
        List<Integer> result = namedParameterJdbcTemplate.query(query, preparedStatementValues, new SingleColumnRowMapper<>(Integer.class));
        return result.get(0);
    }
	
	public Integer totalApplicationsRejected(RegularizationSearchCriteria regularizationSearchCriteria) {
        Map<String, Object> preparedStatementValues = new HashMap<>();
        String query = regularizationQueryBuilder.getGeneralQuery(regularizationSearchCriteria, preparedStatementValues);
        log.info("query for totalApplicationsRejected: "+query);
        List<Integer> result = namedParameterJdbcTemplate.query(query, preparedStatementValues, new SingleColumnRowMapper<>(Integer.class));
        return result.get(0);
	}
	
	public Integer totalApplicationsPending(RegularizationSearchCriteria regularizationSearchCriteria) {
        Map<String, Object> preparedStatementValues = new HashMap<>();
        String query = regularizationQueryBuilder.getPendingApplicationQuery(regularizationSearchCriteria, preparedStatementValues);
        log.info("query for totalApplicationsPending: "+query);
        List<Integer> result = namedParameterJdbcTemplate.query(query, preparedStatementValues, new SingleColumnRowMapper<>(Integer.class));
        return result.get(0);
	}
	
	public BigDecimal getAvgDaysToIssueCertificate(RegularizationSearchCriteria regularizationSearchCriteria) {
        Map<String, Object> preparedStatementValues = new HashMap<>();
        String query = regularizationQueryBuilder.getAvgDaysToIssueCertificateQuery(regularizationSearchCriteria, preparedStatementValues);
        log.info("query for getAvgDaysToIssueCertificate : "+query);
        List<BigDecimal> result = namedParameterJdbcTemplate.query(query, preparedStatementValues, new SingleColumnRowMapper<>(BigDecimal.class));
        return result.get(0);
	}
	
	public Integer getMinDaysToIssueCertificate(RegularizationSearchCriteria regularizationSearchCriteria) {
        Map<String, Object> preparedStatementValues = new HashMap<>();
        String query = regularizationQueryBuilder.getMinDaysToIssueCertificateQuery(regularizationSearchCriteria, preparedStatementValues);
        log.info("query for getMinDaysToIssueCertificate: "+query);
        List<Integer> result = namedParameterJdbcTemplate.query(query, preparedStatementValues, new SingleColumnRowMapper<>(Integer.class));
        return result.get(0);
	}
	
	public Integer getMaxDaysToIssueCertificate(RegularizationSearchCriteria regularizationSearchCriteria) {
        Map<String, Object> preparedStatementValues = new HashMap<>();
        String query = regularizationQueryBuilder.getMaxDaysToIssueCertificateQuery(regularizationSearchCriteria, preparedStatementValues);
        log.info("query for getMaxDaysToIssueCertificate: "+query);
        List<Integer> result = namedParameterJdbcTemplate.query(query, preparedStatementValues, new SingleColumnRowMapper<>(Integer.class));
        return result.get(0);
	}

	public HashMap<String, BigDecimal> getTenantWiseRegularizationApplication(RegularizationSearchCriteria regularizationSearchCriteria) {
        Map<String, Object> preparedStatementValues = new HashMap<>();
        String query = regularizationQueryBuilder.getTenantWiseRegularizationApplicationQuery(regularizationSearchCriteria, preparedStatementValues);
        log.info("query for Regularization Tenant Wise Application List : "+query);
        HashMap<String, BigDecimal> result = namedParameterJdbcTemplate.query(query, preparedStatementValues, new TenantWiseCollectionRowMapper());
        return result;
    }
	
	public HashMap<String, BigDecimal> getTenantWiseRegularizationPermitIssued(RegularizationSearchCriteria regularizationSearchCriteria) {
        Map<String, Object> preparedStatementValues = new HashMap<>();
        String query = regularizationQueryBuilder.getTenantWisePermitIssuedQuery(regularizationSearchCriteria, preparedStatementValues);
        log.info("query for Regularization Tenant Wise Permit Issued List : "+query);
        HashMap<String, BigDecimal> result = namedParameterJdbcTemplate.query(query, preparedStatementValues, new TenantWiseCollectionRowMapper());
        return result;
    }
	
	public HashMap<String, BigDecimal> getTenantWiseRegularizationPendingApplication(RegularizationSearchCriteria regularizationSearchCriteria) {
        Map<String, Object> preparedStatementValues = new HashMap<>();
        String query = regularizationQueryBuilder.getTenantWiseRegularizationPendingApplication(regularizationSearchCriteria, preparedStatementValues);
        log.info("query for Regularization Tenant Wise Pending Application List : "+query);
        HashMap<String, BigDecimal> result = namedParameterJdbcTemplate.query(query, preparedStatementValues, new TenantWiseCollectionRowMapper());
        return result;
    }
   

}
