package org.egov.usm.repository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.validation.Valid;

import org.egov.usm.config.USMConfiguration;
import org.egov.usm.producer.Producer;
import org.egov.usm.repository.builder.TicketQueryBuilder;
import org.egov.usm.repository.rowmapper.TicketRowMapper;
import org.egov.usm.web.model.LookUp;
import org.egov.usm.web.model.Survey;
import org.egov.usm.web.model.SurveySearchCriteria;
import org.egov.usm.web.model.SurveyTicket;
import org.egov.usm.web.model.SurveyTicketRequest;
import org.egov.usm.web.model.TicketSearchCriteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import lombok.extern.slf4j.Slf4j;

@Repository
@Slf4j
public class SurveyTicketRepository {

	private TicketQueryBuilder queryBuilder;

	private JdbcTemplate jdbcTemplate;

	private Producer producer;

	private USMConfiguration config;
	
	private TicketRowMapper rowMapper;
     
	@Autowired
	public SurveyTicketRepository(TicketQueryBuilder queryBuilder, JdbcTemplate jdbcTemplate, Producer producer,
			USMConfiguration config,TicketRowMapper rowMapper) {

		this.queryBuilder = queryBuilder;
		this.jdbcTemplate = jdbcTemplate;
		this.producer = producer;
		this.config = config;
		this.rowMapper=rowMapper;
	}

	public SurveyTicketRepository(Producer producer, USMConfiguration config) {

		this.producer = producer;
		this.config = config;
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
	 * @param TicketSearchCriteria
	 * @return List<SurveyTicket>
	 */
	public List<SurveyTicket> getSurveyTicketRequests(TicketSearchCriteria searchCriteria) {
		log.info("Search Criteria :", searchCriteria.toString());
		List<Object> preparedStmtList = new ArrayList<>();
        String query = queryBuilder.getSurveyTicketSearchQuery(searchCriteria, preparedStmtList);
        List<SurveyTicket> surveyRequests =  jdbcTemplate.query(query, preparedStmtList.toArray(), rowMapper);
		
		if(surveyRequests.isEmpty())
			return Collections.emptyList();
		return surveyRequests;
	}

	
}
