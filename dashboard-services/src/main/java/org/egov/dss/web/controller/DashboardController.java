package org.egov.dss.web.controller;

import javax.validation.Valid;

import org.egov.common.contract.request.RequestInfo;
import org.egov.dss.service.DashboardService;
import org.egov.dss.util.ResponseInfoFactory;
import org.egov.dss.web.model.ChartCriteria;
import org.egov.dss.web.model.DssResponse;
import org.egov.dss.web.model.RequestInfoWrapper;
import org.egov.dss.web.model.ResponseData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
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
	
	@PostMapping("/_jobGenerateData")
	public ResponseEntity<DssResponse> startScheduler(@RequestBody @Valid final RequestInfoWrapper requestInfoWrapper) {
		service.processRequest(requestInfoWrapper);
		DssResponse response = DssResponse.builder()
				.responseInfo(responseInfoFactory.createResponseInfoFromRequestInfo(requestInfoWrapper.getRequestInfo(), true))
				.build();
		return new ResponseEntity<DssResponse>(response, HttpStatus.OK);
		
	}
	
	@Scheduled(cron = "0 0 17 * * ?")
	public void triggerScheduler() {
		log.info("Data update scheduler started..");
		RequestInfoWrapper requestInfoWrapper = new RequestInfoWrapper();
		requestInfoWrapper.setRequestInfo(RequestInfo.builder().build());
		requestInfoWrapper.setChartCriteria(ChartCriteria.builder().build());
		service.processRequest(requestInfoWrapper);
	}
	
	@PostMapping("/_updatePayloadData")
	public ResponseEntity<DssResponse> updatePayloadData(@RequestBody @Valid final RequestInfoWrapper requestInfoWrapper) {
		service.process(requestInfoWrapper);
		DssResponse response = DssResponse.builder()
				.responseInfo(responseInfoFactory.createResponseInfoFromRequestInfo(requestInfoWrapper.getRequestInfo(), true))
				.build();
		return new ResponseEntity<DssResponse>(response, HttpStatus.OK);
		
	}
	
	@PostMapping("/_jobUpdateDemand")
	public ResponseEntity<DssResponse> updateDemand(@RequestBody @Valid final RequestInfoWrapper requestInfoWrapper) {
		service.updateDemand(requestInfoWrapper);
		DssResponse response = DssResponse.builder()
				.responseInfo(responseInfoFactory.createResponseInfoFromRequestInfo(requestInfoWrapper.getRequestInfo(), true))
				.build();
		return new ResponseEntity<DssResponse>(response, HttpStatus.OK);
		
	}
	
	@Scheduled(cron = "0 0 * * SAT")
	public void triggerDemandScheduler() {
		log.info("Demand data update scheduler started..");
		RequestInfoWrapper requestInfoWrapper = new RequestInfoWrapper();
		requestInfoWrapper.setRequestInfo(RequestInfo.builder().build());
		requestInfoWrapper.setChartCriteria(ChartCriteria.builder().build());
		service.updateDemand(requestInfoWrapper);
	}
	
	
}
