package org.egov.mr.service.issuefix;

import org.egov.mr.web.models.MarriageRegistrationSearchCriteria;
import org.egov.mr.web.models.issuefix.IssueFix;
import org.egov.mr.web.models.issuefix.IssueFixRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.util.StringUtils;

import java.util.Arrays;

public interface IIssueFixService {
	
	IssueFix issueFix(IssueFixRequest issueFixRequest, HttpHeaders headers);

	default MarriageRegistrationSearchCriteria getSearchCriteria(IssueFixRequest issueFixRequest) {
		MarriageRegistrationSearchCriteria searchCriteria = MarriageRegistrationSearchCriteria.builder()
				.tenantId(issueFixRequest.getIssueFix().getTenantId()).build();

		if(!StringUtils.isEmpty(issueFixRequest.getIssueFix().getMrNumber())){
			searchCriteria.setMrNumbers(Arrays.asList(issueFixRequest.getIssueFix().getMrNumber()));
		}

		if(!StringUtils.isEmpty(issueFixRequest.getIssueFix().getApplicationNo())){
			searchCriteria.setApplicationNumber(issueFixRequest.getIssueFix().getApplicationNo());
		}

		return searchCriteria;
	}

}
