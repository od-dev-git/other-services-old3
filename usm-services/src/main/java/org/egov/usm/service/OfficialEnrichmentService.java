package org.egov.usm.service;

import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.egov.common.contract.request.RequestInfo;
import org.egov.tracer.model.CustomException;
import org.egov.usm.utility.Constants;
import org.egov.usm.utility.USMUtil;
import org.egov.usm.web.model.AuditDetails;
import org.egov.usm.web.model.USMOfficial;
import org.egov.usm.web.model.USMOfficialRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OfficialEnrichmentService {

	@Autowired
	private UserService userService;

	/**
	 * Enrich usmOfficialRequest
	 * 
	 * @param usmOfficialRequest
	 */
	public void enrichUSMOfficialRequest(@Valid USMOfficialRequest usmOfficialRequest) {

		RequestInfo requestInfo = usmOfficialRequest.getRequestInfo();
		AuditDetails auditDetails = USMUtil.getAuditDetails(requestInfo.getUserInfo().getUuid(), true);
		String officialAccountId = null;

		// Check User isUserPresent as Employee
		officialAccountId = userService.isUserPresent(usmOfficialRequest.getUsmOffcial().getAssigned(), requestInfo,
				usmOfficialRequest.getUsmOffcial().getTenantId(), Constants.ROLE_EMPLOYEE);

		// If not present throw exception
		if (StringUtils.isEmpty(officialAccountId)) {
			throw new CustomException("ID ERROR", "Official id is not present");
		}

		usmOfficialRequest.getUsmOffcial().setId(USMUtil.generateUUID());
		usmOfficialRequest.getUsmOffcial().setAssigned(officialAccountId);
		usmOfficialRequest.getUsmOffcial().setTenantId(usmOfficialRequest.getUsmOffcial().getTenantId());
		usmOfficialRequest.getUsmOffcial().setSlumcode(usmOfficialRequest.getUsmOffcial().getSlumcode());
		usmOfficialRequest.getUsmOffcial().setCategory(usmOfficialRequest.getUsmOffcial().getCategory());
		usmOfficialRequest.getUsmOffcial().setRole(usmOfficialRequest.getUsmOffcial().getRole());
		usmOfficialRequest.getUsmOffcial().setAuditDetails(auditDetails);
	}

	public void enrichDeassignOfficialRequest(@Valid USMOfficialRequest usmOfficialRequest,
			USMOfficial existingOfficialMember) {
		RequestInfo requestInfo = usmOfficialRequest.getRequestInfo();
		AuditDetails auditDetails = USMUtil.getAuditDetails(requestInfo.getUserInfo().getUuid(), false);
		usmOfficialRequest.getUsmOffcial().setAssigned(null);
		usmOfficialRequest.getUsmOffcial().setTenantId(existingOfficialMember.getTenantId());
		usmOfficialRequest.getUsmOffcial().setWard(existingOfficialMember.getWard());
		usmOfficialRequest.getUsmOffcial().setCategory(existingOfficialMember.getCategory());
		usmOfficialRequest.getUsmOffcial().setRole(existingOfficialMember.getRole());
		usmOfficialRequest.getUsmOffcial().setSlumcode(existingOfficialMember.getSlumcode());
		usmOfficialRequest.getUsmOffcial().setAuditDetails(auditDetails);

	}

	public void enrichReassignMembersRequest(@Valid USMOfficialRequest usmOfficialRequest,
			USMOfficial existingOfficial) {
		RequestInfo requestInfo = usmOfficialRequest.getRequestInfo();
		AuditDetails auditDetails = USMUtil.getAuditDetails(requestInfo.getUserInfo().getUuid(), false);
		auditDetails.setCreatedBy(existingOfficial.getAuditDetails().getCreatedBy());
		auditDetails.setCreatedTime(existingOfficial.getAuditDetails().getCreatedTime());

		String officialAccountId = null;

		// Check User isUserPresent as Citizen
		officialAccountId = userService.isUserPresent(usmOfficialRequest.getUsmOffcial().getAssigned(), requestInfo,
				usmOfficialRequest.getUsmOffcial().getTenantId(), Constants.ROLE_EMPLOYEE);

		// If not present throw exception
		if (StringUtils.isEmpty(officialAccountId)) {
			throw new CustomException("ID ERROR", "Official id is not present");
		}

		usmOfficialRequest.getUsmOffcial().setAssigned(officialAccountId);
		usmOfficialRequest.getUsmOffcial().setTenantId(existingOfficial.getTenantId());
		usmOfficialRequest.getUsmOffcial().setSlumcode(existingOfficial.getSlumcode());
		usmOfficialRequest.getUsmOffcial().setCategory(existingOfficial.getCategory());
		usmOfficialRequest.getUsmOffcial().setWard(existingOfficial.getWard());
		usmOfficialRequest.getUsmOffcial().setRole(existingOfficial.getRole());
		usmOfficialRequest.getUsmOffcial().setAuditDetails(auditDetails);

	}
}
