package org.egov.report.repository.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

import org.egov.report.web.model.PropertyDetailsResponse;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

public class OldPropertyIdRowMapper implements ResultSetExtractor<HashMap<String, PropertyDetailsResponse>> {

    HashMap<String, PropertyDetailsResponse> responseMap = new HashMap<String, PropertyDetailsResponse>();

    @Override
    public HashMap<String, PropertyDetailsResponse> extractData(ResultSet rs)
            throws SQLException, DataAccessException {

        while (rs.next()) {

            String propertyid = rs.getString("propertyid");
            String oldpropertyid = rs.getString("oldpropertyid");
            String ddnno = rs.getString("ddnno");
            String legacyholdingno= rs.getString("legacyholdingno");
            PropertyDetailsResponse propertyDetailsResponse= PropertyDetailsResponse
                    .builder()
                    .oldPropertyId(oldpropertyid)
                    .propertyId(propertyid)
                    .ddnNo(ddnno)
                    .legacyHoldingNo(legacyholdingno).build();
            if(!this.responseMap.containsKey(propertyid)) {

                this.responseMap.put(propertyid, propertyDetailsResponse);
            }
        }

        return this.responseMap;

    }

}
