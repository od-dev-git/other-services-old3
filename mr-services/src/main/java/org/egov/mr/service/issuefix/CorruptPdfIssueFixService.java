package org.egov.mr.service.issuefix;

import java.util.Arrays;
import java.util.List;

import org.egov.mr.config.MRConfiguration;
import org.egov.mr.producer.Producer;
import org.egov.mr.service.MarriageRegistrationService;
import org.egov.mr.web.models.DscDetails;
import org.egov.mr.web.models.MarriageRegistration;
import org.egov.mr.web.models.MarriageRegistrationRequest;
import org.egov.mr.web.models.MarriageRegistrationSearchCriteria;
import org.egov.mr.web.models.issuefix.IssueFix;
import org.egov.mr.web.models.issuefix.IssueFixRequest;
import org.egov.tracer.model.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

@Service("corruptPdfIssueFixService")
public class CorruptPdfIssueFixService implements IIssueFixService{
	
	@Autowired
	private MRConfiguration config;
	
	@Autowired
	private Producer producer;
	
	@Autowired
	MarriageRegistrationService marriageRegistrationService;
	
	@Override
    public IssueFix issueFix(IssueFixRequest issueFixRequest, HttpHeaders headers) {
		
		String applicationNumber = issueFixRequest.getIssueFix().getApplicationNo();
		String mrNumber = issueFixRequest.getIssueFix().getMrNumber();
		String tenantId = issueFixRequest.getIssueFix().getTenantId();
		
		MarriageRegistrationSearchCriteria searchCriteria = null;
		
		if (!StringUtils.isEmpty(applicationNumber)) {
			searchCriteria = MarriageRegistrationSearchCriteria.builder().applicationNumber(applicationNumber)
					.tenantId(tenantId).build();
		} else if (!StringUtils.isEmpty(mrNumber)) {
			searchCriteria = MarriageRegistrationSearchCriteria.builder().mrNumbers(Arrays.asList(mrNumber))
					.tenantId(tenantId).build();
		}
		List<MarriageRegistration> marriageRegistrations = marriageRegistrationService.search(searchCriteria,
				issueFixRequest.getRequestInfo(), "MR", headers);
		
		if(CollectionUtils.isEmpty(marriageRegistrations) || marriageRegistrations.size() > 1) {
			throw new CustomException("APPLICATION_SEARCH_ERROR",
					"Found None or Multiple Applications with mentioned Application Number or MR Number !!");
		}
		
		MarriageRegistration mRApplication = marriageRegistrations.get(0);
		
		List<DscDetails> dscDetails = mRApplication.getDscDetails();
		
		if(CollectionUtils.isEmpty(dscDetails) || dscDetails.size() > 1) {
			throw new CustomException("DSC_DETAILS_ERROR",
					"Found None or Multiple DSC Details with mentioned Application Number or MR Number !!");
		}
		
		DscDetails dscDetail = dscDetails.get(0);
		
		if(StringUtils.isEmpty(dscDetail.getDocumentId()) && StringUtils.isEmpty(dscDetail.getDocumentType())) {
			throw new CustomException("DSC_DETAILS_ERROR", "Certificate is not signed yet !!");
		}
		else {
			dscDetail.setDocumentId(null);
			dscDetail.setDocumentType(null);
			producer.push(config.getUpdateDscDetailsTopic(),
					new MarriageRegistrationRequest(issueFixRequest.getRequestInfo(), marriageRegistrations));
		}
		return issueFixRequest.getIssueFix();
    }

}
