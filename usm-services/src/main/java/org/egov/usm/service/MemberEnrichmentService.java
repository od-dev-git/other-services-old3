package org.egov.usm.service;


import javax.validation.Valid;

import org.egov.common.contract.request.RequestInfo;
import org.egov.usm.utility.Constants;
import org.egov.usm.utility.USMUtil;
import org.egov.usm.web.model.AuditDetails;
import org.egov.usm.web.model.SDAMember;
import org.egov.usm.web.model.SDAMembersRequest;
import org.egov.usm.web.model.user.Citizen;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

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
		
		// Check User isUserPresent as Citizen
		Citizen citizen = userService.isUserPresent(sdaMembersRequest.getSdaMember().getMobileNumber(), requestInfo, sdaMembersRequest.getSdaMember().getTenantId(), Constants.ROLE_CITIZEN);

		if (!ObjectUtils.isEmpty(citizen)) {
			// If present Update its official role
			userAccountId = userService.updateUserOfficialRole(citizen, requestInfo, Constants.ROLE_SDA_MEMBER);
		} else {
			//If not present Create a citizen with sda member role
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
		
		// Check User isUserPresent as Citizen
		Citizen citizen = userService.isUserPresent(sdaMembersRequest.getSdaMember().getMobileNumber(), requestInfo, sdaMembersRequest.getSdaMember().getTenantId(), Constants.ROLE_CITIZEN);

		if (!ObjectUtils.isEmpty(citizen)) {
			// If present Update its official role
			userAccountId = userService.updateUserOfficialRole(citizen, requestInfo, Constants.ROLE_SDA_MEMBER);
		} else {
			//If not present Create a citizen with official role
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
