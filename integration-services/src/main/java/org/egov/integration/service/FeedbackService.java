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

    public Feedback createFeedback(FeedbackCreationRequest feedbackCreationRequest){

        feedbackValidator.validateCreateRequest(feedbackCreationRequest);
        enrichmentService.enrichFeedbackCreationRequest(feedbackCreationRequest);

        feedbackRepository.saveFeedback(feedbackCreationRequest);

        return feedbackCreationRequest.getFeedback();
    }

    public List<Feedback> searchFeedbacks(FeedbackSearchCriteria feedbackSearchCriteria, RequestInfoWrapper requestInfoWrapper){
        feedbackValidator.validateSearchCriteria(feedbackSearchCriteria,requestInfoWrapper.getRequestInfo());
        List<Feedback> feedbacks=feedbackRepository.getFeedbackList(feedbackSearchCriteria);
        return feedbacks;
    }
}
