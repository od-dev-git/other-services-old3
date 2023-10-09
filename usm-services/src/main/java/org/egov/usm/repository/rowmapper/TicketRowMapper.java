package org.egov.usm.repository.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.egov.usm.model.enums.TicketStatus;
import org.egov.usm.web.model.AuditDetails;
import org.egov.usm.web.model.SurveyTicket;
import org.egov.usm.web.model.SurveyTicketComment;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

@Component
public class TicketRowMapper implements ResultSetExtractor<List<SurveyTicket>> {

	@Override
	public List<SurveyTicket> extractData(ResultSet rs) throws SQLException, DataAccessException {
		Map<String, SurveyTicket> surveyMap = new LinkedHashMap<>();

		while (rs.next()) {
			String id = rs.getString("id");
			SurveyTicket ticket = surveyMap.get(id);

			if (ticket == null) {
				Long lastModifiedTime = rs.getLong("lastmodifiedtime");
				if (rs.wasNull()) {
					lastModifiedTime = null;
				}

				AuditDetails auditdetails = AuditDetails.builder().createdBy(rs.getString("createdby"))
						.createdTime(rs.getLong("createdtime")).lastModifiedBy(rs.getString("lastmodifiedby"))
						.lastModifiedTime(lastModifiedTime).build();

				ticket = SurveyTicket.builder().id(rs.getString("id")).tenantId(rs.getString("tenantid"))
						.ticketNo(rs.getString("ticketno")).surveyAnswerId(rs.getString("surveyanswerid"))
						.questionId(rs.getString("questionid")).ticketDescription(rs.getString("ticketdescription"))
						.ticketDescriptionOdia(rs.getString("questionstatement_odia"))
						.status(TicketStatus.fromValue(rs.getString("status")))
						.ticketCreatedTime(rs.getLong("ticketcreatedtime"))
						.ticketClosedTime(rs.getLong("ticketclosedtime")).ward(rs.getString("ward"))
						.slumCode(rs.getString("slumcode")).questionCategory(rs.getString("questioncategory"))
						.auditDetails(auditdetails).build();

				if (!ObjectUtils.isEmpty(rs.getObject("issatisfied"))) {
					ticket.setIsSatisfied(rs.getBoolean("issatisfied"));
				}

				if (!ObjectUtils.isEmpty(rs.getObject("unattended"))) {
					ticket.setUnAttended(rs.getBoolean("unattended"));
				}
			}
			addCommentToTicket(rs, ticket);

			List<SurveyTicketComment> surveyTicketComments = ticket.getSurveyTicketComments();
			if (!CollectionUtils.isEmpty(surveyTicketComments)) {
				Collections.sort(ticket.getSurveyTicketComments(), Comparator.comparing(SurveyTicketComment::getId));
			}

			surveyMap.put(id, ticket);
		}
		return new ArrayList<>(surveyMap.values());
	}

	private void addCommentToTicket(ResultSet rs, SurveyTicket ticket) throws SQLException {
		String commentId = rs.getString("commentid");
		String ticketId = rs.getString("ticketid");

		if (commentId == null || ticketId == null) {
			// ticket.addTicketComment(null);
			return;
		}

		List<SurveyTicketComment> surveyTicketComments = ticket.getSurveyTicketComments();

		if (!CollectionUtils.isEmpty(surveyTicketComments))
			for (SurveyTicketComment comment : surveyTicketComments) {
				if (comment.getId().equals(commentId))
					return;
			}

		AuditDetails auditdetails = AuditDetails.builder().createdBy(rs.getString("createdby"))
				.createdTime(rs.getLong("createdtime")).lastModifiedBy(rs.getString("lastmodifiedby"))
				.lastModifiedTime(rs.getLong("lastmodifiedtime")).build();

		SurveyTicketComment surveyTicketComment = SurveyTicketComment.builder().id(commentId).ticketId(ticketId)
				.comment(rs.getString("comment")).auditDetails(auditdetails).build();

		ticket.addTicketComment(surveyTicketComment);

	}
}
