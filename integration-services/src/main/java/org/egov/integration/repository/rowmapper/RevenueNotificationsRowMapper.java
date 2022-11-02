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
					.districtname(rs.getString("districtname")).tenantid(rs.getString("tenantid"))
					.revenuevillage(rs.getString("revenuevillage")).plotno(rs.getString("plotno"))
					.flatno(rs.getString("flatno")).address(rs.getString("address"))
					.currentownername(rs.getString("currentownername"))
					.currentownermobilenumber(rs.getString("currentownermobilenumber"))
					.newownername(rs.getString("newownername"))
					.newownermobilenumber(rs.getString("newownermobilenumber"))
					.actiontaken(rs.getBoolean("actiontaken")).action(rs.getString("action"))
					.additionaldetails(rs.getObject("additionaldetails")).createdby(rs.getString("createdby"))
					.createdtime(rs.getLong("createdtime")).lastmodifiedby(rs.getString("lastmodifiedby"))
					.lastmodifiedtime(rs.getLong("lastmodifiedtime")).build();

			revenueNotifications.add(revenueNotification);

		}

		return revenueNotifications;

	}
}
