package org.egov.report.repository.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.egov.report.web.model.EmployeeWiseWSCollectionResponse;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

public class EmployeeWiseWSCollectionRowMapper implements ResultSetExtractor<List<EmployeeWiseWSCollectionResponse>>{

	@Override
	public List<EmployeeWiseWSCollectionResponse> extractData(ResultSet rs) throws SQLException, DataAccessException {
		
		List<EmployeeWiseWSCollectionResponse> responseList = new ArrayList<>();
		
		while(rs.next()) {
			
			EmployeeWiseWSCollectionResponse response = new EmployeeWiseWSCollectionResponse();
			String tenantId = rs.getString("tenantid");
			response.setTenantId(tenantId);
			response.setUlb(tenantId.substring(3));
			response.setWard(rs.getString("ward"));
			response.setAmount(rs.getBigDecimal("totalamountpaid"));
			response.setConsumerCode(rs.getString("connectionno"));
			response.setEmployeeId(rs.getString("createdby"));
			response.setHead("WATER");
			response.setPaymentDate(rs.getLong("transactiondate"));
			response.setOldConsumerNo(rs.getString("oldconnectionno"));
			response.setPaymentMode(rs.getString("paymentmode"));
			response.setReceiptNo(rs.getString("receiptnumber"));
			
			responseList.add(response);
		}
				
		return responseList;
	}

}
