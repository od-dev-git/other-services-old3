package org.egov.report.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

import org.springframework.util.CollectionUtils;
import org.egov.report.web.model.User;
import org.egov.common.contract.request.RequestInfo;
import org.egov.report.web.model.PropertyDetailsResponse;
import org.egov.report.model.UserSearchCriteria;
import org.egov.report.repository.PropertyDetailsReportRepository;
import org.egov.report.validator.PropertyReportValidator;
import org.egov.report.web.model.PropertyDetailsSearchCriteria;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

@Service
public class PropertyService {
	
	@Autowired
	private PropertyReportValidator prValidator;

	@Autowired
	private PropertyDetailsReportRepository pdRepository;
	
	@Autowired
	private UserService userService;

	public List<PropertyDetailsResponse> getPropertyDetails(RequestInfo requestInfo,
			PropertyDetailsSearchCriteria searchCriteria) {
		// TODO Auto-generated method stub

		prValidator.validatePropertyDetailsSearchCriteria(searchCriteria);

		List<PropertyDetailsResponse> propDetlResponse = pdRepository.getPropertyDetails(searchCriteria);


		//Extracting user info from userService

		Set<String> uuIds = new HashSet<>();
		UserSearchCriteria usCriteria = new UserSearchCriteria();

				if(!CollectionUtils.isEmpty(propDetlResponse)) {
					propDetlResponse.forEach(res -> uuIds.add((res.getUserId())));
					
					usCriteria.setUuId(uuIds);

					 List<User> info = userService.getUserDetails(requestInfo, usCriteria);
					for(PropertyDetailsResponse res : propDetlResponse) {
						info.forEach(
								item -> {
									if(res.getUserId().equalsIgnoreCase(item.getUuid())) {
										res.setMobileNumber(item.getMobileNumber());
										res.setName(item.getName());
									}
								});
						}
					}

		return	propDetlResponse ;	
	}




}
