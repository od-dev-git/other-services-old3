package org.egov.mr.web.controllers;

import javax.validation.Valid;

import org.egov.common.contract.request.RequestInfo;
import org.egov.mr.service.issuefix.IIssueFixService;
import org.egov.mr.service.issuefix.IssueFixService;
import org.egov.mr.util.ResponseInfoFactory;
import org.egov.mr.web.models.issuefix.IssueFix;
import org.egov.mr.web.models.issuefix.IssueFixRequest;
import org.egov.mr.web.models.issuefix.IssueFixResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1")
public class IssueFixController {
	
	@Autowired
    private IssueFixService issueFixService;
	
	@Autowired
	private ResponseInfoFactory responseInfoFactory;


	@PostMapping(value = "/_issuefix")
	public ResponseEntity<IssueFixResponse> fixIssue(@Valid @RequestBody IssueFixRequest issueFixRequest,
			@RequestHeader HttpHeaders headers) {
		IssueFix issueFix = issueFixService.issueFix(issueFixRequest, headers);
		IssueFixResponse issueFixResponse = IssueFixResponse.builder().issueFix(issueFix)
				.responseInfo(
						responseInfoFactory.createResponseInfoFromRequestInfo(issueFixRequest.getRequestInfo(), true))
				.status("FIXED").build();
		return new ResponseEntity<>(issueFixResponse, HttpStatus.OK);
	}
	
    @PostMapping(value = "/_automatePaymentIssueFix")
	public ResponseEntity<IssueFixResponse> automatePaymentIssueFix(@Valid @RequestBody RequestInfo requestInfo) {
        issueFixService.automatePaymentIssueFix(requestInfo);		
		return new ResponseEntity<>(HttpStatus.OK);
	}

}
