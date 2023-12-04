package org.egov.dss.util;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.time.DayOfWeek;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.time.ZoneId;
import java.time.ZoneOffset;
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
		Date date = new Date(timeInstance);
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.setTimeZone(TimeZone.getTimeZone("IST"));
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar.getTimeInMillis();
	}

	public Long endOfDay(Long timeInstance) {
		Date date = new Date(timeInstance);
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.setTimeZone(TimeZone.getTimeZone("IST"));
		calendar.set(Calendar.HOUR_OF_DAY, 23);
		calendar.set(Calendar.MINUTE, 59);
		calendar.set(Calendar.SECOND, 59);
		calendar.set(Calendar.MILLISECOND, 000);
		return calendar.getTimeInMillis();
	}
	
	public Long getStartDateGmt(String financialYear) {
		Calendar cal = Calendar.getInstance();
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

	public Long getEndDateGmt(String financialYear) {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.DAY_OF_MONTH, 31);
		cal.set(Calendar.MONTH, Calendar.MARCH);
		cal.set(Calendar.YEAR, Integer.parseInt(financialYear.split("-")[0]) + 1);
		cal.set(Calendar.HOUR, 11);
		cal.set(Calendar.MINUTE, 59);
		cal.set(Calendar.SECOND, 59);
		cal.set(Calendar.MILLISECOND, 0);
		cal.set(Calendar.AM_PM, Calendar.PM);
		return cal.getTimeInMillis();
	}
	
	public Long previousFYStartDate(String financialYear) {
		Integer previousFY1 = Integer.parseInt(financialYear.split("-")[0])-1;
		Integer previousFY2 = Integer.parseInt(financialYear.split("-")[1])-1;
		Long previousYearStartDate = getStartDateGmt(String.valueOf(previousFY1) + "-" + String.valueOf(previousFY2));
		return previousYearStartDate;
	}
	
	public long previousFYEndDate(String financialYear) {
		Integer previousFY1 = Integer.parseInt(financialYear.split("-")[0])-1;
		Integer previousFY2 = Integer.parseInt(financialYear.split("-")[1])-1;
		Long previousYearEndDate = getEndDateGmt(String.valueOf(previousFY1) + "-" + String.valueOf(previousFY2));
		return previousYearEndDate;
	}
	
	public long previousMonthStartDate() {
		LocalDate currentDate = LocalDate.now();
		LocalDate firstDayOfPreviousMonth = currentDate.minusMonths(1).withDayOfMonth(1);
		LocalDate lastDayOfPreviousMonth = currentDate.minusMonths(1)
				.withDayOfMonth(currentDate.minusMonths(1).lengthOfMonth());
		return firstDayOfPreviousMonth.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli();

	}

	public long previousMonthEndDate() {
		LocalDate currentDate = LocalDate.now();
		LocalDate lastDayOfPreviousMonth = currentDate.minusMonths(1)
				.withDayOfMonth(currentDate.minusMonths(1).lengthOfMonth());
		return lastDayOfPreviousMonth.atTime(23, 59, 59).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
	}
	
	public long previousWeekStartDate() {
		LocalDate currentDate = LocalDate.now();
		LocalDate previousWeekStartDate = currentDate.minusWeeks(1).with(DayOfWeek.MONDAY);
		Instant startInstant = previousWeekStartDate.atStartOfDay().toInstant(ZoneOffset.UTC);
		return startInstant.toEpochMilli();
	}

	public long previousWeekEndDate() {
		LocalDate currentDate = LocalDate.now();
		LocalDate previousWeekEndDate = currentDate.minusWeeks(1).with(DayOfWeek.SUNDAY);
		Instant endInstant = previousWeekEndDate.atTime(23, 59, 59).toInstant(ZoneOffset.UTC);
		return endInstant.toEpochMilli();
	}
	
	public long previousQuarterStartDate() {
		LocalDate currentDate = LocalDate.now();
		int currentYear = currentDate.getYear();
		int currentQuarter = (currentDate.getMonthValue() - 1) / 3 + 1;
		int previousQuarter = currentQuarter - 1;
		int previousYear = currentYear;
		if (previousQuarter == 0) {
			previousQuarter = 4;
			previousYear--;
		}
		LocalDate startDateofQuarter = LocalDate.of(previousYear, Month.of((previousQuarter - 1) * 3 + 1), 1);
		return startDateofQuarter.atStartOfDay(ZoneOffset.UTC).toInstant().toEpochMilli();
	}
	
	public long previousQuarterEndDate() {
		LocalDate currentDate = LocalDate.now();
		int currentYear = currentDate.getYear();
		int currentQuarter = (currentDate.getMonthValue() - 1) / 3 + 1;

		int previousQuarter = currentQuarter - 1;
		int previousYear = currentYear;

		if (previousQuarter == 0) {
			previousQuarter = 4;
			previousYear--;
		}

		LocalDate startOfPreviousQuarter = LocalDate.of(previousYear, Month.of((previousQuarter - 1) * 3 + 1), 1);
		LocalDate endofPreviousQuarter = startOfPreviousQuarter.plusMonths(3).minusDays(1);
		return endofPreviousQuarter.atTime(23, 59, 59).atZone(ZoneOffset.UTC).toInstant().toEpochMilli();
	}
	
	public String addDenominationForAmount(BigDecimal number) {
		BigDecimal oneLakh = new BigDecimal("100000");
		BigDecimal oneCrore = new BigDecimal("10000000");

		if (number.compareTo(oneLakh) < 0) {
			return number.toString();
		} else if (number.compareTo(oneCrore) < 0) {
			BigDecimal lakhs = number.divide(oneLakh);
			return formatNumber(lakhs) + " Lac";
		} else {
			BigDecimal crores = number.divide(oneCrore);
			return formatNumber(crores) + " Cr";
		}
	}

	private String formatNumber(BigDecimal number) {
		DecimalFormat decimalFormat = new DecimalFormat("#,##,##0.00");
		return decimalFormat.format(number);
	}
	
	public String getPreviousFY() {
		String currentFY = getCurrentFinancialYear();
		Integer previousFY1 = Integer.parseInt(currentFY.split("-")[0]) - 1;
		Integer previousFY2 = Integer.parseInt(currentFY.split("-")[1]) - 1;
		String previousFy = String.valueOf(previousFY1).concat("-").concat(String.valueOf(previousFY2));
		return previousFy;
	}
	
	public Long getLastThirtyDayValue() {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DAY_OF_MONTH, -30);
		return calendar.getTimeInMillis();

	}
	

	public Long enrichFormDate(Long fromDate) {
		Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
		cal.setTimeInMillis(fromDate);
		cal.set(Calendar.HOUR, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 1);
		return cal.getTimeInMillis();
	}
	
	public Long enrichToDate(Long toDate) {
		Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
		cal.setTimeInMillis(toDate);
		cal.set(Calendar.HOUR, 11);
		cal.set(Calendar.MINUTE, 59);
		cal.set(Calendar.SECOND, 59);
		cal.set( Calendar.AM_PM, Calendar.PM);
		return cal.getTimeInMillis();
	}
	
}
