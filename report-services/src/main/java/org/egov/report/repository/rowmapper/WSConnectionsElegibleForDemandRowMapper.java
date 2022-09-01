package org.egov.report.repository.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.egov.report.web.model.ULBWiseWaterConnectionDetails;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

public class WSConnectionsElegibleForDemandRowMapper implements ResultSetExtractor<List<ULBWiseWaterConnectionDetails>>{

	List<ULBWiseWaterConnectionDetails> wsConnectionsElegibleForDemand = new ArrayList<>();

	@Override
	public List<ULBWiseWaterConnectionDetails> extractData(ResultSet rs) throws SQLException, DataAccessException {

		while(rs.next()) {

			ULBWiseWaterConnectionDetails noOfWsConnections = new ULBWiseWaterConnectionDetails();

			String tenantId = (rs.getString("tenantid")).replace("od.", "");
			tenantId = tenantId.substring(0,1).toUpperCase() + tenantId.substring(1).toLowerCase();
			noOfWsConnections.setTenantid(tenantId);

			noOfWsConnections.setWard(rs.getString("ward"));

		    noOfWsConnections.setNumberOfConnections(rs.getString("connectionscount"));

			wsConnectionsElegibleForDemand.add(noOfWsConnections);

		}

		return wsConnectionsElegibleForDemand;

}

}