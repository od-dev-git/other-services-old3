package org.egov.report.web.controller;

import java.util.Arrays;
import java.util.List;

import javax.validation.Valid;

import org.egov.report.web.model.PropertyDetailsResponse;
import org.egov.report.service.PropertyService;
import org.egov.report.util.ResponseInfoFactory;
import org.egov.report.web.model.BillSummaryResponses;
import org.egov.report.web.model.ConsumerBillHistoryResponse;
import org.egov.report.web.model.PropertyDetailsSearchCriteria;
import org.egov.report.web.model.PropertyWiseCollectionResponse;
import org.egov.report.web.model.PropertyWiseDemandResponse;
import org.egov.report.web.model.ReportResponse;
import org.egov.report.web.model.RequestInfoWrapper;
import org.egov.report.web.model.TaxCollectorWiseCollectionResponse;
import org.egov.report.web.model.ULBWiseTaxCollectionResponse;
import org.egov.report.web.model.WSReportSearchCriteria;
import org.egov.report.web.model.ULBWiseTaxCollectionResponse;
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

	
	
	@PostMapping("/propertyWiseCollectionReport")
	public ResponseEntity<ReportResponse> propertyWiseCollectionReport(@ModelAttribute PropertyDetailsSearchCriteria searchCriteria,
			@RequestBody @Valid final RequestInfoWrapper requestInfoWrapper) {
		
		List<PropertyWiseCollectionResponse> propertyWiseCollectionResponse = Arrays.asList(
				PropertyWiseCollectionResponse.builder().consumercode("107-FKM-2021-04-05-000002")
				.oldpropertyid("12234556").ward("05").name("Shahrukh").mobilenumber("9090909098")
				.due("87").amountpaid("32").currentdue("55").receiptnumber("04/2021-22/0001892")
				.receiptdate("06-Apr-2021 11:09:28").paymentMode("CASH").build()
				);
		
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
		
		List<TaxCollectorWiseCollectionResponse> taxCollectorWiseCollectionResponse = propertyService.gettaxCollectorWiseCollections(requestInfoWrapper.getRequestInfo() ,searchCriteria);
		
		ReportResponse response = ReportResponse.builder().taxCollectorWiseCollectionResponse(taxCollectorWiseCollectionResponse)
				.responseInfo(responseInfoFactory.createResponseInfoFromRequestInfo(requestInfoWrapper.getRequestInfo(), true)).build();
		
		return new ResponseEntity<ReportResponse>(response,HttpStatus.OK);
	}
}
