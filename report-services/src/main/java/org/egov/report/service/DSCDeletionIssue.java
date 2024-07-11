package org.egov.report.service;

import org.egov.report.repository.IssueFixRepository;
import org.egov.report.web.model.issuefix.IssueFix;
import org.egov.report.web.model.issuefix.IssueFixRequest;
import org.egov.tracer.model.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service("dscDeletionIssue")
public class DSCDeletionIssue implements IIssueFixService {
	
	@Autowired
	private IssueFixRepository repository;

	@Override
	public IssueFix issueFix(IssueFixRequest issueFixRequest) {
		
		IssueFix issueFix = issueFixRequest.getIssueFix();
		
		//get user Id from HRMS employee code
		int userId = getUserIdFromHRMS(issueFix);
		
		//get name from EMAS table corresponding to userId
		String name = getNameFromUserIDFromEMAS(userId);
		
		//Compare the name with input and name got from EMAS
		if (!issueFix.getEmpName().equalsIgnoreCase(name)) {
			throw new CustomException("NAME_ERROR",
					"The Name Provided does not match with the Registered Name " + name);
		}
		
		// if name matches delete DSC token here using sql function
		deleteDSCToken(userId);
		
		return issueFix;
	}

	/**
	 * Delete token from EMAS
	 * 
	 * @param userId
	 * @return
	 */
	private Boolean deleteDSCToken(int userId) {
		
		return repository.deleteDSCTokenEMAS(userId);
	}

	/**
	 * Get Name from user ID in the EMAS table
	 * 
	 * @param userId
	 * @return
	 */
	private String getNameFromUserIDFromEMAS(int userId) {
		return repository.getNameFromEMAS(userId);
	}

	/**
	 * Get User Id from HRMS employee code
	 * 
	 * @param issueFix
	 * @return
	 */
	private int getUserIdFromHRMS(IssueFix issueFix) {
		
		String tenantId = issueFix.getTenantId();
		
		String empId = issueFix.getEmpID();
		
		return repository.getUserIdFromHRMS(tenantId, empId);
	}
	
	

}
