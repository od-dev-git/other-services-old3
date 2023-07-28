package org.egov.usm.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.egov.usm.model.enums.SurveyAnswer;
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
	
	

	public List<SurveyTicket> prepareTickets(SurveyDetailsRequest surveyDetailsRequest) {

		List<SurveyTicket> surveyTickets = new ArrayList<>();
        // Check the No answers
        List<SubmittedAnswer> filterSubmittedAnswers = surveyDetailsRequest.getSurveyDetails().getSubmittedAnswers().stream()
                .filter(answer -> answer.getAnswer().equals(SurveyAnswer.NO)).collect(Collectors.toList());

        // If there is no answer as no then return Empty Ticket
        if (!CollectionUtils.isEmpty(filterSubmittedAnswers)) {
            surveyTickets = enrichmentService.enrichTickets(filterSubmittedAnswers, surveyDetailsRequest);
        }
        // return List of tickets
        return surveyTickets;
    }
   

}
