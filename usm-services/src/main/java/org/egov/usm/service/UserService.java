package org.egov.usm.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.request.Role;
import org.egov.usm.config.USMConfiguration;
import org.egov.usm.repository.ServiceRequestRepository;
import org.egov.usm.utility.Constants;
import org.egov.usm.web.model.SDAMember;
import org.egov.usm.web.model.user.Citizen;
import org.egov.usm.web.model.user.CreateUserRequest;
import org.egov.usm.web.model.user.Gender;
import org.egov.usm.web.model.user.UserResponse;
import org.egov.usm.web.model.user.UserSearchRequest;
import org.egov.usm.web.model.user.UserType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Service used to search User and create user through by calling
 * egov-user-service
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
	 * @param role
	 * @return user uuid
	 */
	public Citizen isUserPresent(String mobileNumber, RequestInfo requestInfo, String tenantId, String role) {

		UserSearchRequest searchRequest = UserSearchRequest.builder().userName(mobileNumber).tenantId(getRootTenantId(tenantId))
				.userType(role).requestInfo(requestInfo).build();
		StringBuilder url = new StringBuilder(config.getUserHost() + config.getUserSearchEndpoint());
		UserResponse res = mapper.convertValue(serviceRequestRepository.fetchResult(url, searchRequest),
				UserResponse.class);
		if (CollectionUtils.isEmpty(res.getUser())) {
			return null;
		}
		return res.getUser().get(0);
	}

	/**
	 * @param sdaMember
	 * @param requestInfo
	 * @return user uuid
	 */
	public String createUser(SDAMember sdaMember, RequestInfo requestInfo) {

		Citizen citizen = new Citizen();
		if (!StringUtils.isEmpty(sdaMember.getName())) {
			citizen.setName(sdaMember.getName());
		} else {
			citizen.setName(Constants.SDA_MEMBER);
		}
		citizen.setUserName(sdaMember.getMobileNumber());
		citizen.setGender(Gender.fromValue(sdaMember.getGender()));
		citizen.setActive(true);
		citizen.setMobileNumber(sdaMember.getMobileNumber());
		citizen.setTenantId(getRootTenantId(sdaMember.getTenant()));
		citizen.setType(UserType.CITIZEN);
		
		List<Role> userRoles = new ArrayList<>();
		userRoles.add(Role.builder().code(Constants.ROLE_CITIZEN).build());
		userRoles.add(Role.builder().code(Constants.ROLE_SDA_MEMBER).build());
		
		citizen.setRoles(userRoles);

		StringBuilder url = new StringBuilder(config.getUserHost() + config.getUserCreateEndpoint());
		CreateUserRequest req = CreateUserRequest.builder().citizen(citizen).requestInfo(requestInfo).build();

		UserResponse res = mapper.convertValue(serviceRequestRepository.fetchResult(url, req), UserResponse.class);
		return res.getUser().get(0).getUuid().toString();
	}

	
	/**
	 * @param tenantId
	 * @return Root TenatId e.g. od.cuttack as od
	 */
	public static String getRootTenantId(String tenantId) {
		tenantId = tenantId.split("\\.")[0];
		return tenantId;
	}
	

	/**
	 * getUserByUuidAndRole
	 * 
	 * @param uuid
	 * @param requestInfo
	 * @param tenantId
	 * @param role
	 * @return Citizen
	 */
	public Citizen getUserByUuidAndRole(String uuid, RequestInfo requestInfo, String tenantId, String role) {
		UserSearchRequest searchRequest = UserSearchRequest.builder().uuid(Collections.singletonList(uuid))
				.tenantId(getRootTenantId(tenantId)).userType(role).requestInfo(requestInfo).build();
		StringBuilder url = new StringBuilder(config.getUserHost()+config.getUserSearchEndpoint()); 
		UserResponse res = mapper.convertValue(serviceRequestRepository.fetchResult(url, searchRequest), UserResponse.class);
		
		if(CollectionUtils.isEmpty(res.getUser())) {
			return null;
		}
		return res.getUser().get(0);
	}
	
	/**
	 * getUserByUuid
	 * @param uuids
	 * @param requestInfo
	 * @return List<Citizen>
	 */
	public List<Citizen> getUserByUuid(List<String> uuids, RequestInfo requestInfo) {
		UserSearchRequest searchRequest = UserSearchRequest.builder().uuid(uuids)
				.requestInfo(requestInfo).build();
		StringBuilder url = new StringBuilder(config.getUserHost()+config.getUserSearchEndpoint()); 
		UserResponse res = mapper.convertValue(serviceRequestRepository.fetchResult(url, searchRequest), UserResponse.class);
		
		if(CollectionUtils.isEmpty(res.getUser())) {
			return null;
		}
		return res.getUser();
	}

	
	/**
	 * 
	 * @param citizen
	 * @param requestInfo
	 * @param officerRole
	 * @return uuid
	 */
	public String updateUserOfficialRole(Citizen citizen, RequestInfo requestInfo, String officerRole) {
		//Get all roles
		List<Role> userRoles = citizen.getRoles();
		
		for(Role userRole : userRoles) {
			if(userRole.getCode().equals(officerRole)) {
				return citizen.getUuid().toString();
			}
		}
		
		userRoles.add(Role.builder().code(officerRole).build());
		citizen.setRoles(userRoles);

		StringBuilder updateUrl = new StringBuilder(config.getUserHost() + config.getUserUpdateEndpoint());
		CreateUserRequest updateRequest = CreateUserRequest.builder().citizen(citizen).requestInfo(requestInfo).build();

		UserResponse updateResponse = mapper.convertValue(serviceRequestRepository.fetchResult(updateUrl, updateRequest), UserResponse.class);
		return updateResponse.getUser().get(0).getUuid().toString();
		
	}

}
