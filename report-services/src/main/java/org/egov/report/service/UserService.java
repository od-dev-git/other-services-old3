package org.egov.report.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.egov.common.contract.request.RequestInfo;
import org.egov.report.config.ReportServiceConfiguration;
import org.egov.report.model.UserSearchCriteria;
import org.egov.report.model.UserSearchRequest;
import org.egov.report.repository.ReportDao;
import org.egov.report.repository.ServiceRepository;
import org.egov.report.web.model.OwnerInfo;
import org.egov.report.web.model.User;
import org.egov.report.web.model.UserDetailResponse;
import org.egov.tracer.model.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

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
	
	@Autowired
	private ReportDao reportDao;
	
	private UserSearchRequest getBaseUserSearchRequest(String tenantId, RequestInfo requestInfo) {
		return UserSearchRequest.builder().requestInfo(requestInfo).userType("EMPLOYEE").tenantId(tenantId).active(true)
				.build();
	}

	public List<OwnerInfo> getUser(RequestInfo requestInfo, List<Long> userIds) {
		List<User> users = reportDao.getEmployeeBaseTenant(userIds);
		Map<String, List<Long>> tenantWiseEmployeeMap = users.stream().collect(Collectors.groupingBy(User::getTenantId, Collectors.mapping(User::getId, Collectors.toList())));
		
		StringBuilder uri = new StringBuilder(configuration.getUserHost())
				.append(configuration.getUserSearchEndpoint());
		
		List<OwnerInfo> usersInfo = new ArrayList<>();
		
		for (String tenantId : tenantWiseEmployeeMap.keySet()) {
			UserSearchRequest userSearchRequest = getBaseUserSearchRequest(tenantId, requestInfo);
			userSearchRequest.setId(tenantWiseEmployeeMap.get(tenantId));
			try {
				Object response = repository.fetchResult(uri, userSearchRequest);
				UserDetailResponse userDetailResponse = mapper.convertValue(response, UserDetailResponse.class);
				usersInfo.addAll(userDetailResponse.getUser());
			} catch (Exception ex) {
				log.error("External Service call error", ex);
				throw new CustomException("USER_FETCH_EXCEPTION", "Unable to fetch user information");
			}
		}
		
		return usersInfo;
	}
	
	public List<OwnerInfo> getUserDetails(RequestInfo requestInfo, UserSearchCriteria userSearchCriteria){

		StringBuilder uri = new StringBuilder(configuration.getUserHost())
				.append(configuration.getUserSearchEndpoint());

		UserSearchRequest request = generateUserSearchRequest(requestInfo, userSearchCriteria);

		try {
			Object response = repository.fetchResult(uri, request);
			UserDetailResponse userDetailResponse = mapper.convertValue(response, UserDetailResponse.class);
			return userDetailResponse.getUser();
		}catch(Exception ex) {
			log.error("External Service Call Erorr", ex);
			throw new CustomException("USER_FETCH_EXCEPTION", "Unable to fetch User Information");
		}
	}

	private UserSearchRequest generateUserSearchRequest(RequestInfo requestInfo, UserSearchCriteria criteria) {
		
		UserSearchRequest request = UserSearchRequest.builder().requestInfo(requestInfo).active(Boolean.TRUE).build();
		
		if(!CollectionUtils.isEmpty(criteria.getUuid())) {
			request.setUuid(criteria.getUuid().stream().collect(Collectors.toSet()));
		}
		
		if(!CollectionUtils.isEmpty(criteria.getId())) {
			request.setId(criteria.getId().stream().collect(Collectors.toList()));
		}
		return request;
	}
	

}
