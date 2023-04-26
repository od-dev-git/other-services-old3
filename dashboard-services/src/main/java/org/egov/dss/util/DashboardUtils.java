package org.egov.dss.util;

import java.sql.SQLException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import org.egov.dss.constants.DashboardConstants;
import org.egov.tracer.model.CustomException;
import org.postgresql.util.PGobject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class DashboardUtils {
	
	@Autowired
	private ObjectMapper mapper;
	
	public Long getStartingTime(Long fromDate) {
		Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
		cal.setTimeInMillis(fromDate);
		cal.set(Calendar.HOUR, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 1);
		return cal.getTimeInMillis();
	}
	
	public Long getEndingTime(Long fromDate) {
		Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
		cal.setTimeInMillis(fromDate);
		cal.set(Calendar.HOUR, 11);
		cal.set(Calendar.MINUTE, 59);
		cal.set(Calendar.SECOND, 59);
		cal.set( Calendar.AM_PM, Calendar.PM);
		return cal.getTimeInMillis();
	}
	
	public Calendar getDayFromLong(Long fromDate) {
		Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
		cal.setTimeInMillis(fromDate);
		return cal;
	}
	
	public Calendar addOneMonth(Long fromDate) {
		Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
		cal.setTimeInMillis(fromDate);
		cal.add(Calendar.MONTH, 1);
		cal.add(Calendar.SECOND, -1);
		return cal;
	}
	

	public PGobject getPGObject(Object responseData) {

		String value = null;
		try {
			value = mapper.writeValueAsString(responseData);
		} catch (JsonProcessingException e) {
			throw new CustomException(DashboardConstants.EG_BS_JSON_EXCEPTION_KEY, DashboardConstants.EG_BS_JSON_EXCEPTION_MSG);
		}

		PGobject json = new PGobject();
		json.setType(DashboardConstants.DB_TYPE_JSONB);
		try {
			json.setValue(value);
		} catch (SQLException e) {
			throw new CustomException(DashboardConstants.EG_BS_JSON_EXCEPTION_KEY, DashboardConstants.EG_BS_JSON_EXCEPTION_MSG);
		}
		return json;
	}
	
	public Long getStartDate(String financialYear) {
		Calendar cal = Calendar.getInstance();
		cal.setTimeZone(TimeZone.getTimeZone("IST"));
		cal.set(Calendar.DAY_OF_MONTH, 1);
		cal.set(Calendar.MONTH, Calendar.APRIL);
		cal.set(Calendar.YEAR, Integer.parseInt(financialYear.split("-")[0]));
		cal.set(Calendar.HOUR, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		cal.set(Calendar.AM_PM, Calendar.AM);
		return cal.getTimeInMillis();

	}
	
	public Long getEndDate(String financialYear) {
		Long today = Calendar.getInstance().getTimeInMillis();
		Calendar cal = Calendar.getInstance();
		cal.setTimeZone(TimeZone.getTimeZone("IST"));
		cal.set(Calendar.DAY_OF_MONTH, 31);
		cal.set(Calendar.MONTH, Calendar.MARCH);
		cal.set(Calendar.YEAR, Integer.parseInt(financialYear.split("-")[0]) + 1);
		cal.set(Calendar.HOUR, 11);
		cal.set(Calendar.MINUTE, 59);
		cal.set(Calendar.SECOND, 59);
		cal.set(Calendar.MILLISECOND, 0);
		cal.set(Calendar.AM_PM, Calendar.PM);
		if (cal.getTimeInMillis() > today) {
			return today;
		} else {
			return cal.getTimeInMillis();
		}

	}
	
	public String getCurrentFinancialYear() {
		int previousYear;
		int currentYear;
		Calendar cal = Calendar.getInstance();
		if (cal.get(Calendar.MONTH) < 3) {
			previousYear = cal.get(Calendar.YEAR) - 1;
			currentYear = cal.get(Calendar.YEAR);
		} else {
			previousYear = cal.get(Calendar.YEAR);
			currentYear = cal.get(Calendar.YEAR) + 1;
		}
		return String.valueOf(previousYear).concat("-").concat(String.valueOf(currentYear));

	}
	
	public Long getStartDateOfQuarter() {
		Calendar cal = Calendar.getInstance();
		cal.setTimeZone(TimeZone.getTimeZone("IST"));
		cal.set(Calendar.DAY_OF_MONTH, 1);
		cal.set(Calendar.MONTH, cal.get(Calendar.MONTH) / 3 * 3);
		cal.set(Calendar.HOUR, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		cal.set(Calendar.AM_PM, Calendar.AM);
		return cal.getTimeInMillis();

	}

	public Long getStartDateOfMonth() {
		Calendar cal = Calendar.getInstance();
		cal.setTimeZone(TimeZone.getTimeZone("IST"));
		cal.set(Calendar.DAY_OF_MONTH, 1);
		cal.set(Calendar.MONTH, cal.get(Calendar.MONTH));
		cal.set(Calendar.HOUR, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		cal.set(Calendar.AM_PM, Calendar.AM);
		return cal.getTimeInMillis();

	}

	public Long getStartDateOfWeek() {
		Calendar cal = Calendar.getInstance();
		cal.setTimeZone(TimeZone.getTimeZone("IST"));
		cal.set(Calendar.DAY_OF_WEEK, 1);
		cal.set(Calendar.HOUR, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		cal.set(Calendar.AM_PM, Calendar.AM);
		return cal.getTimeInMillis();

	}
	
	public Long startOfDay(Long timeInstance) {
		LocalDate date = Instant.ofEpochMilli(timeInstance).atZone(ZoneId.systemDefault()).toLocalDate();
		Instant instant = date.atStartOfDay(ZoneId.systemDefault()).toInstant();
		return instant.toEpochMilli();
	}

	public Long endOfDay(Long timeInstance) {
		LocalDate date = Instant.ofEpochMilli(timeInstance).atZone(ZoneId.systemDefault()).toLocalDate();
		LocalDateTime endOfDate = LocalTime.MAX.atDate(date);
		return endOfDate.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
	}
	
	
}
