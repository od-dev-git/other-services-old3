package org.egov.integration.repository;

import java.util.List;

import org.egov.integration.IntegrationServicesApplication;
import org.egov.integration.model.revenue.RevenueNotification;
import org.egov.integration.producer.RevenueNotificationProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

@Repository
public class RevenueNotificationRepository {

	@Autowired
	private RevenueNotificationProducer revenueNotificationProducer;
	
	@Value("${egov.integration.revenue.notification.save.topic}")
    private String saveRevenueNotification;
	
	public void saveRevenueNotification(List<RevenueNotification> revenueNotifications) {
		
		revenueNotificationProducer.push(saveRevenueNotification, revenueNotifications);
	}

}
