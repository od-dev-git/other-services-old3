package org.egov.dss.repository.rowmapper;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

public class TenantWiseConnectionsRowMapper implements ResultSetExtractor<HashMap<String, BigDecimal>>{
	
	@Override
	public HashMap<String, BigDecimal> extractData(ResultSet rs) throws SQLException, DataAccessException {
		
		HashMap<String, BigDecimal> responseMap = new HashMap<>();
		
		while(rs.next()) {
			String tenantid = rs.getString("tenantid");
			BigDecimal totalCollection = rs.getBigDecimal("connections");
			responseMap.put(tenantid, totalCollection);
		}
		return responseMap;
	}

}
