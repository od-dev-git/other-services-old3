package org.egov.usm.config;

import org.egov.tracer.config.TracerConfiguration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Import({ TracerConfiguration.class })
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Component
public class USMConfiguration {

	// Idgen Config
	@Value("${egov.idgen.host}")
	private String idGenHost;

	@Value("${egov.idgen.path}")
	private String idGenPath;

	@Value("${egov.idgen.usm.surveyno.name}")
	private String surveyNoIdgenName;

	@Value("${egov.idgen.usm.surveyno.format}")
	private String surveyNoIdgenFormat;

	@Value("${egov.idgen.usm.ticketNo.name}")
	private String ticketNoIdgenName;

	@Value("${egov.idgen.usm.ticketNo.format}")
	private String ticketNoIdgenFormat;

	// User Config
	@Value("${egov.user.host}")
	private String userHost;

	@Value("${egov.user.context.path}")
	private String userContextPath;

	@Value("${egov.user.create.path}")
	private String userCreateEndpoint;

	@Value("${egov.user.search.path}")
	private String userSearchEndpoint;

	// Persister Config
	@Value("${persister.create.survey.topic}")
	private String createSurveyTopic;

	@Value("${persister.update.survey.topic}")
	private String updateSurveyTopic;

	@Value("${persister.delete.survey.topic}")
	private String deleteSurveyTopic;

	@Value("${persister.save.submit.survey.topic}")
	private String saveSubmitSurveyTopic;

	@Value("${persister.update.submit.survey.topic}")
	private String updateSubmitSurveyTopic;

	@Value("${persister.create.ticket.topic}")
	private String createTicketTopic;

	@Value("${persister.update.ticket.topic}")
	private String updateTicketTopic;

	@Value("${persister.create.lookup.topic}")
	private String createQuestionLookupTopic;

	@Value("${persister.update.lookup.topic}")
	private String updateQuestionLookupTopic;

	@Value("${persister.save.ticket.comment.topic}")
	private String saveCommentTicketTopic;

	@Value("${persister.create.member.topic}")
	private String createMemberTopic;

	@Value("${persister.update.member.topic}")
	private String updateMemberTopic;

}
