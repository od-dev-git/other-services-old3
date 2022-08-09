package org.egov.report.repository.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import org.egov.report.web.model.BillSummaryQueryResponse;
import org.egov.report.web.model.User;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Component;


public class BillSummaryRowMapper implements ResultSetExtractor<List<BillSummaryQueryResponse>>{
	
	List<BillSummaryQueryResponse> bsrList = new ArrayList<>();
	


	@Override
	public List<BillSummaryQueryResponse> extractData(ResultSet rs) throws SQLException, DataAccessException {
		
		while(rs.next()) {
			
			BillSummaryQueryResponse bsr = new BillSummaryQueryResponse();
			
			bsr.setMonthYear(rs.getString("monthYear"));
			bsr.setTenantId(rs.getString("ulb"));
			
			bsrList.add(bsr);
			
		}
		
		return bsrList;

}
}