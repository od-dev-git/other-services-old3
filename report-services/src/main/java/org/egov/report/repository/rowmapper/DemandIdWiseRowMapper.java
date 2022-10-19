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

public class DemandIdWiseRowMapper implements ResultSetExtractor<List<PropertyDemandResponse>> {

    List<PropertyDemandResponse> propertyDemandResponses = new ArrayList<>();

    @Override
    public List<PropertyDemandResponse> extractData(ResultSet rs) throws SQLException, DataAccessException {

        while (rs.next()) {

            PropertyDemandResponse propertyDemandResponse = new PropertyDemandResponse();

            propertyDemandResponse.setConsumercode(rs.getString("consumercode"));
            propertyDemandResponse.setId(rs.getString("id"));
            propertyDemandResponse.setPayer(rs.getString("payer"));
            propertyDemandResponse.setOldpropertyid(rs.getString("oldpropertyid"));
            propertyDemandResponse.setWard(rs.getString("ward"));
            propertyDemandResponse.setUuid(rs.getString("uuid"));
            propertyDemandResponse.setCreatedby(rs.getString("createdby"));
            propertyDemandResponse.setTaxperiodfrom(rs.getLong("taxperiodfrom"));
            propertyDemandResponse.setTaxperiodto(rs.getLong("taxperiodto"));

            String tenantId = rs.getString("tenantid");
            tenantId = tenantId.replace("od.", "");
            tenantId = tenantId.substring(0, 1).toUpperCase() + tenantId.substring(1).toLowerCase();
            propertyDemandResponse.setTenantid(tenantId);

            propertyDemandResponse.setTaxamount(rs.getBigDecimal("taxamount"));
            propertyDemandResponse.setCollectionamount(rs.getBigDecimal("collectionamount"));
            
            propertyDemandResponses.add(propertyDemandResponse);

        }

        return propertyDemandResponses;

    }

}