package org.egov.usm.web.controller;

import java.util.Collections;
import java.util.List;

import javax.validation.Valid;

import org.egov.usm.service.SurveyResponseService;
import org.egov.usm.utility.ResponseInfoFactory;
import org.egov.usm.web.model.RequestInfoWrapper;
import org.egov.usm.web.model.SurveyDetails;
import org.egov.usm.web.model.SurveyDetailsRequest;
import org.egov.usm.web.model.SurveyDetailsResponse;
import org.egov.usm.web.model.SurveySearchCriteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/survey")
public class SurveyResponseController {
	
	private final SurveyResponseService surveyResponseService;
    
    private final ResponseInfoFactory responseInfoFactory;

    @Autowired
    public SurveyResponseController(SurveyResponseService surveyResponseService, ResponseInfoFactory responseInfoFactory) {
        this.surveyResponseService = surveyResponseService;
        this.responseInfoFactory = responseInfoFactory;
    }
	
	 /**
     * Generate Survey Questions for USM
     * @param surveyRequest
     * @return SurveyResponse
     */
	@PostMapping("/_generateSurvey")
	public ResponseEntity<SurveyDetailsResponse> generateSurvey(@Valid @RequestBody SurveyDetailsRequest surveyRequest) {
		SurveyDetails surveyDetails = surveyResponseService.generateSurvey(surveyRequest);
		SurveyDetailsResponse response =  SurveyDetailsResponse.builder()
				.surveyDetails(Collections.singletonList(surveyDetails))
				.responseInfo(responseInfoFactory.createResponseInfoFromRequestInfo(surveyRequest.getRequestInfo(), true))
				.build();
        return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	/**
	 * Submit Survey Questions with answers
	 * @param surveyRequest
	 * @return SurveyDetailsResponse
	 */
	@PostMapping("/response/_submit")
	public ResponseEntity<SurveyDetailsResponse> submitSurvey(@Valid @RequestBody SurveyDetailsRequest surveyDetailsRequest) {
		SurveyDetails surveyDetails = surveyResponseService.submitSurvey(surveyDetailsRequest);
		SurveyDetailsResponse response =  SurveyDetailsResponse.builder()
				.surveyDetails(Collections.singletonList(surveyDetails))
				.responseInfo(responseInfoFactory.createResponseInfoFromRequestInfo(surveyDetailsRequest.getRequestInfo(), true))
				.build();
        return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	/**
	 * Update Submitted Survey Questions with answers
	 * @param surveyRequest
	 * @return SurveyDetailsResponse
	 */
	@PostMapping("/response/_update")
	public ResponseEntity<SurveyDetailsResponse> updateSubmittedSurvey(@Valid @RequestBody SurveyDetailsRequest surveyDetailsRequest) {
		SurveyDetails surveyDetails = surveyResponseService.updateSubmittedSurvey(surveyDetailsRequest);
		SurveyDetailsResponse response =  SurveyDetailsResponse.builder()
				.surveyDetails(Collections.singletonList(surveyDetails))
				.responseInfo(responseInfoFactory.createResponseInfoFromRequestInfo(surveyDetailsRequest.getRequestInfo(), true))
				.build();
        return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	
	/**
	 * Search Submitted Surveys
	 * @param searchCriteria
	 * @return SurveyDetailsResponse
	 */
	@PostMapping("/response/_search")
	public ResponseEntity<SurveyDetailsResponse> search(@Valid @RequestBody RequestInfoWrapper requestInfoWrapper,
										@Valid @ModelAttribute SurveySearchCriteria searchCriteria) {
		List<SurveyDetails> surveyDetails = surveyResponseService.searchSubmittedSurvey(requestInfoWrapper, searchCriteria);
		
		SurveyDetailsResponse response =  SurveyDetailsResponse.builder()
				.surveyDetails(surveyDetails)
				.responseInfo(responseInfoFactory.createResponseInfoFromRequestInfo(requestInfoWrapper.getRequestInfo(), true))
				.build();
        return new ResponseEntity<>(response, HttpStatus.OK);
	}
}
