package org.egov.dss.service;

import java.util.Arrays;
import java.util.List;

import org.egov.dss.constants.DashboardConstants;
import org.egov.dss.model.BpaSearchCriteria;
import org.egov.dss.model.PayloadDetails;
import org.egov.dss.repository.BPARepository;
import org.egov.dss.web.model.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.google.common.collect.Sets;

@Service
public class BPAService {
	
	@Autowired
	private BPARepository bpaRepository;

	public List<Data> totalPermitIssued(PayloadDetails payloadDetails) {
		BpaSearchCriteria criteria = getBpaSearchCriteria(payloadDetails);
		criteria.setExcludedTenantId(DashboardConstants.TESTING_TENANT);
		criteria.setStatus(DashboardConstants.STATUS_APPROVED);
		Integer totalApplication = (Integer) bpaRepository.getTotalPermitsIssued(criteria);
		return Arrays.asList(Data.builder().headerValue(totalApplication).build());
	}

	private BpaSearchCriteria getBpaSearchCriteria(PayloadDetails payloadDetails) {
		BpaSearchCriteria criteria = new BpaSearchCriteria();

		if (StringUtils.hasText(payloadDetails.getModulelevel())) {
			if (payloadDetails.getModulelevel().equalsIgnoreCase(DashboardConstants.MODULE_LEVEL_OBPS))
				criteria.setBusinessServices(Sets.newHashSet(DashboardConstants.OBPS_ALL_BUSINESS_SERVICES));
		}

		if (StringUtils.hasText(payloadDetails.getTenantid())) {
			criteria.setTenantIds(Sets.newHashSet(payloadDetails.getTenantid()));
		}

		if (payloadDetails.getStartdate() != null && payloadDetails.getStartdate() != 0) {
			criteria.setFromDate(payloadDetails.getStartdate());
		}

		if (payloadDetails.getEnddate() != null && payloadDetails.getEnddate() != 0) {
			criteria.setToDate(payloadDetails.getEnddate());
		}

		return criteria;
	}

}
