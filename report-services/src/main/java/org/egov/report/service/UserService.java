package org.egov.report.service;

import static org.springframework.util.CollectionUtils.isEmpty;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.egov.common.contract.request.RequestInfo;
import org.egov.report.config.ReportServiceConfiguration;
import org.egov.report.model.UserSearchCriteria;
import org.egov.report.model.UserSearchRequest;
import org.egov.report.repository.FileStoreRepository;
import org.egov.report.repository.ReportDao;
import org.egov.report.repository.ServiceRepository;
import org.egov.report.repository.UserRepository;
import org.egov.report.util.EncryptionDecryptionUtil;
import org.egov.report.util.ReportConstants;
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
	
	@Autowired
	private EncryptionDecryptionUtil encryptionDecryptionUtil;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private FileStoreRepository fileRepository;
	
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
		
		if(StringUtils.hasText(criteria.getTenantId())) {
			if(StringUtils.hasText(criteria.getUserType()) && UserSearchCriteria.CITIZEN.equals(criteria.getUserType()))
				request.setTenantId(ReportConstants.STATE_TENANT);
			else
				request.setTenantId(criteria.getTenantId());
		}
		
		if(!CollectionUtils.isEmpty(criteria.getUuid())) {
			request.setUuid(criteria.getUuid().stream().collect(Collectors.toSet()));
		}
		
		if(!CollectionUtils.isEmpty(criteria.getId())) {
			request.setId(criteria.getId().stream().collect(Collectors.toList()));
		}
		
		if(StringUtils.hasText(criteria.getUserType())) {
			request.setUserType(criteria.getUserType());
		}
		return request;
	}
	
	public List<org.egov.report.user.User> searchUsers(org.egov.report.user.UserSearchCriteria searchCriteria, RequestInfo requestInfo) {

		/*
		 * searchCriteria
		 * .setTenantId(getStateLevelTenantForCitizen(searchCriteria.getTenantId(),
		 * searchCriteria.getType()));
		 */

		searchCriteria = encryptionDecryptionUtil.encryptObject(searchCriteria, "UserSearchCriteria",
				org.egov.report.user.UserSearchCriteria.class);
		List<org.egov.report.user.User> list = userRepository.findAll(searchCriteria);

		list = encryptionDecryptionUtil.decryptObject(list, "UserList", org.egov.report.user.User.class, requestInfo);

		//setFileStoreUrlsByFileStoreIds(list);
		return list;
	}
	
	private void setFileStoreUrlsByFileStoreIds(List<org.egov.report.user.User> userList) {
		List<String> fileStoreIds = userList.parallelStream().filter(p -> p.getPhoto() != null).map(org.egov.report.user.User::getPhoto)
				.collect(Collectors.toList());
		if (!isEmpty(fileStoreIds)) {
			Map<String, String> fileStoreUrlList = null;
			try {
				fileStoreUrlList = fileRepository.getUrlByFileStoreId(userList.get(0).getTenantId(), fileStoreIds);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			if (fileStoreUrlList != null && !fileStoreUrlList.isEmpty()) {
				for (org.egov.report.user.User user : userList) {
					user.setPhoto(fileStoreUrlList.get(user.getPhoto()));
				}
			}
		}
	}

}
