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
import org.egov.report.config.ReportServiceConfiguration;
import org.egov.report.model.UtilityReportDetails;
import org.egov.report.model.UtilityReportSearchCriteria;
import org.egov.report.repository.FSMReportRepository;
import org.egov.report.util.FSMDataMartExcelGenerator;
import org.egov.report.validator.FSMReportEnrichAndValidator;
import org.egov.report.web.model.UtilityReportRequest;
import org.egov.tracer.model.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class FSMReportService {

	@Autowired
	private FileStoreService fileStoreService;

	@Autowired
	private ReportServiceConfiguration config;

	@Autowired
	private FSMReportRepository repository;

	@Autowired
	private FSMReportEnrichAndValidator reportEnrichAndValidator;

	public void generateDataMartReport(RequestInfo requestInfo, UtilityReportSearchCriteria searchCriteria)
			throws Exception {

		reportEnrichAndValidator.validateFSMDataMartReport(searchCriteria);
		
		String reportType = searchCriteria.getReportType();
		List<UtilityReportDetails> reportList = repository.isReportExist(reportType);

		reportEnrichAndValidator.validateRecentReport(reportList);

		// new record inserted for each report type
		if (reportList.isEmpty()) {
			UtilityReportDetails reportDetails = reportEnrichAndValidator.enrichSaveReport(requestInfo, reportType);
			repository.saveReportDetails(new UtilityReportRequest(requestInfo, reportDetails));
			reportList.add(reportDetails);
		}

		// Generate report, then upload in filestore and update report details
		Executor executor = Executors.newSingleThreadExecutor();
		executor.execute(() -> {
			log.info("Report Type in Thread Executor : " + reportType);
			String fileName = generateFileName(reportType);
			// Local setup for generate utility report
			// create a folder named "tmp" in local drive
			// For uat and prod
			File temporaryfile = getTemporaryFile(reportType, fileName);
			log.debug("Temporary File path in Thread Executor : " + temporaryfile.getAbsolutePath());
			try {
				switch (reportType) {
				case "DATAMART_REPORT":
					generateDataMartReport(temporaryfile, searchCriteria);
					break;

				default:
					throw new CustomException("INVALID_REPORT_TYPE", "Kindly check the Report Type !!!");
				}

				// push temporary file into file store
				log.info("uploading file to filestore...");
				Object filestoreDetails = fileStoreService.upload(temporaryfile, fileName,
						MediaType.MULTIPART_FORM_DATA_VALUE, "BPA", "od");
				System.out.println("In tread file store" + filestoreDetails.toString());

				// enrich and update Utility Report Details
				UtilityReportRequest reportDetailsRequest = reportEnrichAndValidator.enrichUpdateReport(requestInfo,
						reportList, fileName, filestoreDetails);
				repository.updateReportDetails(reportDetailsRequest);

			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}

	private String generateFileName(String fileInitialName) {
		String fileFormat = ".xlsx";
		String fileSeparator = "_";
		DateFormat dateFormat = new SimpleDateFormat("yyyy_MM_dd_hhmmss");
		dateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));
		String currentDateTime = dateFormat.format(new Date());
		return fileInitialName + fileSeparator + currentDateTime + fileFormat;
	}
	
	private File getTemporaryFile(String reportType, String fileName) {
		//Temp file location
		File currentDirFile = new File(config.getReportTemporaryLocation());
		String currentPath = currentDirFile.getAbsolutePath();
		String absolutePath = currentPath + File.separator + reportType;
		log.info("Temporary storage Path : " + absolutePath);
		File directory = new File(absolutePath);
		if (directory.exists()) {
			try {
				FileUtils.deleteDirectory(directory);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		directory.mkdir();
		File temporaryfile = new File(absolutePath, fileName);
		
		if (!temporaryfile.exists()) {
			try {
				temporaryfile.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return temporaryfile;
	}
	
	public void generateDataMartReport(File temporaryfile, UtilityReportSearchCriteria searchCriteria) throws IOException {
		List<Map<String, Object>> dataMartList = repository.getDataMartReport(searchCriteria);
		log.info("Total Data Mart Report Entries : " + dataMartList.size());
    	FSMDataMartExcelGenerator generator = new FSMDataMartExcelGenerator(dataMartList);
    	//Generate into the temporary file... 
    	generator.generateExcelFile(temporaryfile);
	}

	public Map<String, Object> getDataMartReport(RequestInfo requestInfo,
			@Valid UtilityReportSearchCriteria searchCriteria) {
		
		Map<String, Object> response = new HashMap<>();
		//LinkedHashMap responseMap = null;
		List<UtilityReportDetails> reportList = repository.isReportExist(searchCriteria.getReportType());
		
		reportEnrichAndValidator.validateDownloadReport(reportList, response);
		
		//new record inserted for each report type
		if(!reportList.isEmpty()) {
			UtilityReportDetails reportDetails = reportList.get(0);
			response.put("reportsDetails", reportDetails);
			//fetch file from file store
			//responseMap = (LinkedHashMap) fileStoreService.fetch(reportDetails.getFileStoreId(), reportDetails.getTenantId());
			//response.put("responseMap", new JSONObject(responseMap).toString());
		}  else {
			response.put("Message", "Kindly wait for sometime, Report not generated yet. !!!");
			throw new CustomException("INVALID_REPORT_FETCH", "Kindly wait for sometime, Report not generated yet. !!!");
		}
		
		return response;
	}

}
