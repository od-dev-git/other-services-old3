package org.egov.mr.service.issuefix;

import java.util.Arrays;
import java.util.List;

import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.request.Role;
import org.egov.common.contract.request.User;
import org.egov.mr.config.MRConfiguration;
import org.egov.mr.repository.IssueFixRepository;
import org.egov.mr.validator.IssueFixValidator;
import org.egov.mr.web.models.issuefix.IssueFix;
import org.egov.mr.web.models.issuefix.IssueFixRequest;
import org.egov.mr.web.models.issuefix.PaymentIssueFix;
import org.egov.mr.web.models.issuefix.StatusMismatchIssueFix;
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
	@Qualifier("stepBackService")
	private IIssueFixService stepBackService;

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

            case "STEP_BACK_TO_APPROVAL_PENDING":
            	return stepBackService.issueFix(issueFixRequest, headers);
            	
            default:
                throw new CustomException("UNKNOWN_ISSUE", "The issue is unknown to the system !!");
        }
    }
    
    public void automatePaymentIssueFix(RequestInfo requestInfo) {
		if (config.getMrPaymentIssueFIx()) {
			setUserDetails(requestInfo);
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
	

	private void setUserDetails(RequestInfo requestInfo) {
		Role role = Role.builder().code(config.getMrIssueFixRoleCode()).tenantId(config.getMrIssueFixTenantId())
				.build();
		User userInfo = User.builder().uuid(config.getMrIssueFixUUID()).type("EMPLOYEE").roles(Arrays.asList(role))
				.id(0L).build();
		requestInfo.setUserInfo(userInfo);

	}
	
	public void automateStatusMismatchIssueFix(RequestInfo requestInfo) {
		if (config.getMrStatusMismatchIssueFIx()) {
			setUserDetails(requestInfo);
			log.info("Status Mismatch Issue Fix Scheduler Started Successfully. Time: {}", System.currentTimeMillis());
			List<StatusMismatchIssueFix> statusMismatchIssueApplications = repository.getStatusMismatchApplications();
			if (!CollectionUtils.isEmpty(statusMismatchIssueApplications)) {
				log.info("Number of Status Mismatch Issue Found in MR at : {} :{}", System.currentTimeMillis(),
						statusMismatchIssueApplications.size());

				processStatusMismatchIssues(statusMismatchIssueApplications, "APPLICATION_STATUS_MISMATCH_ISSUE", requestInfo);
				log.info("Status Mismatch Issue Fix Scheduler Completed Successfully. Time: {}", System.currentTimeMillis());
			} else {
				log.info("No Status Mismatch Issue Found in MR at : {}", System.currentTimeMillis());
			}
		} else {
			log.info("MR Status Mismatch Issue Fix Config is : {}", config.getMrStatusMismatchIssueFIx());
		}

	}
	
	private void processStatusMismatchIssues(List<StatusMismatchIssueFix> statusMismatchIssues, String issueType, RequestInfo requestInfo) {
		if(!CollectionUtils.isEmpty(statusMismatchIssues)) {
			HttpHeaders headers = new HttpHeaders();
			statusMismatchIssues.forEach(mismatchIssue -> {
			    try {
			    	applicationStatusMismatchIssueFix.issueFix(IssueFixRequest.builder()
			            .issueFix(IssueFix.builder()
			                .applicationNo(mismatchIssue.getApplicationNo())
			                .issueName(issueType)
			                .tenantId(mismatchIssue.getTenantId())
			                .build())
			                .requestInfo(requestInfo)
			                .build(), headers);
			    } catch (Exception e) {			       
			    	log.error("Error processing status mismatch issue for application number: " + mismatchIssue.getApplicationNo());
			        e.printStackTrace();
			    }
			});
		}

	}


}
