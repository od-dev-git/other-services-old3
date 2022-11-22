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

            PropertyDemandResponse propertyDemandResponse = new PropertyDemandResponse();

            String consumerCode = rs.getString("consumercode");
            propertyDemandResponse.setConsumercode(consumerCode);
            propertyDemandResponse.setId(rs.getString("id"));
            propertyDemandResponse.setPayer(rs.getString("payer"));
            propertyDemandResponse.setOldpropertyid(rs.getString("oldpropertyid"));
            propertyDemandResponse.setWard(rs.getString("ward"));
            propertyDemandResponse.setUuid(rs.getString("uuid"));
            propertyDemandResponse.setCreatedby(rs.getString("createdby"));
            propertyDemandResponse.setTaxperiodfrom(rs.getLong("taxperiodfrom"));
            propertyDemandResponse.setTaxperiodto(rs.getLong("taxperiodto"));

            String tenantId = rs.getString("tenantid");
            String tenantIdStyled = tenantId.replace("od.", "");
            tenantIdStyled = tenantIdStyled.substring(0, 1).toUpperCase() + tenantIdStyled.substring(1).toLowerCase();
            propertyDemandResponse.setTenantid(tenantIdStyled);

            propertyDemandResponse.setTaxamount(rs.getBigDecimal("taxamount"));
            propertyDemandResponse.setCollectionamount(rs.getBigDecimal("collectionamount"));

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