package org.egov.usm.repository;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.egov.usm.config.USMConfiguration;
import org.egov.usm.producer.Producer;
import org.egov.usm.repository.builder.SurveyResponseQueryBuilder;
import org.egov.usm.repository.rowmapper.QuestionDetailRowMapper;
import org.egov.usm.repository.rowmapper.SurveyAnswerDetailsRowMapper;
import org.egov.usm.repository.rowmapper.SurveyDetailsRowMapper;
import org.egov.usm.web.model.QuestionDetail;
import org.egov.usm.web.model.SurveyDetails;
import org.egov.usm.web.model.SurveyDetailsRequest;
import org.egov.usm.web.model.SurveySearchCriteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Repository
public class SurveyDetailsRepository {
	
	private JdbcTemplate jdbcTemplate;

    private SurveyResponseQueryBuilder queryBuilder;

    private SurveyDetailsRowMapper surveyDetailsRowMapper;
    
    private SurveyAnswerDetailsRowMapper surveyAnswerDetailsRowMapper;
    
    private QuestionDetailRowMapper questionDetailsRowMapper;
    
    private Producer producer;

    private USMConfiguration config;

    @Autowired
    public SurveyDetailsRepository(JdbcTemplate jdbcTemplate, SurveyResponseQueryBuilder queryBuilder, SurveyDetailsRowMapper surveyDetailsRowMapper,
    		QuestionDetailRowMapper questionDetailsRowMapper, SurveyAnswerDetailsRowMapper surveyAnswerDetailsRowMapper, Producer producer, USMConfiguration config) {
        this.jdbcTemplate = jdbcTemplate;
        this.queryBuilder = queryBuilder;
        this.surveyDetailsRowMapper = surveyDetailsRowMapper;
        this.questionDetailsRowMapper = questionDetailsRowMapper;
        this.surveyAnswerDetailsRowMapper=surveyAnswerDetailsRowMapper;
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
		log.info("Update Question Lookup Topic :", surveyDetailsRequest.toString());
        producer.push(config.getUpdateQuestionLookupTopic(), surveyDetailsRequest);
	}
	
	public List<String> searchQuestionInLookup(SurveyDetails surveyDetails) {
		log.info("Search in Question LookUp table :", surveyDetails.toString());
		List<Object> preparedStmtList = new ArrayList<>();
        String query = queryBuilder.searchQuestionInLookup(surveyDetails, preparedStmtList);
        List<String> questionIds = jdbcTemplate.queryForList(query, preparedStmtList.toArray(), String.class);
		return questionIds;
	}
	
	
	public void submitSurvey(SurveyDetailsRequest surveyDetailsRequest) {
		log.info("Save Survey Submitted Answers  :", surveyDetailsRequest.toString());
        producer.push(config.getSaveSubmitSurveyTopic(), surveyDetailsRequest);
	}

	public Boolean isSurveyExistsForToday(@Valid SurveySearchCriteria criteria) {
		log.info("Search in Question LookUp table :", criteria.toString());
		List<Object> preparedStmtList = new ArrayList<>();
        String query = queryBuilder.isSurveyExistsForToday(criteria, preparedStmtList);
        Integer count = jdbcTemplate.queryForObject(query, preparedStmtList.toArray(), Integer.class);
		return count > 0;
	}

	public void updateSubmittedSurvey(@Valid SurveyDetailsRequest surveyDetailsRequest) {
		log.info("Update Survey Submitted Answers  :", surveyDetailsRequest.toString());
        producer.push(config.getUpdateSubmitSurveyTopic(), surveyDetailsRequest);
	}

	public List<SurveyDetails> searchSubmittedSurvey(@Valid SurveySearchCriteria searchCriteria) {
		List<Object> preparedStmtList = new ArrayList<>();
        String query = queryBuilder.searchSubmittedSurvey(searchCriteria, preparedStmtList);
        List<SurveyDetails> surveyDetailsList = jdbcTemplate.query(query, preparedStmtList.toArray(), surveyAnswerDetailsRowMapper);
		return surveyDetailsList;
	}

	
}
