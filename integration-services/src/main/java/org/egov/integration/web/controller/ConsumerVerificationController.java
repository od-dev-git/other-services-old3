package org.egov.integration.web.controller;

import java.util.List;

import javax.validation.Valid;

import org.egov.integration.model.ConsumerVerification;
import org.egov.integration.web.model.ConsumerVerificationResponse;
import org.egov.integration.model.ConsumerVerificationSearchCriteria;
import org.egov.integration.service.ConsumerVerificationService;
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
@RequestMapping("/consumer/verification")
public class ConsumerVerificationController {
	
	@Autowired
	private ResponseInfoFactory responseInfoFactor;
	
	@Autowired
	private ConsumerVerificationService consumerVerificationService;

	@PostMapping("/_search")
	public ResponseEntity<ConsumerVerificationResponse> search(@RequestBody @Valid final RequestInfoWrapper requestInfoWrapper) {
		
		ConsumerVerificationSearchCriteria searchCriteria = requestInfoWrapper.getConsumerVerificationRequest();
		
		List<ConsumerVerification> consumerVerificationInfo = consumerVerificationService.search(searchCriteria);
		
		ConsumerVerificationResponse response = ConsumerVerificationResponse.builder().consumerVerification(consumerVerificationInfo)
				.responseInfo(responseInfoFactor.createResponseInfoFromRequestInfo(requestInfoWrapper.getRequestInfo(), true)).build();
		return new ResponseEntity<ConsumerVerificationResponse>(response, HttpStatus.OK);
	}
}
