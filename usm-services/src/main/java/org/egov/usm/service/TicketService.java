package org.egov.usm.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.egov.common.contract.request.RequestInfo;
import org.egov.tracer.model.CustomException;
import org.egov.usm.model.enums.SurveyAnswer;
import org.egov.usm.model.enums.TicketStatus;
import org.egov.usm.repository.SurveyTicketRepository;
import org.egov.usm.utility.USMUtil;
import org.egov.usm.validator.SurveyTicketRequestValidator;
import org.egov.usm.web.model.AuditDetails;
import org.egov.usm.web.model.SubmittedAnswer;
import org.egov.usm.web.model.SurveyDetailsRequest;
import org.egov.usm.web.model.SurveyTicket;
import org.egov.usm.web.model.SurveyTicketListRequest;
import org.egov.usm.web.model.SurveyTicketRequest;
import org.egov.usm.web.model.TicketSearchCriteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

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

	/**
	 * Service layer for create Ticket
	 * 
	 * @param surveyDetailsRequest
	 * @return List<SurveyTicket>
	 */
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
	public List<SurveyTicket> updateSurveyTicket(@Valid SurveyTicketListRequest ticketRequests) {
		List<SurveyTicket> surveyListTickets = new ArrayList<>();

		for (SurveyTicket surveyTicket : ticketRequests.getTickets()) {
			RequestInfo requestInfo = ticketRequests.getRequestInfo();

			/* Validate the existing ticket */
			SurveyTicket existingSurveyTickets = surveyTicketRequestValidator
					.validateSurveyTicketExistence(surveyTicket);

			/* Update the Audit Details */
			AuditDetails auditDetails = USMUtil.getAuditDetails(requestInfo.getUserInfo().getUuid(), false);
			auditDetails.setCreatedBy(existingSurveyTickets.getAuditDetails().getCreatedBy());
			auditDetails.setCreatedTime(existingSurveyTickets.getAuditDetails().getCreatedTime());
			surveyTicket.setAuditDetails(auditDetails);

			surveyTicket.setTicketClosedTime(auditDetails.getLastModifiedTime());
			surveyTicket.setQuestionId(existingSurveyTickets.getQuestionId());
			surveyTicket.setSurveyAnswerId(existingSurveyTickets.getSurveyAnswerId());
			surveyTicket.setTicketCreatedTime(existingSurveyTickets.getTicketCreatedTime());
			surveyTicket.setTicketDescription(existingSurveyTickets.getTicketDescription());

			if (!ObjectUtils.isEmpty(surveyTicket.getIsSatisfied())
					&& existingSurveyTickets.getStatus() == TicketStatus.CLOSED) {
				surveyTicket.setStatus(existingSurveyTickets.getStatus());
				surveyTicket.setHasOpenTicket(Boolean.FALSE);

			} else if (ObjectUtils.isEmpty(surveyTicket.getIsSatisfied())
					&& surveyTicket.getStatus() == TicketStatus.CLOSED) {
				surveyTicketRequestValidator.validateSurveyTicketClose(existingSurveyTickets);
				surveyTicket.setHasOpenTicket(Boolean.FALSE);

			} else if (!ObjectUtils.isEmpty(surveyTicket.getStatus())
					&& !ObjectUtils.isEmpty(surveyTicket.getIsSatisfied())) {
				throw new CustomException("EG_INPUT_PARAM_ERR",
						"The reqest parameter in update request does not valid.");

			} else {
				surveyTicket.setHasOpenTicket(Boolean.TRUE);
			}

			surveyTicket.setTenantId(existingSurveyTickets.getTenantId());
			surveyTicket.setWard(existingSurveyTickets.getWard());
			surveyTicket.setSlumCode(existingSurveyTickets.getSlumCode());
			surveyTicket.setQuestionCategory(existingSurveyTickets.getQuestionCategory());
			surveyTicket.setTicketNo(existingSurveyTickets.getTicketNo());

			repository.update(SurveyTicketRequest.builder().ticket(surveyTicket).requestInfo(requestInfo).build());
			surveyListTickets.add(surveyTicket);
		}

		return surveyListTickets;

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
