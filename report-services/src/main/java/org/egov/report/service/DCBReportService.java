package org.egov.report.service;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.egov.common.contract.request.RequestInfo;
import org.egov.report.config.ReportServiceConfiguration;
import org.egov.report.model.Advance;
import org.egov.report.model.DCBArrearDue;
import org.egov.report.model.DCBDemand;
import org.egov.report.model.DCBPayment;
import org.egov.report.model.DCBProperty;
import org.egov.report.model.DCBReportModel;
import org.egov.report.model.DCBSearchCriteria;
import org.egov.report.model.Demand;
import org.egov.report.model.Payment;
import org.egov.report.model.UtilityReportDetails;
import org.egov.report.model.UtilityReportSearchCriteria;
import org.egov.report.repository.DCBRepository;
import org.egov.report.util.DCBReportExcelGenerator;
import org.egov.report.util.ReportConstants;
import org.egov.report.util.Util;
import org.egov.report.validator.PropertyReportValidator;
import org.egov.report.web.model.RequestInfoWrapper;
import org.egov.report.web.model.UtilityReportRequest;
import org.egov.tracer.model.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class DCBReportService {
	
	@Autowired
	private PropertyReportValidator validator;
	
	@Autowired
	private Util utils;
	
	@Autowired
	private DCBRepository repository;
	
	@Autowired
	private ReportServiceConfiguration config;
	
	@Autowired
	private FileStoreService fileStoreService;
	
	@Autowired
	private EnrichmentService enrichmentService;
	
	public void generateDCBReport(@Valid RequestInfoWrapper requestInfoWrapper,
			@Valid DCBSearchCriteria dcbSearchCriteria) {

		Long startDate = 0L; Long endDate = 0L;

		RequestInfo requestInfo = requestInfoWrapper.getRequestInfo();

		String reportType = ReportConstants.DCB_REPORT_STRING;

		validator.validateDCBReport(dcbSearchCriteria);

		List<String> tenantIds = dcbSearchCriteria.getTenantIds();

		for (String tenantId : tenantIds) {

			String financialYear = dcbSearchCriteria.getFinancialYear();

			List<UtilityReportDetails> reportList = repository.isReportExist(reportType, financialYear, tenantId);

			if(validator.validateIfDCBReportAlreadyExists(reportList, tenantId)) {
				log.info("Skipping... Report already present for tenantid: "+ tenantId);
				continue;
			}

			String[] fyParts = Arrays.stream(financialYear.split("-")).toArray(String[]::new);

			String startingYear = fyParts[0];
			String endingYear = fyParts[1];

			startDate = utils.getStartingDateOfFinancialYear(startingYear);

			endDate = utils.getEndingDateOfFinancialYear(endingYear);

//			if (isRequestForCurrentFY(startingYear)) {
//				createDCBForCurrentFinancialYear(startDate, endDate, tenantId, requestInfo, reportList, reportType,
//						financialYear);
//
//			} else {
				createDCBForPastFY(startDate, endDate, tenantId, requestInfo, reportList, reportType,
						financialYear);
//			}
		}

	}

	private void createDCBForPastFY(Long startDate, Long endDate, String tenantId, RequestInfo requestInfo,
			List<UtilityReportDetails> reportList, String reportType, String financialYear) {
		
		// new record inserted for each report
		if (CollectionUtils.isEmpty(reportList)) {
			UtilityReportDetails reportDetails = enrichmentService.enrichSaveReport(requestInfo, reportType,
					financialYear, tenantId);
			repository.saveReportDetails(new UtilityReportRequest(requestInfo, reportDetails));
			reportList.add(reportDetails);
		}
		
		// fetch properties details here
		Map<String, DCBProperty> propertiesDetails = repository.getPropertyDetails(tenantId);
		
		// fetch tax amount for the input FY
		Map<String, DCBDemand> currentDemands = repository.getDemands(tenantId, startDate, endDate);
		
		// fetch tax amount for all the past FYs 
		Map<String, DCBArrearDue> arrearDue = repository.getArrearDue(tenantId, startDate);
		
		// fetch payments till date 
		Map<String, DCBPayment> paymentsTillDate = repository.getCollections(tenantId, null, endDate);
		
		// fetch payments for the input FY
		Map<String, DCBPayment> currentPayments = repository.getCollections(tenantId, startDate, endDate);
		
		// fetch collection amount till date
		Map<String, DCBDemand> demandsTillDate = repository.getDemands(tenantId, null, null);
		
		// fetch total advance amount till date
		Map<String, Advance> advanceTillDate = repository.getTotalAdvanceAmount(tenantId);
		
		Map<String, DCBReportModel> dcbReportMap = new HashMap<>();
		
		propertiesDetails.forEach((key, property) -> {

			DCBReportModel dcbReportObject = DCBReportModel.builder().oldPropertyId(property.getOldPropertyId())
					.ward(property.getWard()).propertyId(property.getPropertyId())
					.legacyId(property.getLegacyHoldingNo()).build();

			dcbReportMap.put(key, dcbReportObject);
		});
		
		for (Map.Entry<String, DCBReportModel> entry : dcbReportMap.entrySet()) {

			BigDecimal currentPayment = BigDecimal.ZERO;
			BigDecimal currentDemand = BigDecimal.ZERO;
			BigDecimal collectionsTillDate = BigDecimal.ZERO;
			BigDecimal arrearDemand = BigDecimal.ZERO;
			BigDecimal paymentsTillToday = BigDecimal.ZERO;
			BigDecimal advanceAmount = BigDecimal.ZERO;
			BigDecimal arrear = BigDecimal.ZERO;
			BigDecimal totalDemand = BigDecimal.ZERO;
			BigDecimal dueAmt = BigDecimal.ZERO;
			
			BigDecimal currentDemandCollection = BigDecimal.ZERO;
			BigDecimal arrearDemandCollection = BigDecimal.ZERO;
			BigDecimal collectionsBeforeSujog = BigDecimal.ZERO;
			
			String key = entry.getKey();
			DCBReportModel dcbObject = entry.getValue();
			
			if (currentPayments.containsKey(key)) {
				DCBPayment currentPaymentObject = currentPayments.get(key);
				currentPayment = currentPaymentObject.getTotalPaid();
			}

			if (currentDemands.containsKey(key)) {
				DCBDemand currentDemandObject = currentDemands.get(key);
				currentDemand = currentDemandObject.getTaxAmount();
				currentDemandCollection = currentDemandObject.getCollectionAmount();
			}
			
			//This is the advance pending for a property id till date
			if (advanceTillDate.containsKey(key)) {
				Advance advanceObject = advanceTillDate.get(key);
				advanceAmount = advanceObject.getTotalAdvanceAmount().abs();
			}
			
			if (demandsTillDate.containsKey(key)) {
				DCBDemand demandsTillDateObject = demandsTillDate.get(key);
				collectionsTillDate = demandsTillDateObject.getCollectionAmount();
			}
			
			if (paymentsTillDate.containsKey(key)) {
				DCBPayment paymentsTillDateObject = paymentsTillDate.get(key);
				paymentsTillToday = paymentsTillDateObject.getTotalPaid();
			}
			
			if(arrearDue.containsKey(key)) {
				DCBArrearDue arrearDueObject = arrearDue.get(key);
				arrearDemand = arrearDueObject.getTaxAmount();
				arrearDemandCollection = arrearDueObject.getCollectionAmount();
			}
			
			collectionsBeforeSujog = collectionsTillDate.subtract(paymentsTillToday).compareTo(BigDecimal.ZERO) > 0
					? collectionsTillDate.subtract(paymentsTillToday)
					: BigDecimal.ZERO;
			log.info("Collections before Sujog :"+ collectionsBeforeSujog);
			
			/*arrear = arrearDemand.subtract(collectionsTillDate.subtract(paymentsTillToday).subtract(advanceAmount))
					.compareTo(BigDecimal.ZERO) > 0
							? arrearDemand
									.subtract(collectionsTillDate.subtract(paymentsTillToday).subtract(advanceAmount))
							: BigDecimal.ZERO;*/
			
			arrear = arrearDemand.subtract(collectionsBeforeSujog).compareTo(BigDecimal.ZERO) > 0
					? arrearDemand.subtract(collectionsBeforeSujog)
					: BigDecimal.ZERO;
			log.info("arrear :"+ arrear);
			
			dueAmt = arrearDemand.subtract(collectionsBeforeSujog).subtract(paymentsTillToday).add(currentPayment).compareTo(BigDecimal.ZERO) > 0
					? arrearDemand.subtract(collectionsBeforeSujog).subtract(paymentsTillToday).add(currentPayment)
					: BigDecimal.ZERO;
			log.info("dueAmt :"+ dueAmt);
					
			totalDemand = dueAmt.add(currentDemand);
			
			/* currentDemandCollection = currentPayment.subtract(arrear).subtract(advanceAmount).compareTo(BigDecimal.ZERO) > 0
					? currentPayment.subtract(arrear).subtract(advanceAmount)
					: BigDecimal.ZERO; */
			
			currentDemandCollection = currentPayment.subtract(dueAmt).subtract(advanceAmount).compareTo(BigDecimal.ZERO) > 0
					? currentPayment.subtract(dueAmt).subtract(advanceAmount)
					: BigDecimal.ZERO;
			log.info("currentDemandCollection :"+ currentDemandCollection);
			
			arrearDemandCollection = currentPayment.subtract(currentDemandCollection).subtract(advanceAmount).compareTo(BigDecimal.ZERO) > 0
					? currentPayment.subtract(currentDemandCollection).subtract(advanceAmount)
					: BigDecimal.ZERO;
			log.info("arrearDemandCollection :"+ arrearDemandCollection);
			
			dcbObject.setCurrentDemand(currentDemand);
			dcbObject.setCurrentPayment(currentPayment);
			dcbObject.setTotalDemand(totalDemand);
			dcbObject.setArrearDemand(dueAmt);
			dcbObject.setArrearCollection(arrearDemandCollection);
			dcbObject.setCurrentCollection(currentDemandCollection);
		}
		
		// Path and filename for the excel file
		String fileName = generateFileName("DCB_Report", tenantId, financialYear);
		File temporaryfile = getTemporaryFile("DCB_Report", fileName);

		DCBReportExcelGenerator excelGenerator = new DCBReportExcelGenerator(dcbReportMap);

		try {
			excelGenerator.generateExcelFile(temporaryfile);
		} catch (IOException e) {
			log.info("Error Occured while writing into excel file...");
			e.printStackTrace();
		}

		// push temporary file into file store
		log.info("uploading file to filestore...");
		Object filestoreDetails = fileStoreService.upload(temporaryfile, fileName, MediaType.MULTIPART_FORM_DATA_VALUE,
				"PT", "od");
		log.info("Uploaded the file to filestore with details: " + filestoreDetails.toString());

		// enrich and update Utility Report Details
		UtilityReportRequest reportDetailsRequest = enrichmentService.enrichUpdateReport(requestInfo, reportList,
				fileName, filestoreDetails);
		repository.updateReportDetails(reportDetailsRequest);
		
	}

	private boolean isRequestForCurrentFY(String startingYear) {

		LocalDate currentDate = LocalDate.now();

		int year = currentDate.getYear();
		int month = currentDate.getMonthValue();

		int financialYear = (month >= 4) ? year : year - 1;

		String financialYearString = String.valueOf(financialYear);

		if (financialYearString.equalsIgnoreCase(startingYear))
			return true;
		else
			return false;

	}

	/**
	 * @param startDate
	 * @param endDate
	 * @param tenantId
	 * @param requestInfo 
	 */
	private void createDCBForCurrentFinancialYear(Long startDate, Long endDate, String tenantId,
			RequestInfo requestInfo, List<UtilityReportDetails> reportList, String reportType, String financialYear) {

		// new record inserted for each report
		if (CollectionUtils.isEmpty(reportList)) {
			UtilityReportDetails reportDetails = enrichmentService.enrichSaveReport(requestInfo, reportType,
					financialYear, tenantId);
			repository.saveReportDetails(new UtilityReportRequest(requestInfo, reportDetails));
			reportList.add(reportDetails);
		}

		Map<String, DCBProperty> propertiesDetails = repository.getPropertyDetails(tenantId);

		Map<String, DCBPayment> currentPayments = repository.getCollections(tenantId, startDate, endDate);

		Map<String, DCBDemand> currentDemands = repository.getDemands(tenantId, startDate, endDate);

		Map<String, DCBArrearDue> arrearDue = repository.getArrearDue(tenantId, startDate);
		
		Map<String, Advance> advanceAmt = repository.getTotalAdvanceAmount(tenantId);

		Map<String, DCBReportModel> dcbReportMap = new HashMap<>();

		propertiesDetails.forEach((key, property) -> {

			DCBReportModel dcbReportObject = DCBReportModel.builder().oldPropertyId(property.getOldPropertyId())
					.ward(property.getWard()).propertyId(property.getPropertyId()).build();

			dcbReportMap.put(key, dcbReportObject);
		});

		for (Map.Entry<String, DCBReportModel> entry : dcbReportMap.entrySet()) {

			String key = entry.getKey();
			DCBReportModel dcbObject = entry.getValue();

			BigDecimal currentPayment = BigDecimal.ZERO;
			BigDecimal currentDemand = BigDecimal.ZERO;
			BigDecimal currentCollection = BigDecimal.ZERO;
			BigDecimal arrear = BigDecimal.ZERO;
			BigDecimal arrearDemand = BigDecimal.ZERO;
			BigDecimal totalDemand = BigDecimal.ZERO;
			BigDecimal advanceAmount = BigDecimal.ZERO;

			if (currentPayments.containsKey(key)) {
				DCBPayment currentPaymentObject = currentPayments.get(key);
				currentPayment = currentPaymentObject.getTotalPaid();
			}

			if (currentDemands.containsKey(key)) {
				DCBDemand currentDemandObject = currentDemands.get(key);
				currentDemand = currentDemandObject.getTaxAmount();
				currentCollection = currentDemandObject.getCollectionAmount();
			}

			if (arrearDue.containsKey(key)) {
				DCBArrearDue arrearDueObject = arrearDue.get(key);
				arrear = arrearDueObject.getDue();
			}
			
			//This is the advance pending for a property id till date
			if (advanceAmt.containsKey(key)) {
				Advance advanceObject = advanceAmt.get(key);
				advanceAmount = advanceObject.getTotalAdvanceAmount().abs();
			}

			arrearDemand = currentPayment.subtract(currentCollection).subtract(advanceAmount).add(arrear).compareTo(BigDecimal.ZERO) > 0
					? currentPayment.subtract(currentCollection).subtract(advanceAmount).add(arrear)
					: BigDecimal.ZERO;

			totalDemand = arrearDemand.add(currentDemand);

			dcbObject.setCurrentDemand(currentDemand);
			dcbObject.setCurrentPayment(currentPayment);
			dcbObject.setArrearDemand(arrearDemand);
			dcbObject.setTotalDemand(totalDemand);

		}

		// Path and filename for the excel file
		String fileName = generateFileName("DCB_Report", tenantId, financialYear);
		File temporaryfile = getTemporaryFile("DCB_Report", fileName);

		DCBReportExcelGenerator excelGenerator = new DCBReportExcelGenerator(dcbReportMap);

		try {
			excelGenerator.generateExcelFile(temporaryfile);
		} catch (IOException e) {
			log.info("Error Occured while writing into excel file...");
			e.printStackTrace();
		}

		// push temporary file into file store
		log.info("uploading file to filestore...");
		Object filestoreDetails = fileStoreService.upload(temporaryfile, fileName, MediaType.MULTIPART_FORM_DATA_VALUE,
				"PT", "od");
		log.info("Uploaded the file to filestore with details: " + filestoreDetails.toString());

		// enrich and update Utility Report Details
		UtilityReportRequest reportDetailsRequest = enrichmentService.enrichUpdateReport(requestInfo, reportList,
				fileName, filestoreDetails);
		repository.updateReportDetails(reportDetailsRequest);
		
	}
	
	private String generateFileName(String fileInitialName, String tenantId, String financialYear) {
		String fileFormat = ".xlsx";
		String fileSeparator = "_";
//		DateFormat dateFormat = new SimpleDateFormat("yyyy_MM_dd_hhmmss");
//		dateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));
//		String currentDateTime = dateFormat.format(new Date());
		String ulbName = StringUtils.capitalize(tenantId.substring(3));
		return fileInitialName + fileSeparator + ulbName + fileSeparator + financialYear + fileFormat;
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

	public Map<String, Object> getDCBReport(RequestInfo requestInfo, @Valid UtilityReportSearchCriteria searchCriteria) {

		Map<String, Object> response = new HashMap<>();
		
		String reportType = ReportConstants.DCB_REPORT_STRING;

		String tenantId = searchCriteria.getTenant();
		
		String financialYear = searchCriteria.getFinancialYear();

		List<UtilityReportDetails> reportList = repository.isReportExist(reportType, financialYear, tenantId);

		validator.validateIfReportGenerationInProcess(reportList, tenantId);
		
		if(reportList.isEmpty()) {
			throw new CustomException("REPORT_NOT_GENERATED",
					"Report is not generated for mentioned criteria. Kindly generate the report first ..");
		}
		
		List<UtilityReportDetails> reportDetails = reportList.stream()
				.filter(item -> item.getTenantId().equalsIgnoreCase(tenantId)).collect(Collectors.toList());

		response.put("reportsDetails", reportDetails);
		
		return response;
	}
	
}



