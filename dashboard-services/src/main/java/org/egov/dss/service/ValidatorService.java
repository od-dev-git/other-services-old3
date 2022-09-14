package org.egov.dss.service;

import java.util.HashMap;
import java.util.Map;

import org.egov.dss.web.model.RequestInfoWrapper;
import org.egov.tracer.model.CustomException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class ValidatorService {
	
	private void createCustomException(Map<String, String> errorMap) {
		if (!errorMap.isEmpty())
			throw new CustomException(errorMap);
	}

	public void validateRequest(RequestInfoWrapper requestInfoWrapper) {
		Map<String, String> errorMap = new HashMap<>();
		
		if(requestInfoWrapper.getChartCriteria() == null) {
			errorMap.put("INVALID_CHART_CRITERIA", "Criteria should not be empty");
		}
		
		if(!StringUtils.hasText(requestInfoWrapper.getChartCriteria().getVisualizationCode())) {
			errorMap.put("INVALID_CHART_CRITERIA", "Visualization code can not be empty/blank");
		}
		
		createCustomException(errorMap);
		
	}

}
