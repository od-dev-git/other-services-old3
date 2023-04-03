package org.egov.dss.repository.rowmapper;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.util.StringUtils;

public class RateRowMapper implements ResultSetExtractor <LinkedHashMap<String, BigDecimal>> {

	LinkedHashMap<String, BigDecimal> ulbPerformanceRate = new LinkedHashMap<>();


    @Override
    public LinkedHashMap<String, BigDecimal> extractData(ResultSet rs)
            throws SQLException, DataAccessException {

        while (rs.next()) {

            String key = rs.getString("name");
            BigDecimal value = rs.getBigDecimal("value");

            if(!ulbPerformanceRate.containsKey(key)) {
            	ulbPerformanceRate.put(key, value);                
            }

        }

        return ulbPerformanceRate;

    }

}