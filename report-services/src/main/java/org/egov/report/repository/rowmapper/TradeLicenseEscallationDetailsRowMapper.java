package org.egov.report.repository.rowmapper;

import org.egov.report.web.model.TradeLicenseEscallationDetailsResponse;
import org.springframework.jdbc.core.ResultSetExtractor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TradeLicenseEscallationDetailsRowMapper implements ResultSetExtractor<List<TradeLicenseEscallationDetailsResponse>> {

    @Override
    public List<TradeLicenseEscallationDetailsResponse> extractData(ResultSet rs) throws SQLException {
        List<TradeLicenseEscallationDetailsResponse> responseList = new ArrayList<>();
        
        while (rs.next()) {
            TradeLicenseEscallationDetailsResponse response = new TradeLicenseEscallationDetailsResponse();
            
            response.setTenantId(rs.getString("tenantid"));
            response.setLicenseType(rs.getString("licensetype"));
            response.setApplicationType(rs.getString("applicationtype"));
            response.setApplicationNumber(rs.getString("businessid"));
            response.setDateOfPayment(rs.getString("submissiondate"));
          //  response.setDaysSinceApplicationSubmission(rs.getLong("daysSinceApplicationSubmission"));
            response.setStatus(rs.getString("status"));
            response.setComment(rs.getString("comment"));
            response.setEscalatedFrom(rs.getString("createdby"));
            response.setEscalatedTo(rs.getString("assignee"));
            response.setAutoEscallationDate(rs.getString("escallationdate"));

            responseList.add(response);
        }
        
        return responseList;
    }
}