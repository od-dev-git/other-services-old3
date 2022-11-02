package org.egov.integration.validator;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import org.egov.common.contract.request.RequestInfo;
import org.egov.integration.config.IntegrationConfiguration;
import org.egov.integration.model.revenue.RevenueNotificationRequest;
import org.egov.integration.model.revenue.RevenueNotificationSearchCriteria;
import org.egov.integration.repository.ServiceRepository;
import org.egov.integration.util.RevenueNotificationConstants;
import org.egov.mdms.model.MasterDetail;
import org.egov.mdms.model.MdmsCriteria;
import org.egov.mdms.model.MdmsCriteriaReq;
import org.egov.mdms.model.ModuleDetail;
import org.egov.tracer.model.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.jayway.jsonpath.JsonPath;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class RevenueNotificationValidator {
	
	@Autowired
	ServiceRepository serviceRepository;
	
	@Autowired
	IntegrationConfiguration configuration;
	
	private void createCustomException(Map<String, String> errorMap) {
		if (!errorMap.isEmpty())
			throw new CustomException(errorMap);
	}

	public void validateSearch(RevenueNotificationSearchCriteria searchCriteria) {
		Map<String, String> errorMap = new HashMap<>();

		if (!StringUtils.hasText(searchCriteria.getTenantId())) {
			errorMap.put("INVALID_SEARCH_CRITERIA", "Ulb can not be empty/blank");
		}
		createCustomException(errorMap);
	}
	
	public void validateMDMSForCreateRequest(RevenueNotificationRequest request) {
		
		Map<String, String> errorMap = new HashMap<>();
		
		List<String> tenantids = getDataFromMdms(request);	
		request.getRevenueNotifications().stream().forEach(item -> {
			if(!tenantids.contains(item.getTenantid())) {
				errorMap.put("INVAILD_TENANTID", "Tenant Id not valid");
			}
		});	
		if(!errorMap.isEmpty())
			createCustomException(errorMap);
	}

	private List<String> getDataFromMdms(RevenueNotificationRequest request) {
		
		String mdmsHost = configuration.getMdmsHost();
		String mdmsEndpoint = configuration.getMdmsEndpoint();
		StringBuilder uri = new StringBuilder(mdmsHost).append(mdmsEndpoint);
		List<String> names = Arrays.asList(RevenueNotificationConstants.MDMS_NAME_TENANTS);
		MdmsCriteriaReq criteriaReq = prepareMdMsRequest(RevenueNotificationConstants.MDMS_MODULE_NAME, names,
				RevenueNotificationConstants.MDMS_TENANT_ID, RevenueNotificationConstants.MDMS_FILTER,
				request.getRequestInfo());
		try {
			Object result = serviceRepository.fetchResult(uri, criteriaReq);
			return JsonPath.read(result, RevenueNotificationConstants.JSON_FILTER);
		} catch (Exception e) {
			throw new CustomException("MDMS_FETCH_EXCEPTION", "Data fetch exception from MDMS");
		}
	}

	private MdmsCriteriaReq prepareMdMsRequest(String moduleName, List<String> names, String tenantId,
			String filter, RequestInfo requestInfo) {
		
		List<MasterDetail> masterDetails = new ArrayList<>();
		names.forEach(name -> {
			masterDetails.add(MasterDetail.builder().name(name).filter(filter).build());
		});
		ModuleDetail moduleDetail = ModuleDetail.builder().moduleName(moduleName).masterDetails(masterDetails).build();
		List<ModuleDetail> moduleDetails = new ArrayList<>();
		moduleDetails.add(moduleDetail);
		MdmsCriteria mdmsCriteria = MdmsCriteria.builder().tenantId(tenantId).moduleDetails(moduleDetails).build();
		return MdmsCriteriaReq.builder().requestInfo(requestInfo).mdmsCriteria(mdmsCriteria).build();
	}
}
