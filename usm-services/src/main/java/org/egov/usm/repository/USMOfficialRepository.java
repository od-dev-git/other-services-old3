package org.egov.usm.repository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.validation.Valid;

import org.egov.usm.config.USMConfiguration;
import org.egov.usm.producer.Producer;
import org.egov.usm.repository.builder.USMOfficcialQueryBuilder;
import org.egov.usm.repository.rowmapper.USMOfficialRowMapper;
import org.egov.usm.web.model.USMOfficial;
import org.egov.usm.web.model.USMOfficialRequest;
import org.egov.usm.web.model.USMOfficialSearchCriteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Repository
public class USMOfficialRepository {

	private JdbcTemplate jdbcTemplate;

	private USMOfficcialQueryBuilder queryBuilder;

	private Producer producer;

	private USMConfiguration config;

	private USMOfficialRowMapper rowMapper;

	@Autowired
	public USMOfficialRepository(JdbcTemplate jdbcTemplate, USMOfficcialQueryBuilder queryBuilder, Producer producer,
			USMConfiguration config, USMOfficialRowMapper rowMapper) {
		this.jdbcTemplate = jdbcTemplate;
		this.queryBuilder = queryBuilder;
		this.producer = producer;
		this.config = config;
		this.rowMapper = rowMapper;
	}

	/**
	 * Pushes the usmOfficialRequest to create kafka topic
	 * 
	 * @param usmOfficialRequest
	 */

	public void saveOfficial(@Valid USMOfficialRequest usmOfficialRequest) {
		log.info("Save request :", usmOfficialRequest.toString());
		producer.push(config.getCreateOfficialTopic(), usmOfficialRequest);

	}

	/**
	 * Repository for searching officials Requests from db
	 * 
	 * @param USMOfficialSearchCriteria
	 * @return List<USMOfficial>
	 */

	public List<USMOfficial> getOfficialRequests(@Valid USMOfficialSearchCriteria searchCriteria) {
		log.info("Search Criteria :", searchCriteria.toString());
		List<Object> preparedStmtList = new ArrayList<>();
		String query = queryBuilder.getUSMOfficialSearchQuery(searchCriteria, preparedStmtList);
		List<USMOfficial> usmOfficials = jdbcTemplate.query(query, preparedStmtList.toArray(), rowMapper);

		if (usmOfficials.isEmpty())
			return Collections.emptyList();
		return usmOfficials;

	}

}
