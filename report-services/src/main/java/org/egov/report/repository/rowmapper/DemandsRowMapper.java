package org.egov.report.repository.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.egov.report.web.model.PropertyDemandResponse;
import org.egov.report.web.model.PropertyDetailsResponse;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

public class DemandsRowMapper implements ResultSetExtractor<HashMap<String, List<PropertyDemandResponse>>> {

    HashMap<String, List<PropertyDemandResponse>> propertyDemandReportResponse = new HashMap();

    @Override
    public HashMap<String, List<PropertyDemandResponse>> extractData(ResultSet rs)
            throws SQLException, DataAccessException {

        while (rs.next()) {

            String consumerCode = rs.getString("consumercode");
            
            String tenantId = rs.getString("tenantid");
            String tenantIdStyled = tenantId.replace("od.", "");
            tenantIdStyled = tenantIdStyled.substring(0, 1).toUpperCase() + tenantIdStyled.substring(1).toLowerCase();
            
            PropertyDemandResponse propertyDemandResponse = PropertyDemandResponse.builder()
                    .consumercode(consumerCode).id(rs.getString("id"))
                    .oldpropertyid(rs.getString("oldpropertyid")).ward(rs.getString("ward")).tenantid(tenantIdStyled)
                    .uuid(rs.getString("uuid")).payer(rs.getString("payer"))
                    .createdby(rs.getString("createdby"))
                    .taxperiodfrom(rs.getLong("taxperiodfrom")).taxperiodto(rs.getLong("taxperiodto"))
                    .taxamount(rs.getBigDecimal("taxamount")).collectionamount(rs.getBigDecimal("collectionamount"))
                    .build();

            if (!propertyDemandReportResponse.containsKey(consumerCode)) {
                List<PropertyDemandResponse> propertyDemandResponseList = new ArrayList<PropertyDemandResponse>();
                propertyDemandResponseList.add(propertyDemandResponse);
                propertyDemandReportResponse.put(consumerCode, propertyDemandResponseList);
            } else {
                propertyDemandReportResponse.get(consumerCode).add(propertyDemandResponse);
            }

        }

        return propertyDemandReportResponse;

    }

}