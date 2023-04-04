package org.egov.dss.repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.kafka.common.utils.CollectionUtils;
import org.egov.dss.config.ConfigurationLoader;
import org.egov.dss.model.CommonSearchCriteria;
import org.egov.dss.model.PayloadDetails;
import org.egov.dss.repository.builder.BpaQueryBuilder;
import org.egov.dss.repository.builder.CommonQueryBuilder;
import org.egov.dss.repository.builder.CommonServiceQueryBuilder;
import org.egov.dss.repository.rowmapper.ULBPerformanceRateRowMapper;
import org.egov.dss.web.model.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.SingleColumnRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Repository
public class CommonServiceRepository {

	@Autowired
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	@Autowired
	private BpaQueryBuilder bpaQueryBuilder;

	@Autowired
	private CommonServiceQueryBuilder commonQueryBuilder;

	@Autowired
	private ConfigurationLoader config;

	public Integer totalCitizensRegistered(CommonSearchCriteria criteria) {
		Map<String, Object> preparedStatementValues = new HashMap<>();
		String query = commonQueryBuilder.getTotalCitizensRegisteredQuery(criteria, preparedStatementValues);
		log.info("query for total Citizens Registered: " + query);
		List<Integer> result = namedParameterJdbcTemplate.query(query, preparedStatementValues,
				new SingleColumnRowMapper<>(Integer.class));
		if(result.size() == 0)
			return 0;
		
		return result.get(0);
	}

	public Integer ptTotalCompletionCount(CommonSearchCriteria criteria) {
		Map<String, Object> preparedStatementValues = new HashMap<>();
		String query = commonQueryBuilder.ptTotalCompletionCount(criteria, preparedStatementValues);
		log.info("query for pt Total Completion Count: " + query);
		List<Integer> result = namedParameterJdbcTemplate.query(query, preparedStatementValues,
				new SingleColumnRowMapper<>(Integer.class));
		if(result.size() == 0)
			return 0;
		
		return result.get(0);
	}

	public Integer wsTotalCompletionCount(CommonSearchCriteria criteria) {
		Map<String, Object> preparedStatementValues = new HashMap<>();
		String query = commonQueryBuilder.wsTotalCompletionCount(criteria, preparedStatementValues);
		log.info("query for ws Total Completion Count: " + query);
		List<Integer> result = namedParameterJdbcTemplate.query(query, preparedStatementValues,
				new SingleColumnRowMapper<>(Integer.class));
		if(result.size() == 0)
			return 0;
		
		return result.get(0);
	}

	public Integer permitTotalCompletionCount(CommonSearchCriteria criteria) {
		Map<String, Object> preparedStatementValues = new HashMap<>();
		String query = commonQueryBuilder.bpaTotalCompletionCount(criteria, preparedStatementValues);
		log.info("query for permit Total Completion Count: " + query);
		List<Integer> result = namedParameterJdbcTemplate.query(query, preparedStatementValues,
				new SingleColumnRowMapper<>(Integer.class));
		if(result.size() == 0)
			return 0;
		
		return result.get(0);
	}

	public Integer ocTotalCompletionCount(CommonSearchCriteria criteria) {
		Map<String, Object> preparedStatementValues = new HashMap<>();
		String query = commonQueryBuilder.bpaTotalCompletionCount(criteria, preparedStatementValues);
		log.info("query for oc Total Completion Count: " + query);
		List<Integer> result = namedParameterJdbcTemplate.query(query, preparedStatementValues,
				new SingleColumnRowMapper<>(Integer.class));
		if(result.size() == 0)
			return 0;
		
		return result.get(0);
	}

	public Integer tlTotalCompletionCount(CommonSearchCriteria criteria) {
		Map<String, Object> preparedStatementValues = new HashMap<>();
		String query = commonQueryBuilder.tlTotalCompletionCount(criteria, preparedStatementValues);
		log.info("query for tl Total Completion Count: " + query);
		List<Integer> result = namedParameterJdbcTemplate.query(query, preparedStatementValues,
				new SingleColumnRowMapper<>(Integer.class));
		if(result.size() == 0)
			return 0;
		
		return result.get(0);
	}

	public Integer mrTotalCompletionCount(CommonSearchCriteria criteria) {
		Map<String, Object> preparedStatementValues = new HashMap<>();
		String query = commonQueryBuilder.mrTotalCompletionCount(criteria, preparedStatementValues);
		log.info("query for mr Total Completion Count: " + query);
		List<Integer> result = namedParameterJdbcTemplate.query(query, preparedStatementValues,
				new SingleColumnRowMapper<>(Integer.class));
		if(result.size() == 0)
			return 0;
		
		return result.get(0);
	}

	public Integer pgrTotalCompletionCount(CommonSearchCriteria criteria) {
		Map<String, Object> preparedStatementValues = new HashMap<>();
		String query = commonQueryBuilder.pgrTotalCompletionCount(criteria, preparedStatementValues);
		log.info("query for pgr Total Completion Count: " + query);
		List<Integer> result = namedParameterJdbcTemplate.query(query, preparedStatementValues,
				new SingleColumnRowMapper<>(Integer.class));
		if(result.size() == 0)
			return 0;
		
		return result.get(0);
	}

	public HashMap<String, Long> pgrTotalApplicationsTenantWise(CommonSearchCriteria criteria) {
		Map<String, Object> preparedStatementValues = new HashMap<>();
		String query = commonQueryBuilder.pgrTotalApplicationsTenantWise(criteria, preparedStatementValues);
        log.info("PGR Tenant Wise Total Completed Application : "+query);
        HashMap<String, Long> result = namedParameterJdbcTemplate.query(query, preparedStatementValues, new ULBPerformanceRateRowMapper());
        return result;
	}

	public HashMap<String, Long> tlTotalCompletedApplicationsTenantWise(CommonSearchCriteria criteria) {
		Map<String, Object> preparedStatementValues = new HashMap<>();
		String query = commonQueryBuilder.tlTotalCompletedApplicationsTenantWise(criteria, preparedStatementValues);
        log.info("TL Tenant Wise Total Completed Application : "+query);
        HashMap<String, Long> result = namedParameterJdbcTemplate.query(query, preparedStatementValues, new ULBPerformanceRateRowMapper());
        return result;
	}

}
