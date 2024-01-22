package org.egov.mr.validator;

import org.egov.mr.web.models.issuefix.IssueFix;
import org.egov.tracer.model.CustomException;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class IssueFixValidator {
	
	public void validateIssueFixRequest(IssueFix issueFix, HttpHeaders headers) {

		if (StringUtils.isEmpty(issueFix.getIssueName())) {
			throw new CustomException("INVALID_REQUEST",
					"Issue Name can not be empty, Kindly provide issue name to proceed !!");
		}

		if (StringUtils.isEmpty(issueFix.getMrNumber()) && StringUtils.isEmpty(issueFix.getApplicationNo())) {
			throw new CustomException("INVALID_REQUEST",
					"Application Number or Mr Number Number can not be empty, Kindly provide any one to proceed !!");
		}
	}

}
