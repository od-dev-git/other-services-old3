package org.egov.report.repository.rowmapper;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.egov.report.web.model.WaterDemandResponse;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

public class WaterMonthlyDemandsRowMapper implements ResultSetExtractor<Map<String, List<WaterDemandResponse>>>{

    @Override
    public Map<String, List<WaterDemandResponse>>  extractData(ResultSet rs) throws SQLException, DataAccessException {

        Map<String, List<WaterDemandResponse>>  responseMap = new HashMap<>();
        while(rs.next()) {

            //use builder
            String consumerCode = rs.getString("consumercode");

            WaterDemandResponse response = WaterDemandResponse
                    .builder()
                    .taxamount(rs.getBigDecimal("taxamount"))
                    .collectionamount(rs.getBigDecimal("collectionamount"))
                    .taxheadcode(rs.getString("taxheadcode"))
                    .taxperiodfrom(rs.getLong("taxperiodfrom"))
                    .taxperiodto(rs.getLong("taxperiodto"))
                    .tenantid(rs.getString("tenantid"))
                    .demandId(rs.getString("id"))
                    .ward(rs.getString("ward"))
                    .consumercode(rs.getString("consumercode"))
                    .oldconnectionno(rs.getString("oldconnectionno"))
                    .connectiontype(rs.getString("connectiontype"))
                    .userid(rs.getString("userid"))
                    .build();

            if(response != null) {
                if(!responseMap.containsKey(consumerCode))
                    responseMap.put(consumerCode, new ArrayList<>());

                responseMap.get(consumerCode).add(response);                
            }

        }


        return responseMap;
    }
}