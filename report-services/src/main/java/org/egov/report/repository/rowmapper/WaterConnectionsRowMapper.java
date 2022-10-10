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
import org.springframework.jdbc.core.RowCallbackHandler;

public class WaterConnectionsRowMapper implements ResultSetExtractor <List<HashMap<String, String>>> {

    @Override
    public List<HashMap<String , String>>  extractData(ResultSet rs) throws SQLException, DataAccessException  {


        List<HashMap<String , String>> responseMap = new ArrayList<>();
        while(rs.next()) {

            HashMap<String , String> response = new HashMap<>();
            String connectionno = rs.getString("connectionno");
            String userId = rs.getString("userid");

            response.put(connectionno, userId);
            responseMap.add(response);
        }

        return responseMap;
    }
    

}