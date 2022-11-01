package org.egov.integration.validator;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.egov.common.contract.request.RequestInfo;
import org.egov.integration.model.revenue.RevenueNotification;
import org.egov.tracer.model.CustomException;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class RevenueNotificationValidator {
	
	private void createCustomException(Map<String, String> errorMap) {
		if (!errorMap.isEmpty())
			throw new CustomException(errorMap);
	}
	

	public void validateRevenueNotificationRequest(List<RevenueNotification> request) {
		
		Map<String, String> errorMap = new HashMap<>();
		request.stream().forEach(item -> {
			
			if(!StringUtils.hasText(item.getAddress()))
				errorMap.put("NO_DATA_GIVEN", "Address Cannot be Empty");
			
			if(!StringUtils.hasText(item.getDistrictName()))
				errorMap.put("NO_DATA_GIVEN", "District Name Cannot be Empty");
			
			if(!StringUtils.hasText(item.getTenantId()))
				errorMap.put("NO_DATA_GIVEN", "TenantId/ULB Cannot be Empty");
			
			if(!StringUtils.hasText(item.getRevenueVillage()))
				errorMap.put("NO_DATA_GIVEN", "Revenue Village Cannot be Empty");
			
			if(!StringUtils.hasText(item.getPlotNo()))
				errorMap.put("NO_DATA_GIVEN", "Plot Number Cannot be Empty");
			
			if(!StringUtils.hasText(item.getFlatNo()))
				errorMap.put("NO_DATA_GIVEN", "Flat Number Cannot be Empty");
			
			if(!StringUtils.hasText(item.getCurrentOwnerName()))
				errorMap.put("NO_DATA_GIVEN", "Current Owner Name Cannot be Empty");
			
			if(!StringUtils.hasText(item.getCurrentOwnerMobileNumber()))
				errorMap.put("NO_DATA_GIVEN", "Current Owner Mobile Number Cannot be Empty");
			
			if(!StringUtils.hasText(item.getNewOwnerName()))
				errorMap.put("NO_DATA_GIVEN", "New Owner Name Cannot be Empty");
			
			if(!StringUtils.hasText(item.getNewOwnerMobileNumber()))
				errorMap.put("NO_DATA_GIVEN", "New Owner Mobile Number Cannot be Empty");
			
		});
		
		createCustomException(errorMap);
	}


	public void validateMDMSForCreateRequest(RequestInfo requestInfo) {
		// TODO Auto-generated method stub
		
	}
}
