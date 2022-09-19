package org.egov.dss.repository.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.egov.dss.model.UsageTypeResponse;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

public class CollectionByUsageRowMapper implements ResultSetExtractor<List<UsageTypeResponse>>{

	@Override
	public List<UsageTypeResponse> extractData(ResultSet rs) throws SQLException, DataAccessException {

		List<UsageTypeResponse> responseList = new ArrayList();
		
		while(rs.next()) {
			
			UsageTypeResponse response = new UsageTypeResponse();
			response.setConsumerCode(rs.getString("propertyid"));
			response.setUsageCategory(rs.getString("usagecategory"));
			
			responseList.add(response);			
		}
		
		return responseList;
	}

}
