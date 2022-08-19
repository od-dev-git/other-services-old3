package org.egov.report.validator;

import java.util.HashMap;
import java.util.Map;

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

	public void validatetcwcSearchCriteria(PropertyDetailsSearchCriteria searchCriteria) {
		// TODO Auto-generated method stub

Map<String, String> errorMap = new HashMap<>();

		if(!StringUtils.hasText(searchCriteria.getUlbName())) {
			errorMap.put("INVALID_CRITERIA", "ULB Name can not be blank/empty");
		}
		if(searchCriteria.getStartdate() == null ) {
			errorMap.put("INVALID_CRITERIA", "Start Date can not be blank/empty");
		}
		if(searchCriteria.getEnddate() == null ) {
			errorMap.put("INVALID_CRITERIA", "End Date can not be blank/empty");
		}
		if (!errorMap.isEmpty())
			throw new CustomException(errorMap);
	

}
}

