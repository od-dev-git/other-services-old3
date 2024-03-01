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
    @Qualifier("applicationStatusMismatchIssueFix")
    private IIssueFixService applicationStatusMismatchIssueFix;

    @Autowired
    @Qualifier("paymentIssueFixService")
    private IIssueFixService paymentIssueFixService;

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

            case "APPLICATION_STATUS_MISMATCH_ISSUE":
                return applicationStatusMismatchIssueFix.issueFix(issueFixRequest,headers);

            case "PAYMENT_ISSUE":
                return paymentIssueFixService.issueFix(issueFixRequest,headers);

            default:
                throw new CustomException("UNKNOWN_ISSUE", "The issue is unknown to the system !!");
        }
    }
}
