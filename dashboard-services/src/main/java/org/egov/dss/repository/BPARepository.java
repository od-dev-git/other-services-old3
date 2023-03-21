package org.egov.dss.repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.egov.dss.config.ConfigurationLoader;
import org.egov.dss.model.BpaSearchCriteria;
import org.egov.dss.repository.builder.BpaQueryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.SingleColumnRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Repository
public class BPARepository {
	
	@Autowired
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	
	@Autowired
	private BpaQueryBuilder bpaQueryBuilder;
	
	@Autowired
	private ConfigurationLoader config;
	
	public Object getTotalPermitsIssued(BpaSearchCriteria bpaSearchCriteria) {
        Map<String, Object> preparedStatementValues = new HashMap<>();
        String query = bpaQueryBuilder.getTotalPermitIssued(bpaSearchCriteria, preparedStatementValues);
        log.info("query: "+query);
        List<Integer> result = namedParameterJdbcTemplate.query(query, preparedStatementValues, new SingleColumnRowMapper<>(Integer.class));
        return result.get(0);
    }
	
	public Object getSlaAchievedAppCount(BpaSearchCriteria bpaSearchCriteria) {
        Map<String, Object> preparedStatementValues = new HashMap<>();
        bpaSearchCriteria.setSlaThreshold(config.getSlaBpaThreshold());
        String query = bpaQueryBuilder.getTotalPermitIssued(bpaSearchCriteria, preparedStatementValues);
        log.info("query: "+query);
        List<Integer> result = namedParameterJdbcTemplate.query(query, preparedStatementValues, new SingleColumnRowMapper<>(Integer.class));
        return result.get(0);
    }

}
