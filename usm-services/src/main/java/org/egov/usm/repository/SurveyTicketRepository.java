package org.egov.usm.repository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.validation.Valid;

import org.egov.usm.config.USMConfiguration;
import org.egov.usm.producer.Producer;
import org.egov.usm.repository.builder.TicketQueryBuilder;
import org.egov.usm.repository.rowmapper.TicketRowMapper;
import org.egov.usm.web.model.SurveyDetails;
import org.egov.usm.web.model.SurveyTicket;
import org.egov.usm.web.model.SurveyTicketRequest;
import org.egov.usm.web.model.TicketSearchCriteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Repository
public class SurveyTicketRepository {

	private JdbcTemplate jdbcTemplate;

	private TicketQueryBuilder queryBuilder;

	private Producer producer;

	private USMConfiguration config;

	private TicketRowMapper rowMapper;

	@Autowired
	public SurveyTicketRepository(JdbcTemplate jdbcTemplate, TicketQueryBuilder queryBuilder, Producer producer,
			USMConfiguration config, TicketRowMapper rowMapper) {
		this.jdbcTemplate = jdbcTemplate;
		this.queryBuilder = queryBuilder;
		this.producer = producer;
		this.config = config;
		this.rowMapper = rowMapper;
	}

	public List<String> searchQuestionsInTicket(SurveyDetails surveyDetails) {
		log.info("Search Questions in Ticket table for same survey :", surveyDetails.toString());
		List<Object> preparedStmtList = new ArrayList<>();
		String query = queryBuilder.searchQuestionsInTicket(surveyDetails, preparedStmtList);
		List<String> questionIds = jdbcTemplate.queryForList(query, preparedStmtList.toArray(), String.class);
		return questionIds;
	}

	public void save(List<SurveyTicket> surveyTickets) {
		producer.push(config.getCreateTicketTopic(), surveyTickets);

	}

	/**
	 * Pushes the update survey Ticket request to update topic
	 *
	 * @param SurveyTicketRequest
	 */

	public void update(@Valid SurveyTicketRequest surveyTicketRequest) {
		producer.push(config.getUpdateTicketTopic(), surveyTicketRequest);

	}

	/**
	 * Repository for searching SurveyTicket Requests from db
	 * 
	 * @param TicketSearchCriteria
	 * @return List<SurveyTicket>
	 */
	public List<SurveyTicket> getSurveyTicketRequests(TicketSearchCriteria searchCriteria) {
		log.info("Search Criteria :", searchCriteria.toString());
		List<Object> preparedStmtList = new ArrayList<>();
		String query = queryBuilder.getSurveyTicketSearchQuery(searchCriteria, preparedStmtList);
		List<SurveyTicket> surveyRequests = jdbcTemplate.query(query, preparedStmtList.toArray(), rowMapper);

		if (surveyRequests.isEmpty())
			return Collections.emptyList();
		return surveyRequests;
	}

}
