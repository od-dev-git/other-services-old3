package org.egov.usm.repository.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.egov.usm.web.model.AuditDetails;
import org.egov.usm.web.model.USMOfficial;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Component;

@Component
public class USMOfficialRowMapper implements ResultSetExtractor<List<USMOfficial>> {
	@Override
	public List<USMOfficial> extractData(ResultSet rs) throws SQLException, DataAccessException {
		Map<String, USMOfficial> officialMap = new LinkedHashMap<>();

		while (rs.next()) {
			String id = rs.getString("id");
			USMOfficial official = officialMap.get(id);

			if (official == null) {
				Long lastModifiedTime = rs.getLong("lastmodifiedtime");
				if (rs.wasNull()) {
					lastModifiedTime = null;
				}

				AuditDetails auditdetails = AuditDetails.builder().createdBy(rs.getString("createdby"))
						.createdTime(rs.getLong("createdtime")).lastModifiedBy(rs.getString("lastmodifiedby"))
						.lastModifiedTime(lastModifiedTime).build();

				official = USMOfficial.builder().id(rs.getString("id")).role(rs.getString("role"))
						.category(rs.getString("category")).tenantId(rs.getString("tenantid"))
						.ward(rs.getString("ward")).slumcode(rs.getString("slumcode"))
						.assigned(rs.getString("assigned")).auditDetails(auditdetails).build();
			}

			officialMap.put(id, official);
		}
		return new ArrayList<>(officialMap.values());
	}
}
