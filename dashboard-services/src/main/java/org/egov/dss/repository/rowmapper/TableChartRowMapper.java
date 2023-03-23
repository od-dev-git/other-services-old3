package org.egov.dss.repository.rowmapper;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

public class TableChartRowMapper implements ResultSetExtractor<List<HashMap<String, Object>>> {

	@Override
	public List<HashMap<String, Object>> extractData(ResultSet rs) throws SQLException, DataAccessException {

		ResultSetMetaData metaData = rs.getMetaData();
		int columnCount = metaData.getColumnCount();

		List<HashMap<String, Object>> responseList = new ArrayList<>();

		while (rs.next()) {
			HashMap<String, Object> responseMap = new HashMap<>();

			for (int i = 1; i <= columnCount; i++) {
				responseMap.put(metaData.getColumnLabel(i), rs.getObject(i));
			}

			responseList.add(responseMap);
		}

		return responseList;
	}

}
