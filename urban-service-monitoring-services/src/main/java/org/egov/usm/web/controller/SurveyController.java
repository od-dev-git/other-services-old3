package org.egov.usm.web.controller;

import java.util.Collections;
import java.util.List;

import javax.validation.Valid;

import org.egov.common.contract.response.ResponseInfo;
import org.egov.usm.service.SurveyService;
import org.egov.usm.utility.ResponseInfoFactory;
import org.egov.usm.web.model.RequestInfoWrapper;
import org.egov.usm.web.model.Survey;
import org.egov.usm.web.model.SurveyRequest;
import org.egov.usm.web.model.SurveyResponse;
import org.egov.usm.web.model.SurveySearchCriteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/survey")
@Slf4j
public class SurveyController {
	
    private final SurveyService surveyService;
    
    private final ResponseInfoFactory responseInfoFactory;

    @Autowired
    public SurveyController(SurveyService surveyService, ResponseInfoFactory responseInfoFactory) {
        this.surveyService = surveyService;
        this.responseInfoFactory = responseInfoFactory;
    }

    /**
     * Creates Survey Request for USM
     * @param surveyRequest
     * @return SurveyResponse
     */
	@PostMapping("/_create")
	public ResponseEntity<SurveyResponse> create(@Valid @RequestBody SurveyRequest surveyRequest) {
		log.info("In controller request : ", surveyRequest.toString());
		Survey survey = surveyService.create(surveyRequest);
		SurveyResponse response =  SurveyResponse.builder()
				.surveys(Collections.singletonList(survey))
				.responseInfo(responseInfoFactory.createResponseInfoFromRequestInfo(surveyRequest.getRequestInfo(), true))
				.build();
        return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	/**
	 * Searches Survey requests belonging USM based on criteria
	 * @param requestInfoWrapper
	 * @param searchCriteria
	 * @return SurveyResponse
	 */
	@PostMapping("/_search")
	public ResponseEntity<SurveyResponse> search(@Valid @RequestBody RequestInfoWrapper requestInfoWrapper,
												@Valid @ModelAttribute SurveySearchCriteria searchCriteria) {
		List<Survey> surveys = surveyService.searchSurveys(searchCriteria);
		SurveyResponse response = SurveyResponse.builder()
				.surveys(surveys)
				.responseInfo(responseInfoFactory.createResponseInfoFromRequestInfo(requestInfoWrapper.getRequestInfo(), true))
				.build();
        return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	/**
	 * Update Survey Request for USM
	 * @param surveyRequest
	 * @return SurveyResponse
	 */
	@PostMapping("/_update")
	public ResponseEntity<SurveyResponse> update(@Valid @RequestBody SurveyRequest surveyRequest) {
		Survey survey = surveyService.updateSurvey(surveyRequest);
		SurveyResponse response =  SurveyResponse.builder()
				.surveys(Collections.singletonList(survey))
				.responseInfo(responseInfoFactory.createResponseInfoFromRequestInfo(surveyRequest.getRequestInfo(), true))
				.build();
        return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	@RequestMapping(value="/_delete", method = RequestMethod.POST)
    public ResponseEntity<?> delete(@RequestBody @Valid SurveyRequest surveyRequest) {
        surveyService.deleteSurvey(surveyRequest);
        ResponseInfo responseInfo = responseInfoFactory.createResponseInfoFromRequestInfo(surveyRequest.getRequestInfo(), true);
        return new ResponseEntity<>(responseInfo, HttpStatus.OK);
    }
}
