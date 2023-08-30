package org.egov.integration.repository.rowmapper;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.egov.integration.web.model.Feedback;
import org.egov.tracer.model.CustomException;
import org.postgresql.util.PGobject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Component
public class FeedbackRowMapper implements ResultSetExtractor<List<Feedback>> {

    @Autowired
    private ObjectMapper mapper;
    @Override
    public List<Feedback> extractData(ResultSet rs) throws SQLException, DataAccessException {

        List<Feedback> feedbacks = new ArrayList<>();
        while ((rs.next())){
            Feedback feedback = Feedback.builder()
                    .id(rs.getString("id"))
                    .tenantId(rs.getString("tenantId"))
                    .module(rs.getString("module"))
                    .rating(rs.getInt("rating"))
                    .comment(rs.getString("comment"))
                    .createdBy(rs.getString("createdBy"))
                    .lastModifiedBy(rs.getString("lastModifiedBy"))
                    .createdTime(rs.getLong("createdTime"))
                    .lastModifiedTime(rs.getLong("lastModifiedTime"))
                    .build();
            PGobject pgObj = (PGobject) rs.getObject("additionaldetails");
            ObjectNode additionalDetails = null;
            if (pgObj != null) {
                try {
                    additionalDetails = mapper.readValue(pgObj.getValue(), ObjectNode.class);
                } catch (IOException ex) {
                    throw new CustomException("PARSING ERROR", "The additionalDetail json cannot be parsed");
                }
            } else {
                additionalDetails = mapper.createObjectNode();
            }
            feedbacks.add(feedback);
        }
        return feedbacks;
    }
}