package org.egov.report.repository.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.egov.report.model.Property;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

public class PropertyRowMapper implements ResultSetExtractor<List<Property>>{
	
	List<Property> prop = new ArrayList<>();
	
	@Override
	public List<Property> extractData(ResultSet rs) throws SQLException, DataAccessException {


		while(rs.next()) {

			Property pr = new Property();

			pr.setAccountid(rs.getString("accountid"));
			pr.setAcknowledgementnumber(rs.getString("acknowldgementnumber"));
			pr.setAdditionaldetails(rs.getString("additionaldetails"));
			pr.setChannel(rs.getString("channel"));
			pr.setCreatedby(rs.getString("createdby"));
			pr.setCreatedtime(rs.getString("createdtime"));
			pr.setCreationreason(rs.getString("creationreason"));
			pr.setId(rs.getString("id"));
			pr.setLandarea(rs.getString("landarea"));
			pr.setLastmodifiedby(rs.getString("lastmodifiedtime"));
			pr.setLastmodifiedtime(rs.getString("lastmodifiedtime"));
			pr.setLinkedproperties(rs.getString("linkedproperties"));
			pr.setNooffloors(rs.getString("nooffloors"));
			pr.setOldPropertyId(rs.getString("oldpropertyid"));
			pr.setOwnershipcategory(rs.getString("additionaldetails"));
			pr.setPropertyId(rs.getString("propertyid"));
			pr.setPropertytype(rs.getString("propertytype"));
			pr.setSource(rs.getString("source"));
			pr.setStatus(rs.getString("status"));
			pr.setSuperbuiltarea(rs.getString("superbuiltarea"));
			pr.setSurveyid(rs.getString("surveyid"));
			pr.setTenantid(rs.getString("tenantid"));
			pr.setUsagecategory(rs.getString("usagecategory"));

			prop.add(pr);

		}

		
		return prop;
	}
}
