package org.egov.report.repository.rowmapper;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.egov.report.web.model.WaterDemandResponse;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

public class WaterMonthlyDemandRowMapper implements ResultSetExtractor<Map<String, List<WaterDemandResponse>>>{

	@Override
	public Map<String, List<WaterDemandResponse>>  extractData(ResultSet rs) throws SQLException, DataAccessException {

		Map<String, List<WaterDemandResponse>>  responseMap = new HashMap<>();
		while(rs.next()) {

			WaterDemandResponse response = new WaterDemandResponse();
			String demandId = rs.getString("id");
			String consumerCode = rs.getString("consumercode");
			String tenantId = rs.getString("tenantid");
			Long taxPeriodFrom = rs.getLong("taxperiodfrom");
			Long taxPeriodTo = rs.getLong("taxperiodto");
			String taxHeadCode = rs.getString("taxheadcode");
			BigDecimal collectedAmt = rs.getBigDecimal("collectionamount");
			BigDecimal taxAmt =  rs.getBigDecimal("taxamount");

			//response.setConsumercode(consumerCode);
			response.setTaxamount(taxAmt);
			response.setCollectionamount(collectedAmt);
			response.setTaxheadcode(taxHeadCode);
			response.setTaxperiodfrom(taxPeriodFrom);
			response.setTaxperiodto(taxPeriodTo);
			response.setTenantid(tenantId);
			response.setDemandId(demandId);

			if(!responseMap.containsKey(consumerCode))
				responseMap.put(consumerCode, new ArrayList<>());

			responseMap.get(consumerCode).add(response);

		}


		return responseMap;
	}
}
