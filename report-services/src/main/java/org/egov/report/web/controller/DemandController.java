package org.egov.report.web.controller;

import java.util.Arrays;

import javax.validation.Valid;

import org.egov.report.model.ConsumerDemandAuditRequest;
import org.egov.report.model.ConsumerDemandAuditResponse;
import org.egov.report.model.DemandDetailAuditResponse;
import org.egov.report.service.DemandService;
import org.egov.report.util.ResponseInfoFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/demand")
public class DemandController {

	@Autowired
	private DemandService demandService;

	@Autowired
	private ResponseInfoFactory responseInfoFactory;

	@PostMapping("/_demand-details-audit")
	public ResponseEntity<ConsumerDemandAuditResponse> demandDetailAudit(
			@RequestBody @Valid ConsumerDemandAuditRequest demandAuditRequest) {
		DemandDetailAuditResponse demandDetailAuditResponse = demandService.getDemandDetailAudit(demandAuditRequest);
		ConsumerDemandAuditResponse response = ConsumerDemandAuditResponse.builder()
				.demandDetailAuditResponse(Arrays.asList(demandDetailAuditResponse)).responseInfo(responseInfoFactory
						.createResponseInfoFromRequestInfo(demandAuditRequest.getRequestInfo(), true))
				.build();
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	


}
