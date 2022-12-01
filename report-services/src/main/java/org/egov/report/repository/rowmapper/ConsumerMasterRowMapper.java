package org.egov.report.repository.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.egov.report.web.model.ConsumerMasterWSReportResponse;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

public class ConsumerMasterRowMapper implements ResultSetExtractor<List<ConsumerMasterWSReportResponse>>{

	@Override
	public List<ConsumerMasterWSReportResponse> extractData(ResultSet rs) throws SQLException, DataAccessException {
		
		List<ConsumerMasterWSReportResponse> responseList = new ArrayList<>();
		
		while(rs.next())
		{
			ConsumerMasterWSReportResponse response = ConsumerMasterWSReportResponse
			        .builder()
			        .connectionCategory(rs.getString("connectioncategory"))
			        .connectionFacility(rs.getString("connectionfacility"))
			        .ulb((rs.getString("tenantid")).substring(3))
			        .wardNo(rs.getString("ward"))
			        .connectionNo(rs.getString("connectionno"))
			        .oldConnectionNo(rs.getString("oldconnectionno"))
			        .connectionType(rs.getString("connectiontype"))
			        .usageCategory(rs.getString("usagecategory"))
			        .userId(rs.getString("uuid"))
			        .build();
			
			responseList.add(response);
		}
		
		return responseList;
	}

	
	
}
