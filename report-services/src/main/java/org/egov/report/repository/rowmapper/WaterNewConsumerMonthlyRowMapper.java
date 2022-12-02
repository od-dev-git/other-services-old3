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
			
			WaterNewConsumerMonthlyResponse response = WaterNewConsumerMonthlyResponse
			        .builder()
			        .connectionCategory(rs.getString("connectioncategory"))
			        .connectionFacility(rs.getString("connectionfacility"))
			        .ulb((rs.getString("tenantid")).substring(3))
			        .executionDate(wsReportUtils.getConvertedDate(rs.getLong("connectionexecutiondate")))
			        .sanctionDate(wsReportUtils.getConvertedDate(rs.getLong("estimationletterdate")))
			        .ward(rs.getString("ward"))
			        .connectionNo(rs.getString("connectionno"))
			        .connectionType(rs.getString("connectiontype"))
			        .userId(rs.getString("userid"))
			        .connectionPurpose(rs.getString("usagecategory"))
			        .applicationNo(rs.getString("applicationno"))
			        .build();
			
			responseList.add(response);
		}
		
		return responseList;
	}

	
}
