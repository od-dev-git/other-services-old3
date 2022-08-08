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
			ConsumerMasterWSReportResponse response = new ConsumerMasterWSReportResponse();
		
			response.setConnectionCategory(rs.getString("connectioncategory"));
			response.setConnectionFacility(rs.getString("connectionfacility"));
			String tenantId = rs.getString("tenantid");
			response.setUlb(tenantId.substring(3));
			response.setWardNo(rs.getString("ward"));
			response.setConnectionNo(rs.getString("connectionno"));
			response.setOldConnectionNo(rs.getString("oldconnectionno"));
			response.setConnectionType(rs.getString("connectiontype"));
			response.setUsageCategory(rs.getString("usagecategory"));
			response.setUserId(rs.getLong("id"));
			
			responseList.add(response);
		}
		
		return responseList;
	}

	
	
}
