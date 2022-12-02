package org.egov.integration.repository.rowmapper;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.egov.integration.model.revenue.RevenueNotification;
import org.egov.integration.model.revenue.RevenueOwner;
import org.egov.tracer.model.CustomException;
import org.postgresql.util.PGobject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class RevenueNotificationsRowMapper implements ResultSetExtractor<List<RevenueNotification>> {

	
	private ObjectMapper mapper;
	
	@Autowired
	public RevenueNotificationsRowMapper(ObjectMapper mapper) {
		this.mapper = mapper;
	}

	public List<RevenueNotification> extractData(ResultSet rs) throws SQLException, DataAccessException {

		Map<String, RevenueNotification> revenueNotificationsListMap = new HashMap<>();
//		RevenueNotification revenueNotification = new RevenueNotification();

		while (rs.next()) {

			String Id = rs.getString("id");

			log.info("mapper receive id: "+Id);
			
			RevenueNotification revenueNotification = revenueNotificationsListMap.get(Id);

			if (revenueNotification == null) {

				revenueNotification = new RevenueNotification();

				revenueNotification.setDistrictName(rs.getString("districtname"));
				revenueNotification.setTenantId(rs.getString("tenantid"));
				revenueNotification.setRevenueVillage(rs.getString("revenuevillage"));
				revenueNotification.setPlotNo(rs.getString("plotno"));
				revenueNotification.setFlatNo(rs.getString("flatno"));
				revenueNotification.setAddress(rs.getString("address"));
				revenueNotification.setActionTaken(rs.getBoolean("actiontaken"));
				revenueNotification.setAction(rs.getString("action"));
				revenueNotification.setWaterConsumerNo(rs.getString("waterconsumerno"));
				revenueNotification.setPropertyId(rs.getString("propertyid"));
				revenueNotification.setCreatedBy(rs.getString("createdby"));
				revenueNotification.setCreatedTime(rs.getLong("createdtime"));
				revenueNotification.setLastModifiedBy(rs.getString("lastmodifiedby"));
				revenueNotification.setLastModifiedTime(rs.getLong("lastmodifiedtime"));
				revenueNotification.setId(Id);

				PGobject pgObj = (PGobject) rs.getObject("additionaldetails");
				ObjectNode additionalDetails = null;
				if (pgObj != null) {

					try {
						additionalDetails = mapper.readValue(pgObj.getValue(), ObjectNode.class);
					} catch (IOException ex) {
						// TODO Auto-generated catch block
						ex.printStackTrace();
						throw new CustomException("PARSING ERROR", "The additionalDetail json cannot be parsed");
					}
				}
				
				revenueNotification.setAdditionalDetails(additionalDetails);

				revenueNotificationsListMap.put(Id, revenueNotification);

			}
			
			String notification_Id = rs.getString("revenuenotificationid");
			String ownerType = rs.getString("ownertype");

			if (!StringUtils.isEmpty(notification_Id) && ownerType.equals("CURRENT")) {
				RevenueOwner currentOwner = new RevenueOwner();
				currentOwner.setOwnerName(rs.getString("ownername"));
				currentOwner.setMobileNumber(rs.getString("mobilenumber"));
				revenueNotification.addCurrentOwners(currentOwner);
			}
			
			if (!StringUtils.isEmpty(notification_Id) && ownerType.equals("NEW")) {
				RevenueOwner newOwner = new RevenueOwner();
				newOwner.setOwnerName(rs.getString("ownername"));;
				newOwner.setMobileNumber(rs.getString("mobilenumber"));
				revenueNotification.addNewOwners(newOwner);
			}

		}

		return new ArrayList<>(revenueNotificationsListMap.values());

	}

}
