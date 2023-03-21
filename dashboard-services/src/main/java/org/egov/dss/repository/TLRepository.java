package org.egov.dss.repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.egov.dss.model.TLSearchCriteria;
import org.egov.dss.repository.builder.TradeLicenseQueryBuilder;
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
	
	public Object getTotalApplications(TLSearchCriteria tlSearchCriteria) {
        Map<String, Object> preparedStatementValues = new HashMap<>();
        String query = tlQueryBuilder.getTotalApplication(tlSearchCriteria, preparedStatementValues);
        log.info("query: "+query);
        List<Integer> result = namedParameterJdbcTemplate.query(query, preparedStatementValues, new SingleColumnRowMapper<>(Integer.class));
        return result.get(0);
    }

}
