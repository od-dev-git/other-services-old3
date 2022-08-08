package org.egov.report.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.egov.common.contract.request.RequestInfo;
import org.egov.report.config.ReportServiceConfiguration;
import org.egov.report.model.UserSearchRequest;
import org.egov.report.repository.ReportDao;
import org.egov.report.repository.ServiceRepository;
import org.egov.report.web.model.OwnerInfo;
import org.egov.report.web.model.User;
import org.egov.report.web.model.UserDetailResponse;
import org.egov.report.web.model.UserResponse;
import org.egov.tracer.model.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;

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
	
	public List<User> getUserDetails(RequestInfo requestInfo, List<Long> userIds){
	
		List<User> usersInfo = new ArrayList<>();
		
		StringBuilder uri = new StringBuilder(configuration.getUserHost())
				.append(configuration.getUserSearchEndpoint());
		
		UserSearchRequest request = UserSearchRequest.builder().requestInfo(requestInfo).id(userIds).build();
			
		try {
		Object response = repository.fetchResult(uri, request);
		UserResponse userResponse = mapper.convertValue(response, UserResponse.class);
		usersInfo.addAll(userResponse.getUserInfo());
		}catch(Exception ex) {
			log.error("External Service Call Erorr", ex);
			throw new CustomException("USER_FETCH_EXCEPTION", "Unable to fetch User Information");
		}
		
		return usersInfo;
		
	}
	

}
