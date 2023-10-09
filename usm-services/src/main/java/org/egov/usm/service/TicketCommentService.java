package org.egov.usm.service;

import javax.validation.Valid;

import org.egov.usm.repository.TicketCommentRepository;
import org.egov.usm.validator.SurveyTicketRequestValidator;
import org.egov.usm.web.model.SurveyTicket;
import org.egov.usm.web.model.SurveyTicketComment;
import org.egov.usm.web.model.SurveyTicketCommentRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TicketCommentService {

	@Autowired
	private SurveyTicketRequestValidator surveyTicketRequestValidator;

	@Autowired
	private TicketCommentRepository repository;

	/**
	 * Service layer for Creating Ticket Comment Request
	 * 
	 * @param SurveyTicketCommentRequest
	 * 
	 */

	public SurveyTicketComment create(@Valid SurveyTicketCommentRequest surveyTicketCommentRequest) {
		SurveyTicketComment surveyTicketComment = surveyTicketCommentRequest.getSurveyTicketComment();
		SurveyTicket surveyTicket = SurveyTicket.builder()
												.id(surveyTicketCommentRequest.getSurveyTicketComment().getTicketId())
												.build();

		/* Validate the existing ticket */
		surveyTicketRequestValidator.validateSurveyTicketExistence(surveyTicket);

		surveyTicketRequestValidator.enrichTicketCommentRequest(surveyTicketCommentRequest);

		repository.save(surveyTicketCommentRequest);
		return surveyTicketComment;

	}

}
