package org.egov.integration.service;

import java.util.Collections;
import java.util.List;

import javax.validation.Valid;

import org.egov.integration.model.revenue.RevenueNotification;
import org.egov.integration.model.revenue.RevenueNotificationRequest;
import org.egov.integration.model.revenue.RevenueNotificationSearchCriteria;
import org.egov.integration.repository.RevenueNotificationRepository;
import org.egov.integration.validator.RevenueNotificationValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class RevenueNotificationService {
	
	@Autowired
	private RevenueNotificationValidator revenueNotificationValidator;

	@Autowired
	private RevenueNotificationRepository revenueNotificationRepository;
	
	@Autowired
	private EnrichmentService enrichmentService;
	
	public List<RevenueNotification> create(@Valid RevenueNotificationRequest request) {
		
		revenueNotificationValidator.validateMDMSForCreateRequest(request);
		
		enrichmentService.enrichRevenueNotificationRequest(request.getRevenueNotifications());
		
		revenueNotificationRepository.saveRevenueNotification(request);
		
		return request.getRevenueNotifications();
	}

	public List<RevenueNotification> update(@Valid RevenueNotificationRequest request) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<RevenueNotification> search(RevenueNotificationSearchCriteria searchCriteria) {

		revenueNotificationValidator.validateSearch(searchCriteria);

		log.info("validated");
		log.info("entering into query");

		List<RevenueNotification> revenueNotifications;
		revenueNotifications = revenueNotificationRepository.getNotifications(searchCriteria);

		if (revenueNotifications.isEmpty())
			return Collections.emptyList();

		log.info("No of Notifications : " + revenueNotifications.size());

		return revenueNotifications;
	}

}
