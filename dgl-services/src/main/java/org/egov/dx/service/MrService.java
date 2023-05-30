package org.egov.dx.service;

import static org.egov.dx.util.PTServiceDXConstants.BUSINESSSERVICES_FIELD_FOR_SEARCH_URL;
import static org.egov.dx.util.PTServiceDXConstants.CONSUMER_CODE_SEARCH_FIELD_NAME_PAYMENT;
import static org.egov.dx.util.PTServiceDXConstants.SEPARATER;
import static org.egov.dx.util.PTServiceDXConstants.TENANT_ID_FIELD_FOR_SEARCH_URL;
import static org.egov.dx.util.PTServiceDXConstants.URL_PARAMS_SEPARATER;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.egov.dx.repository.Repository;
import org.egov.dx.util.Configurations;
import org.egov.dx.web.models.MRSearchCriteria;
import org.egov.dx.web.models.MarriageRegistration;
import org.egov.dx.web.models.MarriageResponse;
import org.egov.dx.web.models.Payment;
import org.egov.dx.web.models.PaymentResponse;
import org.egov.dx.web.models.PaymentSearchCriteria;
import org.egov.dx.web.models.RequestInfoWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class MrService {
	
	@Autowired
	private Repository repository;

	@Autowired
	private ObjectMapper mapper;

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private Configurations configurations;

	public List<MarriageRegistration> getMarriageRegistrations(MRSearchCriteria criteria,
			RequestInfoWrapper requestInfoWrapper) {
		
		StringBuilder url = getMRSearchUrl(criteria);
		log.info("Marriage Searched with MRNumber : "+criteria.getMrNumbers());
		Object responseObject = repository.fetchResult(url, requestInfoWrapper);
		MarriageResponse response = mapper.convertValue(responseObject, MarriageResponse.class);
		return response.getMarriageRegistrations();
	}

	private StringBuilder getMRSearchUrl(MRSearchCriteria criteria) {
		
		return new StringBuilder().append(configurations.getMrHost())
				.append(configurations.getMrSearchEndpoint()).append(URL_PARAMS_SEPARATER)
				.append(TENANT_ID_FIELD_FOR_SEARCH_URL).append(criteria.getTenantId())
				.append(SEPARATER).append("mrNumbers=")
				.append(StringUtils.join(criteria.getMrNumbers(),","));
	}
	
	

}
