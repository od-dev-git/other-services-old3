package org.egov.usm.service;

import java.util.Arrays;

import org.apache.commons.lang3.StringUtils;
import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.request.Role;
import org.egov.usm.config.USMConfiguration;
import org.egov.usm.repository.ServiceRequestRepository;
import org.egov.usm.utility.Constants;
import org.egov.usm.web.model.SDAMember;
import org.egov.usm.web.model.user.Citizen;
import org.egov.usm.web.model.user.CreateUserRequest;
import org.egov.usm.web.model.user.UserResponse;
import org.egov.usm.web.model.user.UserSearchRequest;
import org.egov.usm.web.model.user.UserType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Service used to   search User and  create user through by calling egov-user-service
 */
@Service
public class UserService {
	
	
	private USMConfiguration config;
	
	private ServiceRequestRepository serviceRequestRepository;

	private ObjectMapper mapper;
	
	@Autowired
	public UserService(USMConfiguration config, ServiceRequestRepository serviceRequestRepository,
			ObjectMapper mapper) {
		this.config = config;
		this.serviceRequestRepository = serviceRequestRepository;
		this.mapper = mapper;
	}


	/**
     * 
     * @param mobileNumber
     * @param requestInfo
     * @param tenantId
     * @return user uuid
     */
    public String isUserPresent(String mobileNumber, RequestInfo requestInfo, String tenantId) {
    	
		UserSearchRequest searchRequest = UserSearchRequest.builder()
				.userName(mobileNumber)
				.tenantId(tenantId)
				.userType(Constants.ROLE_CITIZEN)
				.requestInfo(requestInfo)
				.build();
		StringBuilder url = new StringBuilder(config.getUserHost()+config.getUserSearchEndpoint()); 
		UserResponse res = mapper.convertValue(serviceRequestRepository.fetchResult(url, searchRequest), UserResponse.class);
		if(CollectionUtils.isEmpty(res.getUser())) {
			return null;
		}
		return res.getUser().get(0).getUuid().toString();
	}
    
    
    /**
     * @param sdaMember
     * @param requestInfo
     * @return user uuid
     */
    public String createUser(SDAMember sdaMember, RequestInfo requestInfo) {
		
    	Citizen citizen = new Citizen();
		if(!StringUtils.isEmpty(sdaMember.getName())) {
			citizen.setName(sdaMember.getName());
		} else {
			citizen.setName(Constants.SDA_MEMBER);
		}
		citizen.setUserName(sdaMember.getMobileNumber());
		citizen.setActive(true);
		citizen.setMobileNumber(sdaMember.getMobileNumber());
		citizen.setTenantId(sdaMember.getTenantId());
		citizen.setType(UserType.CITIZEN);
		citizen.setRoles(Arrays.asList(Role.builder().code(Constants.ROLE_CITIZEN).build()));
		
		StringBuilder url = new StringBuilder(config.getUserHost()+config.getUserCreateEndpoint()); 
		CreateUserRequest req = CreateUserRequest.builder().citizen(citizen).requestInfo(requestInfo).build();
		
		UserResponse res = mapper.convertValue(serviceRequestRepository.fetchResult(url, req), UserResponse.class);
		return res.getUser().get(0).getUuid().toString();
	}
}
