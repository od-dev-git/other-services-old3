package org.egov.dss.util;

import java.sql.SQLException;
import java.util.Calendar;
import java.util.TimeZone;
import org.egov.dss.constants.DashboardConstants;
import org.egov.tracer.model.CustomException;
import org.postgresql.util.PGobject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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

}
