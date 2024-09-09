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
    @Qualifier("dscDeletionIssue")
    private IIssueFixService dscDeletionIssue;
	
	@Autowired
    @Qualifier("expireBillIssue")
    private IIssueFixService expireBillIssue;	

	@Autowired
	private IssueFixValidator validator;

    public IssueFix issueFix(IssueFixRequest issueFixRequest) {

        String issueName=issueFixRequest.getIssueFix().getIssueName();
        
        switch (issueName){

            case "UPDATE_TRANSACTION_ISSUE":
            	validator.validateUpdateTransactionRequest(issueFixRequest);
                return updateTransactionIssue.issueFix(issueFixRequest);
                
            case "DSC_DELETION_ISSUE":
            	validator.valiDateDSCDeletionIssue(issueFixRequest);
                return dscDeletionIssue.issueFix(issueFixRequest);  
                
            case "EXPIRE_BILL":
            	validator.validateBillExpireIssue(issueFixRequest);
                return expireBillIssue.issueFix(issueFixRequest);     

            default:
                throw new CustomException("UNKNOWN_ISSUE","The issue is unknown to the system !!");
        }
    }
}
