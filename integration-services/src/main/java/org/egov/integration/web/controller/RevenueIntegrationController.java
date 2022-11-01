package org.egov.integration.web.controller;

import java.util.List;

import javax.validation.Valid;

import org.egov.common.contract.request.RequestInfo;
import org.egov.integration.model.revenue.RevenueNotification;
import org.egov.integration.model.revenue.RevenueNotificationRequest;
import org.egov.integration.model.revenue.RevenueNotificationSearchCriteria;
import org.egov.integration.service.RevenueNotificationService;
import org.egov.integration.util.ResponseInfoFactory;
import org.egov.integration.web.model.RequestInfoWrapper;
import org.egov.integration.web.model.RevenueIntegrationResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/revenue/notification")
public class RevenueIntegrationController {
	
	@Autowired
	private ResponseInfoFactory responseInfoFactor;
	
	@Autowired
	private RevenueNotificationService revenueNotificationService;

	@PostMapping("/_create")
	public ResponseEntity<RevenueIntegrationResponse> create(@RequestBody @Valid final RevenueNotificationRequest request) {
		
		List<RevenueNotification> revenueNotifications = revenueNotificationService.create(request);
		
		RevenueIntegrationResponse response = RevenueIntegrationResponse.builder().revenueNotification(revenueNotifications)
				.responseInfo(responseInfoFactor.createResponseInfoFromRequestInfo(request.getRequestInfo(), true)).build();
		return new ResponseEntity<RevenueIntegrationResponse>(response, HttpStatus.CREATED);
	}
	
	@PostMapping("/_update")
	public ResponseEntity<RevenueIntegrationResponse> update(@RequestBody @Valid final RevenueNotificationRequest request) {

		List<RevenueNotification> revenueNotifications = revenueNotificationService.update(request);
		
		RevenueIntegrationResponse response = RevenueIntegrationResponse.builder().revenueNotification(revenueNotifications)
				.responseInfo(responseInfoFactor.createResponseInfoFromRequestInfo(request.getRequestInfo(), true)).build();
		return new ResponseEntity<RevenueIntegrationResponse>(response, HttpStatus.OK);
	}
	
	@PostMapping("/_search")
	public ResponseEntity<RevenueIntegrationResponse> search(@RequestBody @Valid final RequestInfoWrapper requestInfoWrapper,
			@ModelAttribute RevenueNotificationSearchCriteria searchCriteria) {
		
		List<RevenueNotification> revenueNotifications = revenueNotificationService.search(searchCriteria);
		
		RevenueIntegrationResponse response = RevenueIntegrationResponse.builder().revenueNotification(revenueNotifications)
				.responseInfo(responseInfoFactor.createResponseInfoFromRequestInfo(requestInfoWrapper.getRequestInfo(), true)).build();
		return new ResponseEntity<RevenueIntegrationResponse>(response, HttpStatus.OK);
	}
}
