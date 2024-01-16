package org.egov.report.service;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import javax.validation.Valid;

import org.apache.commons.io.FileUtils;
import org.egov.common.contract.request.RequestInfo;
import org.egov.report.model.UtilityReportDetails;
import org.egov.report.model.UtilityReportSearchCriteria;
import org.egov.report.repository.BPAReportRepository;
import org.egov.report.util.ApplicationsReportExcelGenerator;
import org.egov.report.util.PaymetsReportExcelGenerator;
import org.egov.report.validator.BPAReportEnrichAndValidator;
import org.egov.report.web.model.UtilityReportRequest;
import org.egov.tracer.model.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class BPAReportService {

	@Autowired
	private BPAReportEnrichAndValidator reportEnrichValidator;
	
	@Autowired
	private BPAReportRepository repository;
	
	@Autowired
	private FileStoreService fileStoreService;
	
	
	/**
	 * Service layer for get All Payments Report
	 * 
	 * @param response
	 * @return List of Payments Report
	 * @throws Exception 
	 */
	public void generateUtilityReport(RequestInfo requestInfo, UtilityReportSearchCriteria searchCriteria) throws Exception {
		
		String reportType = searchCriteria.getReportType();
		List<UtilityReportDetails> reportList = repository.isReportExist(reportType);
		
//		reportEnrichValidator.validateRecentReport(reportList);
		
		//new record inserted for each report type
		if(reportList.isEmpty()) {
			UtilityReportDetails reportDetails =  reportEnrichValidator.enrichSaveReport(requestInfo, reportType);
			repository.saveReportDetails(new UtilityReportRequest(requestInfo, reportDetails));
			reportList.add(reportDetails);
		} 
		
		//Generate report, then upload in filestore and update report details
		Executor executor = Executors.newSingleThreadExecutor();
		executor.execute(() -> {
			String fileName = generateFileName(reportType);
			String absolutePath = getAbsolutePath(reportType);
			File temporaryfile = new File(absolutePath, fileName);
	        try {
	        	
	        	switch (reportType) {
	    		case "PAYMENTS_REPORT":
	    			generateAllPaymentsReport(temporaryfile);
	    			break;

	    		case "APPLICATIONS_REPORT":
	    			generateAllApplicationsReport(temporaryfile);
	    			break;

	    		default:
	    			throw new CustomException("INVALID_REPORT_TYPE", "Kindly check the Report Type !!!");
	    		}
	        	
	        	//push temporary file into file store
				log.info("uploading file to filestore");
				Object filestoreDetails = fileStoreService.upload(temporaryfile, fileName, MediaType.MULTIPART_FORM_DATA_VALUE, "BPA", "od");
				System.out.println("In tread file store" + filestoreDetails.toString());
	        	
				//enrich and update Utility Report Details
				UtilityReportRequest reportDetailsRequest =  reportEnrichValidator.enrichUpdateReport(requestInfo, reportList, fileName, filestoreDetails);
				repository.updateReportDetails(reportDetailsRequest);
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
			
		
	}


	
	/**
	 * Helper method for creating temporary folder in project
	 * and delete old temporary file from project
	 * 
	 * @param temporaryFolder
	 * @return absolutePath
	 */
	private String getAbsolutePath(String temporaryFolder) {
		String currentPath = System.getProperty("user.dir");
		String absolutePath = currentPath + File.separator + temporaryFolder;
		File directory = new File(absolutePath);
		if (directory.exists()) {
			try {
				FileUtils.deleteDirectory(directory);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} 
		directory.mkdir();

		return absolutePath;
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
		return fileInitialName + fileSeparator + currentDateTime + fileFormat;
	}


	/**
	 * Generate all Payments report and store in excel format
	 * 
	 * @param temporaryfile
	 * @throws IOException
	 */
	public void generateAllPaymentsReport(File temporaryfile) throws IOException {
		List<Map<String, Object>> paymentDetailsList = repository.getAllPaymentsReport();
    	
    	PaymetsReportExcelGenerator generator = new PaymetsReportExcelGenerator(paymentDetailsList);
    	//Generate into the temporary file... 
    	generator.generateExcelFile(temporaryfile);
	}
	
	
	/**
	 * Service layer for get All Applications Report
	 * 
	 * @param temporaryfile
	 */
	public void generateAllApplicationsReport(File temporaryfile) throws IOException {
		List<Map<String, Object>> applicationsDetailsList = repository.getAllApplicationsReport();
    	
		ApplicationsReportExcelGenerator generator = new ApplicationsReportExcelGenerator(applicationsDetailsList);
    	//Generate into the temporary file... 
    	generator.generateExcelFile(temporaryfile);
	}
	
	
	
	/**
	 * Search and download latest report file from db
	 * 
	 * @param requestInfo
	 * @param searchCriteria
	 * @return response
	 */
	public Map<String, Object> getUtilityReport(RequestInfo requestInfo, @Valid UtilityReportSearchCriteria searchCriteria) {
		Map<String, Object> response = new HashMap<>();
		
		List<UtilityReportDetails> reportList = repository.isReportExist(searchCriteria.getReportType());
		File reportFile = null;
		
		reportEnrichValidator.validateDownloadReport(reportList, response);
		
		//new record inserted for each report type
		if(!reportList.isEmpty()) {
			UtilityReportDetails reportDetails = reportList.get(0);
			//fetch file from file store
			reportFile = fileStoreService.fetch(reportDetails.getFileStoreId(), "BPA", reportDetails.getFileName(), reportDetails.getTenantId());
			response.put("file", reportFile);
		}  else {
			response.put("Message", "Kindly wait for sometime, Report not generated yet. !!!");
			throw new CustomException("INVALID_REPORT_FETCH", "Kindly wait for sometime, Report not generated yet. !!!");
		}
		
		response.put("reportsDetails", reportList);
		return response;
	}
}
