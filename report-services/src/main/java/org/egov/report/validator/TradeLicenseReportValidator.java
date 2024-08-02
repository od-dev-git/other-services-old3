package org.egov.report.validator;

import java.util.HashMap;
import java.util.Map;

import org.egov.report.web.model.TradeLicenseSearchCriteria;
import org.egov.tracer.model.CustomException;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class TradeLicenseReportValidator {

	public void validateTradeLicenseSearchCriteria(TradeLicenseSearchCriteria searchCriteria) {
		// TODO Auto-generated method stub
		Map<String, String> errorMap = new HashMap<>();

		if(!StringUtils.hasText(searchCriteria.getTenant())) {
			errorMap.put("INVALID_CRITERIA", "ULB Name can not be blank/empty");
		}
		if (!errorMap.isEmpty())
			throw new CustomException(errorMap);
	}

}