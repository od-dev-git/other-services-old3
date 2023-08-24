package org.egov.usm.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.egov.common.contract.request.RequestInfo;
import org.egov.usm.model.enums.SurveyAnswer;
import org.egov.usm.model.enums.TicketStatus;
import org.egov.usm.repository.SurveyTicketRepository;
import org.egov.usm.validator.SurveyTicketRequestValidator;
import org.egov.usm.web.model.SubmittedAnswer;
import org.egov.usm.web.model.SurveyDetailsRequest;
import org.egov.usm.web.model.SurveyTicket;
import org.egov.usm.web.model.SurveyTicketRequest;
import org.egov.usm.web.model.TicketSearchCriteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class TicketService {

	@Autowired
	private EnrichmentService enrichmentService;

	@Autowired
	private SurveyTicketRepository repository;

	@Autowired
	private SurveyTicketRequestValidator surveyTicketRequestValidator;

	public List<SurveyTicket> prepareTickets(SurveyDetailsRequest surveyDetailsRequest) {

		List<SurveyTicket> surveyTickets = new ArrayList<>();

		// Check the No answers
		List<SubmittedAnswer> submittedAnswersAsNO = surveyDetailsRequest.getSurveyDetails().getSubmittedAnswers()
				.stream().filter(answer -> answer.getAnswer().equals(SurveyAnswer.NO)).collect(Collectors.toList());

		// Check ticket already exists for same survey
		List<String> questionsExistsInTicket = repository
				.searchQuestionsInTicket(surveyDetailsRequest.getSurveyDetails());

		// Filter answers for ticket creation
		List<SubmittedAnswer> filterSubmittedAnswers = submittedAnswersAsNO.stream()
				.filter(answer -> questionsExistsInTicket.stream()
						.noneMatch(questionId -> questionId.equals(answer.getQuestionId())))
				.collect(Collectors.toList());

		// If there is no answer as no then return Empty Ticket
		if (!CollectionUtils.isEmpty(filterSubmittedAnswers)) {
			surveyTickets = enrichmentService.enrichTickets(filterSubmittedAnswers, surveyDetailsRequest);
		}
		// return List of tickets
		return surveyTickets;
	}

	/**
	 * Service layer for Updating SurveyTicket
	 * 
	 * @param SurveyTicketRequest
	 * @return updated Survey
	 */

	public SurveyTicket updateSurveyTicket(@Valid SurveyTicketRequest ticketRequest) {
		SurveyTicket surveyTicket = ticketRequest.getTicket();
		RequestInfo requestInfo = ticketRequest.getRequestInfo();

		/* Validate the existing ticket */
		SurveyTicket existingSurveyTickets = surveyTicketRequestValidator.validateSurveyTicketExistence(surveyTicket);

		surveyTicket.setAuditDetails(existingSurveyTickets.getAuditDetails());

		/* Update the Audit Details */
		surveyTicket.getAuditDetails().setLastModifiedBy(requestInfo.getUserInfo().getUuid());
		surveyTicket.getAuditDetails().setLastModifiedTime(System.currentTimeMillis());
		surveyTicket.setTicketClosedTime(System.currentTimeMillis());

		if (surveyTicket.getIsSatisfied() != null && existingSurveyTickets.getStatus().equals(TicketStatus.CLOSED)) {
			surveyTicket.setStatus(existingSurveyTickets.getStatus());
			surveyTicket.setHasOpenTicket(Boolean.FALSE);
		} else if (surveyTicket.getStatus().equals(TicketStatus.CLOSED)) {
			surveyTicketRequestValidator.validateSurveyTicketClose(existingSurveyTickets);
			surveyTicket.setHasOpenTicket(Boolean.FALSE);
		} else {

			surveyTicket.setHasOpenTicket(Boolean.TRUE);
		}

		repository.update(ticketRequest);
		return surveyTicket;
	}

	/**
	 * return List<SurveyTicket> based on search criteria
	 * 
	 * @param searchCriteria
	 * @return List<SurveyTicket>
	 */
	public List<SurveyTicket> searchTicket(TicketSearchCriteria searchCriteria) {

		log.info("search: " + searchCriteria.toString());
		List<SurveyTicket> surveyTickets = repository.getSurveyTicketRequests(searchCriteria);
		return surveyTickets;
	}

}
