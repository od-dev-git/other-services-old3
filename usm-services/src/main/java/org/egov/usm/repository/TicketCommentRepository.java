package org.egov.usm.repository;

import javax.validation.Valid;

import org.egov.usm.config.USMConfiguration;
import org.egov.usm.producer.Producer;
import org.egov.usm.web.model.SurveyTicketCommentRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Repository
public class TicketCommentRepository {

	private USMConfiguration config;

	private Producer producer;

	@Autowired
	public TicketCommentRepository(USMConfiguration config, Producer producer) {

		this.config = config;
		this.producer = producer;
	}

	/**
	 * Pushes the survey Ticket comment request to save topic
	 *
	 * @param SurveyTicketCommentRequest
	 */

	public void save(@Valid SurveyTicketCommentRequest surveyTicketCommentRequest) {
		log.info("Save request :", surveyTicketCommentRequest.toString());
		producer.push(config.getSaveCommentTicketTopic(), surveyTicketCommentRequest);

	}

}
