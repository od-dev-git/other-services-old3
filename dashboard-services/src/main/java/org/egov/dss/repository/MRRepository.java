package org.egov.dss.repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.egov.dss.model.MarriageSearchCriteria;
import org.egov.dss.repository.builder.MarriageQueryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.SingleColumnRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Repository
public class MRRepository {
	
	@Autowired
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	
	@Autowired
	private MarriageQueryBuilder mrQueryBuilder;
	
	public Object getTotalApplications(MarriageSearchCriteria mrSearchCriteria) {
        Map<String, Object> preparedStatementValues = new HashMap<>();
        String query = mrQueryBuilder.getTotalApplication(mrSearchCriteria, preparedStatementValues);
        log.info("query: "+query);
        List<Integer> result = namedParameterJdbcTemplate.query(query, preparedStatementValues, new SingleColumnRowMapper<>(Integer.class));
        return result.get(0);
    }


}
