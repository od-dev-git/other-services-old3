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
	
	if(requestInfoWrapper.getPayloadDetails() == null) {
		errorMap.put("INVALID_PAYLOAD", "Payload Should not be empty or null");
	}
	
	if(!StringUtils.hasText(requestInfoWrapper.getPayloadDetails().getVisualizationcode())) {
		errorMap.put("INVALID_PAYLOAD", "Visualization code can not be empty/blank");
	}
	
	createCustomException(errorMap);
	
}

}
