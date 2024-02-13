package org.egov.mr.validator;

import java.util.List;

import org.egov.mr.web.models.MarriageRegistration;
import org.egov.mr.web.models.issuefix.IssueFix;
import org.egov.mr.web.models.workflow.ProcessInstance;
import org.egov.tracer.model.CustomException;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

@Component
public class IssueFixValidator {
	
	public void validateIssueFixRequest(IssueFix issueFix, HttpHeaders headers) {
		
		if (StringUtils.isEmpty(issueFix.getTenantId())) {
			throw new CustomException("INVALID_REQUEST",
					"TenantId can not be empty, Kindly provide tenant id to proceed !!");
		}

		if (StringUtils.isEmpty(issueFix.getIssueName())) {
			throw new CustomException("INVALID_REQUEST",
					"Issue Name can not be empty, Kindly provide issue name to proceed !!");
		}

		if (StringUtils.isEmpty(issueFix.getMrNumber()) && StringUtils.isEmpty(issueFix.getApplicationNo())) {
			throw new CustomException("INVALID_REQUEST",
					"Application Number or Mr Number Number can not be empty, Kindly provide any one to proceed !!");
		}
	}
	
	public void validateDscDuplicateIssueFix(List<String> dscList ){

	    if(CollectionUtils.isEmpty(dscList)){
	        throw new CustomException("INVALID_DATA","DSC details is empty for the given MR Application.");
	    }
	}

	public void validateMarriageRegistrationApplicationStatusMismatch(List<MarriageRegistration> marriageRegistrations) {
		if(CollectionUtils.isEmpty(marriageRegistrations)){
			throw new CustomException("INVALID_INPUT","No Water connection Data was Found ");
		}
		if(marriageRegistrations.size()>=2){
			throw new CustomException("INVALID_DATA","Multiple Water Connection applications were found");
		}
	}

	public void validateProcessInstanceApplicationStatusMismatch(MarriageRegistration marriageRegistration, List<ProcessInstance> processInstance) {

		if(CollectionUtils.isEmpty(processInstance)){
			throw new CustomException("INVALID_DATA","No Data was found in Process Instances");
		}
		ProcessInstance currentProcessInstance = processInstance.get(0);
		if(currentProcessInstance.getState().getApplicationStatus().equalsIgnoreCase(marriageRegistration.getStatus())){
			throw new CustomException("INVALID_INPUT","The Application data is having no mismatch");
		}
	}
}
