package org.egov.report.repository.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.egov.report.util.WSReportUtils;
import org.egov.report.web.model.WaterNewConsumerMonthlyResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

public class WaterNewConsumerMonthlyRowMapper implements ResultSetExtractor<List<WaterNewConsumerMonthlyResponse>>{

	WSReportUtils wsReportUtils = new WSReportUtils();
	
	@Override
	public List<WaterNewConsumerMonthlyResponse> extractData(ResultSet rs) throws SQLException, DataAccessException {
		
		List<WaterNewConsumerMonthlyResponse> responseList = new ArrayList<>();
		
		while(rs.next()) {
			
			WaterNewConsumerMonthlyResponse response = new WaterNewConsumerMonthlyResponse();
			
			response.setConnectionCategory(rs.getString("connectioncategory"));
			response.setConnectionFacility(rs.getString("connectionfacility"));
			String tenantId = rs.getString("tenantid");
			response.setUlb(tenantId.substring(3));
			response.setExecutionDate(wsReportUtils.getConvertedDate(rs.getLong("connectionexecutiondate")));
			response.setSanctionDate(wsReportUtils.getConvertedDate(rs.getLong("estimationletterdate")));
			response.setWard(rs.getString("ward"));
			response.setConnectionNo(rs.getString("connectionno"));
			response.setConnectionType(rs.getString("connectiontype"));
			response.setUserId(rs.getString("userid"));
			response.setConnectionPurpose(rs.getString("usagecategory"));
			response.setApplicationNo(rs.getString("applicationno"));
		
			
			
			responseList.add(response);
		}
		
		return responseList;
	}

	
}
