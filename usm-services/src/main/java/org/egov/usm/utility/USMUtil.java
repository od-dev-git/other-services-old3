package org.egov.usm.utility;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Locale;
import java.util.UUID;
import java.util.regex.Pattern;

import org.egov.tracer.model.CustomException;
import org.egov.usm.web.model.AuditDetails;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class USMUtil {
	
	/**
	 * Method to return auditDetails for create/update flows
	 *
	 * @param by
	 * @param isCreate
	 * @return AuditDetails
	 */
	public static AuditDetails getAuditDetails(String by, Boolean isCreate) {
		Long time = System.currentTimeMillis();
		log.info("Audit user by :", by);
		if(isCreate)
			return AuditDetails.builder().createdBy(by).lastModifiedBy(by).createdTime(time).lastModifiedTime(time).build();
		else
			return AuditDetails.builder().lastModifiedBy(by).lastModifiedTime(time).build();
	}

	
	/**
	 * To generate UUID
	 * @return generated UUID
	 */
	public static String generateUUID () {
		return UUID.randomUUID().toString();
	}
	
	
	/**
	 * To check current time is after given time limit
	 * 
	 * @param startTime
	 * @return true or false 
	 */
	public static boolean isAfterTime(String inputTime) {
		return LocalTime.now(ZoneId.of(Constants.LOCAL_ZONE_ID)).isAfter(LocalTime.parse(inputTime.toUpperCase(), DateTimeFormatter.ofPattern("hh:mm a", Locale.UK)));
	}
	
	
	/**
	 * To check current time is before given time limit
	 * 
	 * @param startTime
	 * @return true or false 
	 */
	public static boolean isBeforeTime(String inputTime) {
		return LocalTime.now(ZoneId.of(Constants.LOCAL_ZONE_ID)).isBefore(LocalTime.parse(inputTime.toUpperCase(), DateTimeFormatter.ofPattern("hh:mm a", Locale.UK)));
	}
	
	
	/**
	 * Validate input time format 
	 * 
	 * @param inputTime
	 */
	public static void validateInputTimeFormat(String inputTime) {
		if(!ObjectUtils.isEmpty(inputTime)) {
			final Pattern pattern = Pattern.compile("^(00|0[0-9]|1[012]):[0-5][0-9] ?((a|p)m|(A|P)M)$");
			if (!pattern.matcher(inputTime).matches()) {
				throw new CustomException("EG_SY_TIME_FORMAT_ERR", "Provide valid time format. e.g. 06:15 PM");
		    }
		}
	}


	/**
	 * validate time in unix epoch format
	 * 
	 * @param inputUnixTime
	 * @return true or false 
	 */
	public static boolean isAfterTime(Long inputUnixTime) {
		Instant instant = Instant.ofEpochSecond(inputUnixTime/1000);
		instant = instant.plus(Constants.ADD_COMMENT_TIME_IN_HOUR, ChronoUnit.HOURS);
		return LocalDateTime.now(ZoneId.of(Constants.LOCAL_ZONE_ID)).toInstant(ZoneOffset.UTC).isAfter(instant);
	}
	
}
