package org.egov.arc.util;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Objects;

import org.egov.arc.util.Constants.Constants;
import org.egov.common.contract.request.RequestInfo;
import org.egov.tracer.model.CustomException;
import org.postgresql.util.PGobject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class Util {

	@Autowired
	private ObjectMapper mapper;

	/**
	 * validates state level tenant-id for citizens and employees
	 * 
	 */
	public void validateTenantIdForUserType(String tenantId, RequestInfo requestInfo) {

		String userType = null;
		if (requestInfo.getUserInfo() != null) {
			userType = requestInfo.getUserInfo().getType();
		}
		if (Constants.EMPLOYEE_TYPE_CODE.equalsIgnoreCase(userType) && tenantId.split("\\.").length == 1) {
			throw new CustomException("EG_BS_INVALID_TENANTID",
					"Employees cannot search based on state level tenantid");
		}
	}

	public JsonNode getJsonValue(PGobject pGobject) {
		try {
			if (Objects.isNull(pGobject) || Objects.isNull(pGobject.getValue()))
				return null;
			else
				return mapper.readTree(pGobject.getValue());
		} catch (Exception e) {
			throw new CustomException(Constants.EG_BS_JSON_EXCEPTION_KEY, Constants.EG_BS_JSON_EXCEPTION_MSG);
		}
	}

	public PGobject getPGObject(Object responseData) {

		String value = null;
		try {
			value = mapper.writeValueAsString(responseData);
		} catch (JsonProcessingException e) {
			throw new CustomException(Constants.EG_BS_JSON_EXCEPTION_KEY, Constants.EG_BS_JSON_EXCEPTION_MSG);
		}

		PGobject json = new PGobject();
		json.setType(Constants.DB_TYPE_JSONB);
		try {
			json.setValue(value);
		} catch (SQLException e) {
			throw new CustomException(Constants.EG_BS_JSON_EXCEPTION_KEY, Constants.EG_BS_JSON_EXCEPTION_MSG);
		}
		return json;
	}

	public Long getArchivalMonthStartDate(int archivalMonthCnt) {
		LocalDate currentDate = LocalDate.now();
		LocalDate monthsBack = currentDate.minusMonths(archivalMonthCnt).withDayOfMonth(1);
		return monthsBack.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli();
	}

	
}
