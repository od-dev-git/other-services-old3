package org.egov.report.validator;

import java.util.HashMap;
import java.util.Map;

import org.egov.report.web.model.IncentiveReportCriteria;
import org.egov.tracer.model.CustomException;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class ReportValidator {

	public void validateIncentiveCriteria(IncentiveReportCriteria incentiveReportCriteria) {
		
		Map<String, String> errorMap = new HashMap<>();
		
		if(!StringUtils.hasText(incentiveReportCriteria.getTenantId())) {
			errorMap.put("INVALID_CRITERIA", "tenantId can not be blank/empty");
		}
		
		if(!StringUtils.hasText(incentiveReportCriteria.getModule())) {
			errorMap.put("INVALID_CRITERIA", "Module can not be blank/empty");
		}
		
		if (!errorMap.isEmpty())
			throw new CustomException(errorMap);
		
	}
	
	

}
