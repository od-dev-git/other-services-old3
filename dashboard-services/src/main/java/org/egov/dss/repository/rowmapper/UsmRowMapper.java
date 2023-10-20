package org.egov.dss.repository.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.egov.dss.model.Chart;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

public class UsmRowMapper implements ResultSetExtractor<List<Chart>> {

	List<Chart> chartResponseList = new ArrayList<>();

	@Override
	public List<Chart> extractData(ResultSet rs) throws SQLException, DataAccessException {

		while (rs.next()) {

			Chart chartResponse = Chart.builder().name(rs.getString("name")).value(rs.getBigDecimal("value")).build();

			chartResponseList.add(chartResponse);

		}

		return chartResponseList;

	}

}
