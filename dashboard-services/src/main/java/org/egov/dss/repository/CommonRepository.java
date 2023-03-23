package org.egov.dss.repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.egov.dss.config.ConfigurationLoader;
import org.egov.dss.model.PayloadDetails;
import org.egov.dss.repository.builder.CommonQueryBuilder;
import org.egov.dss.repository.rowmapper.PayloadDetailsRowMapper;
import org.egov.dss.util.DashboardUtils;
import org.egov.dss.web.model.ResponseData;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Repository
public class CommonRepository {
	 
	@Autowired
	private CommonQueryBuilder commonQueryBuilder;

	@Autowired
	private JdbcTemplate jdbcTemplate;
	
    @Autowired
	private DashboardUtils utils;
	
	@Autowired
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	
	public List<PayloadDetails> fetchSchedulerPayloads() {
		Map<String, Object> preparedStatementValues = new HashMap<>();
        String query = commonQueryBuilder.PAYLOAD_QUERY_SQL;
        log.info("query: "+query);
        return namedParameterJdbcTemplate.query(query, preparedStatementValues, new PayloadDetailsRowMapper());
       
    }

	public void update(PayloadDetails payloadDetails) {
		jdbcTemplate.update(commonQueryBuilder.RESPONSE_DATA_UPDATE_QUERY, new PreparedStatementSetter() {

			@Override
			public void setValues(PreparedStatement ps) throws SQLException {
				ps.setObject(1, utils.getPGObject(payloadDetails.getResponsedata()));
				ps.setLong(2, payloadDetails.getLastModifiedTime());
				ps.setLong(3, payloadDetails.getEnddate());
				ps.setString(4, payloadDetails.getId());

			}

		});
	}
	
	
}
