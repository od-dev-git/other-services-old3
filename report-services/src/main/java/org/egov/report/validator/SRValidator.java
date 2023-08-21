package org.egov.report.validator;

import java.util.HashMap;
import java.util.Map;

import org.egov.report.web.model.SRReportSearchCriteria;
import org.egov.report.web.model.WSReportSearchCriteria;
import org.egov.tracer.model.CustomException;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class SRValidator {
	
	private void createCustomException(Map<String, String> errorMap) {
		if (!errorMap.isEmpty())
			throw new CustomException(errorMap);
	}
	
	public void validateSRTicketDetail(SRReportSearchCriteria searchCriteria) {
		Map<String, String> errorMap = new HashMap<>();
		
//		if(!StringUtils.hasText(searchCriteria.getTenantId())) {
//			errorMap.put("INVALID_SEARCH_CRITERIA", "Ulb can not be empty/blank");
//		}
		
		if(searchCriteria.getFromDate() == null){
			errorMap.put("INVALID_SEARCH_CRITERIA", "From Date can not be empty/blank");
		}

		if(searchCriteria.getToDate() == null){
			errorMap.put("INVALID_SEARCH_CRITERIA", "To Date can not be empty/blank");
		}
		
		createCustomException(errorMap);
	}

}
