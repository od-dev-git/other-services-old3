package org.egov.mr.service.issuefix;

import org.egov.mr.validator.IssueFixValidator;
import org.egov.mr.web.models.issuefix.IssueFix;
import org.egov.mr.web.models.issuefix.IssueFixRequest;
import org.egov.tracer.model.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

@Service("issueFixService")
public class IssueFixService {

    @Autowired
    @Qualifier("corruptPdfIssueFixService")
    private IIssueFixService corruptPdfIssueFixService;

    @Autowired
    @Qualifier("duplicateDscIssueFixService")
    private IIssueFixService duplicateDscIssueFixService;

    @Autowired
    private IssueFixValidator validator;

    public IssueFix issueFix(IssueFixRequest issueFixRequest, HttpHeaders headers) {

        validator.validateIssueFixRequest(issueFixRequest.getIssueFix(), headers);
        String issueName = issueFixRequest.getIssueFix().getIssueName();

        switch (issueName) {

            case "CORRUPT_PDF_ISSUE":
                return corruptPdfIssueFixService.issueFix(issueFixRequest, headers);

            case "DUPLICATE_DSC_ISSUE":
                return duplicateDscIssueFixService.issueFix(issueFixRequest, headers);

            default:
                throw new CustomException("UNKNOWN_ISSUE", "The issue is unknown to the system !!");
        }
    }
}
