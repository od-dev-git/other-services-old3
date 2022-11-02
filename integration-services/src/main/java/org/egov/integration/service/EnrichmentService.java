package org.egov.integration.service;

import java.util.List;
import java.util.UUID;

import org.egov.integration.model.revenue.RevenueNotification;
import org.springframework.stereotype.Service;

@Service
public class EnrichmentService {

	public void enrichRevenueNotificationRequest(List<RevenueNotification> revenueNotifications) {
		
		revenueNotifications.stream().forEach(item -> {
			
			item.setId(UUID.randomUUID().toString());
			item.setAction("No Action Taken");
			item.setActiontaken(false);
			item.setCreatedtime(System.currentTimeMillis());
			item.setLastmodifiedtime(System.currentTimeMillis());		
		});
		
	}
	
	

}
