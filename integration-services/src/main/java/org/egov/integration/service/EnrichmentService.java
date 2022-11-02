package org.egov.integration.service;

import java.util.List;
import java.util.UUID;

import org.egov.integration.model.revenue.RevenueNotification;
import org.egov.integration.model.revenue.RevenueNotificationRequest;
import org.springframework.stereotype.Service;

@Service
public class EnrichmentService {

	public void enrichRevenueNotificationRequest(RevenueNotificationRequest request) {
		
		request.getRevenueNotifications().stream().forEach(item -> {
			
			item.setId(UUID.randomUUID().toString());
			item.setCreatedby(request.getRequestInfo().getUserInfo().getUuid());
			item.setLastmodifiedby(request.getRequestInfo().getUserInfo().getUuid());
			item.setActiontaken(false);
			item.setCreatedtime(System.currentTimeMillis());
			item.setLastmodifiedtime(System.currentTimeMillis());		
		});
		
	}
	
	

}
