package org.egov.report.service;

import java.util.List;

import org.egov.report.repository.IssueFixRepository;
import org.egov.report.web.model.issuefix.IssueFix;
import org.egov.report.web.model.issuefix.IssueFixRequest;
import org.egov.tracer.model.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service("expireBillIssue")
public class ExpireBillIssue implements IIssueFixService {

    @Autowired
    private IssueFixRepository issueFixRepository;

    private static final int CHUNK_SIZE = 100;  // Process in chunks of 100 consumerCodes

    @Override
    public IssueFix issueFix(IssueFixRequest issueFixRequest) {
        IssueFix issueFix = issueFixRequest.getIssueFix();

        List<String> consumerCodes = issueFix.getConsumerCodes();

        if (CollectionUtils.isEmpty(consumerCodes)) {
            log.error("ConsumerCodes list is null or empty.");
            throw new CustomException("INVALID_REQUEST", "ConsumerCodes list cannot be null or empty.");
        }

        log.info("Processing bill expire issue for {} consumer codes.", consumerCodes.size());

        // Process consumerCodes in chunks of 100
        int totalConsumerCodes = consumerCodes.size();
        for (int i = 0; i < totalConsumerCodes; i += CHUNK_SIZE) {
            List<String> chunk = consumerCodes.subList(i, Math.min(totalConsumerCodes, i + CHUNK_SIZE));
            log.info("Processing chunk from index {} to {} with size: {}", i, Math.min(totalConsumerCodes, i + CHUNK_SIZE), chunk.size());

            for (String consumerCode : chunk) {
                try {
                    log.info("Expiring bill for consumerCode: {}", consumerCode);
                    issueFixRepository.expireBill(consumerCode);
                } catch (Exception e) {
                    log.error("Error while expiring bill for consumerCode: {}. Error: {}", consumerCode, e.getMessage());
                    throw new CustomException("BILL_EXPIRATION_ERROR", "Error expiring bill for consumerCode: " + consumerCode);
                }
            }
        }

        log.info("Bill expiration process completed for all consumer codes.");

        return issueFix;
    }
}
