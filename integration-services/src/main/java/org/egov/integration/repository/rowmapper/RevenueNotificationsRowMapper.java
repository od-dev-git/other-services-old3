package org.egov.integration.repository.rowmapper;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.egov.integration.model.revenue.RevenueNotification;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class RevenueNotificationsRowMapper implements ResultSetExtractor<List<RevenueNotification>> {

	public List<RevenueNotification> extractData(ResultSet rs) throws SQLException, DataAccessException {

		List<RevenueNotification> revenueNotifications = new ArrayList<>();

		while (rs.next()) {

			RevenueNotification revenueNotification = RevenueNotification.builder().id(rs.getString("id"))
					.districtName(rs.getString("districtname")).tenantId(rs.getString("tenantid"))
					.revenueVillage(rs.getString("revenuevillage")).plotNo(rs.getString("plotno"))
					.flatNo(rs.getString("flatno")).address(rs.getString("address"))
					.actionTaken(rs.getBoolean("actiontaken")).action(rs.getString("action"))
					.additionalDetails(rs.getObject("additionaldetails")).createdBy(rs.getString("createdby"))
					.createdTime(rs.getLong("createdtime")).lastModifiedBy(rs.getString("lastmodifiedby"))
					.lastModifiedTime(rs.getLong("lastmodifiedtime")).build();

			revenueNotifications.add(revenueNotification);

		}

		return revenueNotifications;

	}
}
