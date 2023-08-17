package org.egov.usm.service;


import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.egov.common.contract.request.RequestInfo;
import org.egov.usm.utility.Constants;
import org.egov.usm.utility.USMUtil;
import org.egov.usm.web.model.AuditDetails;
import org.egov.usm.web.model.SDAMember;
import org.egov.usm.web.model.SDAMembersRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MemberEnrichmentService {
	
	@Autowired
	private UserService userService;
	
	/**
	 * Enrich sdaMembersRequest
	 * 
	 * @param sdaMembersRequest
	 */
	public void enrichSDAMembersRequest(@Valid SDAMembersRequest sdaMembersRequest) {
		
		RequestInfo requestInfo = sdaMembersRequest.getRequestInfo();
		AuditDetails auditDetails = USMUtil.getAuditDetails(requestInfo.getUserInfo().getUuid(), true);
		String userAccountId = null ;
		
		//Check User isUserPresent as Citizen
		userAccountId = userService.isUserPresent(sdaMembersRequest.getSdaMember().getMobileNumber(), requestInfo, sdaMembersRequest.getSdaMember().getTenantId());
		
		//If not present create SdaMember as citizen user
		if (StringUtils.isEmpty(userAccountId)) {
			userAccountId = userService.createUser(sdaMembersRequest.getSdaMember() , requestInfo);
		}
		
		sdaMembersRequest.getSdaMember().setId(USMUtil.generateUUID());
		sdaMembersRequest.getSdaMember().setUserId(userAccountId);
		sdaMembersRequest.getSdaMember().setActive(Boolean.TRUE);
		sdaMembersRequest.getSdaMember().setAuditDetails(auditDetails);
	}

	
	
	/**
	 * Enrich update SDA member request
	 * 
	 * @param sdaMembersRequest
	 * @param existingSdaMember
	 */
	public void enrichReassignMembersRequest(@Valid SDAMembersRequest sdaMembersRequest, SDAMember existingSdaMember) {
		RequestInfo requestInfo = sdaMembersRequest.getRequestInfo();
		AuditDetails auditDetails = USMUtil.getAuditDetails(requestInfo.getUserInfo().getUuid(), false);
		auditDetails.setCreatedBy(existingSdaMember.getAuditDetails().getCreatedBy());
		auditDetails.setCreatedTime(existingSdaMember.getAuditDetails().getCreatedTime());
		
		String userAccountId = null ;
		
		//Check User isUserPresent as Citizen
		userAccountId = userService.isUserPresent(sdaMembersRequest.getSdaMember().getMobileNumber(), requestInfo, sdaMembersRequest.getSdaMember().getTenantId());
		
		//If not present create SdaMember as citizen user
		if (StringUtils.isEmpty(userAccountId)) {
			userAccountId = userService.createUser(sdaMembersRequest.getSdaMember() , requestInfo);
		}
		
		sdaMembersRequest.getSdaMember().setUserId(userAccountId);
		sdaMembersRequest.getSdaMember().setTenantId(existingSdaMember.getTenantId());
		sdaMembersRequest.getSdaMember().setWard(existingSdaMember.getWard());
		sdaMembersRequest.getSdaMember().setSlumCode(existingSdaMember.getSlumCode());
		sdaMembersRequest.getSdaMember().setActive(Boolean.TRUE);
		sdaMembersRequest.getSdaMember().setAuditDetails(auditDetails);
		
	}



	public void enrichDeassignMembersRequest(@Valid SDAMembersRequest sdaMembersRequest, SDAMember existingSdaMember) {
		RequestInfo requestInfo = sdaMembersRequest.getRequestInfo();
		AuditDetails auditDetails = USMUtil.getAuditDetails(requestInfo.getUserInfo().getUuid(), false);
		
		sdaMembersRequest.getSdaMember().setUserId(Constants.EMPTY_STRING);
		sdaMembersRequest.getSdaMember().setTenantId(existingSdaMember.getTenantId());
		sdaMembersRequest.getSdaMember().setWard(existingSdaMember.getWard());
		sdaMembersRequest.getSdaMember().setSlumCode(existingSdaMember.getSlumCode());
		sdaMembersRequest.getSdaMember().setActive(Boolean.FALSE);
		sdaMembersRequest.getSdaMember().setAuditDetails(auditDetails);
		
	}
	
}
