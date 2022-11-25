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

			String consumerCode = rs.getString("connectionno");
			
			WaterConnectionDetails response = WaterConnectionDetails
			        .builder()
			        .tenantid(consumerCode)
			        .userid(rs.getString("userid"))
			        .ward(rs.getString("ward"))
			        .connectiontype(rs.getString("connectiontype"))
			        .oldconnectionno(rs.getString("oldconnectionno"))
			        .build();			

			if(!responseMap.containsKey(consumerCode)) {
	            responseMap.put(consumerCode, response);
			}
		}

		return responseMap;
	}

}
