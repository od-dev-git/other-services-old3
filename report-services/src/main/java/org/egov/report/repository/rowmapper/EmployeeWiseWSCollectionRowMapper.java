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
			
         EmployeeWiseWSCollectionResponse response = EmployeeWiseWSCollectionResponse
                 .builder()
                 .tenantId(rs.getString("tenantid"))
                 .ulb((rs.getString("tenantid")).substring(3))
                 .ward(rs.getString("ward"))
                 .amount(rs.getBigDecimal("totalamountpaid"))
                 .consumerCode(rs.getString("connectionno"))
                 .employeeId(rs.getString("createdby"))
                 .head("WATER")
                 .paymentDate(rs.getLong("transactiondate"))
                 .oldConsumerNo(rs.getString("oldconnectionno"))
                 .paymentMode(rs.getString("paymentmode"))
                 .receiptNo(rs.getString("receiptnumber"))
                 .build();
			
			responseList.add(response);
		}
				
		return responseList;
	}

}
