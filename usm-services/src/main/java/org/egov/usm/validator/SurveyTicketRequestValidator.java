package org.egov.usm.validator;

import java.util.List;

import javax.validation.Valid;

import org.egov.common.contract.request.RequestInfo;
import org.egov.tracer.model.CustomException;
import org.egov.usm.model.enums.TicketStatus;
import org.egov.usm.repository.SDAMemberRepository;
import org.egov.usm.repository.SurveyTicketRepository;
import org.egov.usm.repository.USMOfficialRepository;
import org.egov.usm.utility.Constants;
import org.egov.usm.utility.USMUtil;
import org.egov.usm.web.model.AuditDetails;
import org.egov.usm.web.model.MemberSearchCriteria;
import org.egov.usm.web.model.SDAMember;
import org.egov.usm.web.model.SurveyTicket;
import org.egov.usm.web.model.SurveyTicketCommentRequest;
import org.egov.usm.web.model.TicketSearchCriteria;
import org.egov.usm.web.model.USMOfficial;
import org.egov.usm.web.model.USMOfficialSearchCriteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

@Service
public class SurveyTicketRequestValidator {

	@Autowired
	private SurveyTicketRepository ticketRepository;
	
	@Autowired
	private SDAMemberRepository sdaMemberRepository;
	
	@Autowired
	private USMOfficialRepository officialRepository;

	
	public SurveyTicket validateSurveyTicketExistence(SurveyTicket surveyTicket) {
		if (ObjectUtils.isEmpty(surveyTicket.getId()))
			throw new CustomException("EG_SY_UUID_NOT_PROVIDED_ERR",
					"Providing surveyTicket  id is mandatory for updating ticket");

		TicketSearchCriteria criteria = TicketSearchCriteria.builder().ticketId(surveyTicket.getId()).build();

		List<SurveyTicket> surveyTickets = ticketRepository.getSurveyTicketRequests(criteria);
		if (CollectionUtils.isEmpty(surveyTickets))
			throw new CustomException("EG_SURVEYTICKET_DOES_NOT_EXIST_ERR",
					"The surveyTicket entity provided in update request does not exist.");

		return surveyTickets.get(0);
	}


	public void enrichTicketCommentRequest(@Valid SurveyTicketCommentRequest surveyTicketCommentRequest) {

		RequestInfo requestInfo = surveyTicketCommentRequest.getRequestInfo();
		String uuid = requestInfo.getUserInfo().getUuid();
		AuditDetails auditDetails = USMUtil.getAuditDetails(uuid, true);

		surveyTicketCommentRequest.getSurveyTicketComment().setId(USMUtil.generateUUID());
		// set audit details
		surveyTicketCommentRequest.getSurveyTicketComment().setAuditDetails(auditDetails);

	}

	
	
	public void validateSurveyTicketClose(SurveyTicket existingSurveyTickets) {
		if (existingSurveyTickets.getUnAttended() == Boolean.TRUE) {
			throw new CustomException("EG_SY_UNATTENDED_TICKET_ERR", "You can't close unattended ticket");
		}
	}


	public void validateTicketRequest(RequestInfo requestInfo, SurveyTicket surveyTicket,
		SurveyTicket existingSurveyTickets) {
		
		if (!ObjectUtils.isEmpty(surveyTicket.getStatus())
				&& !ObjectUtils.isEmpty(surveyTicket.getIsSatisfied())) {
			throw new CustomException("EG_INPUT_PARAM_ERR", "Invalid reqest parameter or value in update request.");
		} 
		
		if (ObjectUtils.isEmpty(surveyTicket.getStatus())
				&& ObjectUtils.isEmpty(surveyTicket.getIsSatisfied())) {
			throw new CustomException("EG_INPUT_PARAM_ERR", "Invalid reqest parameter or value in update request.");
		} 
		
		if (!ObjectUtils.isEmpty(surveyTicket.getIsSatisfied())) {
			if(!existingSurveyTickets.getStatus().equals(TicketStatus.CLOSED))
				throw new CustomException("EG_INPUT_PARAM_ERR", "Invalid feedback submit before close ticket.");
			
			//check sda member has access for submit feedback on this ticket
			validateSDAMemberAccess(requestInfo, surveyTicket);
		}
		
		if (!ObjectUtils.isEmpty(surveyTicket.getStatus())) {
			if(!surveyTicket.getStatus().equals(TicketStatus.CLOSED))
				throw new CustomException("EG_INPUT_PARAM_ERR", "Invalid status in update request.");
			
			//check Nodal Officer has access for submit feedback on this ticket
			validateNodalOfficerAccess(requestInfo, surveyTicket);
		}
		
		if (existingSurveyTickets.getStatus() == TicketStatus.CLOSED
				&& surveyTicket.getStatus() == TicketStatus.CLOSED) {
			throw new CustomException("EG_SY_CLOSED_TICKET", "The status is already closed ");
		}
		
		if (!ObjectUtils.isEmpty(existingSurveyTickets.getIsSatisfied())
				&& !ObjectUtils.isEmpty(surveyTicket.getIsSatisfied())) {
			throw new CustomException("EG_SY_FEEDBACK_TICKET", "The feedback already submitted");
		} 
	}

	
	
	private void validateSDAMemberAccess(RequestInfo requestInfo, SurveyTicket surveyTicket) {
		//Validate SDA with slum access
		MemberSearchCriteria searchCriteria = MemberSearchCriteria.builder()
													.ticketId(surveyTicket.getId())
													.build();
		List<SDAMember> sdaMembers = sdaMemberRepository.searchSDAMembers(searchCriteria);
		
		if(CollectionUtils.isEmpty(sdaMembers)) 
			throw new CustomException("EG_SY_UNAUTHORIZED_ACCESS", "You don't have valid access for submit feedback");
	}
	
	
	
	private void validateNodalOfficerAccess(RequestInfo requestInfo, SurveyTicket surveyTicket) {
		//Validate Nodal Officer with slum access
		USMOfficialSearchCriteria searchCriteria = USMOfficialSearchCriteria.builder()
														.ticketId(surveyTicket.getId())
														.role(Constants.ROLE_NODAL_OFFICER)
														.build();
		List<USMOfficial> usmOfficials = officialRepository.getOfficialRequests(searchCriteria);
		
		if(CollectionUtils.isEmpty(usmOfficials)) 
			throw new CustomException("EG_SY_UNAUTHORIZED_ACCESS", "You don't have valid access for close the ticket");
	}

}