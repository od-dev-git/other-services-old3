package org.egov.usm.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.egov.usm.model.enums.SurveyAnswer;
import org.egov.usm.web.model.QuestionDetail;
import org.egov.usm.web.model.SurveyDetailsRequest;
import org.egov.usm.web.model.SurveyTicket;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

@Service
public class TicketService {

	public List<SurveyTicket> prepareTickets(SurveyDetailsRequest surveyRequest) {
		
		List<SurveyTicket>  surveyTickets = new ArrayList<>();
		
		// check the No answers
		List<QuestionDetail> questionDetails = surveyRequest.getSurveyDetails().getQuestionDetails().stream()
										.filter(qs -> qs.getAnswer().equals(SurveyAnswer.NO))
										.collect(Collectors.toList());
		
		
		//IF there is no answer as NO then return Empty TIcket 
		
		if(CollectionUtils.isEmpty(questionDetails)) {
			return surveyTickets;
		}
		
		
		//IF there are atleast one NO answer then prepare the LIST<Ticket>
		
				//Enrich TicketRequest
		
				// Generate Ticket No
				// Add the ticket into the LIST
		
	
		
		
		// return List of tickets
		return null;
	}

}
