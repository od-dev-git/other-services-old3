package org.egov.report.repository.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.egov.report.model.DCBDemand;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

public class DCBDemandRowMapper implements ResultSetExtractor<Map<String,DCBDemand>> {

	@Override
	public Map<String,DCBDemand> extractData(ResultSet rs) throws SQLException, DataAccessException {

		Map<String,DCBDemand> demands = new HashMap<>();

		while (rs.next()) {

			DCBDemand demand = DCBDemand.builder().consumerCode(rs.getString("consumercode"))
					.taxAmount(rs.getBigDecimal("taxamount")).collectionAmount(rs.getBigDecimal("collectionamount"))
					.build();

			demands.put(rs.getString("consumercode"),demand);
		}

		return demands;

	}

}
