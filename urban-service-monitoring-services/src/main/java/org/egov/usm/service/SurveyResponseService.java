package org.egov.usm.service;

import java.util.Collections;
import java.util.List;

import javax.validation.Valid;

import org.egov.usm.repository.SurveyDetailsRepository;
import org.egov.usm.repository.SurveyRepository;
import org.egov.usm.validator.SurveyRequestValidator;
import org.egov.usm.web.model.QuestionDetail;
import org.egov.usm.web.model.SurveyDetails;
import org.egov.usm.web.model.SurveyDetailsRequest;
import org.egov.usm.web.model.SurveyTicket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class SurveyResponseService {
	
	@Autowired
	private SurveyDetailsRepository repository;
	
	@Autowired
	private TicketService ticketService ;
	
	@Autowired
	private EnrichmentService enrichmentService;
	
	public SurveyDetails generateSurvey(SurveyDetailsRequest surveyDetailsRequest) {
		List<SurveyDetails> surveyResponseForCurrentDate = repository.validateSurveyForCurrentDate(surveyDetailsRequest.getSurveyDetails());
		
		if(surveyResponseForCurrentDate.isEmpty()) {
			List<QuestionDetail> questionsList = repository.getQuestionDetails(surveyDetailsRequest.getSurveyDetails());
					
			return SurveyDetails.builder()
					.questionDetails(questionsList)
					.build();
		} else {
			return surveyResponseForCurrentDate.get(0);
		}
	}

	
	public SurveyDetails submitSurvey(SurveyDetailsRequest surveyDetailsRequest) {
		
		// Validate Survey Submitted Request
		
		// Enrich survey details
		enrichmentService.enrichSurveySubmitRequest(surveyDetailsRequest);
		
		// persist the submitted survey
		
		// persist the submitted answer
		
		
		// Check if any answers are NO then  Create the TICKET
		List<SurveyTicket> tickets = ticketService.prepareTickets(surveyDetailsRequest) ;
		
		// If any ticket created, then create or update the lookup table
		if(! CollectionUtils.isEmpty(tickets)) {
			// persist the ticket table
			
			// persist the lookup table
		}
		
		return null;
	}

}
