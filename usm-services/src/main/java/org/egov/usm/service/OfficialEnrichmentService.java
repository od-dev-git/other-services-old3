package org.egov.usm.service;

import javax.validation.Valid;

import org.egov.common.contract.request.RequestInfo;
import org.egov.usm.utility.USMUtil;
import org.egov.usm.web.model.AuditDetails;
import org.egov.usm.web.model.USMOfficial;
import org.egov.usm.web.model.USMOfficialRequest;
import org.springframework.stereotype.Service;

@Service
public class OfficialEnrichmentService {

	

	/**
	 * Enrich usmOfficialRequest
	 * 
	 * @param usmOfficialRequest
	 */
	public void enrichUSMOfficialRequest(@Valid USMOfficialRequest usmOfficialRequest) {

		RequestInfo requestInfo = usmOfficialRequest.getRequestInfo();
		AuditDetails auditDetails = USMUtil.getAuditDetails(requestInfo.getUserInfo().getUuid(), true);

		usmOfficialRequest.getUsmOffcial().setId(USMUtil.generateUUID());
		usmOfficialRequest.getUsmOffcial().setAssigned(usmOfficialRequest.getUsmOffcial().getAssigned());
		usmOfficialRequest.getUsmOffcial().setTenant(usmOfficialRequest.getUsmOffcial().getTenant());
		usmOfficialRequest.getUsmOffcial().setSlumcode(usmOfficialRequest.getUsmOffcial().getSlumcode());
		usmOfficialRequest.getUsmOffcial().setCategory(usmOfficialRequest.getUsmOffcial().getCategory().toUpperCase());
		usmOfficialRequest.getUsmOffcial().setRole(usmOfficialRequest.getUsmOffcial().getRole());
		usmOfficialRequest.getUsmOffcial().setAuditDetails(auditDetails);
	}

	public void enrichDeassignOfficialRequest(@Valid USMOfficialRequest usmOfficialRequest,
			USMOfficial existingOfficialMember) {
		RequestInfo requestInfo = usmOfficialRequest.getRequestInfo();
		AuditDetails auditDetails = USMUtil.getAuditDetails(requestInfo.getUserInfo().getUuid(), false);
		usmOfficialRequest.getUsmOffcial().setAssigned(null);
		usmOfficialRequest.getUsmOffcial().setTenant(existingOfficialMember.getTenant());
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

		usmOfficialRequest.getUsmOffcial().setAssigned(usmOfficialRequest.getUsmOffcial().getAssigned());
		usmOfficialRequest.getUsmOffcial().setTenant(existingOfficial.getTenant());
		usmOfficialRequest.getUsmOffcial().setSlumcode(existingOfficial.getSlumcode());
		usmOfficialRequest.getUsmOffcial().setCategory(existingOfficial.getCategory().toUpperCase());
		usmOfficialRequest.getUsmOffcial().setWard(existingOfficial.getWard());
		usmOfficialRequest.getUsmOffcial().setRole(existingOfficial.getRole());
		usmOfficialRequest.getUsmOffcial().setAuditDetails(auditDetails);

	}
}
