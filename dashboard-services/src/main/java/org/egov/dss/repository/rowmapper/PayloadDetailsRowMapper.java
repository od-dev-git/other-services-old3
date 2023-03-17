package org.egov.dss.repository.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.egov.dss.model.PayloadDetails;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

public class PayloadDetailsRowMapper implements ResultSetExtractor<List<PayloadDetails>>{

	@Override
	public List<PayloadDetails> extractData(ResultSet rs) throws SQLException, DataAccessException {

		List<PayloadDetails> responseList = new ArrayList();
		
		while(rs.next()) {
			
			PayloadDetails response = new PayloadDetails();
			//response.setConsumerCode(rs.getString("propertyid"));
			//response.setUsageCategory(rs.getString("usagecategory"));
			response.setId(rs.getString("id"));
			response.setVisualizationcode(rs.getString("visualizationcode"));
			response.setModulelevel(rs.getString("modulelevel"));
			response.setStartdate(rs.getLong("startdate"));
			response.setEnddate(rs.getLong("enddate"));
			response.setTimeinterval(rs.getString("timeinterval"));
			response.setCharttype(rs.getString("charttype"));
			response.setTenantid(rs.getString("tenantid"));
			response.setDistrictid(rs.getString("districtid"));
			response.setCity(rs.getString("city"));
			response.setHeadername(rs.getString("headername"));
			response.setValuetype(rs.getString("valuetype"));
			
			responseList.add(response);			
		}
		
		return responseList;
	}

}
