package org.egov.report.repository.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

public class DataMartRowMapper implements ResultSetExtractor<List<Map<String,Object>>>{

	@Override
	public List<Map<String, Object>> extractData(ResultSet rs) throws SQLException, DataAccessException {
		
		List<Map<String,Object>> dataMartList = new ArrayList<>();
		
		while (rs.next()){
			
			Map<String, Object> dataMartObject = new HashMap<>();
			
			dataMartObject.put("ulb", rs.getString("ulb"));
			dataMartObject.put("applicationid", rs.getString("applicationid"));
			dataMartObject.put("applicationstatus", rs.getString("applicationstatus"));
			dataMartObject.put("nooftrips", rs.getInt("nooftrips"));
			dataMartObject.put("propertytype", rs.getString("propertytype"));
			dataMartObject.put("propertysubtype", rs.getString("propertysubtype"));
			dataMartObject.put("onsitesanitationtype", rs.getString("onsitesanitationtype"));
			dataMartObject.put("doornumber", rs.getString("doornumber"));
			dataMartObject.put("streetname", rs.getString("streetname"));
			dataMartObject.put("city", rs.getString("city"));
			dataMartObject.put("pincode", rs.getString("pincode"));
			dataMartObject.put("urbanruralflag", rs.getString("urbanruralflag"));
			dataMartObject.put("locality", rs.getString("locality"));
			dataMartObject.put("gp", rs.getString("gp"));
			dataMartObject.put("village", rs.getString("village"));
			dataMartObject.put("district", rs.getString("district"));
			dataMartObject.put("state", rs.getString("state"));
			dataMartObject.put("slumname", rs.getString("slumname"));
			dataMartObject.put("applicationsource", rs.getString("applicationsource"));
			dataMartObject.put("desludgingentity", rs.getString("desludgingentity"));
			dataMartObject.put("longitude", rs.getString("longitude"));
			dataMartObject.put("latitude", rs.getString("latitude"));
			dataMartObject.put("geolocationprovided", rs.getString("geolocationprovided"));
			dataMartObject.put("desludgingvehiclenumber", rs.getString("desludgingvehiclenumber"));
			dataMartObject.put("vehicletype", rs.getString("vehicletype"));
			dataMartObject.put("vehiclecapacity", rs.getString("vehiclecapacity"));
			dataMartObject.put("tripamount", rs.getString("tripamount"));
			dataMartObject.put("advanceamount", rs.getString("advanceamount"));
			dataMartObject.put("paymentamount", rs.getString("paymentamount"));
			dataMartObject.put("paymentsource", rs.getString("paymentsource"));
			dataMartObject.put("paymentinstrumenttype", rs.getString("paymentinstrumenttype"));
			dataMartObject.put("applicationsumbitdate", rs.getString("applicationsumbitdate"));
			
			dataMartList.add(dataMartObject);
			
		}
		
		return dataMartList;
	}

}
