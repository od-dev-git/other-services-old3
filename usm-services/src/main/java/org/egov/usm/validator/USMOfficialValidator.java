package org.egov.usm.validator;

import java.util.Collections;
import java.util.List;

import javax.validation.Valid;

import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.request.Role;
import org.egov.tracer.model.CustomException;
import org.egov.usm.repository.USMOfficialRepository;
import org.egov.usm.service.UserService;
import org.egov.usm.utility.Constants;
import org.egov.usm.web.model.USMOfficial;
import org.egov.usm.web.model.USMOfficialRequest;
import org.egov.usm.web.model.USMOfficialSearchCriteria;
import org.egov.usm.web.model.user.Citizen;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

@Service
public class USMOfficialValidator {

	@Autowired
	private USMOfficialRepository repository;
	
	@Autowired
	private UserService userService;

	/**
	 * Throw error if member already exists
	 * 
	 * @param sdaMember
	 */
	public void isOfficialAlreadyExists(USMOfficial usmOfficial) {

		USMOfficialSearchCriteria searchCriteria = USMOfficialSearchCriteria.builder()
				.tenantId(usmOfficial.getTenant()).ward(usmOfficial.getWard()).slumcode(usmOfficial.getSlumcode())
				.build();

		List<USMOfficial> usmOfficials = repository.getOfficialRequests(searchCriteria);
		if (!CollectionUtils.isEmpty(usmOfficials))
			throw new CustomException("EG_USM_OFFICIAL_ALREADY_EXIST_ERR",
					"The official entity provided in assign request already exist.");
	}

	/**
	 * return the existing member details
	 * 
	 * @param usmOfficial
	 * @return existing USMOfficial
	 */
	public USMOfficial isOfficialExists(USMOfficial usmOfficial) {
		if (ObjectUtils.isEmpty(usmOfficial.getId()))
			throw new CustomException("EG_SY_UUID_NOT_PROVIDED_ERR",
					"Providing official id is mandatory for updating and deleting official ");

		USMOfficialSearchCriteria searchCriteria = USMOfficialSearchCriteria.builder().id(usmOfficial.getId()).build();

		List<USMOfficial> usmOfficials = repository.getOfficialRequests(searchCriteria);
		if (CollectionUtils.isEmpty(usmOfficials))
			throw new CustomException("EG_SURVEY_DOES_NOT_EXIST_ERR",
					"The survey entity provided in update request does not exist.");

		return usmOfficials.get(0);
	}

	public void validateUSMOfficialRequest(@Valid USMOfficialRequest usmOfficialRequest) {
		RequestInfo requestInfo = usmOfficialRequest.getRequestInfo();
		
		// Check User isUserPresent
		List<Citizen> citizens = userService.getUserByUuid(Collections.singletonList(usmOfficialRequest.getUsmOffcial().getAssigned()), requestInfo);

		if(CollectionUtils.isEmpty(citizens)) {
			throw new CustomException("ID ERROR", "Official id is not present");
		} 
		
		Boolean isEmployee = false;
		Boolean isOfficer = false;
		for(Role role :  citizens.get(0).getRoles()){
			if(role.getCode().equalsIgnoreCase(Constants.ROLE_EMPLOYEE))
				isEmployee = true;
			
			if(role.getCode().equalsIgnoreCase(usmOfficialRequest.getUsmOffcial().getRole()))
				isOfficer = true;
		}
		
		if(!isEmployee)
			throw new CustomException("INVALID USER ERROR", "Official id is unauthorized Employee Type");
		
		if(!isOfficer)
			throw new CustomException("INVALID USER ERROR", "Official id is unauthorized " + usmOfficialRequest.getUsmOffcial().getRole() + " Type");
	}

}
