package org.egov.report.web.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.validation.Valid;

import org.egov.report.model.UtilityReportSearchCriteria;
import org.egov.report.service.FSMReportService;
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
@RequestMapping("/reports/FSM")
public class FSMReportController {
	
	
	@Autowired
	private ResponseInfoFactory responseInfoFactory;
	
	@Autowired
	private FSMReportService fsmReportService;
	
	@PostMapping(value = "/_generateDataMartReport")
	public ResponseEntity<UtilityReportResponse> generateUtilityReport(@Valid @RequestBody RequestInfoWrapper requestInfoWrapper,
			@Valid @ModelAttribute UtilityReportSearchCriteria searchCriteria) throws Exception {
		Map<String, Object> map = new HashMap<>();
		fsmReportService.generateDataMartReport(requestInfoWrapper.getRequestInfo(), searchCriteria);
		
		map.put("Message", "Report generation is in progress.");
		UtilityReportResponse response = UtilityReportResponse.builder()
				.response(map)
				.responseInfo(responseInfoFactory.createResponseInfoFromRequestInfo(requestInfoWrapper.getRequestInfo(), true))
				.build();
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@PostMapping(value = "/_getDataMartReport")
	public ResponseEntity<UtilityReportResponse> getUtilityReport(@Valid @RequestBody RequestInfoWrapper requestInfoWrapper,
			@Valid @ModelAttribute UtilityReportSearchCriteria searchCriteria) throws IOException {
		Map<String, Object> responseMap = fsmReportService.getDataMartReport(requestInfoWrapper.getRequestInfo(), searchCriteria);
		
		UtilityReportResponse response = UtilityReportResponse.builder()
				.response(responseMap)
				.responseInfo(responseInfoFactory.createResponseInfoFromRequestInfo(requestInfoWrapper.getRequestInfo(), true))
				.build();
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
}
