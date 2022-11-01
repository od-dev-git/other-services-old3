package org.egov.integration.service;

import java.util.List;

import org.egov.integration.model.revenue.RevenueNotification;
import org.springframework.stereotype.Service;

@Service
public class EnrichmentService {

	public void enrichRevenueNotificationRequest(List<RevenueNotification> revenueNotifications) {
		
		revenueNotifications.stream().forEach(item -> {
			
			item.setAction(false);
			item.setActionTaken("No Action Taken");
			item.setCreatedTime(System.currentTimeMillis());
			item.setLastModifiedTime(System.currentTimeMillis());		
		});
		
	}
	
	

}
