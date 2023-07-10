package org.egov.usm.web.controller;

import javax.validation.Valid;

import org.egov.usm.web.model.RequestInfoWrapper;
import org.egov.usm.web.model.SurveyTicketRequest;
import org.egov.usm.web.model.SurveyTicketResponse;
import org.egov.usm.web.model.TicketSearchCriteria;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/ticket")
public class SurveyTicketController {

	@PostMapping("/_create")
	public ResponseEntity<SurveyTicketResponse> create(@Valid @RequestBody SurveyTicketRequest tiketRequest) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@PostMapping("/_update")
	public ResponseEntity<SurveyTicketResponse> update(@Valid @RequestBody SurveyTicketRequest tiketRequest) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@PostMapping("/_search")
	public ResponseEntity<SurveyTicketResponse> search(@Valid @RequestBody RequestInfoWrapper requestInfoWrapper,
														@Valid @ModelAttribute TicketSearchCriteria searchCriteria) {
		// TODO Auto-generated method stub
		return null;
	}
}
