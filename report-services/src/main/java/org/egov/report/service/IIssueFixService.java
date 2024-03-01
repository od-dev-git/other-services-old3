package org.egov.report.service;

import org.egov.report.web.model.issuefix.IssueFix;
import org.egov.report.web.model.issuefix.IssueFixRequest;

public interface IIssueFixService {

	IssueFix issueFix(IssueFixRequest issueFixRequest);

}
