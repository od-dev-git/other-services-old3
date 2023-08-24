package org.egov.usm.validator;

import java.util.List;

import javax.validation.Valid;

import org.egov.common.contract.request.RequestInfo;
import org.egov.tracer.model.CustomException;
import org.egov.usm.repository.SurveyTicketRepository;
import org.egov.usm.utility.USMUtil;
import org.egov.usm.web.model.AuditDetails;
import org.egov.usm.web.model.Survey;
import org.egov.usm.web.model.SurveyTicket;
import org.egov.usm.web.model.SurveyTicketCommentRequest;
import org.egov.usm.web.model.TicketSearchCriteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

@Service
public class SurveyTicketRequestValidator {


	@Autowired
	private SurveyTicketRepository ticketRepository;

	public void validateUserType(RequestInfo requestInfo) { // TODO Auto-generated method stub

	}

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

	public void validateQuestionsWhileUpdate(Survey survey) {
		// TODO Auto-generated method stub

	}

	public void validateUpdateRequest(Survey survey) {
		// TODO Auto-generated method stub

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

}