package org.egov.report.web.controller;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.egov.report.model.DCBSearchCriteria;
import org.egov.report.model.UserSearchCriteria;
import org.egov.report.model.UtilityReportSearchCriteria;
import org.egov.report.service.DCBReportService;
import org.egov.report.service.PropertyService;
import org.egov.report.util.ResponseInfoFactory;
import org.egov.report.web.model.PTAssessmentSearchCriteria;
import org.egov.report.web.model.PropertyDetailsResponse;
import org.egov.report.web.model.PropertyDetailsSearchCriteria;
import org.egov.report.web.model.PropertyWiseCollectionResponse;
import org.egov.report.web.model.PropertyWiseDemandResponse;
import org.egov.report.web.model.ReportResponse;
import org.egov.report.web.model.RequestInfoWrapper;
import org.egov.report.web.model.TaxCollectorWiseCollectionResponse;
import org.egov.report.web.model.ULBWiseTaxCollectionResponse;
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
@RequestMapping("/reports/pt")
public class PropertyReportController {
	
	@Autowired
	private ResponseInfoFactory responseInfoFactory;

	@Autowired
	private PropertyService propertyService;
	
	@Autowired
	private DCBReportService dcbReportService;

	@PostMapping("/propertyDetails")
	public ResponseEntity<ReportResponse> propertyDetails(@ModelAttribute PropertyDetailsSearchCriteria searchCriteria,
			@RequestBody @Valid final RequestInfoWrapper requestInfoWrapper) {
		List<PropertyDetailsResponse> propertyDetailsResponses = propertyService.getPropertyDetails(requestInfoWrapper.getRequestInfo() ,searchCriteria);

		ReportResponse response = ReportResponse.builder().propertyDetailsResponse(propertyDetailsResponses)
				.responseInfo(responseInfoFactory.createResponseInfoFromRequestInfo(requestInfoWrapper.getRequestInfo(), true)).build();
		return new ResponseEntity<ReportResponse>(response, HttpStatus.OK);
	}
	
	@PostMapping("/propertyWiseCollectionReport")
	public ResponseEntity<ReportResponse> propertyWiseCollectionReport(@ModelAttribute PropertyDetailsSearchCriteria searchCriteria,
			@RequestBody @Valid final RequestInfoWrapper requestInfoWrapper) {
		
		List<PropertyWiseCollectionResponse> propertyWiseCollectionResponse = propertyService.getpropertyCollectionReport(requestInfoWrapper.getRequestInfo() ,searchCriteria);
		
		ReportResponse response = ReportResponse.builder().propertyWiseCollectionResponse(propertyWiseCollectionResponse)
				.responseInfo(responseInfoFactory.createResponseInfoFromRequestInfo(requestInfoWrapper.getRequestInfo(), true)).build();
		
		return new ResponseEntity<ReportResponse>(response,HttpStatus.OK);
	}
	
	@PostMapping("/propertyWiseDemandReport")
	public ResponseEntity<ReportResponse> propertyWiseDemandReport(@ModelAttribute PropertyDetailsSearchCriteria searchCriteria,
			@RequestBody @Valid final RequestInfoWrapper requestInfoWrapper) {
		
		List<PropertyWiseDemandResponse> propertyWiseDemandResponse = propertyService.getpropertyWiseDemandReport(requestInfoWrapper.getRequestInfo() ,searchCriteria);
		
		ReportResponse response = ReportResponse.builder().propertyWiseDemandResponse(propertyWiseDemandResponse)
				.responseInfo(responseInfoFactory.createResponseInfoFromRequestInfo(requestInfoWrapper.getRequestInfo(), true)).build();
		
		return new ResponseEntity<ReportResponse>(response,HttpStatus.OK);
	}
	
	@PostMapping("ulbWiseTaxCollectionReport")
	public ResponseEntity<ReportResponse> ulbWiseTaxCollectionReport(@ModelAttribute PropertyDetailsSearchCriteria searchCriteria,
			@RequestBody @Valid final RequestInfoWrapper requestInfoWrapper) {
		
		List<ULBWiseTaxCollectionResponse> ulbBWiseTaxCollectionResponse = propertyService.getulbWiseTaxCollections(requestInfoWrapper.getRequestInfo() ,searchCriteria);
		
		ReportResponse response = ReportResponse.builder().ulbWiseTaxCollectionResponse(ulbBWiseTaxCollectionResponse)
				.responseInfo(responseInfoFactory.createResponseInfoFromRequestInfo(requestInfoWrapper.getRequestInfo(), true)).build();
		
		return new ResponseEntity<ReportResponse>(response,HttpStatus.OK);
	}
	
	@PostMapping("taxCollectorWiseCollectionReport")
	public ResponseEntity<ReportResponse> taxCollectorWiseCollectionReport(@ModelAttribute PropertyDetailsSearchCriteria searchCriteria,
			@RequestBody @Valid final RequestInfoWrapper requestInfoWrapper) {
		
		List<TaxCollectorWiseCollectionResponse> taxCollectorWiseCollectionResponse = propertyService.getTaxCollectorWiseCollections(requestInfoWrapper.getRequestInfo() ,searchCriteria);
		
		ReportResponse response = ReportResponse.builder().taxCollectorWiseCollectionResponse(taxCollectorWiseCollectionResponse)
				.responseInfo(responseInfoFactory.createResponseInfoFromRequestInfo(requestInfoWrapper.getRequestInfo(), true)).build();
		
		return new ResponseEntity<ReportResponse>(response,HttpStatus.OK);
	}
	
	@PostMapping("/_generateDCBReport")
	public void generateDCBReport(@Valid @RequestBody RequestInfoWrapper requestInfoWrapper,
			@Valid @ModelAttribute DCBSearchCriteria dcbSearchCriteria) {
		
		dcbReportService.generateDCBReport(requestInfoWrapper, dcbSearchCriteria);
	}
	
	@PostMapping(value = "/_getDCBReport")
	public ResponseEntity<UtilityReportResponse> getDCBReport(@Valid @RequestBody RequestInfoWrapper requestInfoWrapper,
			@Valid @ModelAttribute UtilityReportSearchCriteria searchCriteria) throws IOException {
		Map<String, Object> responseMap = dcbReportService.getDCBReport(requestInfoWrapper.getRequestInfo(), searchCriteria);
		
		UtilityReportResponse response = UtilityReportResponse.builder()
				.response(responseMap)
				.responseInfo(responseInfoFactory.createResponseInfoFromRequestInfo(requestInfoWrapper.getRequestInfo(), true))
				.build();
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	@PostMapping("/_generatePTAssessmentReport")
	public void generatePTAssessmentReport(@Valid @RequestBody RequestInfoWrapper requestInfoWrapper,
			@Valid @ModelAttribute PTAssessmentSearchCriteria ptAssessmentSearchCriteria) {
		
		propertyService.generatePTAssessmentReport(requestInfoWrapper, ptAssessmentSearchCriteria);
	}
	
	@PostMapping(value = "/_getPTAssessmentReport")
	public ResponseEntity<UtilityReportResponse> getPTAssessmentReport(@Valid @RequestBody RequestInfoWrapper requestInfoWrapper,
			@Valid @ModelAttribute UtilityReportSearchCriteria searchCriteria) throws IOException {
		Map<String, Object> responseMap = propertyService.getPTAssessmentsReporteport(requestInfoWrapper.getRequestInfo(), searchCriteria);
		
		UtilityReportResponse response = UtilityReportResponse.builder()
				.response(responseMap)
				.responseInfo(responseInfoFactory.createResponseInfoFromRequestInfo(requestInfoWrapper.getRequestInfo(), true))
				.build();
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	@PostMapping("/_generatePTDDNNoReport")
	public void generateUserDetailsReport(@Valid @RequestBody RequestInfoWrapper requestInfoWrapper,
			@Valid @ModelAttribute PTAssessmentSearchCriteria userSearchCriteria) {
		
		propertyService.generatePTDDNNoReport(requestInfoWrapper, userSearchCriteria);
	}
	
	@PostMapping(value = "/_getPTDDNNoReport")
	public ResponseEntity<UtilityReportResponse> getPTDDNNoReport(@Valid @RequestBody RequestInfoWrapper requestInfoWrapper,
			@Valid @ModelAttribute UtilityReportSearchCriteria searchCriteria) throws IOException {
		Map<String, Object> responseMap = propertyService.getPTDDNNoReport(requestInfoWrapper.getRequestInfo(), searchCriteria);
		
		UtilityReportResponse response = UtilityReportResponse.builder()
				.response(responseMap)
				.responseInfo(responseInfoFactory.createResponseInfoFromRequestInfo(requestInfoWrapper.getRequestInfo(), true))
				.build();
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
}
