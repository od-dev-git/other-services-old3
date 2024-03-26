package org.egov.report.repository.rowmapper;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
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

		ResultSetMetaData  metaData = rs.getMetaData();
		while(rs.next()) {

			int columnCount = metaData.getColumnCount();
			Boolean hasOwnershipCategoryColumn = false;
			
			for (int i = 1; i <= columnCount ; i++) {
				String columnName = metaData.getColumnName(i);
				if(columnName.equalsIgnoreCase("ownershipcategory")) {
					hasOwnershipCategoryColumn = true;
					break;
				}
			}
			
			String tenantId = rs.getString("tenantid");
			String tenantIdStyled = tenantId.replace("od.", "");
			tenantIdStyled = tenantIdStyled.substring(0,1).toUpperCase() + tenantIdStyled.substring(1).toLowerCase();
			
			String propertyType = rs.getString("propertytype");
			String propertyTypeStyled = extractSecondPart(propertyType);
		//	propertyTypeStyled = propertyTypeStyled.substring(0,1).toUpperCase() + propertyTypeStyled.substring(1).toLowerCase();
			
			String usageCategory = rs.getString("usagecategory");
			String usageCategoryStyled =  extractSecondPart(usageCategory);
		//	usageCategoryStyled = usageCategoryStyled.substring(0,1).toUpperCase() + usageCategoryStyled.substring(1).toLowerCase();
			

	         PropertyDetailsResponse propertyDetailsResponse = PropertyDetailsResponse.builder()
	                 .ulbName(tenantIdStyled)
	                 .wardNumber(rs.getString("ward"))
	                 .oldPropertyId(rs.getString("oldPropertyId"))
	                 .propertyId(rs.getString("propertyId"))
					 .ddnNo(rs.getString("ddnno"))
					 .legacyHoldingNo(rs.getString("legacyholdingno"))
	                 .propertyType(propertyTypeStyled)
	                 .usageCategory(usageCategoryStyled)
	                 .uuid(rs.getString("uuid"))
	                 .doorNo(rs.getString("doorNo"))
	                 .buildingName(rs.getString("buildingName"))
	                 .street(rs.getString("street"))
	                 .city(rs.getString("city"))
	                 .pincode(rs.getString("pincode"))
	                 .build();

			StringBuilder address = getAddress(propertyDetailsResponse);
			
			if (hasOwnershipCategoryColumn) {
				String ownershipCategory = rs.getString("ownershipcategory");
				String ownershipCategoryStyled = ownershipCategory.replace(".", " : ");
				propertyDetailsResponse.setOwnershipCategory(ownershipCategoryStyled);
			}
			
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
    
    private String extractSecondPart(String input) {
        if (input.contains(".")) {
            return input.split("\\.")[1];
        } else {
            return input;
        }
    }

}