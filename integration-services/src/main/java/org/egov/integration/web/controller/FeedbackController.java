package org.egov.integration.web.controller;

import org.egov.integration.service.FeedbackService;
import org.egov.integration.web.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/feedback")
public class FeedbackController {


    @Autowired
    private FeedbackService feedbackService;
    @PostMapping("/_create")
    public ResponseEntity<FeedbackCreationResponse> create(@RequestBody @Valid final FeedbackCreationRequest feedbackCreationRequest){
        FeedbackCreationResponse response= feedbackService.createFeedback(feedbackCreationRequest);
        return new ResponseEntity<FeedbackCreationResponse>(response,HttpStatus.OK);
    }

    @PostMapping("/_search")
    public ResponseEntity<FeedbackSearchResponse> search(@RequestBody @Valid final FeedbackSearchRequest feedbackSearchRequest,
                                                         @Valid @ModelAttribute FeedbackSearchCriteria feedbackSearchCriteria){
        FeedbackSearchResponse response = feedbackService.searchFeedbacks(feedbackSearchCriteria,feedbackSearchRequest);
        return new ResponseEntity<FeedbackSearchResponse>(response,HttpStatus.OK);
    }
}
