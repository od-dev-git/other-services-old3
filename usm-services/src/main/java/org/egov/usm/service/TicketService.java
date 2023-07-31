package org.egov.usm.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.egov.usm.model.enums.SurveyAnswer;
import org.egov.usm.repository.SurveyTicketRepository;
import org.egov.usm.web.model.SubmittedAnswer;
import org.egov.usm.web.model.SurveyDetailsRequest;
import org.egov.usm.web.model.SurveyTicket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

@Service
public class TicketService {
	
	@Autowired
	private EnrichmentService enrichmentService;
	
	@Autowired
	private SurveyTicketRepository repository;

	public List<SurveyTicket> prepareTickets(SurveyDetailsRequest surveyDetailsRequest) {

		List<SurveyTicket> surveyTickets = new ArrayList<>();
        
		// Check the No answers
        List<SubmittedAnswer> submittedAnswersAsNO = surveyDetailsRequest.getSurveyDetails().getSubmittedAnswers().stream()
                .filter(answer -> answer.getAnswer().equals(SurveyAnswer.NO)).collect(Collectors.toList());
        
        // Check ticket already exists for same survey
        List<String> questionsExistsInTicket = repository.searchQuestionsInTicket(surveyDetailsRequest.getSurveyDetails());
        
        //Filter answers for ticket creation
        List<SubmittedAnswer> filterSubmittedAnswers = submittedAnswersAsNO.stream()
				.filter(answer -> questionsExistsInTicket.stream().noneMatch(questionId -> questionId.equals(answer.getQuestionId())))
				.collect(Collectors.toList());
        
        // If there is no answer as no then return Empty Ticket
        if (!CollectionUtils.isEmpty(filterSubmittedAnswers)) {
            surveyTickets = enrichmentService.enrichTickets(filterSubmittedAnswers, surveyDetailsRequest);
        }
        // return List of tickets
        return surveyTickets;
    }
	
	
	
   

}
