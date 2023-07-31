package org.egov.usm.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.egov.common.contract.request.RequestInfo;
import org.egov.usm.model.enums.SurveyAnswer;
import org.egov.usm.model.enums.TicketStatus;
import org.egov.usm.repository.SurveyTicketRepository;
import org.egov.usm.utility.USMUtil;
import org.egov.usm.validator.SurveyTicketRequestValidator;
import org.egov.usm.web.model.SubmittedAnswer;
import org.egov.usm.web.model.SurveyDetailsRequest;
import org.egov.usm.web.model.SurveyTicket;
import org.egov.usm.web.model.SurveyTicketRequest;
import org.egov.usm.web.model.TicketSearchCriteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class TicketService {

	@Autowired
	private SurveyTicketRepository repository;
	@Autowired
	private SurveyTicketRequestValidator surveyTicketRequestValidator;

	@Autowired
	private USMUtil usmUtil;

	@Autowired
	private EnrichmentService enrichmentService;
	
	/**
	 * Service layer for Updating SurveyTicket
	 * @param SurveyTicketRequest
	 * @return updated Survey
	 */

	public SurveyTicket updateSurveyTicket(@Valid SurveyTicketRequest ticketRequest) {
		SurveyTicket surveyTicket = ticketRequest.getTicket();
		RequestInfo requestInfo = ticketRequest.getRequestInfo(); 
		
		
      /*Validate the existing ticket */
		SurveyTicket existingSurveyTickets = surveyTicketRequestValidator.validateSurveyTicketExistence(surveyTicket);
      
		surveyTicket.setAuditDetails(existingSurveyTickets.getAuditDetails());
		
		/* Update  the Audit Details */
		surveyTicket.getAuditDetails().setLastModifiedBy(requestInfo.getUserInfo().getUuid());
		surveyTicket.getAuditDetails().setLastModifiedTime(System.currentTimeMillis());
		surveyTicket.setTicketClosedTime(System.currentTimeMillis());
		   
		if(ticketRequest.getTicket().getStatus().equals(TicketStatus.CLOSED)) {
			surveyTicket.setHasOpenTicket(Boolean.FALSE);
		}
		else {
			surveyTicket.setHasOpenTicket(Boolean.TRUE);
		}
		
		repository.update(ticketRequest);
		return surveyTicket;
	}

	public List<SurveyTicket> prepareTickets(SurveyDetailsRequest surveyDetailsRequest) {

		List<SurveyTicket> surveyTickets = new ArrayList<>();
		// Check the No answers
		List<SubmittedAnswer> filterSubmittedAnswers = surveyDetailsRequest.getSurveyDetails().getSubmittedAnswers()
				.stream().filter(answer -> answer.getAnswer().equals(SurveyAnswer.NO)).collect(Collectors.toList());

		// If there is no answer as no then return Empty Ticket
		if (!CollectionUtils.isEmpty(filterSubmittedAnswers)) {
			surveyTickets = enrichmentService.enrichTickets(filterSubmittedAnswers, surveyDetailsRequest);
		}
		// return List of tickets
		return surveyTickets;
	}

	public List<SurveyTicket> searchTicket(TicketSearchCriteria searchCriteria) {
		log.info("search: " + searchCriteria.toString());
		List<SurveyTicket> surveyTickets = repository.getSurveyTicketRequests(searchCriteria);
		return surveyTickets;
	}

}
