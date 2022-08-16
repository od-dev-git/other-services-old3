package org.egov.report.util;

import java.util.Calendar;
import java.util.TimeZone;

import org.springframework.stereotype.Component;

@Component
public class PaymentUtil {
	
	public Long enrichFormDate(Long fromDate) {
		Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("IST"));
		cal.setTimeInMillis(fromDate);
		cal.set(Calendar.HOUR, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 1);
		return cal.getTimeInMillis();
	}
	
	public Long enrichToDate(Long toDate) {
		Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("IST"));
		cal.setTimeInMillis(toDate);
		cal.set(Calendar.HOUR, 11);
		cal.set(Calendar.MINUTE, 59);
		cal.set(Calendar.SECOND, 59);
		cal.set( Calendar.AM_PM, Calendar.PM);
		return cal.getTimeInMillis();
	}
	
	public String getMonthYear(Long date) {
		Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("IST"));
		cal.setTimeInMillis(date);
		return cal.get(Calendar.MONTH)+"/"+cal.get(Calendar.YEAR);	
	}

}
