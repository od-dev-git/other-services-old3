package org.egov.report.web.controller;

import javax.validation.Valid;

import org.egov.report.service.IncentiveService;
import org.egov.report.util.ResponseInfoFactory;
import org.egov.report.web.model.IncentiveReportCriteria;
import org.egov.report.web.model.IncentiveResponse;
import org.egov.report.web.model.ReportResponse;
import org.egov.report.web.model.RequestInfoWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/reports")
public class ReportController {
	
	@Autowired
	private IncentiveService incentiveService;
	
	@Autowired
	private ResponseInfoFactory responseInfoFactory;

	@PostMapping("/incentive")
	@ResponseBody
	public ResponseEntity<ReportResponse> getIncentiveReport(@ModelAttribute IncentiveReportCriteria incentiveReportCriteria,
			@RequestBody @Valid final RequestInfoWrapper requestInfoWrapper) {
		
		IncentiveResponse incentiveResponse = incentiveService.getIncentiveReport(requestInfoWrapper.getRequestInfo(), incentiveReportCriteria);
		
		ReportResponse response = ReportResponse.builder().incentiveResponse(incentiveResponse)
				.responseInfo(responseInfoFactory.createResponseInfoFromRequestInfo(requestInfoWrapper.getRequestInfo(), true)).build();
		return new ResponseEntity<ReportResponse>(response, HttpStatus.OK);
	}
}
