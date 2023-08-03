package org.egov.usm.web.controller;

import java.util.Collections;

import javax.validation.Valid;

import org.egov.usm.service.TicketCommentService;
import org.egov.usm.utility.ResponseInfoFactory;
import org.egov.usm.web.model.SurveyTicketComment;
import org.egov.usm.web.model.SurveyTicketCommentRequest;
import org.egov.usm.web.model.SurveyTicketCommentResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/comment")
@Slf4j
public class SurveyTicketCommentController {

	private final TicketCommentService ticketCommentService;
	private final ResponseInfoFactory responseInfoFactory;

	@Autowired
	public SurveyTicketCommentController(ResponseInfoFactory responseInfoFactory,
			TicketCommentService ticketCommentService) {
		this.ticketCommentService = ticketCommentService;
		this.responseInfoFactory = responseInfoFactory;

	}

	/**
	 * Creates Survey Request for USM
	 * 
	 * @param surveyRequest
	 * @return SurveyResponse
	 */
	@PostMapping("/_create")
	public ResponseEntity<SurveyTicketCommentResponse> create(
			@Valid @RequestBody SurveyTicketCommentRequest surveyTicketCommentRequest) {
		log.info("In controller request : ", surveyTicketCommentRequest.toString());
		SurveyTicketComment ticketComment = ticketCommentService.create(surveyTicketCommentRequest);
		SurveyTicketCommentResponse response = SurveyTicketCommentResponse.builder()
				.surveyTicketComments(Collections.singletonList(ticketComment)).responseInfo(responseInfoFactory
						.createResponseInfoFromRequestInfo(surveyTicketCommentRequest.getRequestInfo(), true))
				.build();
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

}
