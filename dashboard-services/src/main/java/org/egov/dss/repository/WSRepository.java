package org.egov.dss.repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.egov.dss.model.WaterSearchCriteria;
import org.egov.dss.repository.builder.WaterServiceQueryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.SingleColumnRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Repository
public class WSRepository {
	
	@Autowired
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	
	@Autowired
	private WaterServiceQueryBuilder wsQueryBuilder;

	public Object getActiveWaterConnectionCount(WaterSearchCriteria waterSearchCriteria) {
        Map<String, Object> preparedStatementValues = new HashMap<>();
        String query = wsQueryBuilder.getActiveConnectionCount(waterSearchCriteria, preparedStatementValues);
        log.info("query: "+query);
        List<Integer> result = namedParameterJdbcTemplate.query(query, preparedStatementValues, new SingleColumnRowMapper<>(Integer.class));
        return result.get(0);
    }

}
