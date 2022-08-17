package org.egov.report.util;

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
		
		Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("IST")); 
		cal.setTimeInMillis(time);
		return cal.get(Calendar.DATE)+"/"+cal.get(Calendar.MONTH)+"/"+cal.get(Calendar.YEAR);
	}
}
