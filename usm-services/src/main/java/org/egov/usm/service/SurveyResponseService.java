package org.egov.usm.service;

import java.util.List;

import javax.validation.Valid;

import org.egov.usm.repository.SurveyDetailsRepository;
import org.egov.usm.validator.SurveyResponseValidator;
import org.egov.usm.web.model.QuestionDetail;
import org.egov.usm.web.model.RequestInfoWrapper;
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
	
	private SurveyResponseValidator validator;
	
	@Autowired
	public SurveyResponseService(SurveyDetailsRepository repository, TicketService ticketService,
			EnrichmentService enrichmentService, SurveyResponseValidator validator) {
		this.repository = repository;
		this.ticketService = ticketService;
		this.enrichmentService = enrichmentService;
		this.validator = validator;
	}


	/**
	 * Service layer for generate Questions for Survey
	 * 
	 * @param surveyDetailsRequest
	 * @return SurveyDetails
	 */
	public SurveyDetails generateSurvey(SurveyDetailsRequest surveyDetailsRequest) {
		SurveyDetails surveyDetails = null;
		
		validator.validateGenerateSurveyRequest(surveyDetailsRequest);
		
		List<SurveyDetails> surveyResponseForCurrentDate = repository.validateSurveyForCurrentDate(surveyDetailsRequest.getSurveyDetails());
		
		if(surveyResponseForCurrentDate.isEmpty()) {
			List<QuestionDetail> questionsList = repository.getQuestionDetails(surveyDetailsRequest.getSurveyDetails());
			surveyDetails = SurveyDetails.builder()
								.questionDetails(questionsList)
								.build();
		} else {
			surveyDetails = surveyResponseForCurrentDate.get(0);
		}
		
		surveyDetails.setSurveyId(surveyDetailsRequest.getSurveyDetails().getSurveyId());
		surveyDetails.setTenantId(surveyDetailsRequest.getSurveyDetails().getTenantId());
		surveyDetails.setWard(surveyDetailsRequest.getSurveyDetails().getWard());
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
		
		// Validate survey existence
		validator.validateSurveySubmittedForToday(surveyDetailsRequest);
        
		// Enrich survey details
		enrichmentService.enrichSurveySubmitRequest(surveyDetailsRequest);
		
		// Check if any answers are NO then  Create the TICKET
		List<SurveyTicket> tickets = ticketService.prepareTickets(surveyDetailsRequest) ;
		surveyDetailsRequest.getSurveyDetails().setSurveyTickets(tickets);
		
		// If any ticket created, then create or update the lookup table
		if(! CollectionUtils.isEmpty(tickets)) {
			
			//check is present in question lookup
			List<String> questionIds = repository.searchQuestionInLookup(surveyDetailsRequest.getSurveyDetails());
			
			//enrich Save Question Lookup
			enrichmentService.enrichSaveQuestionLookup(surveyDetailsRequest, questionIds);
			
			//update question lookup
			enrichmentService.enrichUpdateQuestionLookup(surveyDetailsRequest, tickets);
		}
		
		repository.submitSurvey(surveyDetailsRequest);
		
		return surveyDetailsRequest.getSurveyDetails();
	}

	
	/**
	 * Service layer for Update Survey  Answers
	 * 
	 * @param surveyDetailsRequest
	 * @return SurveyDetails
	 */
	public SurveyDetails updateSubmittedSurvey(@Valid SurveyDetailsRequest surveyDetailsRequest) {
		
		// Validate survey existence
        Boolean isSurveyExists = validator.validateSurveyExistsForToday(surveyDetailsRequest);
        if(isSurveyExists) {
        	// Enrich survey details
    		enrichmentService.enrichSurveyUpdateRequest(surveyDetailsRequest);

    		// Check if any answers are NO then  Create the TICKET
    		List<SurveyTicket> tickets = ticketService.prepareTickets(surveyDetailsRequest) ;
    		surveyDetailsRequest.getSurveyDetails().setSurveyTickets(tickets);

    		// If any ticket created, then create or update the lookup table
    		if(! CollectionUtils.isEmpty(tickets)) {
    			//update question lookup
    			enrichmentService.enrichUpdateQuestionLookup(surveyDetailsRequest, tickets);
    		}

    		repository.updateSubmittedSurvey(surveyDetailsRequest);
        }
		return surveyDetailsRequest.getSurveyDetails();
	}
	
	

	/**
	 * Service layer for Search Survey with searchCriteria
	 * @param requestInfoWrapper 
	 * 
	 * @param searchCriteria
	 * @return List<SurveyDetails>
	 */
	public List<SurveyDetails> searchSubmittedSurvey(RequestInfoWrapper requestInfoWrapper, SurveySearchCriteria searchCriteria) {
		
		//Add User uuid for searching Survey for specific user
		searchCriteria.setCreatedBy(requestInfoWrapper.getRequestInfo().getUserInfo().getUuid());
				
		List<SurveyDetails> surveys = repository.searchSubmittedSurvey(searchCriteria);
		return surveys;
	}



}
