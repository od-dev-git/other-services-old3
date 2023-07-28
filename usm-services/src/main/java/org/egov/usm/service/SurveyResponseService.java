package org.egov.usm.service;

import java.util.List;

import javax.validation.Valid;

import org.egov.usm.repository.SurveyDetailsRepository;
import org.egov.usm.web.model.QuestionDetail;
import org.egov.usm.web.model.SurveyDetails;
import org.egov.usm.web.model.SurveyDetailsRequest;
import org.egov.usm.web.model.SurveySearchCriteria;
import org.egov.usm.web.model.SurveyTicket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

@Service
public class SurveyResponseService {
	
	private SurveyDetailsRepository repository;
	
	private TicketService ticketService ;
	
	private EnrichmentService enrichmentService;
	
	
	@Autowired
	public SurveyResponseService(SurveyDetailsRepository repository, TicketService ticketService,
			EnrichmentService enrichmentService) {
		this.repository = repository;
		this.ticketService = ticketService;
		this.enrichmentService = enrichmentService;
	}


	/**
	 * Service layer for generate Questions for Survey
	 * 
	 * @param surveyDetailsRequest
	 * @return SurveyDetails
	 */
	public SurveyDetails generateSurvey(SurveyDetailsRequest surveyDetailsRequest) {
		SurveyDetails surveyDetails = null;
		
		List<SurveyDetails> surveyResponseForCurrentDate = repository.validateSurveyForCurrentDate(surveyDetailsRequest.getSurveyDetails());
		
		if(surveyResponseForCurrentDate.isEmpty()) {
			List<QuestionDetail> questionsList = repository.getQuestionDetails(surveyDetailsRequest.getSurveyDetails());
			
			// Enrich survey details
			enrichmentService.enrichLookupDetails(surveyDetailsRequest);
			repository.updateLookupDetails(surveyDetailsRequest);
			surveyDetails = SurveyDetails.builder()
								.questionDetails(questionsList)
								.build();
		} else {
			surveyDetails = surveyResponseForCurrentDate.get(0);
		}
		
		surveyDetails.setSurveyId(surveyDetailsRequest.getSurveyDetails().getSurveyId());
		surveyDetails.setTenantId(surveyDetailsRequest.getSurveyDetails().getTenantId());
		surveyDetails.setSlumCode(surveyDetailsRequest.getSurveyDetails().getSlumCode());
		
		return surveyDetails;
	}

	/**
	 * Service layer for submit Survey with Questions and Answers
	 * 
	 * @param surveyDetailsRequest
	 * @return SurveyDetails
	 */
	public SurveyDetails submitSurvey(SurveyDetailsRequest surveyDetailsRequest) {
		
		// Enrich survey details
		enrichmentService.enrichSurveySubmitRequest(surveyDetailsRequest);
		
		// Check if any answers are NO then  Create the TICKET
		List<SurveyTicket> tickets = ticketService.prepareTickets(surveyDetailsRequest) ;
		surveyDetailsRequest.getSurveyDetails().setSurveyTickets(tickets);
		
		// If any ticket created, then create or update the lookup table
		if(! CollectionUtils.isEmpty(tickets)) {
			//check is present in question lookup
			List<String> questionIds = repository.searchQuestionInLookup(surveyDetailsRequest.getSurveyDetails());
			
			if(CollectionUtils.isEmpty(questionIds) || questionIds.size() != surveyDetailsRequest.getSurveyDetails().getSubmittedAnswers().size()) {
				enrichmentService.enrichSaveQuestionLookup(surveyDetailsRequest, questionIds);
			} 
			
			//update question lookup
			enrichmentService.enrichUpdateQuestionLookup(surveyDetailsRequest, tickets);
		}
		
		repository.submitSurvey(surveyDetailsRequest);
		
		return surveyDetailsRequest.getSurveyDetails();
	}

	
	/**
	 * 
	 * @param surveyRequest
	 * @return SurveyDetails
	 */
	public SurveyDetails updateSubmittedSurvey(@Valid SurveyDetailsRequest surveyRequest) {
		// TODO Auto-generated method stub
		return null;
	}
	
	

	/**
	 * 
	 * @param searchCriteria
	 * @return
	 */
	public List<SurveyDetails> searchSubmittedSurvey(@Valid SurveySearchCriteria searchCriteria) {
		// TODO Auto-generated method stub
		return null;
	}



}
