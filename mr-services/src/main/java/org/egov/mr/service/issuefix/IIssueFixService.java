package org.egov.mr.service.issuefix;

import org.egov.mr.web.models.issuefix.IssueFix;
import org.egov.mr.web.models.issuefix.IssueFixRequest;
import org.springframework.http.HttpHeaders;

public interface IIssueFixService {
	
	IssueFix issueFix(IssueFixRequest issueFixRequest, HttpHeaders headers);

}
