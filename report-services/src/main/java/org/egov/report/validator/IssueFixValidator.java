package org.egov.report.validator;

import java.util.List;

import org.egov.report.web.model.issuefix.IssueFix;
import org.egov.report.web.model.issuefix.IssueFixRequest;
import org.egov.tracer.model.CustomException;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
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

	public void valiDateDSCDeletionIssue(IssueFixRequest issueFixRequest) {

		IssueFix issueFix = issueFixRequest.getIssueFix();
		if (StringUtils.isEmpty(issueFix.getIssueName()) || StringUtils.isEmpty(issueFix.getTenantId())
				|| StringUtils.isEmpty(issueFix.getEmpID()) || StringUtils.isEmpty(issueFix.getEmpName())) {
			throw new CustomException("INVALID REQUEST",
					"Tenant Id, Employee ID, Employee Name and Issue Name are mandatory for this request. Kindly provide them to proceed ..!");
		}

	}
	
	
	public void validateBillExpireIssue(IssueFixRequest issueFixRequest) {
		log.info("Starting validation for Bill Expire Issue with request: {}", issueFixRequest);

        IssueFix issueFix = issueFixRequest.getIssueFix();

        // Null and Empty String checks for mandatory fields
        log.info("Validating mandatory fields for IssueFix object...");
        if (!StringUtils.hasText(issueFix.getIssueName())) {
            log.error("Mandatory field validation failed. Missing required fields.");
            throw new CustomException("INVALID REQUEST",
                    "Issue Name are mandatory for this request. Please provide valid values.");
        }

        List<String> consumerCodes = issueFix.getConsumerCodes();

        // Ensure the consumerCodes list is not null or empty
        log.info("Validating consumerCodes list...");
        if (consumerCodes == null || consumerCodes.isEmpty()) {
            log.error("ConsumerCodes list is null or empty.");
            throw new CustomException("INVALID REQUEST", "ConsumerCodes list cannot be null or empty.");
        }

        // Log the size of consumerCodes
        log.info("Size of consumerCodes list: {}", consumerCodes.size());

        // Check the size of consumerCodes
        log.info("Validating the size of consumerCodes...");
        int maxLimit = issueFix.getAllowExtendedLimit() ? 1000 : 10;
        if (consumerCodes.size() > maxLimit) {
            log.error("ConsumerCodes list exceeds the limit of {}.", maxLimit);
            throw new CustomException("INVALID REQUEST",
                    "Size of consumerCodes cannot exceed " + maxLimit + ". Please reduce the number of codes or enable extended limit.");
        }

        // Check for duplicate consumer codes
        log.info("Checking for duplicate consumer codes...");
        if (consumerCodes.size() != consumerCodes.stream().distinct().count()) {
            log.error("Duplicate consumer codes found.");
            throw new CustomException("INVALID REQUEST", "Duplicate consumer codes found in the list.");
        }

        log.info("Validation for Bill Expire Issue completed successfully.");
    }
}
