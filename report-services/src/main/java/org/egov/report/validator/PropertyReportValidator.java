package org.egov.report.validator;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.egov.report.model.DCBSearchCriteria;
import org.egov.report.model.UtilityReportDetails;
import org.egov.report.web.model.PropertyDetailsSearchCriteria;
import org.egov.tracer.model.CustomException;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

@Component
public class PropertyReportValidator {
	
	public void validatePropertyDetailsSearchCriteria(PropertyDetailsSearchCriteria searchCriteria) {
		// TODO Auto-generated method stub

Map<String, String> errorMap = new HashMap<>();

		if(!StringUtils.hasText(searchCriteria.getUlbName())) {
			errorMap.put("INVALID_CRITERIA", "ULB Name can not be blank/empty");
		}
		if (!errorMap.isEmpty())
			throw new CustomException(errorMap);
	}

	public void validateTaxCollectorWiseCollectionSearchCriteria(PropertyDetailsSearchCriteria searchCriteria) {
		// TODO Auto-generated method stub

Map<String, String> errorMap = new HashMap<>();

		if(!StringUtils.hasText(searchCriteria.getUlbName())) {
			errorMap.put("INVALID_CRITERIA", "ULB Name can not be blank/empty");
		}
		if(searchCriteria.getStartDate() == null ) {
			errorMap.put("INVALID_CRITERIA", "Start Date can not be blank/empty");
		}
		if(searchCriteria.getEndDate() == null ) {
			errorMap.put("INVALID_CRITERIA", "End Date can not be blank/empty");
		}
		if (!errorMap.isEmpty())
			throw new CustomException(errorMap);
	

}

	public void validatePropertyWiseCollectionSearchCriteria(PropertyDetailsSearchCriteria searchCriteria) {
		
		Map<String, String> errorMap = new HashMap<>();

		if(!StringUtils.hasText(searchCriteria.getUlbName())) {
			errorMap.put("INVALID_CRITERIA", "ULB Name can not be blank/empty");
		}
		if (!errorMap.isEmpty())
			throw new CustomException(errorMap);
		
	}
	
	public void validateDCBReport(DCBSearchCriteria dcbSearchCriteria) {

		if (StringUtils.isEmpty(dcbSearchCriteria.getFinancialYear())
				|| CollectionUtils.isEmpty(dcbSearchCriteria.getTenantIds())) {

			throw new CustomException("INVALID_REQUEST",
					" Either Financial Year or TenantId is missing ! Kindly provide both to proceed ..");

		}
	}

	public void validateIfDCBReportAlreadyExists(List<UtilityReportDetails> reportList, String tenantId) {

		if (!reportList.isEmpty()) {

			UtilityReportDetails dcbReport = reportList.get(0);

			if (dcbReport.getTenantId().equalsIgnoreCase(tenantId)) {
				throw new CustomException("REPORT_EXISTS",
						"Report For the given criteria already present. Kindly download the same ..");
			}

		}

	}

	public void validateIfReportGenerationInProcess(List<UtilityReportDetails> reportList, String tenantId) {

		if (!reportList.isEmpty()) {

			UtilityReportDetails dcbReport = reportList.get(0);

			if (dcbReport.getTenantId().equalsIgnoreCase(tenantId) && StringUtils.isEmpty(dcbReport.getFileStoreId())) {
				throw new CustomException("REPORT_GENERATION_INPROCESS",
						"Report Generation is currently in process. Kindly wait ..");
			}

		}

	}

}

