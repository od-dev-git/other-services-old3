package org.egov.report.validator;

import org.egov.report.web.model.issuefix.IssueFix;
import org.egov.report.web.model.issuefix.IssueFixRequest;
import org.egov.tracer.model.CustomException;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class IssueFixValidator {

	public void validateUpdateTransactionRequest(IssueFixRequest issueFixRequest) {
		
		IssueFix issueFix = issueFixRequest.getIssueFix();
		
		if (StringUtils.isEmpty(issueFix.getIssueName()) || StringUtils.isEmpty(issueFix.getTxnId())
				|| StringUtils.isEmpty(issueFix.getConsumerCode()) || StringUtils.isEmpty(issueFix.getTenantId())) {
			throw new CustomException("INVALID REQUEST",
					"Tenant Id, Txn Id, Application Number and Issue Name are mandatory for this request. Kindly provide them to proceed ..!");
		}

	}
}
