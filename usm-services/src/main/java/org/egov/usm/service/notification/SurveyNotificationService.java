package org.egov.usm.service.notification;

import java.util.LinkedList;
import java.util.List;

import org.egov.usm.config.USMConfiguration;
import org.egov.usm.service.MailNotificationEnrichmentService;
import org.egov.usm.service.SMSNotificationEnrichmentService;
import org.egov.usm.utility.NotificationUtil;
import org.egov.usm.web.model.EmailRequest;
import org.egov.usm.web.model.SMSRequest;
import org.egov.usm.web.model.SurveyDetailsRequest;
import org.egov.usm.web.model.SurveyTicketRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

@Service
public class SurveyNotificationService {
	
	@Autowired
	private SMSNotificationEnrichmentService smsEnrichmentService;
	
	@Autowired
	private MailNotificationEnrichmentService mailEnrichmentService;
	
	@Autowired
	private NotificationUtil notificationUtil;
	
	@Autowired
	private USMConfiguration config;
	
	/**
	 * 
	 * @param surveyDetailsRequest
	 */
	public void processTicketCreatedNotification(SurveyDetailsRequest surveyDetailsRequest) {
		List<SMSRequest> smsRequests = new LinkedList<>();
		List<EmailRequest> emailRequests = new LinkedList<>();
		
		if(null != config.getIsSMSEnabled()) {
			if(config.getIsSMSEnabled()) {
				smsEnrichmentService.enrichTicketCreatedSMSRequest(surveyDetailsRequest,smsRequests);
				if(!CollectionUtils.isEmpty(smsRequests))
					notificationUtil.sendSMS(smsRequests,true);
			}
		}
		
		if(null != config.getIsEmailEnabled()) {
			if(config.getIsEmailEnabled()) {
				mailEnrichmentService.enrichTicketCreatedEmailRequest(surveyDetailsRequest, emailRequests);
				if(!CollectionUtils.isEmpty(emailRequests)) 
					notificationUtil.sendEmail(emailRequests, true);
			}
		}
		
	}


	public void processTicketUpdateNotification(SurveyTicketRequest surveyTicketRequest) {
		List<SMSRequest> smsRequests = new LinkedList<>();
		List<EmailRequest> emailRequests = new LinkedList<>();
		
		if(null != config.getIsSMSEnabled()) {
			if(config.getIsSMSEnabled()) {
				smsEnrichmentService.enrichTicketUpdateSMSRequest(surveyTicketRequest,smsRequests);
				if(!CollectionUtils.isEmpty(smsRequests))
					notificationUtil.sendSMS(smsRequests,true);
			}
		}
		
		if(null != config.getIsEmailEnabled()) {
			if(config.getIsEmailEnabled()) {
				mailEnrichmentService.enrichTicketUpdateEmailRequest(surveyTicketRequest, emailRequests);
				if(!CollectionUtils.isEmpty(emailRequests)) 
					notificationUtil.sendEmail(emailRequests, true);
			}
		}
		
		
	}

	
}
