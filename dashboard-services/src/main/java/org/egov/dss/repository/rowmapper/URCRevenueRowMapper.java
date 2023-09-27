package org.egov.dss.repository.rowmapper;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedHashMap;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

public class URCRevenueRowMapper implements ResultSetExtractor<HashMap<String, BigDecimal>>{

	@Override
	public LinkedHashMap<String, BigDecimal> extractData(ResultSet rs) throws SQLException, DataAccessException {
		
		LinkedHashMap<String, BigDecimal> responseMap = new LinkedHashMap<>();
		
		while(rs.next()) {
			String name = rs.getString("name");
			BigDecimal value = rs.getBigDecimal("value");
			responseMap.put(name, value);
		}
		return responseMap;
	}

}
