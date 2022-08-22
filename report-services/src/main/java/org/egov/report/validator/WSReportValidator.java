package org.egov.report.validator;

import java.util.HashMap;
import java.util.Map;

import org.egov.report.web.model.WSReportSearchCriteria;
import org.egov.tracer.model.CustomException;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.lang.Long;

@Component
public class WSReportValidator {

	private void createCustomException(Map<String, String> errorMap) {
		if (!errorMap.isEmpty())
			throw new CustomException(errorMap);
	}
	
	public void validateEmployeeDateWiseWSCollectionReport(WSReportSearchCriteria searchCriteria) {
		Map<String, String> errorMap = new HashMap<>();
		
		if(!StringUtils.hasText(searchCriteria.getTenantId())) {
			errorMap.put("INVALID_SEARCH_CRITERIA", "Ulb can not be empty/blank");
		}
		
		if(searchCriteria.getCollectionDate()==null || searchCriteria.getCollectionDate()==0) {
			errorMap.put("INVALID_SEARCH_CRITERIA", "Collection date can not be empty/blank");
		}
		
		createCustomException(errorMap);
	}
	
	public void validateconsumerMasterWSReport(WSReportSearchCriteria searchCriteria) {
		Map<String, String> errorMap = new HashMap<>();
		
		if(!StringUtils.hasText(searchCriteria.getTenantId())) {
			errorMap.put("INVALID_SEARCH_CRITERIA", "Ulb can not be empty/blank");
		}
		
		if(searchCriteria.getWard() == null) {
			errorMap.put("INVALID_SEARCH_CRITERIA", "Ward can not be empty/blank");
		}
		
		createCustomException(errorMap);
	}
	
	public void validateBillSummary(WSReportSearchCriteria searchCriteria) {
		Map<String, String> errorMap = new HashMap<>();
		
		if((searchCriteria.getMonthYear()) == null) {
			errorMap.put("INVALID_SEARCH_CRITERIA", "monthYear can not be empty/blank");
		}
		
		createCustomException(errorMap);
	}
	
	public void validateconsumerPaymentHistoryReport(WSReportSearchCriteria searchCriteria) {
		
		Map<String, String> errorMap = new HashMap<>();
		
		if(!StringUtils.hasText(searchCriteria.getTenantId())) {
			errorMap.put("INVALID_SEARCH_CRITERIA", "Ulb can not be empty/blank");
		}
		
		if(!StringUtils.hasText(searchCriteria.getConsumerCode())){
			errorMap.put("INVALID_SEARCH_CRITERIA", "Consumer Number can not be empty/blank");
		}
		
		createCustomException(errorMap);
	}
	
	public void validateWaterNewConsumerMonthlyReport(WSReportSearchCriteria searchCriteria) {
		
		Map<String, String> errorMap = new HashMap<>();
		
		if(!StringUtils.hasText(searchCriteria.getTenantId())) {
			errorMap.put("INVALID_SEARCH_CRITERIA", "Ulb can not be empty/blank");
		}
		
		if(searchCriteria.getMonthYear()==null){
			errorMap.put("INVALID_SEARCH_CRITERIA", "Month Year can not be empty/blank");
		}
		
		createCustomException(errorMap);
	}
	
	public void validateConsumerBillHistoryReport(WSReportSearchCriteria searchCriteria) {
		
		Map<String, String> errorMap = new HashMap<>();
		
		if(!StringUtils.hasText(searchCriteria.getConsumerCode())) {
			errorMap.put("INVALID_SEARCH_CRITERIA", "Connection No can not be empty/blank");
		}
		
		createCustomException(errorMap);
	}
	
	public void validateWaterMonthlyDemandReport(WSReportSearchCriteria searchCriteria) {

		Map<String, String> errorMap = new HashMap<>();

		if(!StringUtils.hasText(searchCriteria.getTenantId())) {
			errorMap.put("INVALID_SEARCH_CRITERIA", "Ulb can not be empty/blank");
		}

		if(searchCriteria.getFromDate() == null){
			errorMap.put("INVALID_SEARCH_CRITERIA", "From Date can not be empty/blank");
		}

		if(searchCriteria.getToDate() == null){
			errorMap.put("INVALID_SEARCH_CRITERIA", "To Date can not be empty/blank");
		}

		createCustomException(errorMap);
	}

}
