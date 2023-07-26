package org.egov.usm.repository;

import java.util.ArrayList;
import java.util.List;

import org.egov.usm.config.USMConfiguration;
import org.egov.usm.producer.Producer;
import org.egov.usm.repository.builder.SurveyQueryBuilder;
import org.egov.usm.repository.rowmapper.QuestionDetailRowMapper;
import org.egov.usm.repository.rowmapper.SurveyDetailsRowMapper;
import org.egov.usm.web.model.QuestionDetail;
import org.egov.usm.web.model.SurveyDetails;
import org.egov.usm.web.model.SurveyDetailsRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Repository
public class SurveyDetailsRepository {
	
	private JdbcTemplate jdbcTemplate;

    private SurveyQueryBuilder queryBuilder;

    private SurveyDetailsRowMapper surveyDetailsRowMapper;
    
    private QuestionDetailRowMapper questionDetailsRowMapper;
    
    private Producer producer;

    private USMConfiguration config;

    @Autowired
    public SurveyDetailsRepository(JdbcTemplate jdbcTemplate, SurveyQueryBuilder queryBuilder, SurveyDetailsRowMapper surveyDetailsRowMapper,
    		QuestionDetailRowMapper questionDetailsRowMapper,Producer producer, USMConfiguration config) {
        this.jdbcTemplate = jdbcTemplate;
        this.queryBuilder = queryBuilder;
        this.surveyDetailsRowMapper = surveyDetailsRowMapper;
        this.questionDetailsRowMapper = questionDetailsRowMapper;
        this.producer = producer;
        this.config = config;
    }

	public List<SurveyDetails> validateSurveyForCurrentDate(SurveyDetails surveyDetails) {
		log.info("Search Criteria :", surveyDetails.toString());
		List<Object> preparedStmtList = new ArrayList<>();
        String query = queryBuilder.validateSurveyDetailsForCurrentDate(surveyDetails, preparedStmtList);
        List<SurveyDetails> surveyDetailsList =  jdbcTemplate.query(query, preparedStmtList.toArray(), surveyDetailsRowMapper);
		return surveyDetailsList;
	}

	public List<QuestionDetail> getQuestionDetails(SurveyDetails surveyDetails) {
		log.info("Search Criteria :", surveyDetails.toString());
		List<Object> preparedStmtList = new ArrayList<>();
        String query = queryBuilder.getQuestionDetails(surveyDetails, preparedStmtList);
        List<QuestionDetail> questionDetails =  jdbcTemplate.query(query, preparedStmtList.toArray(), questionDetailsRowMapper);
		return questionDetails;
	}

	public boolean isPresentInQuestionLookup(SurveyDetails surveyDetails) {
		log.info("Search in Question LookUp table :", surveyDetails.toString());
		List<Object> preparedStmtList = new ArrayList<>();
        String query = queryBuilder.isPresentInQuestionLookup(surveyDetails, preparedStmtList);
        Integer count = jdbcTemplate.queryForObject(query, preparedStmtList.toArray(), Integer.class);
		return count > 0;
	}

	
	public void updateLookupDetails(SurveyDetailsRequest surveyDetailsRequest) {
		log.info("Save request :", surveyDetailsRequest.toString());
        producer.push(config.getUpdateQuestionLookupTopic(), surveyDetailsRequest);
	}
	
	
	public void submitSurvey(SurveyDetailsRequest surveyDetailsRequest) {
		log.info("Save request :", surveyDetailsRequest.toString());
        producer.push(config.getSaveSubmitSurveyTopic(), surveyDetailsRequest);
	}

	
}
