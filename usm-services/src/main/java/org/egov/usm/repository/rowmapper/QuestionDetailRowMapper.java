package org.egov.usm.repository.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.egov.usm.model.enums.Status;
import org.egov.usm.web.model.AuditDetails;
import org.egov.usm.web.model.QuestionDetail;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class QuestionDetailRowMapper implements ResultSetExtractor<List<QuestionDetail>>{
	
	@Override
	public List<QuestionDetail> extractData(ResultSet rs) throws SQLException, DataAccessException {
		Map<String,QuestionDetail> questionMap = new LinkedHashMap<>();

        while (rs.next()){
            String id = rs.getString("id");
            QuestionDetail question = questionMap.get(id);
            
            if(question == null) {
                AuditDetails auditdetails = AuditDetails.builder()
                        .createdBy(rs.getString("createdby"))
                        .createdTime(rs.getLong("createdtime"))
                        .lastModifiedBy(rs.getString("lastmodifiedby"))
                        .lastModifiedTime(rs.getLong("lastmodifiedtime"))
                        .build();

                question =  QuestionDetail.builder()
                        .id(rs.getString("id"))
                        .surveyId(rs.getString("surveyid"))
                        .questionStatement(rs.getString("questionstatement"))
                        .category(rs.getString("category"))
                        .status(Status.fromValue(rs.getString("status")))
                        .required(rs.getBoolean("required"))
                        .type(rs.getString("type"))
                        .questionOrder(rs.getInt("questionorder"))
                        .options(rs.getString("options"))
                        .auditDetails(auditdetails)
                        .build();
            }
            log.info("Question : ", question);
            questionMap.put(id, question);
        }
        return new ArrayList<>(questionMap.values());
	}

}
