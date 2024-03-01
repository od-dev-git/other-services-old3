package org.egov.report.web.controller;

import javax.validation.Valid;

import org.egov.report.service.IssueFixService;
import org.egov.report.util.ResponseInfoFactory;
import org.egov.report.web.model.issuefix.IssueFix;
import org.egov.report.web.model.issuefix.IssueFixRequest;
import org.egov.report.web.model.issuefix.IssueFixResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class IssueFixController {
	
	@Autowired
    private IssueFixService issueFixService;

	@Autowired
	private ResponseInfoFactory responseInfoFactory;
	
	@PostMapping(value = "/_issuefix")
	public ResponseEntity<IssueFixResponse> fixIssue(@Valid @RequestBody IssueFixRequest issueFixRequest,
			@RequestHeader HttpHeaders headers) {
		IssueFix issueFix = issueFixService.issueFix(issueFixRequest);
		IssueFixResponse issueFixResponse = IssueFixResponse.builder().issueFix(issueFix)
				.responseInfo(
						responseInfoFactory.createResponseInfoFromRequestInfo(issueFixRequest.getRequestInfo(), true))
				.status("FIXED").build();
		return new ResponseEntity<>(issueFixResponse, HttpStatus.OK);
	}

}
