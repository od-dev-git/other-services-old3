package org.egov.dx.service;

import static org.egov.dx.util.PTServiceDXConstants.SEPARATER;
import static org.egov.dx.util.PTServiceDXConstants.TENANT_ID_FIELD_FOR_SEARCH_URL;
import static org.egov.dx.util.PTServiceDXConstants.URL_PARAMS_SEPARATER;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.egov.common.contract.request.RequestInfo;
import org.egov.dx.repository.Repository;
import org.egov.dx.util.Configurations;
import org.egov.dx.web.models.RequestInfoWrapper;
import org.egov.dx.web.models.MR.MRSearchCriteria;
import org.egov.dx.web.models.MR.MarriageRegistration;
import org.egov.dx.web.models.MR.MarriageResponse;
import org.egov.dx.web.models.TL.TradeLicense;
import org.egov.dx.web.models.TL.TradeLicenseResponse;
import org.egov.dx.web.models.TL.TradeLicenseSearchCriteria;
import org.egov.mdms.model.MasterDetail;
import org.egov.mdms.model.MdmsCriteria;
import org.egov.mdms.model.MdmsCriteriaReq;
import org.egov.mdms.model.ModuleDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class TLService {
	
	@Autowired
	private Repository repository;

	@Autowired
	private ObjectMapper mapper;

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private Configurations configurations;
	
	public List<TradeLicense> getTradeLicenses(TradeLicenseSearchCriteria criteria,
			RequestInfoWrapper requestInfoWrapper) {
		
		StringBuilder url = getTlSearchUrl(criteria);
		log.info("Search with these License Numbers: "+criteria.getLicenseNumbers());
		Object responseObject = repository.fetchResult(url, requestInfoWrapper);
		TradeLicenseResponse response = mapper.convertValue(responseObject, TradeLicenseResponse.class);
		return response.getLicenses();
	}

	private StringBuilder getTlSearchUrl(TradeLicenseSearchCriteria criteria) {
		
		return new StringBuilder().append(configurations.getTlHost())
				.append(configurations.getTlSearchEndpoint()).append(URL_PARAMS_SEPARATER)
				.append(TENANT_ID_FIELD_FOR_SEARCH_URL).append(criteria.getTenantId())
				.append(SEPARATER).append("licenseNumbers=")
				.append(StringUtils.join(criteria.getLicenseNumbers(),","));
	}
	
	public Object mDMSCall(RequestInfo requestInfo, String tenantId, Boolean reqForDistricts) {
		MdmsCriteriaReq mdmsCriteriaReq = getMDMSRequest(requestInfo, tenantId, reqForDistricts);
		Object result = repository.fetchResult(getMdmsSearchUrl(), mdmsCriteriaReq);
		return result;
	}
	
	public StringBuilder getMdmsSearchUrl() {
		return new StringBuilder().append(configurations.getMdmsHost()).append(configurations.getMdmsEndpoint());
	}

	public MdmsCriteriaReq getMDMSRequest(RequestInfo requestInfo, String tenantId, Boolean reqForDistricts) {
		List<ModuleDetail> moduleRequest;
		if(reqForDistricts)
			moduleRequest = getRequestForDistricts(tenantId);
		else
			moduleRequest = getRequestForTradeType(tenantId);

		List<ModuleDetail> moduleDetails = new LinkedList<>();
		moduleDetails.addAll(moduleRequest);

		MdmsCriteria mdmsCriteria = MdmsCriteria.builder().moduleDetails(moduleDetails).tenantId(tenantId).build();

		MdmsCriteriaReq mdmsCriteriaReq = MdmsCriteriaReq.builder().mdmsCriteria(mdmsCriteria).requestInfo(requestInfo)
				.build();
		return mdmsCriteriaReq;
	}
	
	public List<ModuleDetail> getRequestForDistricts(String tenantId) {
		List<MasterDetail> masterDetails = new ArrayList<>();
		final String filters = "$.[?(@.code=='"+tenantId+"')]";
		masterDetails.add(MasterDetail.builder().name("tenants").filter(filters).build());
		ModuleDetail moduleDetails = ModuleDetail.builder().masterDetails(masterDetails)
				.moduleName("tenant").build();

		return Arrays.asList(moduleDetails);
	}
	
	public List<ModuleDetail> getRequestForTradeType(String tenantId) {
	
		List<MasterDetail> masterDetails = new ArrayList<>();
		//final String filters = "$.[?(@.code=='"+tenantId+"')]";
		masterDetails.add(MasterDetail.builder().name("TradeType").build());
		ModuleDetail moduleDetails = ModuleDetail.builder().masterDetails(masterDetails)
				.moduleName("TradeLicense").build();

		return Arrays.asList(moduleDetails);
	}
}
