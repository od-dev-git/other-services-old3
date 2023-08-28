package org.egov.integration.service;

import org.egov.integration.repository.FeedbackRepository;
import org.egov.integration.validator.FeedbackValidator;
import org.egov.integration.web.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FeedbackService {

    @Autowired
    private FeedbackValidator feedbackValidator;

    @Autowired
    private EnrichmentService enrichmentService;

    @Autowired
    private FeedbackRepository feedbackRepository;

    public FeedbackCreationResponse createFeedback(FeedbackCreationRequest feedbackCreationRequest){

        feedbackValidator.validateCreateRequest(feedbackCreationRequest);
        enrichmentService.enrichFeedbackCreationRequest(feedbackCreationRequest);

        feedbackRepository.saveFeedback(feedbackCreationRequest);

        FeedbackCreationResponse feedbackCreationResponse= FeedbackCreationResponse.builder()
                .requestInfo(feedbackCreationRequest.getRequestInfo())
                .feedback(feedbackCreationRequest.getFeedback()).build();
        return feedbackCreationResponse;
    }

    public FeedbackSearchResponse searchFeedbacks(FeedbackSearchCriteria feedbackSearchCriteria, FeedbackSearchRequest feedbackSearchRequest){
        feedbackValidator.validateSearchCriteria(feedbackSearchCriteria,feedbackSearchRequest.getRequestInfo());
        List<Feedback> feedbacks=feedbackRepository.getFeedbackList(feedbackSearchCriteria);
        FeedbackSearchResponse feedbackSearchResponse = FeedbackSearchResponse.builder()
                .feedbacks(feedbacks).requestInfo(feedbackSearchRequest.getRequestInfo()).build();
        return feedbackSearchResponse;
    }
}
