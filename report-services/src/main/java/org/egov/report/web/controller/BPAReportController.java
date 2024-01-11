package org.egov.report.web.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.validation.Valid;

import org.egov.report.model.UtilityReportSearchCriteria;
import org.egov.report.service.BPAReportService;
import org.egov.report.util.ResponseInfoFactory;
import org.egov.report.web.model.RequestInfoWrapper;
import org.egov.report.web.model.UtilityReportResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/reports/bpa")
public class BPAReportController {

	@Autowired
	private BPAReportService reportService;
	
	@Autowired
	private ResponseInfoFactory responseInfoFactory;
	
	
	/**
	 * Get all payments report and download in excel format
	 * 
	 * @param requestInfoWrapper
	 * @param searchCriteria
	 * @throws Exception
	 */
	@PostMapping(value = "/_generateUtilityReport")
	public ResponseEntity<UtilityReportResponse> generateUtilityReport(@Valid @RequestBody RequestInfoWrapper requestInfoWrapper,
			@Valid @ModelAttribute UtilityReportSearchCriteria searchCriteria) throws Exception {
		Map<String, Object> map = new HashMap<>();
		reportService.generateUtilityReport(requestInfoWrapper.getRequestInfo(), searchCriteria);
		
		map.put("Message", "Report generation is in progress.");
		UtilityReportResponse response = UtilityReportResponse.builder()
				.response(map)
				.responseInfo(responseInfoFactory.createResponseInfoFromRequestInfo(requestInfoWrapper.getRequestInfo(), true))
				.build();
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	
	/**
	 * Get all applications report and download in excel format
	 * 
	 * @param requestInfoWrapper
	 * @param response
	 * @throws IOException
	 */
	@PostMapping(value = "/_getUtilityReport")
	public ResponseEntity<UtilityReportResponse> getUtilityReport(@Valid @RequestBody RequestInfoWrapper requestInfoWrapper,
			@Valid @ModelAttribute UtilityReportSearchCriteria searchCriteria) throws IOException {
		Map<String, Object> responseMap = reportService.getUtilityReport(requestInfoWrapper.getRequestInfo(), searchCriteria);
		
		UtilityReportResponse response = UtilityReportResponse.builder()
				.response(responseMap)
				.responseInfo(responseInfoFactory.createResponseInfoFromRequestInfo(requestInfoWrapper.getRequestInfo(), true))
				.build();
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
}
