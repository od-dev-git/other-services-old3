package org.egov.report.web.controller;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.egov.report.model.UserSearchCriteria;
import org.egov.report.model.UtilityReportSearchCriteria;
import org.egov.report.service.IncentiveService;
import org.egov.report.service.SRService;
import org.egov.report.service.UserService;
import org.egov.report.util.ResponseInfoFactory;
import org.egov.report.web.model.IncentiveReportCriteria;
import org.egov.report.web.model.IncentiveResponse;
import org.egov.report.web.model.ReportResponse;
import org.egov.report.web.model.RequestInfoWrapper;
import org.egov.report.web.model.SRReportSearchCriteria;
import org.egov.report.web.model.TicketDetails;
import org.egov.report.web.model.UtilityReportResponse;
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
	
	@Autowired
	private SRService srService;
	
	@Autowired
	private UserService userService;

	@PostMapping("/incentive")
	@ResponseBody
	public ResponseEntity<ReportResponse> getIncentiveReport(@ModelAttribute IncentiveReportCriteria incentiveReportCriteria,
			@RequestBody @Valid final RequestInfoWrapper requestInfoWrapper) {
		
		IncentiveResponse incentiveResponse = incentiveService.getIncentiveReport(requestInfoWrapper.getRequestInfo(), incentiveReportCriteria);
		
		ReportResponse response = ReportResponse.builder().incentiveResponse(incentiveResponse)
				.responseInfo(responseInfoFactory.createResponseInfoFromRequestInfo(requestInfoWrapper.getRequestInfo(), true)).build();
		return new ResponseEntity<ReportResponse>(response, HttpStatus.OK);
	}
	
	@PostMapping("/srTicketDetails")
	@ResponseBody
	public ResponseEntity<ReportResponse> getSRTicketDetails(@ModelAttribute SRReportSearchCriteria srReportSearchCriteria,
			@RequestBody @Valid final RequestInfoWrapper requestInfoWrapper) {
		
		List<TicketDetails> tickets = srService.getTicketDetails(requestInfoWrapper.getRequestInfo(), srReportSearchCriteria);
		
		ReportResponse response = ReportResponse.builder().srTicketDetailsResponse(tickets)
				.responseInfo(responseInfoFactory.createResponseInfoFromRequestInfo(requestInfoWrapper.getRequestInfo(), true)).build();
		return new ResponseEntity<ReportResponse>(response, HttpStatus.OK);
	}
	
	@PostMapping("/_generateUserDetailsReport")
	public void generateUserDetailsReport(@Valid @RequestBody RequestInfoWrapper requestInfoWrapper,
			@Valid @ModelAttribute UserSearchCriteria userSearchCriteria) {

		userService.generateUserDetailsReport(requestInfoWrapper, userSearchCriteria);
	}

	@PostMapping(value = "/_getUserDetailsReport")
	public ResponseEntity<UtilityReportResponse> getUserDetailsReport(@Valid @RequestBody RequestInfoWrapper requestInfoWrapper,
			@Valid @ModelAttribute UtilityReportSearchCriteria searchCriteria) throws IOException {
		Map<String, Object> responseMap = userService.getUserDetailsReport(requestInfoWrapper.getRequestInfo(), searchCriteria);

		UtilityReportResponse response = UtilityReportResponse.builder()
				.response(responseMap)
				.responseInfo(responseInfoFactory.createResponseInfoFromRequestInfo(requestInfoWrapper.getRequestInfo(), true))
				.build();
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
}
