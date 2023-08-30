package org.egov.integration.web.controller;

import org.egov.integration.service.FeedbackService;
import org.egov.integration.util.ResponseInfoFactory;
import org.egov.integration.web.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/feedback")
public class FeedbackController {


    @Autowired
    private FeedbackService feedbackService;

    @Autowired
    private ResponseInfoFactory responseInfoFactory;

    @PostMapping("/_create")
    public ResponseEntity<FeedbackCreationResponse> create(@RequestBody @Valid final FeedbackCreationRequest feedbackCreationRequest){
        Feedback feedback= feedbackService.createFeedback(feedbackCreationRequest);
        FeedbackCreationResponse response = FeedbackCreationResponse.builder()
                .feedback(feedback)
                .responseInfo(responseInfoFactory.createResponseInfoFromRequestInfo(feedbackCreationRequest.getRequestInfo(), true)).build();
        return new ResponseEntity<FeedbackCreationResponse>(response,HttpStatus.OK);
    }

    @PostMapping("/_search")
    public ResponseEntity<FeedbackSearchResponse> search(@RequestBody @Valid final RequestInfoWrapper requestInfoWrapper,
                                                         @Valid @ModelAttribute FeedbackSearchCriteria feedbackSearchCriteria){
        List<Feedback> feedbacks = feedbackService.searchFeedbacks(feedbackSearchCriteria,requestInfoWrapper);
        FeedbackSearchResponse response = FeedbackSearchResponse.builder()
                .feedbacks(feedbacks)
                .responseInfo(responseInfoFactory.createResponseInfoFromRequestInfo(requestInfoWrapper.getRequestInfo(), true)).build();
        return new ResponseEntity<FeedbackSearchResponse>(response,HttpStatus.OK);
    }
}
