package org.egov.integration.service;

import java.util.List;
import java.util.UUID;

import javax.validation.Valid;

import org.egov.integration.model.revenue.RevenueNotification;
import org.egov.integration.model.revenue.RevenueNotificationRequest;
import org.egov.integration.util.RevenueNotificationConstants;
import org.springframework.stereotype.Service;

@Service
public class EnrichmentService {

	public void enrichRevenueNotificationRequest(RevenueNotificationRequest request) {
		
		request.getRevenueNotifications().stream().forEach(item -> {
			
			String revenueNotificationId = UUID.randomUUID().toString();
			item.setId(revenueNotificationId);
			item.setCreatedBy(request.getRequestInfo().getUserInfo().getUuid());
			item.setLastModifiedBy(request.getRequestInfo().getUserInfo().getUuid());
			item.setActionTaken(false);
			item.setCreatedTime(System.currentTimeMillis());
			item.setLastModifiedTime(System.currentTimeMillis());
			
			item.getCurrentOwner().stream().forEach(owner -> {
				owner.setCreatedBy(request.getRequestInfo().getUserInfo().getUuid());
				owner.setLastModifiedBy(request.getRequestInfo().getUserInfo().getUuid());
				owner.setCreatedTime(System.currentTimeMillis());
				owner.setLastModifiedTime(System.currentTimeMillis());
				owner.setRevenueNotificationId(revenueNotificationId);
				owner.setOwnerType(RevenueNotificationConstants.CURRENT_OWNER);
			}) ;
			
			item.getNewOwner().stream().forEach(owner -> {
				owner.setCreatedBy(request.getRequestInfo().getUserInfo().getUuid());
				owner.setLastModifiedBy(request.getRequestInfo().getUserInfo().getUuid());
				owner.setCreatedTime(System.currentTimeMillis());
				owner.setLastModifiedTime(System.currentTimeMillis());
				owner.setRevenueNotificationId(revenueNotificationId);
				owner.setOwnerType(RevenueNotificationConstants.NEW_OWNER);
			}) ;
		});
		
	}

	public void enrichRevenueNotificationUpdateRequest(@Valid RevenueNotificationRequest request) {
		
		request.getRevenueNotifications().stream().forEach(item -> {
			
			item.setLastModifiedBy(request.getRequestInfo().getUserInfo().getUuid());
			item.setLastModifiedTime(System.currentTimeMillis());
		});
		
	}
}
