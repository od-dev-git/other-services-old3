package org.egov.report.repository.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.egov.report.web.model.MiscellaneousWaterDetails;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

public class MiscellaneousWaterDetailsRowMapper implements ResultSetExtractor<Map<String, MiscellaneousWaterDetails>>{
    


    @Override
    public Map<String, MiscellaneousWaterDetails> extractData(ResultSet rs) throws SQLException, DataAccessException {

        Map<String,MiscellaneousWaterDetails> responseMap = new HashMap<>();

        while(rs.next()) {

            String consumerCode = rs.getString("connectionno");
            
            MiscellaneousWaterDetails response = MiscellaneousWaterDetails
                    .builder()
                    .ward(rs.getString("ward"))
                    .oldconnectionno(rs.getString("oldconnectionno"))
                    .build();           

            if(!responseMap.containsKey(consumerCode)) {
                responseMap.put(consumerCode, response);
            }
        }

        return responseMap;
    }



}
