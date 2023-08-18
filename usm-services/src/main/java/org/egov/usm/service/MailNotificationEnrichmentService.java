package org.egov.usm.service;


import java.util.List;

import org.egov.usm.utility.NotificationUtil;
import org.egov.usm.web.model.EmailRequest;
import org.egov.usm.web.model.SurveyDetailsRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MailNotificationEnrichmentService {
	
	@Autowired
	private NotificationUtil notificationUtil;
	
	
	/**
	 * Enrich mail
	 * @param surveyDetailsRequest
	 * @param emailRequests
	 */
	public void enrichEmailRequest(SurveyDetailsRequest surveyDetailsRequest, List<EmailRequest> emailRequests) {
		// TODO Auto-generated method stub
		
	}
	
}
