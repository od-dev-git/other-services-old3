package org.egov.usm.repository.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.egov.usm.web.model.AuditDetails;
import org.egov.usm.web.model.Survey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class SurveyRowMapper implements ResultSetExtractor<List<Survey>> {

	@Autowired
	private ObjectMapper mapper;
	
	@Override
	public List<Survey> extractData(ResultSet rs) throws SQLException, DataAccessException {
		Map<String, Survey> surveyMap = new HashMap<>();
		while (rs.next()) {
			String currentId = rs.getString("id");
			log.info("mapper receive id: " + currentId);
			Survey currentSurvey = surveyMap.get(currentId);
			
			if (currentSurvey == null) {
				AuditDetails auditDetails = AuditDetails.builder().createdBy(rs.getString("createdby"))
						.createdTime(rs.getLong("createdtime")).lastModifiedBy(rs.getString("lastmodifiedby"))
						.lastModifiedTime(rs.getLong("lastModifiedTime")).build();
				
				currentSurvey = Survey.builder().id(rs.getString("id"))
						.tenantId(rs.getString("tenantid")).build();
				
				surveyMap.put(currentId, currentSurvey);
			}

		}
		return new ArrayList<>(surveyMap.values());
	}

}
