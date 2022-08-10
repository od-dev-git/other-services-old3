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
import org.egov.report.model.PropertyDetailsResponseList;
import org.egov.report.repository.PropertyDetailsReportRepository;
import org.egov.report.validator.PropertyReportValidator;
import org.egov.report.web.model.PropertyDetailsResponse;
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

	public PropertyDetailsResponse getPropertyDetails(RequestInfo requestInfo,
			PropertyDetailsSearchCriteria searchCriteria) {
		// TODO Auto-generated method stub

		prValidator.validatePropertyDetailsSearchCriteria(searchCriteria);

		List<PropertyDetailsResponseList> propDetlResponse = pdRepository.getPropertyDetails(searchCriteria);


		//Extracting user info from userService

		Set<String> userIds = new HashSet<>();

				if(!CollectionUtils.isEmpty(propDetlResponse)) {
					propDetlResponse.forEach(res -> userIds.add(res.getUserId()));

					 List<User> info = userService.getUserDetails(requestInfo, userIds);
					for(PropertyDetailsResponseList res : propDetlResponse) {
						info.forEach(
								item -> {
									if(res.getUserId().equalsIgnoreCase(item.getUuid())) {
										res.setMobileNumber(item.getMobileNumber());
										res.setName(item.getName());
									}
								});
						}
					}

		 
		 PropertyDetailsResponse pdr = new PropertyDetailsResponse(propDetlResponse);

		 return pdr;
	}




}
