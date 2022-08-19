package org.egov.report.util;

import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

import org.springframework.stereotype.Component;

@Component
public class WSReportUtils {
	
	public Long addOneMonth(Long time) {
		
		Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("IST")); 
		cal.setTimeInMillis(time);
		cal.add(Calendar.MONTH, 1);
		return cal.getTimeInMillis();
	}

	public String getConvertedDate(Long time) {
		
		if(time == 0 || time == null )
			return null;
		
		Date date = new Date(time);
		DateFormat format = new SimpleDateFormat("dd/MM/yyyy");
		format.setTimeZone(TimeZone.getTimeZone("IST"));
		String formatted = format.format(date);
		return formatted;
	}
}
