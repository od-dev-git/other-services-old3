package org.egov.report.web.controller;

import java.util.List;

import javax.validation.Valid;

import org.egov.report.web.model.PropertyDetailsResponse;
import org.egov.report.service.PropertyService;
import org.egov.report.util.ResponseInfoFactory;
import org.egov.report.web.model.BillSummaryResponses;
import org.egov.report.web.model.PropertyDetailsSearchCriteria;
import org.egov.report.web.model.ReportResponse;
import org.egov.report.web.model.RequestInfoWrapper;
import org.egov.report.web.model.WSReportSearchCriteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/reports/pt")
public class PropertyReportController {
	

	@Autowired
	private ResponseInfoFactory responseInfoFactory;

	@Autowired
	private PropertyService propertyService;

	@PostMapping("/propertyDetails")
	public ResponseEntity<ReportResponse> propertyDetails(@ModelAttribute PropertyDetailsSearchCriteria searchCriteria,
			@RequestBody @Valid final RequestInfoWrapper requestInfoWrapper) {
		List<PropertyDetailsResponse> propertyDetailsResponses = propertyService.getPropertyDetails(requestInfoWrapper.getRequestInfo() ,searchCriteria);

		ReportResponse response = ReportResponse.builder().propertyDetailsResponse(propertyDetailsResponses)
				.responseInfo(responseInfoFactory.createResponseInfoFromRequestInfo(requestInfoWrapper.getRequestInfo(), true)).build();
		return new ResponseEntity<ReportResponse>(response, HttpStatus.OK);
	}

}
