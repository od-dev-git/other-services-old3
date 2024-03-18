package org.egov.report.repository.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.egov.report.model.DCBArrearDue;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Service;

@Service
public class DCBArrearDueRowMapper implements ResultSetExtractor<Map<String,DCBArrearDue>> {

	@Override
	public Map<String,DCBArrearDue> extractData(ResultSet rs) throws SQLException, DataAccessException {

		Map<String,DCBArrearDue> arrearDueList = new HashMap<>();

		while (rs.next()) {

			DCBArrearDue arrearDue = DCBArrearDue.builder().consumerCode(rs.getString("consumercode"))
					.taxAmount(rs.getBigDecimal("taxamount")).collectionAmount(rs.getBigDecimal("collectionamount"))
					.due(rs.getBigDecimal("due")).build();

			arrearDueList.put(rs.getString("consumercode"),arrearDue);
		}
		return arrearDueList;

	}

}
