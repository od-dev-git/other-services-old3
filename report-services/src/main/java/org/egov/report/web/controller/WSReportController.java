package org.egov.report.web.controller;

import java.util.Arrays;
import java.util.List;

import javax.validation.Valid;

import org.egov.report.service.WaterService;
import org.egov.report.util.ResponseInfoFactory;
import org.egov.report.web.model.ConsumerMasterWSReportResponse;
import org.egov.report.web.model.ConsumerPaymentHistoryResponse;
import org.egov.report.web.model.BillSummaryResponses;
import org.egov.report.web.model.EmployeeDateWiseWSCollectionResponse;
import org.egov.report.web.model.ReportResponse;
import org.egov.report.web.model.RequestInfoWrapper;
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
		List<ConsumerPaymentHistoryResponse> consumerPaymentHistoryList = Arrays.asList(ConsumerPaymentHistoryResponse.builder()
				.ulb("Cuttack")
				.ward("01")
				.consumerCode("WS/CTC/019911")
				.conumerName("Test")
				.consumerAddress("Address, Cuttack")
				.head("Water")
				.transactionDate(1659119399000L)
				.transactionId("txn9881188191")
				.employeeId("262552")
				.employeeName("Test_Employee").build());
		
		ReportResponse response = ReportResponse.builder().consumerPaymentHistoryResponse(consumerPaymentHistoryList)
				.responseInfo(responseInfoFactory.createResponseInfoFromRequestInfo(requestInfoWrapper.getRequestInfo(), true)).build();
		return new ResponseEntity<ReportResponse>(response, HttpStatus.OK);
	}
	
	@PostMapping("/waterMonthlyDemandReport")
	public ResponseEntity<ReportResponse> waterMonthlyDemandReport(@ModelAttribute WSReportSearchCriteria searchCriteria,
			@RequestBody @Valid final RequestInfoWrapper requestInfoWrapper) {
		List<WaterMonthlyDemandResponse> waterMonthlyDemandResponseList = Arrays.asList(
				WaterMonthlyDemandResponse.builder().ulb("Cuttack").ward("01").connectionNo("WS/CTC/012111").connectionType("Non-Metered").connectionHolderName("Atul Sahoo").mobile("992822037").addrss("Address").taxPriodFrom(1656617731000L).taxPeriodTo(1659289291000L).build());
		
		ReportResponse response = ReportResponse.builder().waterMonthlyDemandResponse(waterMonthlyDemandResponseList)
				.responseInfo(responseInfoFactory.createResponseInfoFromRequestInfo(requestInfoWrapper.getRequestInfo(), true)).build();
		return new ResponseEntity<ReportResponse>(response, HttpStatus.OK);
	}
	
	@PostMapping("/waterNewConsumerMonthlyReport")
	public ResponseEntity<ReportResponse> waterNewConsumerMonthlyReport(@ModelAttribute WSReportSearchCriteria searchCriteria,
			@RequestBody @Valid final RequestInfoWrapper requestInfoWrapper) {
		
		List<WaterNewConsumerMonthlyResponse> waterNewConsumerMonthlyResponseList = Arrays.asList(
				WaterNewConsumerMonthlyResponse.builder().ulb("Cuttack").ward("01").connectionNo("WS/CTC/012111")
				.connectionType("Non-Metered").userName("Mohan").applicationNo("WS_AP/CTC/2022-23/000001").connectionCategory("Permanent")
				.connectionFacility("Water").connectionPurpose("Domestic").userAddress("Address").date(1660043998000L).sanctionDate(1660043998000L)
				.mobile("9779689189").build());
		
		ReportResponse response = ReportResponse.builder().responseInfo(responseInfoFactory.createResponseInfoFromRequestInfo(requestInfoWrapper.getRequestInfo(), true))
				.waterNewConsumerMonthlyResponses(waterNewConsumerMonthlyResponseList).build();
		
		return new ResponseEntity<ReportResponse>(response, HttpStatus.OK);
	}
	
	@PostMapping("/wsConsumerHistoryReport")
	public ResponseEntity<ReportResponse> wsConsumerHistoryReport(@ModelAttribute WSReportSearchCriteria searchCriteria,
			@RequestBody @Valid final RequestInfoWrapper requestInfoWrapper) {
		
		return new ResponseEntity<ReportResponse>( HttpStatus.OK);
	}
	
	@PostMapping("/consumerBillHistoryReport")
	public ResponseEntity<ReportResponse> consumerBillHistoryReport(@ModelAttribute WSReportSearchCriteria searchCriteria,
			@RequestBody @Valid final RequestInfoWrapper requestInfoWrapper) {
		
		return new ResponseEntity<ReportResponse>( HttpStatus.OK);
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
