package org.egov.report.service;

import java.util.List;

import org.egov.common.contract.request.RequestInfo;
import org.egov.report.config.ReportServiceConfiguration;
import org.egov.report.model.UserSearchRequest;
import org.egov.report.repository.ServiceRepository;
import org.egov.report.web.model.UserDetailResponse;
import org.egov.tracer.model.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class UserService {
	
	@Autowired
	private ServiceRepository repository;
	
	@Autowired
	private ObjectMapper mapper;
	
	@Autowired
	private ReportServiceConfiguration configuration;
	
	private UserSearchRequest getBaseUserSearchRequest(String tenantId, RequestInfo requestInfo) {
		return UserSearchRequest.builder().requestInfo(requestInfo).userType("EMPLOYEE").tenantId(tenantId).active(true)
				.build();
	}

	public UserDetailResponse getUser(String tenantId, RequestInfo requestInfo, List<Long> userIds) {
		StringBuilder uri = new StringBuilder(configuration.getUserHost())
				.append(configuration.getUserSearchEndpoint());
		
		UserSearchRequest userSearchRequest = getBaseUserSearchRequest(tenantId, requestInfo);
		userSearchRequest.setId(userIds);
		
		try {
			Object response = repository.fetchResult(uri, userSearchRequest);
			return mapper.convertValue(response, UserDetailResponse.class);
		} catch (Exception ex) {
			log.error("External Service call error", ex);
			throw new CustomException("USER_FETCH_EXCEPTION", "Unable to fetch user information");
		}
		
	}

}
