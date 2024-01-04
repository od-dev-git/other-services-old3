package org.egov.report.web.controller;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.egov.report.service.BPAReportService;
import org.egov.report.util.ApplicationsReportExcelGenerator;
import org.egov.report.util.PaymetsReportExcelGenerator;
import org.egov.report.web.model.RequestInfoWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/reports/bpa")
public class BPAReportController {

	@Autowired
	private BPAReportService reportService;
	
	
	/**
	 * Get all payments report and download in excel format
	 * 
	 * @param requestInfoWrapper
	 * @param response
	 * @throws IOException
	 */
	@PostMapping(value = "/_getAllPaymentsReport")
	public void getAllPaymentReport(@Valid @RequestBody RequestInfoWrapper requestInfoWrapper, HttpServletResponse response) throws IOException {
		List<Map<String, Object>> paymentDetailsList = reportService.getAllPaymentsReport(response);
        
		PaymetsReportExcelGenerator generator = new PaymetsReportExcelGenerator(paymentDetailsList);
        generator.generateExcelFile(response);
	}
	
	
	/**
	 * Get all applications report and download in excel format
	 * 
	 * @param requestInfoWrapper
	 * @param response
	 * @throws IOException
	 */
	@PostMapping(value = "/_getAllApplicationsReport")
	public void getAllApplicationsReport(@Valid @RequestBody RequestInfoWrapper requestInfoWrapper, HttpServletResponse response) throws IOException {
		List<Map<String, Object>> applicationsDetailsList = reportService.getAllApplicationsReport(response);
        
		ApplicationsReportExcelGenerator generator = new ApplicationsReportExcelGenerator(applicationsDetailsList);
        generator.generateExcelFile(response);
	}
}
