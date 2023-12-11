package org.egov.dss.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Month;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.egov.dss.config.ConfigurationLoader;
import org.egov.dss.constants.DashboardConstants;
import org.egov.dss.model.BpaSearchCriteria;
import org.egov.dss.model.Chart;
import org.egov.dss.model.PayloadDetails;
import org.egov.dss.model.PgrSearchCriteria;
import org.egov.dss.model.PropertySerarchCriteria;
import org.egov.dss.model.RegularizationSearchCriteria;
import org.egov.dss.repository.BPARepository;
import org.egov.dss.repository.RegularizationRepository;
import org.egov.dss.util.DashboardUtility;
import org.egov.dss.util.DashboardUtils;
import org.egov.dss.web.model.Data;
import org.egov.dss.web.model.Plot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.google.common.collect.Sets;
import com.jayway.jsonpath.Criteria;

@Service
public class RegularizationService {

	@Autowired
	private RegularizationRepository regularizationRepository;

	@Autowired
	private ConfigurationLoader config;
	
	@Autowired
	private DashboardUtils dashboardUtils;
	
	public RegularizationSearchCriteria getRegularizationSearchCriteria(PayloadDetails payloadDetails) {
		RegularizationSearchCriteria criteria = new RegularizationSearchCriteria();

		if (StringUtils.hasText(payloadDetails.getModulelevel())) {
			if (payloadDetails.getModulelevel().equalsIgnoreCase(DashboardConstants.MODULE_LEVEL_REGULARIZATION))
				criteria.setBusinessServices(Sets.newHashSet(DashboardConstants.REGULARIZATION_ALL_BUSINESS_SERVICES));
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

	public List<Data> totalApplicationsReceived(PayloadDetails payloadDetails) {
		RegularizationSearchCriteria criteria = getRegularizationSearchCriteria(payloadDetails);
		criteria.setExcludedTenantId(DashboardConstants.TESTING_TENANT);
		criteria.setStatusNotIn(Sets.newHashSet(DashboardConstants.REGULARIZATION_REJECTED_STATUSES));
		criteria.setBusinessServices(Sets.newHashSet(DashboardConstants.REGULARIZATION_ALL_BUSINESS_SERVICES));
		Integer totalApplication = (Integer) regularizationRepository.totalApplicationsReceived(criteria);
		return Arrays.asList(Data.builder().headerValue(totalApplication).build());
	}
	
	public List<Data> totalRegularizationCertificateIssued(PayloadDetails payloadDetails) {
		RegularizationSearchCriteria criteria = getRegularizationSearchCriteria(payloadDetails);
		criteria.setExcludedTenantId(DashboardConstants.TESTING_TENANT);
		criteria.setStatus(Sets.newHashSet(DashboardConstants.STATUS_APPROVED));
		criteria.setBusinessServices(Sets.newHashSet(DashboardConstants.REGULARIZATION_ALL_BUSINESS_SERVICES));
		Integer totalApplication = (Integer) regularizationRepository.getTotalRegularizationCertificateIssued(criteria);
		return Arrays.asList(Data.builder().headerValue(totalApplication).build());
	}
	
	public List<Data> totalApplicationsRejected(PayloadDetails payloadDetails) {
		RegularizationSearchCriteria criteria = getRegularizationSearchCriteria(payloadDetails);
		criteria.setExcludedTenantId(DashboardConstants.TESTING_TENANT);
		criteria.setStatus(Sets.newHashSet(DashboardConstants.STATUS_REJECTED));
		criteria.setBusinessServices(Sets.newHashSet(DashboardConstants.REGULARIZATION_ALL_BUSINESS_SERVICES));
		Integer totalApplication = (Integer) regularizationRepository.totalApplicationsRejected(criteria);
		return Arrays.asList(Data.builder().headerValue(totalApplication).build());
	}
	
	public List<Data> totalApplicationsPending(PayloadDetails payloadDetails) {
		RegularizationSearchCriteria criteria = getRegularizationSearchCriteria(payloadDetails);
		criteria.setExcludedTenantId(DashboardConstants.TESTING_TENANT);
		criteria.setStatusNotIn(Sets.newHashSet(DashboardConstants.REGULARIZATION_REJECTED_STATUS_TOTAL_APPLICATIONS_PENDING));
		criteria.setBusinessServices(Sets.newHashSet(DashboardConstants.REGULARIZATION_ALL_BUSINESS_SERVICES));
		criteria.setFromDate(null);
		criteria.setDeleteStatus(DashboardConstants.STATUS_DELETED);
		Integer totalApplication = (Integer) regularizationRepository.totalApplicationsPending(criteria);
		return Arrays.asList(Data.builder().headerValue(totalApplication).build());
	}
	
	public List<Data> avgDaysToIssueCertificate(PayloadDetails payloadDetails) {
		RegularizationSearchCriteria criteria = getRegularizationSearchCriteria(payloadDetails);
		criteria.setExcludedTenantId(DashboardConstants.TESTING_TENANT);
		criteria.setStatus(Sets.newHashSet(DashboardConstants.STATUS_APPROVED));
		criteria.setBusinessServices(Sets.newHashSet(DashboardConstants.REGULARIZATION_ALL_BUSINESS_SERVICES));
		BigDecimal totalApplication = (BigDecimal) regularizationRepository.getAvgDaysToIssueCertificate(criteria);// change it
		return Arrays.asList(Data.builder().headerValue(totalApplication).build());
	}
	
	public List<Data> minDaysToIssueCertificate(PayloadDetails payloadDetails) {
		RegularizationSearchCriteria criteria = getRegularizationSearchCriteria(payloadDetails);
		criteria.setExcludedTenantId(DashboardConstants.TESTING_TENANT);
		criteria.setStatus(Sets.newHashSet(DashboardConstants.STATUS_APPROVED));
		criteria.setBusinessServices(Sets.newHashSet(DashboardConstants.REGULARIZATION_ALL_BUSINESS_SERVICES));
		Integer totalApplication = (Integer) regularizationRepository.getMinDaysToIssueCertificate(criteria);
		return Arrays.asList(Data.builder().headerValue(totalApplication).build());
	}
	
	public List<Data> maxDaysToIssueCertificate(PayloadDetails payloadDetails) {
		RegularizationSearchCriteria criteria = getRegularizationSearchCriteria(payloadDetails);
		criteria.setExcludedTenantId(DashboardConstants.TESTING_TENANT);
		criteria.setStatus(Sets.newHashSet(DashboardConstants.STATUS_APPROVED));
		criteria.setBusinessServices(Sets.newHashSet(DashboardConstants.REGULARIZATION_ALL_BUSINESS_SERVICES));
		Integer totalApplication = (Integer) regularizationRepository.getMaxDaysToIssueCertificate(criteria);
		return Arrays.asList(Data.builder().headerValue(totalApplication).build());
	}

	
}
