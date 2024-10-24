package org.egov.integration.repository;

import java.util.ArrayList;
import java.util.List;

import org.egov.integration.IntegrationServicesApplication;
import org.egov.integration.config.IntegrationConfiguration;
import org.egov.integration.model.BPAVerification;
import org.egov.integration.model.BPAVerificationSearchCriteria;
import org.egov.integration.model.revenue.RevenueNotification;
import org.egov.integration.model.revenue.RevenueNotificationRequest;
import org.egov.integration.model.revenue.RevenueNotificationSearchCriteria;
import org.egov.integration.producer.RevenueNotificationProducer;
import org.egov.integration.repository.builder.RevenueNotificationQueryBuilder;
import org.egov.integration.repository.rowmapper.BPAVerificationRowMapper;
import org.egov.integration.repository.rowmapper.RevenueNotificationsRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.fasterxml.jackson.databind.ObjectMapper;

@Repository
public class RevenueNotificationRepository {
	
	@Autowired
	private ObjectMapper mapper;

	@Autowired
	private RevenueNotificationProducer revenueNotificationProducer;

	@Autowired
	RevenueNotificationQueryBuilder queryBuilder;

	@Autowired
	JdbcTemplate jdbcTemplate;

	@Autowired
	IntegrationConfiguration configuration;

	public void saveRevenueNotification(RevenueNotificationRequest revenueNotificationsRequest) {

		revenueNotificationProducer.push(configuration.getSaveRevenueNotification(), revenueNotificationsRequest);
	}
	
	public void updateRevenueNotification(RevenueNotificationRequest revenueNotificationsRequest) {

		revenueNotificationProducer.push(configuration.getUpdateRevenueNotification(), revenueNotificationsRequest);
	}

	public List<RevenueNotification> getNotifications(RevenueNotificationSearchCriteria searchCriteria) {

		List<Object> preparedStmtList = new ArrayList<>();

		String query = queryBuilder.getNotificationsSearchQuery(searchCriteria, preparedStmtList);

		return jdbcTemplate.query(query, preparedStmtList.toArray(), new RevenueNotificationsRowMapper(mapper));
	}
	
	public List<BPAVerification> getBPADataFromDatabase(BPAVerificationSearchCriteria criteria) {
		
		List<Object> preparedStmtList = new ArrayList<>();

		String query = queryBuilder.getBPADataSearchQuery(criteria, preparedStmtList);
		
		return jdbcTemplate.query(query, preparedStmtList.toArray(), new BPAVerificationRowMapper());
		
	}

}
