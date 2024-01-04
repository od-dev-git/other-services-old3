package org.egov.report.service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import javax.servlet.http.HttpServletResponse;

import org.egov.report.repository.BPAReportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BPAReportService {

	@Autowired
	private BPAReportRepository repository;

	
	/**
	 * Service layer for get All Payments Report
	 * 
	 * @param response
	 * @return List of Payments Report
	 */
	public List<Map<String, Object>> getAllPaymentsReport(HttpServletResponse response) {
		String fileInitialName = "Payments_Report";
		setResponseHeader(response, generateFileName(fileInitialName));
        
		return repository.getAllPaymentsReport();
	}

	
	
	/**
	 * Set Response Header
	 * 
	 * @param response
	 * @param fileName
	 */
	private void setResponseHeader(HttpServletResponse response, String fileName) {
        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=" + fileName;
        response.setHeader(headerKey, headerValue);
        response.setContentType("application/octet-stream");
	}

	

	/**
	 * Generate Full File Name using current date time and initial file name
	 * 
	 * @param fileInitialName
	 * @return exported File Name
	 */
	private String generateFileName(String fileInitialName) {
		String fileFormat = ".xlsx";
		String fileSeparator = "_";
		DateFormat dateFormat = new SimpleDateFormat("yyyy_MM_dd_hhmmss");
		dateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));
		String currentDateTime = dateFormat.format(new Date());
		return fileInitialName+ fileSeparator + currentDateTime + fileFormat;
	}


	
	/**
	 * Service layer for get All Applications Report
	 * 
	 * @param response
	 * @return List of Applications Report
	 */
	public List<Map<String, Object>> getAllApplicationsReport(HttpServletResponse response) {
		String fileInitialName = "Applications_Report";
		setResponseHeader(response, generateFileName(fileInitialName));
        
		return repository.getAllApplicationsReport();
	}
	
}
