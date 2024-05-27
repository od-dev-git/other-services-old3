package org.egov.mr.service.issuefix;

import java.util.List;

import org.egov.common.contract.request.RequestInfo;
import org.egov.mr.config.MRConfiguration;
import org.egov.mr.repository.IssueFixRepository;
import org.egov.mr.validator.IssueFixValidator;
import org.egov.mr.web.models.issuefix.IssueFix;
import org.egov.mr.web.models.issuefix.IssueFixRequest;
import org.egov.mr.web.models.issuefix.PaymentIssueFix;
import org.egov.tracer.model.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
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
    
    @Autowired
    private MRConfiguration config;

    @Autowired
    private IssueFixRepository repository;

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
    
    public void automatePaymentIssueFix(RequestInfo requestInfo) {
		if (config.getMrPaymentIssueFIx()) {
			log.info("Payment Issue Fix Scheduler Started Successfully. Time: {}", System.currentTimeMillis());
			List<PaymentIssueFix> paymentIssueApplications = repository.getPaymentIssueApplications();
			if (!CollectionUtils.isEmpty(paymentIssueApplications)) {
				log.info("Number of Payment Issue Found in MR at : {} :{}", System.currentTimeMillis(),
						paymentIssueApplications.size());

				processPaymentIssues(paymentIssueApplications, "PAYMENT_ISSUE", requestInfo);
				log.info("Payment Issue Fix Scheduler Completed Successfully. Time: {}", System.currentTimeMillis());
			} else {
				log.info("No Payment Issue Found in MR at : {}", System.currentTimeMillis());
			}
		} else {
			log.info("MR Payment Issue Fix Config is : {}", config.getMrPaymentIssueFIx());
		}

	}

	private void processPaymentIssues(List<PaymentIssueFix> paymentIssues, String issueType, RequestInfo requestInfo) {
		HttpHeaders httpHeaders = new HttpHeaders();
		if (!CollectionUtils.isEmpty(paymentIssues)) {
			paymentIssues.forEach(paymentIssue -> {
				try {
					paymentIssueFixService
							.issueFix(IssueFixRequest.builder()
									.issueFix(IssueFix.builder().applicationNo(paymentIssue.getApplicationNumber())
											.issueName(issueType).tenantId(paymentIssue.getTenantId()).build())
									.requestInfo(requestInfo)
									.build(), httpHeaders);
				} catch (Exception e) {
					log.error("Error processing mutation fee payment issue for application no: "
							+ paymentIssue.getApplicationNumber() + " - " + e.getMessage());
					e.printStackTrace();
				}
			});
		}

	}

}
