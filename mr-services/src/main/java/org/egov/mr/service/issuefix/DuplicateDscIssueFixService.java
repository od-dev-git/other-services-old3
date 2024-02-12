package org.egov.mr.service.issuefix;

import java.util.List;

import org.egov.mr.repository.IssueFixRepository;
import org.egov.mr.validator.IssueFixValidator;
import org.egov.mr.web.models.issuefix.IssueFix;
import org.egov.mr.web.models.issuefix.IssueFixRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Service("duplicateDscIssueFixService")
@Slf4j
public class DuplicateDscIssueFixService implements IIssueFixService {

	@Autowired
	private IssueFixValidator validator;

	@Autowired
	private IssueFixRepository repository;

	@Override
	public IssueFix issueFix(IssueFixRequest issueFixRequest, HttpHeaders headers) {

		List<String> data = repository.getDSC(issueFixRequest.getIssueFix());
		validator.validateDscDuplicateIssueFix(data);
		repository.updateDSC(issueFixRequest.getIssueFix());

		return null;

	}

}