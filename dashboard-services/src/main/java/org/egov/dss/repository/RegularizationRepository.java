package org.egov.dss.repository;

import java.math.BigDecimal;
import java.util.HashMap;
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
   
}
