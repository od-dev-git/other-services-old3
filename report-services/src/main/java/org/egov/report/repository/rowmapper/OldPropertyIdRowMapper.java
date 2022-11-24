package org.egov.report.repository.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.egov.report.web.model.PropertyDemandResponse;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.util.StringUtils;

public class OldPropertyIdRowMapper implements ResultSetExtractor<HashMap<String, String>> {

    HashMap<String, String> oldPropertyIdMap = new HashMap<String, String>();

    @Override
    public HashMap<String, String> extractData(ResultSet rs)
            throws SQLException, DataAccessException {

        while (rs.next()) {

            String key = rs.getString("propertyid");
            String value = rs.getString("oldpropertyid");

            if(!oldPropertyIdMap.containsKey(key)) {
                oldPropertyIdMap.put(key, value);                
            }
        }

        return oldPropertyIdMap;

    }

}
