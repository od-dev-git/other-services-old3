package org.egov.report.web.controller;

import java.util.Arrays;
import java.util.List;

import javax.validation.Valid;

import org.egov.report.service.WaterService;
import org.egov.report.util.ResponseInfoFactory;
import org.egov.report.web.model.ConsumerMasterWSReportResponse;
import org.egov.report.web.model.ConsumerPaymentHistoryResponse;
import org.egov.report.web.model.BillSummaryResponses;
import org.egov.report.web.model.ConsumerBillHistoryResponse;
import org.egov.report.web.model.EmployeeDateWiseWSCollectionResponse;
import org.egov.report.web.model.ReportResponse;
import org.egov.report.web.model.RequestInfoWrapper;
import org.egov.report.web.model.WSConsumerHistoryResponse;
import org.egov.report.web.model.WSReportSearchCriteria;
import org.egov.report.web.model.WaterMonthlyDemandResponse;
import org.egov.report.web.model.WaterNewConsumerMonthlyResponse;
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
	
	@PostMapping("/consumerMaster")
	public ResponseEntity<ReportResponse> consumerMaster(@ModelAttribute WSReportSearchCriteria searchCriteria,
			@RequestBody @Valid final RequestInfoWrapper requestInfoWrapper){
		
		List<ConsumerMasterWSReportResponse> consumerMasterWSReportResponses = waterService.consumerMasterWSReport(requestInfoWrapper.getRequestInfo(), searchCriteria);
		
		ReportResponse response = ReportResponse.builder().consumerMasterWSReportResponses(consumerMasterWSReportResponses)
				.responseInfo(responseInfoFactory.createResponseInfoFromRequestInfo(requestInfoWrapper.getRequestInfo(), true)).build();
		return new ResponseEntity<ReportResponse>(response, HttpStatus.OK);
	}
		
	@PostMapping("/consumerPaymentHistory")
	public ResponseEntity<ReportResponse> consumerPaymentHistory(@ModelAttribute WSReportSearchCriteria searchCriteria,
			@RequestBody @Valid final RequestInfoWrapper requestInfoWrapper) {
		
		List<ConsumerPaymentHistoryResponse> consumerPaymentHistoryList = waterService.consumerPaymentHistory(requestInfoWrapper.getRequestInfo(), searchCriteria);
		
		ReportResponse response = ReportResponse.builder().consumerPaymentHistoryResponse(consumerPaymentHistoryList)
				.responseInfo(responseInfoFactory.createResponseInfoFromRequestInfo(requestInfoWrapper.getRequestInfo(), true)).build();
		return new ResponseEntity<ReportResponse>(response, HttpStatus.OK);
	}
	
	@PostMapping("/waterMonthlyDemandReport")
	public ResponseEntity<ReportResponse> waterMonthlyDemandReport(@ModelAttribute WSReportSearchCriteria searchCriteria,
			@RequestBody @Valid final RequestInfoWrapper requestInfoWrapper) {
		
		List<WaterMonthlyDemandResponse> waterMonthlyDemandResponseList = waterService.waterMonthlyDemandReport(requestInfoWrapper.getRequestInfo(),searchCriteria);
		
		ReportResponse response = ReportResponse.builder().waterMonthlyDemandResponse(waterMonthlyDemandResponseList)
				.responseInfo(responseInfoFactory.createResponseInfoFromRequestInfo(requestInfoWrapper.getRequestInfo(), true)).build();
		return new ResponseEntity<ReportResponse>(response, HttpStatus.OK);
	}
	
	@PostMapping("/waterNewConsumerMonthlyReport")
	public ResponseEntity<ReportResponse> waterNewConsumerMonthlyReport(@ModelAttribute WSReportSearchCriteria searchCriteria,
			@RequestBody @Valid final RequestInfoWrapper requestInfoWrapper) {
		
		List<WaterNewConsumerMonthlyResponse> waterNewConsumerMonthlyResponseList = waterService.waterNewConsumerMonthlyReport(requestInfoWrapper.getRequestInfo(), searchCriteria);
		
		ReportResponse response = ReportResponse.builder().responseInfo(responseInfoFactory.createResponseInfoFromRequestInfo(requestInfoWrapper.getRequestInfo(), true))
				.waterNewConsumerMonthlyResponses(waterNewConsumerMonthlyResponseList).build();
		
		return new ResponseEntity<ReportResponse>(response, HttpStatus.OK);
	}
	
	@PostMapping("/wsConsumerHistoryReport")
	public ResponseEntity<ReportResponse> wsConsumerHistoryReport(@ModelAttribute WSReportSearchCriteria searchCriteria,
			@RequestBody @Valid final RequestInfoWrapper requestInfoWrapper) {
		
		List<WSConsumerHistoryResponse> wsConsumerHistoryResponseList = waterService.wsConsumerHistoryReport(requestInfoWrapper.getRequestInfo(), searchCriteria);
				
		ReportResponse response = ReportResponse.builder().wsConsumerHistoryReport(wsConsumerHistoryResponseList).responseInfo(
				responseInfoFactory.createResponseInfoFromRequestInfo(requestInfoWrapper.getRequestInfo(), true)).build();
		
		
		return new ResponseEntity<ReportResponse>(response,HttpStatus.OK);
	}
	
	@PostMapping("/consumerBillHistoryReport")
	public ResponseEntity<ReportResponse> consumerBillHistoryReport(@ModelAttribute WSReportSearchCriteria searchCriteria,
			@RequestBody @Valid final RequestInfoWrapper requestInfoWrapper) {
		
		List<ConsumerBillHistoryResponse> consumerBillHistoryResponse = waterService.consumerBillHistoryReport(requestInfoWrapper.getRequestInfo() ,searchCriteria);
		
		ReportResponse response = ReportResponse.builder().consumerBillHistoryResponse(consumerBillHistoryResponse)
				.responseInfo(responseInfoFactory.createResponseInfoFromRequestInfo(requestInfoWrapper.getRequestInfo(), true)).build();
		
		return new ResponseEntity<ReportResponse>(response,HttpStatus.OK);
	}

	@PostMapping("/billSummary")
	public ResponseEntity<ReportResponse> billSummary(@ModelAttribute WSReportSearchCriteria searchCriteria,
			@RequestBody @Valid final RequestInfoWrapper requestInfoWrapper) {
		List<BillSummaryResponses> billSummaryResponses = waterService.billSummary(requestInfoWrapper.getRequestInfo() ,searchCriteria);
		
		ReportResponse response = ReportResponse.builder().billSummaryResponses(billSummaryResponses)
				.responseInfo(responseInfoFactory.createResponseInfoFromRequestInfo(requestInfoWrapper.getRequestInfo(), true)).build();
		return new ResponseEntity<ReportResponse>(response, HttpStatus.OK);
	}
}
