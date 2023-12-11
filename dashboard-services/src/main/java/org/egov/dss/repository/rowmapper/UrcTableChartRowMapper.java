package org.egov.dss.repository.rowmapper;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

public class UrcTableChartRowMapper implements ResultSetExtractor<List<LinkedHashMap<String, Object>>> {

	@Override
	public List<LinkedHashMap<String, Object>> extractData(ResultSet rs) throws SQLException, DataAccessException {

		ResultSetMetaData metaData = rs.getMetaData();
		int columnCount = metaData.getColumnCount();

		List<LinkedHashMap<String, Object>> responseList = new ArrayList<>();

		while (rs.next()) {
			LinkedHashMap<String, Object> responseMap = new LinkedHashMap<>();

			for (int i = 1; i <= columnCount; i++) {
				responseMap.put(metaData.getColumnLabel(i), rs.getObject(i));
			}

			responseList.add(responseMap);
		}

		return responseList;
	}

}
