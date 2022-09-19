package org.egov.dss.repository.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.egov.dss.model.CollectionByUsageResponse;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

public class CollectionByUsageRowMapper implements ResultSetExtractor<List<CollectionByUsageResponse>>{

	@Override
	public List<CollectionByUsageResponse> extractData(ResultSet rs) throws SQLException, DataAccessException {

		List<CollectionByUsageResponse> responseList = new ArrayList();
		
		while(rs.next()) {
			
			CollectionByUsageResponse response = new CollectionByUsageResponse();
			response.setAmount(rs.getBigDecimal("amount"));
			response.setUsageCategory(rs.getString("usagecategory"));
			
			responseList.add(response);			
		}
		
		return responseList;
	}

}
