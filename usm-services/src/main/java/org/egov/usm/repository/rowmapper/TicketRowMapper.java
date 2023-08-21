package org.egov.usm.repository.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.egov.usm.model.enums.TicketStatus;
import org.egov.usm.web.model.AuditDetails;
import org.egov.usm.web.model.SurveyTicket;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Component;

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

						.status(TicketStatus.fromValue(rs.getString("status")))
						.ticketCreatedTime(rs.getLong("ticketcreatedtime"))
						.ticketClosedTime(rs.getLong("ticketclosedtime")).unAttended(rs.getBoolean("unattended"))
						.ward(rs.getString("ward")).slumCode(rs.getString("slumcode"))
						.questionCategory(rs.getString("questioncategory")).auditDetails(auditdetails).build();
			}

			surveyMap.put(id, ticket);
		}
		return new ArrayList<>(surveyMap.values());
	}
}
