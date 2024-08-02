package org.egov.report.service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

import org.egov.common.contract.request.RequestInfo;
import org.egov.report.repository.TradeLicenseReportRepository;
import org.egov.report.util.TradeLicenseReportUtil;
import org.egov.report.validator.TradeLicenseReportValidator;
import org.egov.report.web.model.TradeLicenseEscallationDetailsResponse;
import org.egov.report.web.model.TradeLicenseSearchCriteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jayway.jsonpath.JsonPath;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class TLService {

	@Autowired
	private TradeLicenseReportValidator tlValidator;

	@Autowired
	private TradeLicenseReportRepository tlRepository;
	
	@Autowired
	private TradeLicenseReportUtil tradeLicenseUtil;

	public List<TradeLicenseEscallationDetailsResponse> getTLAutoEscallationReport(RequestInfo requestInfo,
	        TradeLicenseSearchCriteria searchCriteria) {

	    // Get and log the current date and time
	    LocalDateTime currentDateTime = LocalDateTime.now(ZoneId.of("Asia/Kolkata"));
	    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM yyyy HH:mm:ss");
	    String formattedCurrentDateTime = currentDateTime.format(formatter);
	    log.info("Starting to process the TL Auto Escalation Report at {} with search criteria: {}", formattedCurrentDateTime, searchCriteria);

	    // Validate search criteria
	    log.info("Validating search criteria");
	    tlValidator.validateTradeLicenseSearchCriteria(searchCriteria);

	    // Fetch report data
	    log.info("Fetching trade license auto escalation report data");
	    List<TradeLicenseEscallationDetailsResponse> reportList = tlRepository.getTLAutoEscallationReport(searchCriteria);

	    // Fetch holiday data
	    log.info("Fetching holiday data from MDMS");
	    Set<LocalDate> holidays = new HashSet<>();

	    try {
	        Object mdmsHolidayCalenderData = tradeLicenseUtil.mdmsCallForCalender(requestInfo);
	        List<LinkedHashMap<String, Object>> holidayList = JsonPath.read(mdmsHolidayCalenderData, "$.MdmsRes.common-masters.HolidayCalender");
	        holidayList.forEach(hl -> holidays.add(LocalDate.parse(String.valueOf(hl.get("date")), DateTimeFormatter.ofPattern("dd/MM/yyyy"))));
	    } catch (Exception e) {
	        log.error("Error while fetching or parsing holiday data: {}", e.getMessage(), e);
	        throw new RuntimeException("Error fetching holiday data from MDMS");
	    }
	    log.info("Total holidays are: {}", holidays.size());

	    // Current date
	    LocalDate currentDateLocalDate = currentDateTime.toLocalDate();

	    // Process each report item
	    for (TradeLicenseEscallationDetailsResponse response : reportList) {
	        log.info("Processing application number: {}", response.getApplicationNumber());

	        LocalDate submissionDateLocalDate;
	        try {
	            long dateOfPaymentMillis = Long.parseLong(response.getDateOfPayment());
	            submissionDateLocalDate = Instant.ofEpochMilli(dateOfPaymentMillis).atZone(ZoneId.of("Asia/Kolkata")).toLocalDate();
	        } catch (Exception e) {
	            log.error("Error parsing date of payment for application number {}: {}", response.getApplicationNumber(), e.getMessage(), e);
	            continue;
	        }

	        // Calculate and log days before holidays adjustment
	        long daysSinceSubmission = ChronoUnit.DAYS.between(submissionDateLocalDate, currentDateLocalDate);
	        log.info("Days since submission before holiday adjustment for application number {}: {}", response.getApplicationNumber(), daysSinceSubmission);

	        // Adjust for holidays
	        long holidaysCount = holidays.stream()
	            .filter(holiday -> !holiday.isBefore(submissionDateLocalDate) && !holiday.isAfter(currentDateLocalDate))
	            .count();
	        long daysAdjusted = daysSinceSubmission - holidaysCount;

	        log.info("Holidays adjusted for application number {}: {}", response.getApplicationNumber(), holidaysCount);
	        log.info("Days since submission after holiday adjustment for application number {}: {}", response.getApplicationNumber(), daysAdjusted);
	        response.setDaysSinceApplicationSubmission(daysAdjusted);

	        try {
	            long autoEscallationDateMillis = Long.parseLong(response.getAutoEscallationDate());

	            LocalDateTime dateOfPaymentDateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(Long.parseLong(response.getDateOfPayment())), ZoneId.of("Asia/Kolkata"));
	            LocalDateTime autoEscallationDateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(autoEscallationDateMillis), ZoneId.of("Asia/Kolkata"));

	            String formattedDop = dateOfPaymentDateTime.format(formatter);
	            String formattedAed = autoEscallationDateTime.format(formatter);

	            response.setDateOfPayment(formattedDop);
	            response.setAutoEscallationDate(formattedAed);

	            log.info("Formatted Date of Payment (IST) for application number {}: {}", response.getApplicationNumber(), formattedDop);
	            log.info("Formatted Auto Escallation Date (IST) for application number {}: {}", response.getApplicationNumber(), formattedAed);
	        } catch (Exception e) {
	            log.error("Error formatting dates for application number {}: {}", response.getApplicationNumber(), e.getMessage(), e);
	        }
	    }

	    // Enriching report list with user details
	    log.info("Enriching report list with user details");
	    tradeLicenseUtil.enrichingWithUserDetailsUsingUUIDS(requestInfo, reportList);

	    log.info("Finished processing TL Auto Escalation Report at {}", LocalDateTime.now(ZoneId.of("Asia/Kolkata")).format(formatter));
	    return reportList;
	}



}
