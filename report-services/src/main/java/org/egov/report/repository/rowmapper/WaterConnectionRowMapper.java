package org.egov.report.repository.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.egov.report.web.model.WaterConnectionDetails;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

public class WaterConnectionRowMapper implements ResultSetExtractor<Map<String, WaterConnectionDetails>> {

	@Override
	public Map<String, WaterConnectionDetails> extractData(ResultSet rs) throws SQLException, DataAccessException {

		Map<String,WaterConnectionDetails> responseMap = new HashMap<>();

		while(rs.next()) {

			WaterConnectionDetails response = new WaterConnectionDetails();

			String consumerCode = rs.getString("connectionno");
			response.setTenantid(rs.getString("tenantid"));
			response.setUserid(rs.getString("userid"));
			response.setWard(rs.getString("ward"));
			response.setConnectiontype(rs.getString("connectiontype"));
			response.setOldconnectionno(rs.getString("oldconnectionno"));


			responseMap.put(consumerCode, response);

		}

		return responseMap;
	}

}
