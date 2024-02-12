package org.egov.mr.service.issuefix;

import org.egov.mr.repository.IssueFixRepository;
import org.egov.mr.validator.IssueFixValidator;
import org.egov.mr.web.models.issuefix.IssueFix;
import org.egov.mr.web.models.issuefix.IssueFixRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;

public class ApplicationStatusMismatchIssueFixService implements IIssueFixService{

    @Autowired
    private IssueFixValidator issueFixValidator;

    @Autowired
    private IssueFixRepository issueFixRepository;
    @Override
    public IssueFix issueFix(IssueFixRequest issueFixRequest, HttpHeaders headers) {
        return null;
    }
}
