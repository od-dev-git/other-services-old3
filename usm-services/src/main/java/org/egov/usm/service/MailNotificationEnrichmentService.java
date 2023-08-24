package org.egov.usm.service;


import java.util.List;

import org.egov.usm.web.model.EmailRequest;
import org.egov.usm.web.model.SurveyDetailsRequest;
import org.egov.usm.web.model.SurveyTicketRequest;
import org.springframework.stereotype.Service;

@Service
public class MailNotificationEnrichmentService {
	
	
	/**
	 * Enrich mail for create ticket
	 * 
	 * @param surveyDetailsRequest
	 * @param emailRequests
	 */
	public void enrichTicketCreatedEmailRequest(SurveyDetailsRequest surveyDetailsRequest,
			List<EmailRequest> emailRequests) {
		// TODO Auto-generated method stub

	}



	/**
	 * Enrich mail for close ticket
	 * 
	 * @param surveyTicketRequest
	 * @param emailRequests
	 */
	public void enrichTicketUpdateEmailRequest(SurveyTicketRequest surveyTicketRequest,
			List<EmailRequest> emailRequests) {
		// TODO Auto-generated method stub
		
	}
	
}
