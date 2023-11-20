package org.egov.usm.service;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.egov.common.contract.request.RequestInfo;
import org.egov.usm.model.enums.TicketStatus;
import org.egov.usm.repository.SurveyDetailsRepository;
import org.egov.usm.repository.USMOfficialRepository;
import org.egov.usm.utility.Constants;
import org.egov.usm.utility.NotificationUtil;
import org.egov.usm.web.model.SMSRequest;
import org.egov.usm.web.model.SubmittedAnswer;
import org.egov.usm.web.model.SurveyDetails;
import org.egov.usm.web.model.SurveyDetailsRequest;
import org.egov.usm.web.model.SurveySearchCriteria;
import org.egov.usm.web.model.SurveyTicket;
import org.egov.usm.web.model.SurveyTicketRequest;
import org.egov.usm.web.model.USMOfficialSearchCriteria;
import org.egov.usm.web.model.user.Citizen;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

@Service
public class SMSNotificationEnrichmentService {

	@Autowired
	private NotificationUtil notificationUtil;

	@Autowired
	private UserService userService;

	@Autowired
	private USMOfficialRepository usmOfficialRepository;
	
	@Autowired
	private SurveyDetailsRepository surveyDetailsRepository;

	/**
	 * Enrich SMS messages
	 * 
	 * @param surveyDetailsRequest
	 * @param smsRequests
	 */
	public void enrichTicketCreatedSMSRequest(SurveyDetailsRequest surveyDetailsRequest, List<SMSRequest> smsRequests) {
		RequestInfo requestInfo = surveyDetailsRequest.getRequestInfo();
		String localizationMessage = notificationUtil.getLocalizationMessages(surveyDetailsRequest.getSurveyDetails().getTenantId(), surveyDetailsRequest.getSurveyDetails().getSlumCode(), requestInfo);
		
		surveyDetailsRequest.getSurveyDetails().getSurveyTickets().forEach(surveyTicket -> {

			// Send Message to SDA Member
			enrichTicketCreatedSMSRequestForSDAMember(requestInfo, surveyDetailsRequest.getSurveyDetails(), surveyTicket, smsRequests, localizationMessage);

			// Send Message to Nodal Officer
			enrichTicketCreatedSMSRequestForNodalOfficer(requestInfo, surveyDetailsRequest.getSurveyDetails(), surveyTicket, smsRequests, localizationMessage);

		});
	}

	
	private void enrichTicketCreatedSMSRequestForNodalOfficer(RequestInfo requestInfo, SurveyDetails surveyDetails,
			SurveyTicket surveyTicket, List<SMSRequest> smsRequests, String localizationMessage) {

		String message = getCustomizedMsgForNodalOfficer(requestInfo, surveyDetails, surveyTicket, localizationMessage);
		createSMSRequestForOfficials(requestInfo, message, surveyTicket, smsRequests, Constants.ROLE_NODAL_OFFICER);
	}
	

	private void enrichTicketCreatedSMSRequestForSDAMember(RequestInfo requestInfo, SurveyDetails surveyDetails,
			SurveyTicket surveyTicket, List<SMSRequest> smsRequests, String localizationMessage) {

		String message = getCustomizedMsgForSDAMember(surveyDetails, surveyTicket, localizationMessage);
		createSMSRequestForSDAMember(requestInfo, message, surveyTicket, smsRequests);
	}

	
	
	/**
	 * Enrich Msg for update ticket
	 * 
	 * @param surveyTicketRequest
	 * @param smsRequests
	 */
	public void enrichTicketUpdateSMSRequest(SurveyTicketRequest surveyTicketRequest, List<SMSRequest> smsRequests) {
		
		if (surveyTicketRequest.getTicket().getIsSatisfied() == null && surveyTicketRequest.getTicket().getStatus() == TicketStatus.CLOSED) {
			enrichTicketClosedSMSRequest(surveyTicketRequest, smsRequests);
		}
		
		if (surveyTicketRequest.getTicket().getIsSatisfied() == Boolean.TRUE ) {
			enrichSatisfiedTicketSMSRequest(surveyTicketRequest, smsRequests);
		}

		if (surveyTicketRequest.getTicket().getIsSatisfied() == Boolean.FALSE ) {
			enrichUnsatisfiedTicketSMSRequest(surveyTicketRequest, smsRequests);
		}

	}

	
	
	/**
	 * Enrich Msg for closed ticket
	 * 
	 * @param surveyTicketRequest
	 * @param smsRequests
	 */
	private void enrichTicketClosedSMSRequest(SurveyTicketRequest surveyTicketRequest, List<SMSRequest> smsRequests) {
		
		RequestInfo requestInfo = surveyTicketRequest.getRequestInfo();
		SurveyTicket surveyTicket = surveyTicketRequest.getTicket();
		SurveyDetails surveyDetails = getSurveyDetails(surveyTicket.getId());
		
		String localizationMessage = notificationUtil.getLocalizationMessages(surveyTicket.getTenantId(), surveyDetails.getSlumCode(), requestInfo);
		// Send Message to SDA Member
		String message = getCustomizedMsgForSDAMember(surveyDetails, surveyTicket, localizationMessage);
		createSMSRequestForSDAMember(requestInfo, message, surveyTicket, smsRequests);
	}
	
	
	/**
	 * Enrich Msg for Satisfied ticket
	 * 
	 * @param surveyTicketRequest
	 * @param smsRequests
	 */
	private void enrichSatisfiedTicketSMSRequest(SurveyTicketRequest surveyTicketRequest, List<SMSRequest> smsRequests) {
		RequestInfo requestInfo = surveyTicketRequest.getRequestInfo();
		SurveyTicket surveyTicket = surveyTicketRequest.getTicket();
		
		String localizationMessage = notificationUtil.getLocalizationMessages(surveyTicket.getTenantId(), null, requestInfo);
		// Send Message to SDA Member
		String message = getCustomizedMsgForSDAMember(null, surveyTicket, localizationMessage);
		createSMSRequestForSDAMember(requestInfo, message, surveyTicket, smsRequests);
	}
	
	

	/**
	 * Enrich Msg for unsatisfied ticket
	 * 
	 * @param surveyTicketRequest
	 * @param smsRequests
	 */
	private void enrichUnsatisfiedTicketSMSRequest(SurveyTicketRequest surveyTicketRequest,
			List<SMSRequest> smsRequests) {
		RequestInfo requestInfo = surveyTicketRequest.getRequestInfo();
		SurveyTicket surveyTicket = surveyTicketRequest.getTicket();
		SurveyDetails surveyDetails = getSurveyDetails(surveyTicket.getId());
		
		String localizationMessage = notificationUtil.getLocalizationMessages(surveyTicket.getTenantId(), surveyDetails.getSlumCode(), requestInfo);
		
		// Send Message to SDA Member
		enrichTicketDissatisfiedSMSRequestForSDAMember(requestInfo, surveyDetails, surveyTicket, localizationMessage, smsRequests);
		
		// Send Message to Nodal Officer
		enrichTicketDissatisfiedSMSRequestForNodalOfficer(requestInfo, surveyDetails, surveyTicket, localizationMessage, smsRequests);
		
		// Send Message to Escalation Officer
		enrichTicketDissatisfiedSMSRequestForEscalationOfficer(requestInfo, surveyDetails, surveyTicket, localizationMessage, smsRequests);
		
	}

	
	private void enrichTicketDissatisfiedSMSRequestForSDAMember(RequestInfo requestInfo, SurveyDetails surveyDetails, SurveyTicket surveyTicket,
			String localizationMessage, List<SMSRequest> smsRequests) {
		String message = getCustomizedMsgForSDAMember(surveyDetails, surveyTicket, localizationMessage);
		createSMSRequestForSDAMember(requestInfo, message, surveyTicket, smsRequests);
		
	}

	private void enrichTicketDissatisfiedSMSRequestForNodalOfficer(RequestInfo requestInfo, SurveyDetails surveyDetails, SurveyTicket surveyTicket,
			String localizationMessage, List<SMSRequest> smsRequests) {
		
		String message = getCustomizedMsgForNodalOfficer(requestInfo, surveyDetails, surveyTicket, localizationMessage);
		createSMSRequestForOfficials(requestInfo, message, surveyTicket, smsRequests, Constants.ROLE_NODAL_OFFICER);
	}

	
	private void enrichTicketDissatisfiedSMSRequestForEscalationOfficer(RequestInfo requestInfo, SurveyDetails surveyDetails, SurveyTicket surveyTicket, 
			String localizationMessage, List<SMSRequest> smsRequests) {

		String message = getCustomizedMsgForEscalationOfficer(requestInfo, surveyDetails, surveyTicket, localizationMessage);
		createSMSRequestForOfficials(requestInfo, message, surveyTicket, smsRequests, Constants.ROLE_ESCALATION_OFFICER);
	}

	private SurveyDetails getSurveyDetails(String ticketId) {
		SurveySearchCriteria criteria = SurveySearchCriteria.builder()
				.ticketId(ticketId)
				.isAdmin(Boolean.TRUE)
				.build();
		List<SurveyDetails> surveyDetailsList = surveyDetailsRepository.searchSubmittedSurvey(criteria);
		return surveyDetailsList.get(0);
	}


	private String getCustomizedMsgForSDAMember(SurveyDetails surveyDetails, SurveyTicket surveyTicket,
			String localizationMessage) {
		String messageTemplate = null, slumName = null , surveyTitle = null;
		String TICKET_STATUS = surveyTicket.getStatus().name();
		if (!ObjectUtils.isEmpty(surveyDetails)) {
			for(SubmittedAnswer submittedAnswer:  surveyDetails.getSubmittedAnswers()) {
				if(surveyTicket.getQuestionId().equals(submittedAnswer.getQuestionId())) {
					surveyTitle = submittedAnswer.getQuestionCategory();
				}
			}
		}
		
		
		if(TICKET_STATUS == Constants.TICKET_STATUS_OPEN) {
			messageTemplate = notificationUtil.getMessageTemplate(Constants.TICKET_CREATED_SDA_MEMBER, localizationMessage);
		}
		
		if(TICKET_STATUS == Constants.TICKET_STATUS_CLOSED) {
			messageTemplate = notificationUtil.getMessageTemplate(Constants.TICKET_CLOSED_SDA_MEMBER, localizationMessage);
		}
		
		if(surveyTicket.getIsSatisfied() == Boolean.TRUE) {
			messageTemplate = notificationUtil.getMessageTemplate(Constants.TICKET_SATISFIED_SDA_MEMBER, localizationMessage);
		}
		
		if(surveyTicket.getIsSatisfied() == Boolean.FALSE) {
			messageTemplate = notificationUtil.getMessageTemplate(Constants.TICKET_UNSATISFIED_SDA_MEMBER, localizationMessage);
		}
		
		return getCustomizedMsg(surveyTicket.getTicketNo(), surveyTitle , slumName,  messageTemplate);
	}

	
	
	private String getCustomizedMsgForNodalOfficer(RequestInfo requestInfo, SurveyDetails surveyDetails, SurveyTicket surveyTicket,
			String localizationMessage) {
		String messageTemplate = null, slumName = null, surveyTitle = null;
		String TICKET_STATUS = surveyTicket.getStatus().name();
		if (!ObjectUtils.isEmpty(surveyDetails)) {
			surveyTitle = surveyDetails.getSurveyTitle();
		}
		
		
		if(TICKET_STATUS == Constants.TICKET_STATUS_OPEN) {
			String slumCodeKey =  notificationUtil.getSlumCodeKey(surveyDetails.getTenantId(), surveyDetails.getSlumCode());
			slumName =  notificationUtil.getMessageTemplate(slumCodeKey, localizationMessage);
			
			messageTemplate = notificationUtil.getMessageTemplate(Constants.TICKET_CREATED_NODAL_OFFICER,
					localizationMessage);
		}
		
		if(surveyTicket.getIsSatisfied() == Boolean.FALSE) {
			messageTemplate = notificationUtil.getMessageTemplate(Constants.TICKET_UNSATISFIED_NODAL_OFFICER,
					localizationMessage);
		}
		

		return getCustomizedMsg(surveyTicket.getTicketNo(), surveyTitle, slumName, messageTemplate);
	}
	
	
	
	private String getCustomizedMsgForEscalationOfficer(RequestInfo requestInfo, SurveyDetails surveyDetails, SurveyTicket surveyTicket,
			String localizationMessage) {
		String messageTemplate = null, slumName = null, surveyTitle = null;
		
		if (!ObjectUtils.isEmpty(surveyDetails)) {
			surveyTitle = surveyDetails.getSurveyTitle();
		}
		
		String slumCodeKey =  notificationUtil.getSlumCodeKey(surveyDetails.getTenantId(), surveyDetails.getSlumCode());
		slumName =  notificationUtil.getMessageTemplate(slumCodeKey, localizationMessage);
		
		if(surveyTicket.getIsSatisfied() == Boolean.FALSE) {
			messageTemplate = notificationUtil.getMessageTemplate(Constants.TICKET_UNSATISFIED_ESCALATION_OFFICER,
					localizationMessage);
		}

		return getCustomizedMsg(surveyTicket.getTicketNo(), surveyTitle, slumName, messageTemplate);
	}


	
	private String getCustomizedMsg(String ticketNo, String surveyTitle, String slumName, String message) {
		if (!ObjectUtils.isEmpty(ticketNo)) {
			message = message.replace("<1>", ticketNo);
		}

		if (!ObjectUtils.isEmpty(surveyTitle)) {
			message = message.replace("<2>", surveyTitle);
		}
		
		if (!ObjectUtils.isEmpty(slumName)) {
			message = message.replace("<3>", slumName);
		}
		
		return message;
	}

	
	
	/**
	 * create SMSRequest For SDAMember
	 * 
	 * @param requestInfo
	 * @param message
	 * @param surveyTicket
	 * @param smsRequests
	 */
	private void createSMSRequestForSDAMember(RequestInfo requestInfo, String message, SurveyTicket surveyTicket,
			List<SMSRequest> smsRequests) {
		
		enrichSMSRequest(requestInfo, Collections.singletonList(surveyTicket.getAuditDetails().getCreatedBy()), message, smsRequests);
	}
	
	
	/**
	 * create SMSRequest For Officials
	 * 
	 * @param requestInfo
	 * @param message
	 * @param surveyTicket
	 * @param smsRequests
	 * @param officerRole
	 */
	private void createSMSRequestForOfficials(RequestInfo requestInfo, String message, SurveyTicket surveyTicket, List<SMSRequest> smsRequests, String officerRole) {
		
		USMOfficialSearchCriteria searchCriteria = USMOfficialSearchCriteria.builder()
				.ticketId(surveyTicket.getId())
				.role(officerRole)
				.build();
		List<String> officerUuid = usmOfficialRepository.getUuidOfUSMOfficials(searchCriteria);
		enrichSMSRequest(requestInfo,officerUuid, message, smsRequests);
	}


	/**
	 * enrich SMSRequest an create SMSRequest
	 * 
	 * @param requestInfo
	 * @param officerUuid
	 * @param message
	 * @param smsRequests
	 */
	private void enrichSMSRequest(RequestInfo requestInfo, List<String> uuids, String message, List<SMSRequest> smsRequests) {
		Map<String, String> mobileNumberToOwner = new HashMap<>();
		List<Citizen> citizens = userService.getUserByUuid(uuids, requestInfo);

		citizens.forEach(citizen -> {
			mobileNumberToOwner.put(citizen.getMobileNumber(), citizen.getName());
		});
		
		smsRequests.addAll(notificationUtil.createSMSRequest(message, mobileNumberToOwner));
	}


}
