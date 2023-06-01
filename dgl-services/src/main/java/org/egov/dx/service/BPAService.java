package org.egov.dx.service;

import static org.egov.dx.util.PTServiceDXConstants.SEPARATER;
import static org.egov.dx.util.PTServiceDXConstants.TENANT_ID_FIELD_FOR_SEARCH_URL;
import static org.egov.dx.util.PTServiceDXConstants.URL_PARAMS_SEPARATER;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.egov.dx.repository.Repository;
import org.egov.dx.util.Configurations;
import org.egov.dx.web.models.RequestInfoWrapper;
import org.egov.dx.web.models.BPA.BPA;
import org.egov.dx.web.models.BPA.BPAResponse;
import org.egov.dx.web.models.BPA.BPASearchCriteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;
@Slf4j
@Service
public class BPAService {

	@Autowired
	private Repository repository;

	@Autowired
	private ObjectMapper mapper;

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private Configurations configurations;
	
	public List<BPA> getBPAPermitLetter(BPASearchCriteria criteria, RequestInfoWrapper requestInfoWrapper) {
		
		StringBuilder url = getBPASearchUrl(criteria);
		log.info("Search with these Permit Letter Number: "+criteria.getApprovalNo());
		requestInfoWrapper.getRequestInfo().setTs(null);
		Object responseObject = repository.fetchResult(url, requestInfoWrapper);
		BPAResponse response = mapper.convertValue(responseObject, BPAResponse.class);
		return response.getBPA();
	}

	private StringBuilder getBPASearchUrl(BPASearchCriteria criteria) {
		
		return new StringBuilder().append(configurations.getBpaHost())
				.append(configurations.getBpaSearchEndpoint()).append(URL_PARAMS_SEPARATER)
				.append(TENANT_ID_FIELD_FOR_SEARCH_URL).append(criteria.getTenantId())
				.append(SEPARATER).append("approvalNo=")
				.append(StringUtils.join(criteria.getApprovalNo()));
	}
}
