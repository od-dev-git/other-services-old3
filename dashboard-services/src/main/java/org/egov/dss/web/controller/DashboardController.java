package org.egov.dss.web.controller;

import javax.validation.Valid;

import org.egov.dss.service.DashboardService;
import org.egov.dss.util.ResponseInfoFactory;
import org.egov.dss.web.model.DssResponse;
import org.egov.dss.web.model.RequestInfoWrapper;
import org.egov.dss.web.model.ResponseData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DashboardController {
	
	@Autowired
	private DashboardService service;
	
	@Autowired
	private ResponseInfoFactory responseInfoFactory;

	@PostMapping("/getChart")
	public ResponseEntity<DssResponse> getChart(@RequestBody @Valid final RequestInfoWrapper requestInfoWrapper) {
		ResponseData responseData = service.serveRequest(requestInfoWrapper);
        DssResponse response = DssResponse
				.builder().responseInfo(responseInfoFactory
						.createResponseInfoFromRequestInfo(requestInfoWrapper.getRequestInfo(), true))
				.responseData(responseData).build();
		return new ResponseEntity<DssResponse>(response, HttpStatus.OK);

	}
	
	@PostMapping("/_generateData")
	public ResponseEntity<DssResponse> startScheduler(@RequestBody @Valid final RequestInfoWrapper requestInfoWrapper) {
		service.processRequest(requestInfoWrapper);
		DssResponse response = DssResponse.builder()
				.responseInfo(responseInfoFactory.createResponseInfoFromRequestInfo(requestInfoWrapper.getRequestInfo(), true))
				.build();
		return new ResponseEntity<DssResponse>(response, HttpStatus.OK);
		
	}
}
