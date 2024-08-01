package org.egov.report.web.controller;

import java.util.List;
import javax.validation.Valid;
import org.egov.report.service.TLService;
import org.egov.report.util.ResponseInfoFactory;
import org.egov.report.web.model.ReportResponse;
import org.egov.report.web.model.RequestInfoWrapper;
import org.egov.report.web.model.TradeLicenseEscallationDetailsResponse;
import org.egov.report.web.model.TradeLicenseSearchCriteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/reports/tl")
public class TLReportController {

	@Autowired
	private ResponseInfoFactory responseInfoFactory;

	@Autowired
	private TLService tlService;

	@PostMapping("/tlAutoEscallationReport")
	public ResponseEntity<ReportResponse> tlAutoEscallationReport(
			@ModelAttribute TradeLicenseSearchCriteria searchCriteria,
			@RequestBody @Valid final RequestInfoWrapper requestInfoWrapper) {

		List<TradeLicenseEscallationDetailsResponse> tradeLicenseEscallationDetailsResponse = tlService
				.getTLAutoEscallationReport(requestInfoWrapper.getRequestInfo(), searchCriteria);

		ReportResponse response = ReportResponse.builder()
				.tradeLicenseEscallationDetailsResponse(tradeLicenseEscallationDetailsResponse)
				.responseInfo(responseInfoFactory.createResponseInfoFromRequestInfo(requestInfoWrapper.getRequestInfo(),
						true))
				.build();
		return new ResponseEntity<ReportResponse>(response, HttpStatus.OK);
	}

}