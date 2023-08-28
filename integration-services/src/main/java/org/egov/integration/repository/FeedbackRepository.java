package org.egov.integration.repository;

import org.egov.integration.config.IntegrationConfiguration;
import org.egov.integration.producer.FeedbackProducer;
import org.egov.integration.repository.builder.FeedbackQueryBuilder;
import org.egov.integration.repository.rowmapper.FeedbackRowMapper;
import org.egov.integration.web.model.Feedback;
import org.egov.integration.web.model.FeedbackCreationRequest;
import org.egov.integration.web.model.FeedbackSearchCriteria;
import org.egov.integration.web.model.FeedbackSearchResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Repository
public class FeedbackRepository {

    @Autowired
    private FeedbackProducer feedbackProducer;

    @Autowired
    private IntegrationConfiguration configuration;

    @Autowired
    private FeedbackQueryBuilder feedbackQueryBuilder;

    @Autowired
    private FeedbackRowMapper feedbackRowMapper;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void saveFeedback(FeedbackCreationRequest feedbackCreationRequest){
        feedbackProducer.push(configuration.getSaveFeedbackTopic(),feedbackCreationRequest);
    }

    public List<Feedback> getFeedbackList(FeedbackSearchCriteria feedbackSearchCriteria){
        List<Object> preparedStatement = new ArrayList<>();
        String query= feedbackQueryBuilder.getSearchQueryString(feedbackSearchCriteria,preparedStatement);
        if (query == null)
            return Collections.emptyList();

        return jdbcTemplate.query(query,preparedStatement.toArray(),feedbackRowMapper);

    }
}
