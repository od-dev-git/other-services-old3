package org.egov.usm.repository;

import java.util.ArrayList;
import java.util.List;

import org.egov.usm.repository.builder.TicketQueryBuilder;
import org.egov.usm.web.model.SurveyDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Repository
public class SurveyTicketRepository {
	
	private JdbcTemplate jdbcTemplate;

	private TicketQueryBuilder queryBuilder;
	
	@Autowired
	public SurveyTicketRepository(JdbcTemplate jdbcTemplate, TicketQueryBuilder queryBuilder) {
		this.jdbcTemplate = jdbcTemplate;
		this.queryBuilder = queryBuilder;
	}

	public List<String> searchQuestionsInTicket(SurveyDetails surveyDetails) {
		log.info("Search Questions in Ticket table for same survey :", surveyDetails.toString());
		List<Object> preparedStmtList = new ArrayList<>();
        String query = queryBuilder.searchQuestionsInTicket(surveyDetails, preparedStmtList);
        List<String> questionIds = jdbcTemplate.queryForList(query, preparedStmtList.toArray(), String.class);
		return questionIds;
	}

    

}
