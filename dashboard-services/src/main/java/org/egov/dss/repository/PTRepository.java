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
import org.egov.dss.model.PropertySerarchCriteria;
import org.egov.dss.repository.builder.PTServiceQueryBuilder;
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
	
	public Object getSlaAchievedAppCount(PropertySerarchCriteria propertySearchCriteria) {
        Map<String, Object> preparedStatementValues = new HashMap<>();
        propertySearchCriteria.setSlaThreshold(config.getSlaPtThreshold());
        String query = ptServiceQueryBuilder.getAccessedPropertiesCountQuery(propertySearchCriteria, preparedStatementValues);
        log.info("query: "+query);
        List<Integer> result = namedParameterJdbcTemplate.query(query, preparedStatementValues, new SingleColumnRowMapper<>(Integer.class));
        return result.get(0);
    }
	
}
