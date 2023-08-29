package org.egov.usm.web.controller;

import java.util.List;

import javax.validation.Valid;

import org.egov.usm.service.TicketService;
import org.egov.usm.utility.ResponseInfoFactory;
import org.egov.usm.web.model.RequestInfoWrapper;
import org.egov.usm.web.model.SurveyTicket;
import org.egov.usm.web.model.SurveyTicketListRequest;
import org.egov.usm.web.model.SurveyTicketRequest;
import org.egov.usm.web.model.SurveyTicketResponse;
import org.egov.usm.web.model.TicketSearchCriteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/ticket")
public class SurveyTicketController {

	private final TicketService ticketService;

	private final ResponseInfoFactory responseInfoFactory;

	@Autowired
	public SurveyTicketController(TicketService ticketService, ResponseInfoFactory responseInfoFactory) {
		super();
		this.ticketService = ticketService;
		this.responseInfoFactory = responseInfoFactory;
	}

	@PostMapping("/_create")
	public ResponseEntity<SurveyTicketResponse> create(@Valid @RequestBody SurveyTicketRequest tiketRequest) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * Update SurveyTicket Request for USM
	 * 
	 * @param SurveyTicketRequest
	 * @return SurveyTicketResponse
	 */

	@PostMapping("/_update")
	public ResponseEntity<SurveyTicketResponse> update(@Valid @RequestBody SurveyTicketListRequest tiketRequest) {
		List<SurveyTicket> surveyTicket = ticketService.updateSurveyTicket(tiketRequest);
		SurveyTicketResponse response = SurveyTicketResponse.builder().tickets(surveyTicket)
				.responseInfo(
						responseInfoFactory.createResponseInfoFromRequestInfo(tiketRequest.getRequestInfo(), true))
				.build();
		return new ResponseEntity<>(response, HttpStatus.OK);

	}

	/**
	 * Searches SurveyTicket requests belonging USM based on criteria
	 * 
	 * @param requestInfoWrapper
	 * @param TicketSearchCriteria
	 * @return SurveyTicketResponse
	 */

	@PostMapping("/_search")
	public ResponseEntity<SurveyTicketResponse> search(@Valid @RequestBody RequestInfoWrapper requestInfoWrapper,
			@Valid @ModelAttribute TicketSearchCriteria searchCriteria) {
		List<SurveyTicket> tickets = ticketService.searchTicket(searchCriteria);
		SurveyTicketResponse response = SurveyTicketResponse.builder().tickets(tickets).responseInfo(
				responseInfoFactory.createResponseInfoFromRequestInfo(requestInfoWrapper.getRequestInfo(), true))
				.build();
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

}
