package org.egov.report.repository.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.egov.report.web.model.PropertyDetailsResponse;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.util.StringUtils;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PropertyDetailsRowMapper implements ResultSetExtractor<List<PropertyDetailsResponse>>{

List<PropertyDetailsResponse> propertyDetailsResponseList = new ArrayList<>();



	@Override
	public List<PropertyDetailsResponse> extractData(ResultSet rs) throws SQLException, DataAccessException {

		while(rs.next()) {

			String tenantId = rs.getString("tenantid");
			String tenantIdStyled = tenantId.replace("od.", "");
			tenantIdStyled = tenantIdStyled.substring(0,1).toUpperCase() + tenantIdStyled.substring(1).toLowerCase();
			
	         PropertyDetailsResponse propertyDetailsResponse = PropertyDetailsResponse.builder()
	                 .ulbName(tenantIdStyled)
	                 .wardNumber(rs.getString("ward"))
	                 .oldPropertyId(rs.getString("oldPropertyId"))
	                 .propertyId(rs.getString("propertyId"))
	                 .uuid(rs.getString("uuid"))
	                 .doorNo(rs.getString("doorNo"))
	                 .buildingName(rs.getString("buildingName"))
	                 .street(rs.getString("street"))
	                 .city(rs.getString("city"))
	                 .pincode(rs.getString("pincode"))
	                 .build();

			StringBuilder address = getAddress(propertyDetailsResponse);
			propertyDetailsResponse.setAddress(address.toString());

			propertyDetailsResponseList.add(propertyDetailsResponse);

		}

		return propertyDetailsResponseList;

}



    private StringBuilder getAddress(PropertyDetailsResponse pdr) {
        StringBuilder address = new StringBuilder();
        if (StringUtils.hasText(pdr.getDoorNo())) {
            address.append(pdr.getDoorNo() + ", ");
        }
        if (StringUtils.hasText(pdr.getBuildingName())) {
            address.append(pdr.getBuildingName() + ", ");
        }
        if (StringUtils.hasText(pdr.getStreet())) {
            address.append(pdr.getStreet() + ", ");
        }
        if (StringUtils.hasText(pdr.getCity())) {
            address.append(pdr.getCity() + ", ");
        }
        if (StringUtils.hasText(pdr.getPincode())) {
            address.append(pdr.getPincode() + ", ");
        }
        if (address.length() != 0) {
            address = address.delete(address.length() - 2, address.length());
        }
        return address;
    }

}