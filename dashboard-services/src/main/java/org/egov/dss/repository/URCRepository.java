package org.egov.dss.repository;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.egov.dss.config.ConfigurationLoader;
import org.egov.dss.model.PaymentSearchCriteria;
import org.egov.dss.repository.builder.BpaQueryBuilder;
import org.egov.dss.repository.builder.URCQueryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.SingleColumnRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Repository
public class URCRepository {

	@Autowired
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	
	@Autowired
	private URCQueryBuilder urcQueryBuilder;
	
	@Autowired
	private ConfigurationLoader config;
	
	public Object getTotalCollection(PaymentSearchCriteria criteria) {
        Map<String, Object> preparedStatementValues = new HashMap<>();
		String query = urcQueryBuilder.getTotalCollection(criteria, preparedStatementValues);
		log.info("Query: " + query);
		log.info("Params: "+preparedStatementValues);
		List<BigDecimal> result = namedParameterJdbcTemplate.query(query, preparedStatementValues,
				new SingleColumnRowMapper<>(BigDecimal.class));
		return result.get(0);

	}
}
