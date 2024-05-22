package org.egov.report.repository.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.egov.report.model.DCBProperty;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Service;

@Service
public class DCBPropertyRowMapper implements ResultSetExtractor<Map<String, DCBProperty>> {

	@Override
	public Map<String, DCBProperty> extractData(ResultSet rs) throws SQLException, DataAccessException {

		Map<String, DCBProperty> properties = new HashMap();

		while (rs.next()) {

			DCBProperty property = DCBProperty.builder().propertyId(rs.getString("propertyid"))
					.oldPropertyId(rs.getString("oldpropertyid")).ward(rs.getString("ward"))
					.legacyHoldingNo(rs.getString("legacyholdingno")).build();

			properties.put(rs.getString("propertyid"), property);
		}
		return properties;
	}

}
