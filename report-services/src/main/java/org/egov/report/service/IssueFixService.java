package org.egov.report.service;

import org.egov.report.validator.IssueFixValidator;
import org.egov.report.web.model.issuefix.IssueFix;
import org.egov.report.web.model.issuefix.IssueFixRequest;
import org.egov.tracer.model.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service("issueFixService")
public class IssueFixService {

	@Autowired
    @Qualifier("updateTransactionIssue")
    private IIssueFixService updateTransactionIssue;

	@Autowired
	private IssueFixValidator validator;

    public IssueFix issueFix(IssueFixRequest issueFixRequest) {

        String issueName=issueFixRequest.getIssueFix().getIssueName();

        validator.validateUpdateTransactionRequest(issueFixRequest);
        
        switch (issueName){

            case "UPDATE_TRANSACTION_ISSUE":
                return updateTransactionIssue.issueFix(issueFixRequest);

            default:
                throw new CustomException("UNKNOWN_ISSUE","The issue is unknown to the system !!");
        }
    }
}
