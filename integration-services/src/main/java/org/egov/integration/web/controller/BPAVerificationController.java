package org.egov.integration.web.controller;

import javax.validation.Valid;

import org.egov.integration.model.BPAVerification;
import org.egov.integration.model.BPAVerificationSearchCriteria;
import org.egov.integration.service.ConsumerVerificationService;
import org.egov.integration.util.ResponseInfoFactory;
import org.egov.integration.web.model.BPAVerificationResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/BPA")
public class BPAVerificationController {
	
	@Autowired
	private ResponseInfoFactory responseInfoFactor;
	
	@Autowired
	private ConsumerVerificationService consumerVerificationService;
	
	@PostMapping("/_verification")
	public ResponseEntity<BPAVerificationResponse> search(@RequestBody @Valid final BPAVerificationSearchCriteria searchCriteria) {
				
		BPAVerification bpaVerification = consumerVerificationService.searchBPA(searchCriteria);
		
		BPAVerificationResponse response = BPAVerificationResponse.builder().bpaVerification(bpaVerification).build();
		return new ResponseEntity<BPAVerificationResponse>(response, HttpStatus.OK);
	}

}
