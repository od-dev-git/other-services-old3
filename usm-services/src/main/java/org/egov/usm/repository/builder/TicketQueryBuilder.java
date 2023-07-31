package org.egov.usm.repository.builder;

import java.util.List;

import org.egov.usm.web.model.SurveyDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

@Component
public class TicketQueryBuilder {

	public String searchQuestionsInTicket(SurveyDetails surveyDetails, List<Object> preparedStmtList) {
		StringBuilder query = new StringBuilder("SELECT ticket.questionid FROM eg_usm_survey_ticket ticket");
		query.append(" LEFT OUTER JOIN eg_usm_survey_submitted_answer answer ON ticket.surveyanswerid = answer.id");
		if (!ObjectUtils.isEmpty(surveyDetails.getId())) {
			query.append(" WHERE answer.surveysubmittedid = ?");
			preparedStmtList.add(surveyDetails.getId());
		}

		return query.toString();
	}

}
