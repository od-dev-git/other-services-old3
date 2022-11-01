package org.egov.integration.service;

import java.util.List;

import javax.validation.Valid;

import org.egov.integration.model.revenue.RevenueNotification;
import org.egov.integration.model.revenue.RevenueNotificationRequest;
import org.egov.integration.model.revenue.RevenueNotificationSearchCriteria;
import org.egov.integration.repository.RevenueNotificationRepository;
import org.egov.integration.validator.RevenueNotificationValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RevenueNotificationService {
	
	@Autowired
	private RevenueNotificationValidator revenueNotificationValidator;

	@Autowired
	private RevenueNotificationRepository revenueNotificationRepository;
	
	@Autowired
	private EnrichmentService enrichmentService;
	
	public List<RevenueNotification> create(@Valid RevenueNotificationRequest request) {
		
		revenueNotificationValidator.validateRevenueNotificationRequest(request.getRevenueNotifications());
		
		//yet to implement
		//revenueNotificationValidator.validateMDMSForCreateRequest(request.getRequestInfo());
		
		enrichmentService.enrichRevenueNotificationRequest(request.getRevenueNotifications());
		
		revenueNotificationRepository.saveRevenueNotification(request.getRevenueNotifications());
		
		return null;
	}

	public List<RevenueNotification> update(@Valid RevenueNotificationRequest request) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<RevenueNotification> search(RevenueNotificationSearchCriteria searchCriteria) {
		// TODO Auto-generated method stub
		return null;
	}

}
