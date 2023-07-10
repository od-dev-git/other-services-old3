package org.egov.usm.web.controller;

import javax.validation.Valid;

import org.egov.usm.web.model.RequestInfoWrapper;
import org.egov.usm.web.model.SurveyRequest;
import org.egov.usm.web.model.SurveyResponse;
import org.egov.usm.web.model.SurveySearchCriteria;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/survey")
public class SurveyController {

	@PostMapping("/_create")
	public ResponseEntity<SurveyResponse> create(@Valid @RequestBody SurveyRequest surveyRequest) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@PostMapping("/_search")
	public ResponseEntity<SurveyResponse> search(@Valid @RequestBody RequestInfoWrapper requestInfoWrapper,
												@Valid @ModelAttribute SurveySearchCriteria searchCriteria) {
		// TODO Auto-generated method stub
		return null;
	}
}
