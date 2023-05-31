package org.egov.dx.service;

import static org.egov.dx.util.PTServiceDXConstants.SEPARATER;
import static org.egov.dx.util.PTServiceDXConstants.TENANT_ID_FIELD_FOR_SEARCH_URL;
import static org.egov.dx.util.PTServiceDXConstants.URL_PARAMS_SEPARATER;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.egov.dx.repository.Repository;
import org.egov.dx.util.Configurations;
import org.egov.dx.web.models.RequestInfoWrapper;
import org.egov.dx.web.models.MR.MRSearchCriteria;
import org.egov.dx.web.models.MR.MarriageRegistration;
import org.egov.dx.web.models.MR.MarriageResponse;
import org.egov.dx.web.models.TL.TradeLicenseSearchCriteria;
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
	
	public List<MarriageRegistration> getTradeLicenses(TradeLicenseSearchCriteria criteria,
			RequestInfoWrapper requestInfoWrapper) {
		
		StringBuilder url = getTlSearchUrl(criteria);
		log.info("Search with these License Numbers: "+criteria.getLicenseNumbers());
		Object responseObject = repository.fetchResult(url, requestInfoWrapper);
		MarriageResponse response = mapper.convertValue(responseObject, MarriageResponse.class);
		return response.getMarriageRegistrations();
	}

	private StringBuilder getTlSearchUrl(TradeLicenseSearchCriteria criteria) {
		
		return new StringBuilder().append(configurations.getTlHost())
				.append(configurations.getTlSearchEndpoint()).append(URL_PARAMS_SEPARATER)
				.append(TENANT_ID_FIELD_FOR_SEARCH_URL).append(criteria.getTenantId())
				.append(SEPARATER).append("licenseNumbers=")
				.append(StringUtils.join(criteria.getLicenseNumbers(),","));
	}

}
