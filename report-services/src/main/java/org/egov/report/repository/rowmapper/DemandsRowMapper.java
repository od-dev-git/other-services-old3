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

public class DemandsRowMapper implements ResultSetExtractor<HashMap<String,List<PropertyDemandResponse>>> {
	
	HashMap<String,List<PropertyDemandResponse>>  pdrResponse = new HashMap();
	
	@Override
	public HashMap<String,List<PropertyDemandResponse>> extractData(ResultSet rs) throws SQLException, DataAccessException {

		while(rs.next()) {

			PropertyDemandResponse pdr = new PropertyDemandResponse();
			
			String cc = rs.getString("consumercode");
			pdr.setConsumercode(cc);
			pdr.setId(rs.getString("id"));
			pdr.setPayer(rs.getString("payer"));
			pdr.setCreatedby(rs.getString("createdby"));
			pdr.setTaxperiodfrom(rs.getLong("taxperiodfrom"));
			pdr.setTaxperiodto(rs.getLong("taxperiodto"));
			
			

			String ti = rs.getString("tenantid");
			String tiNew = ti.replace("od.", "");
			tiNew = tiNew.substring(0,1).toUpperCase() + tiNew.substring(1).toLowerCase();
			pdr.setTenantid(tiNew);

			pdr.setTaxamount(rs.getBigDecimal("taxamount"));
			pdr.setCollectionamount(rs.getBigDecimal("collectionamount"));

			
			if(!pdrResponse.containsKey(cc) ){
				pdrResponse.put(cc,new ArrayList<PropertyDemandResponse>());
			}
			
			pdrResponse.get(cc).add(pdr);
			

		}

		return pdrResponse;

}

}