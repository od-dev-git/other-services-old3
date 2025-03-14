package org.egov.dss.repository.rowmapper;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.util.StringUtils;

public class BPAPerformanceRateRowMapper implements ResultSetExtractor <LinkedHashMap<String, Long>> {

	LinkedHashMap<String, Long> ulbPerformanceRate = new LinkedHashMap<>();


    @Override
    public LinkedHashMap<String, Long> extractData(ResultSet rs)
            throws SQLException, DataAccessException {

        while (rs.next()) {

            String key = rs.getString("name");
            Long value = rs.getLong("value");

            if(!ulbPerformanceRate.containsKey(key)) {
            	ulbPerformanceRate.put(key, value);                
            }

        }

        return ulbPerformanceRate;

    }

}