package org.egov.report.repository.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.egov.report.web.model.PropertyDetailsResponse;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PropertyDetailsRowMapper implements ResultSetExtractor<List<PropertyDetailsResponse>>{

List<PropertyDetailsResponse> pdrList = new ArrayList<>();



	@Override
	public List<PropertyDetailsResponse> extractData(ResultSet rs) throws SQLException, DataAccessException {

		while(rs.next()) {

			PropertyDetailsResponse pdr = new PropertyDetailsResponse();

			String ti = rs.getString("tenantid");
			String tiNew = ti.replace("od.", "");
			tiNew = tiNew.substring(0,1).toUpperCase() + tiNew.substring(1).toLowerCase();
			pdr.setUlbName(tiNew);

			pdr.setWardNumber(rs.getString("ward"));

			pdr.setOldPropertyId(rs.getString("oldPropertyId"));

			pdr.setPropertyId(rs.getString("propertyId"));

			pdr.setUuid(rs.getString("uuid"));

			String doorNo = rs.getString("doorNo");
			pdr.setDoorNo(rs.getString("doorNo"));

			String buildingName = rs.getString("buildingName");
			pdr.setBuildingName(rs.getString("buildingName"));

			String street = rs.getString("street");
			pdr.setStreet(rs.getString("street"));

			String city = rs.getString("city"); 
			pdr.setCity(rs.getString("city"));

			String pinCode = rs.getString("pincode");
			pdr.setPincode(rs.getString("pincode"));

			StringBuilder address = new StringBuilder();
			if(doorNo != null) {
				address.append(doorNo +", ");
	     }
			if(buildingName != null) {
				address.append(buildingName +", ");
		     }
			if(street != null) {
				address.append(street+", ");
		     }
			if(city != null) {
				address.append(city+", ");
		     }
			if(pinCode != null) {
				address.append(pinCode+", ");
		     }

			if(address.length() != 0) {
			    address = address.delete(address.length()-2, address.length());   
			}
			
			pdr.setAddress(address.toString());

			pdrList.add(pdr);

		}

		return pdrList;

}

}