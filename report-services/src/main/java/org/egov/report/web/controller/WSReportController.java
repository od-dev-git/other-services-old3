package org.egov.report.web.controller;

import java.util.List;

import javax.validation.Valid;

import org.egov.report.service.WaterService;
import org.egov.report.util.ResponseInfoFactory;
import org.egov.report.web.model.EmployeeDateWiseWSCollectionResponse;
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
@RequestMapping("/reports/ws")
public class WSReportController {
	
	@Autowired
	private ResponseInfoFactory responseInfoFactory;
	
	@Autowired
	private WaterService waterService;
	
	@PostMapping("/employeeDateWiseWSCollection")
	public ResponseEntity<ReportResponse> employeeDateWiseWSCollection(@ModelAttribute WSReportSearchCriteria searchCriteria,
			@RequestBody @Valid final RequestInfoWrapper requestInfoWrapper) {
		List<EmployeeDateWiseWSCollectionResponse> dateWiseWSCollectionResponses = waterService.employeeDateWiseWSCollection(requestInfoWrapper.getRequestInfo() ,searchCriteria);
		
		ReportResponse response = ReportResponse.builder().employeeDateWiseWSCollectionResponse(dateWiseWSCollectionResponses)
				.responseInfo(responseInfoFactory.createResponseInfoFromRequestInfo(requestInfoWrapper.getRequestInfo(), true)).build();
		return new ResponseEntity<ReportResponse>(response, HttpStatus.OK);
	}

}
