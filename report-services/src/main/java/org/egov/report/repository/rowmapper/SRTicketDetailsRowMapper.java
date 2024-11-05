package org.egov.report.repository.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.egov.report.web.model.TicketDetails;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

public class SRTicketDetailsRowMapper implements ResultSetExtractor<List<TicketDetails>>{
	
	@Override
	public List<TicketDetails> extractData(ResultSet rs) throws SQLException, DataAccessException {
		
		List<TicketDetails> responseList = new ArrayList<>();
		
		while(rs.next())
		{
			TicketDetails reponse = new TicketDetails();
			reponse.setTenantId(rs.getString("tenantid"));
			reponse.setServiceRequestId(rs.getString("servicerequestid"));
			reponse.setCreatedDate(rs.getLong("createdtime"));
			reponse.setRaisedby(rs.getString("createdby"));
			reponse.setName(rs.getString("firstname"));
			reponse.setMobilenumber(rs.getString("phone"));
			reponse.setService(rs.getString("service"));
			reponse.setType(rs.getString("servicetype"));
			reponse.setDescription(rs.getString("description"));
			reponse.setPriority(rs.getString("priority"));
			reponse.setCity(rs.getString("city"));
			String status = rs.getString("status");
			reponse.setStatus(status);
			if(status.equalsIgnoreCase("closed")) {
				reponse.setClosedDate(rs.getLong("lastmodifiedtime"));
			}
			
			responseList.add(reponse);
		}
		
		return responseList;
	}

}
