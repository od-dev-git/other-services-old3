package org.egov.usm.repository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.validation.Valid;

import org.egov.usm.config.USMConfiguration;
import org.egov.usm.producer.Producer;
import org.egov.usm.repository.builder.SurveyQueryBuilder;
import org.egov.usm.repository.rowmapper.SurveyRowMapper;
import org.egov.usm.web.model.Survey;
import org.egov.usm.web.model.SurveyRequest;
import org.egov.usm.web.model.SurveySearchCriteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Repository
public class SurveyRepository {
	
	private JdbcTemplate jdbcTemplate;

    private SurveyQueryBuilder queryBuilder;

    private SurveyRowMapper rowMapper;
    
    private Producer producer;

    private USMConfiguration config;

    @Autowired
    public SurveyRepository(JdbcTemplate jdbcTemplate, SurveyQueryBuilder queryBuilder, SurveyRowMapper rowMapper,
                        Producer producer, USMConfiguration config) {
        this.jdbcTemplate = jdbcTemplate;
        this.queryBuilder = queryBuilder;
        this.rowMapper = rowMapper;
        this.producer = producer;
        this.config = config;
    }
	
    /**
     * Pushes the submit survey request to create topic 
     *
     * @param surveyRequest
     */
	public void save(SurveyRequest surveyRequest) {
		log.info("Save request :", surveyRequest.toString());
        producer.push(config.getSubmitSurveyTopic(), surveyRequest);
    }

	/**
	 * Repository for searching Suvey Requests from db
	 * @param searchCriteria
	 * @return List<Survey>
	 */
	public List<Survey> getSurveyRequests(SurveySearchCriteria searchCriteria) {
		log.info("Search Criteria :", searchCriteria.toString());
		List<Object> preparedStmtList = new ArrayList<>();
        String query = queryBuilder.getSurveySearchQuery(searchCriteria, preparedStmtList);
        List<Survey> surveyRequests =  jdbcTemplate.query(query, preparedStmtList.toArray(), rowMapper);
		
		if(surveyRequests.isEmpty())
			return Collections.emptyList();
		return surveyRequests;
	}

	/**
     * Pushes the update survey request to update topic 
     *
     * @param surveyRequest
     */
	public void update(SurveyRequest surveyRequest) {
		log.info("Update request :", surveyRequest.toString());
		producer.push(config.getUpdateSurveyTopic(), surveyRequest);
	}

	/**
     * Pushes the delete survey request 
     *
     * @param surveyRequest
     */
	public void deleteSurvey(@Valid SurveyRequest surveyRequest) {
		log.info("Delete request :", surveyRequest.toString());
		producer.push(config.getUpdateSurveyTopic(), surveyRequest);
	}

}
