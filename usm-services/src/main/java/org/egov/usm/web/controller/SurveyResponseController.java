package org.egov.usm.web.controller;

import javax.validation.Valid;

import org.egov.usm.service.SurveyResponseService;
import org.egov.usm.utility.ResponseInfoFactory;
import org.egov.usm.web.model.SurveyDetails;
import org.egov.usm.web.model.SurveyDetailsRequest;
import org.egov.usm.web.model.SurveyDetailsResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
     * Creates Survey Request for USM
     * @param surveyRequest
     * @return SurveyResponse
     */
	@PostMapping("/_generateSurvey")
	public ResponseEntity<SurveyDetailsResponse> generateSurvey(@Valid @RequestBody SurveyDetailsRequest surveyRequest) {
		SurveyDetails surveyDetails = surveyResponseService.generateSurvey(surveyRequest);
		SurveyDetailsResponse response =  SurveyDetailsResponse.builder()
				.surveyDetails(surveyDetails)
				.responseInfo(responseInfoFactory.createResponseInfoFromRequestInfo(surveyRequest.getRequestInfo(), true))
				.build();
        return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	@PostMapping("/response/_submit")
	public ResponseEntity<SurveyDetailsResponse> submitSurvey(@Valid @RequestBody SurveyDetailsRequest surveyRequest) {
		SurveyDetails surveyDetails = surveyResponseService.submitSurvey(surveyRequest);
		SurveyDetailsResponse response =  SurveyDetailsResponse.builder()
				.surveyDetails(surveyDetails)
				.responseInfo(responseInfoFactory.createResponseInfoFromRequestInfo(surveyRequest.getRequestInfo(), true))
				.build();
        return new ResponseEntity<>(response, HttpStatus.OK);
	}
}
