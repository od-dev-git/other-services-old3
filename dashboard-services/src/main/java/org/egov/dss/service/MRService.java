package org.egov.dss.service;

import java.util.Arrays;
import java.util.List;

import org.egov.dss.constants.DashboardConstants;
import org.egov.dss.model.MarriageSearchCriteria;
import org.egov.dss.model.PayloadDetails;
import org.egov.dss.repository.MRRepository;
import org.egov.dss.web.model.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.google.common.collect.Sets;

@Service
public class MRService {
	
	@Autowired
	private MRRepository mrRepository;
	
	public List<Data> totalApplications(PayloadDetails payloadDetails) {
		MarriageSearchCriteria criteria = getMarriageSearchCriteria(payloadDetails);
	    criteria.setExcludedTenantId(DashboardConstants.TESTING_TENANT);
		Integer totalApplication =  (Integer) mrRepository.getTotalApplications(criteria);
		return Arrays.asList(Data.builder().headerValue(totalApplication).build());
	}
	
	private MarriageSearchCriteria getMarriageSearchCriteria(PayloadDetails payloadDetails) {
		MarriageSearchCriteria criteria = new MarriageSearchCriteria();

		if (StringUtils.hasText(payloadDetails.getModulelevel())) {
			criteria.setBusinessServices(Sets.newHashSet(payloadDetails.getModulelevel()));
		}
		
		if(StringUtils.hasText(payloadDetails.getTenantid())) {
			criteria.setTenantIds(Sets.newHashSet(payloadDetails.getTenantid()));
		}
		
		if(payloadDetails.getStartdate() != null && payloadDetails.getStartdate() != 0) {
			criteria.setFromDate(payloadDetails.getStartdate());
		}
		
		if(payloadDetails.getEnddate() != null && payloadDetails.getEnddate() != 0) {
			criteria.setToDate(payloadDetails.getEnddate());
		}
		
		return criteria;
	}
	

}
