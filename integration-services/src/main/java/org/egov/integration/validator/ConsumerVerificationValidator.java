package org.egov.integration.validator;

import java.util.HashMap;
import java.util.Map;

import javax.validation.Valid;

import org.egov.integration.model.BPAVerificationSearchCriteria;
import org.egov.integration.model.ConsumerVerificationSearchCriteria;
import org.egov.tracer.model.CustomException;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class ConsumerVerificationValidator {

	private void createCustomException(Map<String, String> errorMap) {
		if (!errorMap.isEmpty())
			throw new CustomException(errorMap);
	}
	
	public void validateSearch(ConsumerVerificationSearchCriteria searchCriteria) {
		Map<String, String> errorMap = new HashMap<>();

		if (!StringUtils.hasText(searchCriteria.getTenantId())) {
			errorMap.put("INVALID_SEARCH_CRITERIA", "Ulb can not be empty/blank");
		}
		if (!StringUtils.hasText(searchCriteria.getConsumerNo())) {
			errorMap.put("INVALID_SEARCH_CRITERIA", "Consumer Number can not be empty/blank");
		}
		if (!StringUtils.hasText(searchCriteria.getBusinessService())) {
			errorMap.put("INVALID_SEARCH_CRITERIA", "Business Service can not be empty/blank");
		}		
		createCustomException(errorMap);
	}

	public void validateBPASearch(@Valid BPAVerificationSearchCriteria searchCriteria) {
		
		Map<String, String> errorMap = new HashMap<>();

		if (!StringUtils.hasText(searchCriteria.getTenantId())) {
			errorMap.put("INVALID_SEARCH_CRITERIA", "Ulb can not be empty/blank");
		}
		if (!StringUtils.hasText(searchCriteria.getPermitNumber())) {
			errorMap.put("INVALID_SEARCH_CRITERIA", "Consumer Number can not be empty/blank");
		}	
		createCustomException(errorMap);
	}
}
