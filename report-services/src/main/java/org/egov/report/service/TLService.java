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
		
		log.info("Starting to process the TL Auto Escalation Report with search criteria: {}", searchCriteria);

		// Validate search criteria
		log.info("Validating search criteria");
		tlValidator.validateTradeLicenseSearchCriteria(searchCriteria);

		// Fetch report data
		log.info("Fetching trade license auto escalation report data");
		List<TradeLicenseEscallationDetailsResponse> reportList = tlRepository.getTLAutoEscallationReport(searchCriteria);

		// Fetch holiday data
		log.info("Fetching holiday data from MDMS");
		Set<LocalDate> holidays = new HashSet<>();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM yyyy HH:mm:ss");

		Object mdmsHolidayCalenderData = tradeLicenseUtil.mdmsCallForCalender(requestInfo);
		List<LinkedHashMap<String, Object>> holidayList = JsonPath.read(mdmsHolidayCalenderData, "$.MdmsRes.common-masters.HolidayCalender");
		holidayList.forEach(hl -> hl.forEach((k, v) -> holidays.add(LocalDate.parse(String.valueOf(hl.get("date")), DateTimeFormatter.ofPattern("dd/MM/yyyy")))));
		log.info("Total holidays are  : " + holidays.size());

		// Current date
		Instant currentDateInstant = Instant.ofEpochMilli(System.currentTimeMillis());
		LocalDate currentDateLocalDate = currentDateInstant.atZone(ZoneId.systemDefault()).toLocalDate();
		ZoneId istZoneId = ZoneId.of("Asia/Kolkata");

		// Process each report item
		for (TradeLicenseEscallationDetailsResponse response : reportList) {
			log.info("Current Application is : " + response.getApplicationNumber());

			long dateOfPaymentMillis = Long.parseLong(response.getDateOfPayment());
			Instant submissionDateInstant = Instant.ofEpochMilli(dateOfPaymentMillis);
			LocalDate submissionDateLocalDate = submissionDateInstant.atZone(ZoneId.systemDefault()).toLocalDate();

			
			long daysBetweenBeforeHolidayAdjustment = ChronoUnit.DAYS.between(submissionDateLocalDate, currentDateLocalDate);
			log.info("Days since application submission for report item {}: before holidays adjustment : {}", response.getApplicationNumber(), daysBetweenBeforeHolidayAdjustment);

			
			// Count holidays adjusted
			int holidayAdjustments = 0;
			
			// Adjust current date for holidays
			log.info("Adjusting for holidays between submission date and current date");
			for (LocalDate holiday : holidays) {
				if ((holiday.isAfter(submissionDateLocalDate) && holiday.isBefore(currentDateLocalDate.plusDays(1)))
						|| holiday.isEqual(submissionDateLocalDate)) {
					currentDateLocalDate = currentDateLocalDate.minusDays(1);
					holidayAdjustments++;
//					log.info("Adjusted current date due to holiday: {}", holiday);
				}
			}

			// Log total holidays adjusted for this response
			log.info("Total holidays adjusted for application number {}: {}", response.getApplicationNumber(), holidayAdjustments);

			long daysBetween = ChronoUnit.DAYS.between(submissionDateLocalDate, currentDateLocalDate);
			response.setDaysSinceApplicationSubmission(daysBetween);
			log.info("Days since application submission for report item {}: {}", response.getApplicationNumber(), daysBetween);

			long autoEscallationDateMillis = Long.parseLong(response.getAutoEscallationDate());

			// Converting epoch time in milliseconds to Instant
			Instant dateOfPaymentInstant = Instant.ofEpochMilli(dateOfPaymentMillis);
			Instant autoEscallationDateInstant = Instant.ofEpochMilli(autoEscallationDateMillis);

			// Converting Instant to LocalDateTime in IST
			LocalDateTime dateOfPaymentDateTime = LocalDateTime.ofInstant(dateOfPaymentInstant, istZoneId);
			LocalDateTime autoEscallationDateTime = LocalDateTime.ofInstant(autoEscallationDateInstant, istZoneId);

			String formattedDop = dateOfPaymentDateTime.format(formatter);
			String formattedAed = autoEscallationDateTime.format(formatter);

			log.info("Formatted Date of Payment (IST): {}", formattedDop);
			log.info("Formatted Auto Escallation Date (IST): {}", formattedAed);

			response.setDateOfPayment(formattedDop);
			response.setAutoEscallationDate(formattedAed);
		}

		log.info("Enriching report list with user details");
		tradeLicenseUtil.enrichingWithUserDetailsUsingUUIDS(requestInfo, reportList);
		
		log.info("Finished processing TL Auto Escalation Report");
		return reportList;
	}
}
