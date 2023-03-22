package org.egov.dss.repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.egov.dss.config.ConfigurationLoader;
import org.egov.dss.model.PgrSearchCriteria;
import org.egov.dss.repository.builder.PgrQueryBuilder;
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


}
