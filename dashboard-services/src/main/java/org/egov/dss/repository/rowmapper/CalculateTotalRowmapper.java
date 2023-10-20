package org.egov.dss.repository.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

public class CalculateTotalRowmapper implements ResultSetExtractor<HashMap<String, Long>> {

	HashMap<String, Long> ulbPerformanceRate = new HashMap<>();

	@Override
	public HashMap<String, Long> extractData(ResultSet rs) throws SQLException, DataAccessException {

		while (rs.next()) {

			String key = rs.getString("tenantid");
			Long value = rs.getLong("totalamt");

			if (!ulbPerformanceRate.containsKey(key)) {
				ulbPerformanceRate.put(key, value);
			}

		}

		return ulbPerformanceRate;

	}

}
