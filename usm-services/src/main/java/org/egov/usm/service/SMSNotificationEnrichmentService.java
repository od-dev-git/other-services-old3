package org.egov.usm.service;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.egov.common.contract.request.RequestInfo;
import org.egov.usm.repository.USMOfficialRepository;
import org.egov.usm.utility.Constants;
import org.egov.usm.utility.NotificationUtil;
import org.egov.usm.web.model.SMSRequest;
import org.egov.usm.web.model.SurveyDetailsRequest;
import org.egov.usm.web.model.SurveyTicket;
import org.egov.usm.web.model.SurveyTicketRequest;
import org.egov.usm.web.model.user.Citizen;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SMSNotificationEnrichmentService {
	
	@Autowired
	private NotificationUtil notificationUtil;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private USMOfficialRepository usmOfficialRepository;
	

	/**
	 * Enrich SMS messages 
	 * 
	 * @param surveyDetailsRequest
	 * @param smsRequests
	 */
	public void enrichTicketCreatedSMSRequest(SurveyDetailsRequest surveyDetailsRequest, List<SMSRequest> smsRequests) {
		RequestInfo requestInfo = surveyDetailsRequest.getRequestInfo();
		String tenantId = surveyDetailsRequest.getSurveyDetails().getTenantId();
		
		surveyDetailsRequest.getSurveyDetails().getSurveyTickets().forEach(surveyTicket -> {
			
			//Send Message to SDA Member
			enrichTicketCreatedSMSRequestForSDAMember(requestInfo, tenantId, surveyTicket, smsRequests);
			
			
			//Send Message to USM OFFICIALS
			enrichTicketCreatedSMSRequestForOfficials(requestInfo, tenantId, surveyTicket, smsRequests);
			
		});
	}
	
	
	 
	private void enrichTicketCreatedSMSRequestForOfficials(RequestInfo requestInfo, String tenantId, SurveyTicket surveyTicket,
			List<SMSRequest> smsRequests) {
		
		String localizationMessage = notificationUtil.getLocalizationMessages(tenantId, requestInfo);
		String message = getCustomizedMsgForOfficials(requestInfo, surveyTicket, localizationMessage);
		List<String> nodalOfficerUuid = usmOfficialRepository.getUuidOfUSMOfficials(surveyTicket);

		nodalOfficerUuid.forEach(uuid -> {
			Map<String, String> mobileNumberToOwner = new HashMap<>();
			Citizen employee = userService.getUserByUuidAndRole(uuid, requestInfo, tenantId, Constants.ROLE_EMPLOYEE);

			if (employee != null)
				mobileNumberToOwner.put(employee.getMobileNumber(), employee.getName());

			smsRequests.addAll(notificationUtil.createSMSRequest(message,mobileNumberToOwner));
		});
	}



	private void enrichTicketCreatedSMSRequestForSDAMember(RequestInfo requestInfo, String tenantId, SurveyTicket surveyTicket,
			List<SMSRequest> smsRequests) {
		
		String localizationMessage = notificationUtil.getLocalizationMessages(tenantId, requestInfo);
		String message = getCustomizedMsgForSDAMember(requestInfo, surveyTicket, localizationMessage);

		Map<String, String> mobileNumberToOwner = new HashMap<>();

		Citizen citizen = userService.getUserByUuidAndRole(requestInfo.getUserInfo().getUuid(), requestInfo, tenantId, Constants.ROLE_CITIZEN);

		if (citizen != null)
			mobileNumberToOwner.put(citizen.getMobileNumber(), citizen.getName());

		smsRequests.addAll(notificationUtil.createSMSRequest(message,mobileNumberToOwner));

	}



	private String getCustomizedMsgForSDAMember(RequestInfo requestInfo, SurveyTicket surveyTicket,
			String localizationMessage) {
		String message = null, messageTemplate;
		String ACTION_STATUS = surveyTicket.getStatus().name();
		
		switch (ACTION_STATUS) {

		case Constants.TICKET_STATUS_OPEN:
			messageTemplate = notificationUtil.getMessageTemplate(Constants.TICKET_CREATED_SDA_MEMBER, localizationMessage);
			message = getTicketCreatedMsg(surveyTicket, messageTemplate);
			break;

		case Constants.TICKET_STATUS_CLOSED:
			messageTemplate = notificationUtil.getMessageTemplate(Constants.TICKET_CLOSED_SDA_MEMBER, localizationMessage);
			message = getTicketClosedMsg(surveyTicket, messageTemplate);
			break;
		}
		
		return message;
	}



	private String getCustomizedMsgForOfficials(RequestInfo requestInfo, SurveyTicket surveyTicket,
			String localizationMessage) {
		String message = null, messageTemplate;
		String ACTION_STATUS = surveyTicket.getStatus().name();
		
		switch (ACTION_STATUS) {

		case Constants.TICKET_STATUS_OPEN:
			messageTemplate = notificationUtil.getMessageTemplate(Constants.TICKET_CREATED_USM_OFFICIAL, localizationMessage);
			message = getTicketCreatedMsg(surveyTicket, messageTemplate);
			break;

		}
		
		return message;
	}

	
	
	private String getTicketCreatedMsg(SurveyTicket surveyTicket, String message) {
		message = message.replace("<ticket_no>", surveyTicket.getTicketNo());

		return message;
	}


	private String getTicketClosedMsg(SurveyTicket surveyTicket, String message) {
		message = message.replace("<ticket_no>", surveyTicket.getTicketNo());
		
		return message;
	}


	


	/**
	 * Enrich Msg for closed ticket 
	 * 
	 * @param surveyTicketRequest
	 * @param smsRequests
	 */
	public void enrichTicketClosedSMSRequest(SurveyTicketRequest surveyTicketRequest, List<SMSRequest> smsRequests) {
		RequestInfo requestInfo = surveyTicketRequest.getRequestInfo();
		SurveyTicket surveyTicket = surveyTicketRequest.getTicket();
		
		//Send Message to SDA Member
		
		String localizationMessage = notificationUtil.getLocalizationMessages(surveyTicket.getTenantId(), requestInfo);
		String message = getCustomizedMsgForSDAMember(requestInfo, surveyTicket, localizationMessage);

		Map<String, String> mobileNumberToOwner = new HashMap<>();

		Citizen citizen = userService.getUserByUuidAndRole(surveyTicket.getAuditDetails().getCreatedBy(), requestInfo, surveyTicket.getTenantId(), Constants.ROLE_CITIZEN);

		if (citizen != null)
			mobileNumberToOwner.put(citizen.getMobileNumber(), citizen.getName());

		smsRequests.addAll(notificationUtil.createSMSRequest(message,mobileNumberToOwner));
		
	}
	
}
