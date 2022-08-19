package org.egov.report.repository.rowmapper;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.egov.report.util.WSReportUtils;
import org.egov.report.web.model.ConsumerBillHistoryResponse;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

public class ConsumerBillHistoryRowMapper implements ResultSetExtractor<List<ConsumerBillHistoryResponse>>{

	WSReportUtils wsReportUtils = new WSReportUtils();
	
	@Override
	public List<ConsumerBillHistoryResponse> extractData(ResultSet rs) throws SQLException, DataAccessException {
		
		List<ConsumerBillHistoryResponse> responseList = new ArrayList<>();
		
		
		while(rs.next()) {
			
			ConsumerBillHistoryResponse response = new ConsumerBillHistoryResponse();
			
			String tenantId = rs.getString("tenantid");
			response.setUlb(tenantId.substring(3));
			response.setConsumerCode(rs.getString("consumercode"));
			response.setPeriodFrom(wsReportUtils.getConvertedDate(rs.getLong("taxperiodfrom")));
			response.setPeriodTo(wsReportUtils.getConvertedDate(rs.getLong("taxperiodto")));
			Boolean isPaymentCompleted = rs.getBoolean("ispaymentcompleted");
			if(isPaymentCompleted)
				response.setPaymentCompleted("Yes");
			else
				response.setPaymentCompleted("No");
			BigDecimal taxAmt = rs.getBigDecimal("totalTax");
			BigDecimal collectedAmt = rs.getBigDecimal("totalCollected");
			response.setTaxAmount(taxAmt);
			response.setCollectedAmount(collectedAmt);
			response.setDueAmount(taxAmt.subtract(collectedAmt));
			
			
			responseList.add(response);
		}
		
		return responseList;
	}

}
