package org.egov.dss.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.egov.dss.config.ConfigurationLoader;
import org.egov.dss.constants.DashboardConstants;
import org.egov.dss.model.BpaSearchCriteria;
import org.egov.dss.model.Chart;
import org.egov.dss.model.PayloadDetails;
import org.egov.dss.model.PgrSearchCriteria;
import org.egov.dss.model.PropertySerarchCriteria;
import org.egov.dss.repository.BPARepository;
import org.egov.dss.web.model.Data;
import org.egov.dss.web.model.Plot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.google.common.collect.Sets;
import com.jayway.jsonpath.Criteria;

@Service
public class BPAService {
	
	@Autowired
	private BPARepository bpaRepository;
	
	@Autowired
	private ConfigurationLoader config;

	public List<Data> totalPermitIssued(PayloadDetails payloadDetails) {
		BpaSearchCriteria criteria = getBpaSearchCriteria(payloadDetails);
		
		criteria.setExcludedTenantId(DashboardConstants.TESTING_TENANT);
		String[] status = {"APPROVED"};   
		Set <String> statusIn = Arrays.asList(status).stream().collect(Collectors.toSet());
		criteria.setStatus(statusIn);
		
		String[] businessService = {"BPA1","BPA2","BPA3","BPA4","BPA5","BPA6","BPA7","BPA8","BPA9","BPA10"};   
		Set <String> businessServiceIn = Arrays.asList(businessService).stream().collect(Collectors.toSet());
		criteria.setBusinessServices(businessServiceIn);
		Integer totalApplication = (Integer) bpaRepository.getTotalPermitsIssued(criteria);
		return Arrays.asList(Data.builder().headerValue(totalApplication).build());
	}    
	
	public List<Data> slaAchieved(PayloadDetails payloadDetails) {
		BpaSearchCriteria criteria = getBpaSearchCriteria(payloadDetails);
		criteria.setExcludedTenantId(DashboardConstants.TESTING_TENANT);
		String[] status = {"APPROVED"};   
		Set <String> statusIn = Arrays.asList(status).stream().collect(Collectors.toSet());
		criteria.setStatus(statusIn);
		Integer totalApplication = (Integer) bpaRepository.getTotalPermitsIssued(criteria);
		Integer slaAchievedAppCount = (Integer) bpaRepository.getSlaAchievedAppCount(criteria);
		return Arrays.asList(Data.builder()
				.headerValue((slaAchievedAppCount.doubleValue() / totalApplication.doubleValue()) * 100).build());
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

	public List<Data> totalApplicationsReceived(PayloadDetails payloadDetails) {
		BpaSearchCriteria criteria = getBpaSearchCriteria(payloadDetails);
		criteria.setExcludedTenantId(DashboardConstants.TESTING_TENANT);
		String[] status = {"INITIATED","CITIZEN_APPROVAL_INPROCESS","INPROGRESS","PENDING_APPL_FEE"};   
		Set <String> statusNotIn = Arrays.asList(status).stream().collect(Collectors.toSet());
		criteria.setStatusNotIn(statusNotIn);
		
		String[] businessService = {"BPA1","BPA2","BPA3","BPA4","BPA5","BPA6","BPA7","BPA8","BPA9","BPA10"};   
		Set <String> businessServiceIn = Arrays.asList(businessService).stream().collect(Collectors.toSet());
		criteria.setBusinessServices(businessServiceIn);
		
		Integer totalApplication = (Integer) bpaRepository.totalApplicationsReceived(criteria);
		return Arrays.asList(Data.builder().headerValue(totalApplication).build());
	}

	public List<Data> totalApplicationsRejected(PayloadDetails payloadDetails) {
		BpaSearchCriteria criteria = getBpaSearchCriteria(payloadDetails);
		
		criteria.setExcludedTenantId(DashboardConstants.TESTING_TENANT);
		String[] status = {"REJECTED"};   
		Set <String> statusIn = Arrays.asList(status).stream().collect(Collectors.toSet());
		criteria.setStatus(statusIn);
		
		String[] businessService = {"BPA1","BPA2","BPA3","BPA4","BPA5","BPA6","BPA7","BPA8","BPA9","BPA10"};   
		Set <String> businessServiceIn = Arrays.asList(businessService).stream().collect(Collectors.toSet());
		criteria.setBusinessServices(businessServiceIn);
		Integer totalApplication = (Integer) bpaRepository.totalApplicationsRejected(criteria);
		return Arrays.asList(Data.builder().headerValue(totalApplication).build());
	}

	public List<Data> totalApplicationsPending(PayloadDetails payloadDetails) {
		BpaSearchCriteria criteria = getBpaSearchCriteria(payloadDetails);
		criteria.setExcludedTenantId(DashboardConstants.TESTING_TENANT);
		String[] status = {"INITIATED","CITIZEN_APPROVAL_INPROCESS","INPROGRESS","PENDING_APPL_FEE","APPROVED","REJECTED"};   
		Set <String> statusNotIn = Arrays.asList(status).stream().collect(Collectors.toSet());
		criteria.setStatusNotIn(statusNotIn);
		
		String[] businessService = {"BPA1","BPA2","BPA3","BPA4","BPA5","BPA6","BPA7","BPA8","BPA9","BPA10"};   
		Set <String> businessServiceIn = Arrays.asList(businessService).stream().collect(Collectors.toSet());
		criteria.setBusinessServices(businessServiceIn);
		
		Integer totalApplication = (Integer) bpaRepository.totalApplicationsPending(criteria);
		return Arrays.asList(Data.builder().headerValue(totalApplication).build());
	}

	public List<Data> avgDaysToIssuePermit(PayloadDetails payloadDetails) {
		BpaSearchCriteria criteria = getBpaSearchCriteria(payloadDetails);
		
		criteria.setExcludedTenantId(DashboardConstants.TESTING_TENANT);
		String[] status = {"APPROVED"};   
		Set <String> statusIn = Arrays.asList(status).stream().collect(Collectors.toSet());
		criteria.setStatus(statusIn);
		
		String[] businessService = {"BPA1","BPA2","BPA3","BPA4","BPA5","BPA6","BPA7","BPA8","BPA9","BPA10"};   
		Set <String> businessServiceIn = Arrays.asList(businessService).stream().collect(Collectors.toSet());
		criteria.setBusinessServices(businessServiceIn);
		BigDecimal totalApplication = (BigDecimal) bpaRepository.getAvgDaysToIssuePermit(criteria);//change it
		return Arrays.asList(Data.builder().headerValue(totalApplication).build());
	}

	public List<Data> minDaysToIssuePermit(PayloadDetails payloadDetails) {
		BpaSearchCriteria criteria = getBpaSearchCriteria(payloadDetails);
		
		criteria.setExcludedTenantId(DashboardConstants.TESTING_TENANT);
		String[] status = {"APPROVED"};   
		Set <String> statusIn = Arrays.asList(status).stream().collect(Collectors.toSet());
		criteria.setStatus(statusIn);
		
		String[] businessService = {"BPA1","BPA2","BPA3","BPA4","BPA5","BPA6","BPA7","BPA8","BPA9","BPA10"};   
		Set <String> businessServiceIn = Arrays.asList(businessService).stream().collect(Collectors.toSet());
		criteria.setBusinessServices(businessServiceIn);
		Integer totalApplication = (Integer) bpaRepository.getMinDaysToIssuePermit(criteria);
		return Arrays.asList(Data.builder().headerValue(totalApplication).build());
	}

	public List<Data> maxDaysToIssuePermit(PayloadDetails payloadDetails) {
		BpaSearchCriteria criteria = getBpaSearchCriteria(payloadDetails);
		
		criteria.setExcludedTenantId(DashboardConstants.TESTING_TENANT);
		String[] status = {"APPROVED"};   
		Set <String> statusIn = Arrays.asList(status).stream().collect(Collectors.toSet());
		criteria.setStatus(statusIn);
		
		String[] businessService = {"BPA1","BPA2","BPA3","BPA4","BPA5","BPA6","BPA7","BPA8","BPA9","BPA10"};   
		Set <String> businessServiceIn = Arrays.asList(businessService).stream().collect(Collectors.toSet());
		criteria.setBusinessServices(businessServiceIn);
		Integer totalApplication = (Integer) bpaRepository.getMaxDaysToIssuePermit(criteria);
		return Arrays.asList(Data.builder().headerValue(totalApplication).build());
	}

	
	public List<Data> slaCompliancePermit(PayloadDetails payloadDetails) {
		BpaSearchCriteria criteria = getBpaSearchCriteria(payloadDetails);
		
		criteria.setExcludedTenantId(DashboardConstants.TESTING_TENANT);
		String[] status = {"APPROVED"};   
		Set <String> statusIn = Arrays.asList(status).stream().collect(Collectors.toSet());
		criteria.setStatus(statusIn);
		
		String[] businessService = {"BPA1","BPA2","BPA3","BPA4","BPA5","BPA6","BPA7","BPA8","BPA9","BPA10"};   
		Set <String> businessServiceIn = Arrays.asList(businessService).stream().collect(Collectors.toSet());
		criteria.setBusinessServices(businessServiceIn);
		criteria.setSlaThreshold(config.getSlaBpaPermitsThreshold());
		Integer totalApplication = (Integer) bpaRepository.getSlaCompliancePermit(criteria);
		return Arrays.asList(Data.builder().headerValue(totalApplication).build());
	}

	public List<Data> slaComplianceOtherThanLowRisk(PayloadDetails payloadDetails) {
		BpaSearchCriteria criteria = getBpaSearchCriteria(payloadDetails);
		
		criteria.setExcludedTenantId(DashboardConstants.TESTING_TENANT);
		String[] status = {"APPROVED"};   
		Set <String> statusIn = Arrays.asList(status).stream().collect(Collectors.toSet());
		criteria.setStatus(statusIn);
		
		String[] businessService = {"BPA2","BPA3","BPA4"};   
		Set <String> businessServiceIn = Arrays.asList(businessService).stream().collect(Collectors.toSet());
		criteria.setBusinessServices(businessServiceIn);
		criteria.setSlaThreshold(config.getSlaBpaOtherThanLowRiskThreshold());
		Integer totalApplication = (Integer) bpaRepository.getSlaComplianceOtherThanLowRisk(criteria);
		return Arrays.asList(Data.builder().headerValue(totalApplication).build());
	}

	public List<Data> slaCompliancePreApprovedPlan(PayloadDetails payloadDetails) {
		BpaSearchCriteria criteria = getBpaSearchCriteria(payloadDetails);
		
		criteria.setExcludedTenantId(DashboardConstants.TESTING_TENANT);
		String[] status = {"APPROVED"};   
		Set <String> statusIn = Arrays.asList(status).stream().collect(Collectors.toSet());
		criteria.setStatus(statusIn);
		
		String[] businessService = {"BPA6"};   
		Set <String> businessServiceIn = Arrays.asList(businessService).stream().collect(Collectors.toSet());
		criteria.setBusinessServices(businessServiceIn);
		criteria.setSlaThreshold(config.getSlaBpaPreApprovedPlanThreshold());
		Integer totalApplication = (Integer) bpaRepository.getSlaCompliancePreApprovedPlan(criteria);
		return Arrays.asList(Data.builder().headerValue(totalApplication).build());
	}

	public List<Data> slaComplianceBuildingPermit(PayloadDetails payloadDetails) {
		BpaSearchCriteria criteria = getBpaSearchCriteria(payloadDetails);
	
		criteria.setExcludedTenantId(DashboardConstants.TESTING_TENANT);
		String[] status = {"APPROVED"};   
		Set <String> statusIn = Arrays.asList(status).stream().collect(Collectors.toSet());
		criteria.setStatus(statusIn);
		
		String[] businessService = {"BPA1","BPA2","BPA3","BPA4","BPA6"};   
		Set <String> businessServiceIn = Arrays.asList(businessService).stream().collect(Collectors.toSet());
		criteria.setBusinessServices(businessServiceIn);
		criteria.setSlaThreshold(config.getSlaBpaBuildingPermitThreshold());
		Integer totalApplication = (Integer) bpaRepository.getSlaComplianceBuildingPermit(criteria);
		return Arrays.asList(Data.builder().headerValue(totalApplication).build());
	}
	
	private List<Chart> mapTenantsForPerformanceRate(HashMap<String, Long> numeratorMap,
			HashMap<String, Long> denominatorMap) {
		List<Chart> percentList = new ArrayList();
		numeratorMap.entrySet().stream().forEach(item ->{
			Long numerator = item.getValue();
			Long denominator = denominatorMap.get(item.getKey());
			BigDecimal percent =new BigDecimal(numerator * 100) .divide(new BigDecimal(denominator), 2, RoundingMode.HALF_EVEN);
			percentList.add(Chart.builder().name(item.getKey()).value(percent).build());
		});
		return percentList;
	}

	public List<Data> topUlbByPerformance(PayloadDetails payloadDetails) {
		BpaSearchCriteria criteria = getBpaSearchCriteria(payloadDetails);
		criteria.setExcludedTenantId(DashboardConstants.TESTING_TENANT);
		
		String[] businessService = {"BPA1","BPA2","BPA3","BPA4","BPA5","BPA6","BPA7","BPA8","BPA9","BPA10"};   
		Set <String> businessServiceIn = Arrays.asList(businessService).stream().collect(Collectors.toSet());
		criteria.setBusinessServices(businessServiceIn);
		
		String[] statusIncluded = {"APPROVED"};   
		Set <String> statusIn = Arrays.asList(statusIncluded).stream().collect(Collectors.toSet());
		criteria.setStatus(statusIn);
		
		HashMap<String, Long> tenantWisePermitsIssuedList = bpaRepository.getTenantWisePermitsIssuedList(criteria);
		criteria.setStatus(null);
		
		
		String[] status = {"INITIATED","CITIZEN_APPROVAL_INPROCESS","INPROGRESS","PENDING_APPL_FEE"};   
		Set <String> statusNotIn = Arrays.asList(status).stream().collect(Collectors.toSet());
		criteria.setStatusNotIn(statusNotIn);
		
		HashMap<String, Long> tenantWiseApplicationsReceivedList = bpaRepository.getTenantWiseApplicationsReceivedList(criteria);

		List<Chart> percentList = mapTenantsForPerformanceRate(tenantWisePermitsIssuedList, tenantWiseApplicationsReceivedList);

		 Collections.sort(percentList,Comparator.comparing(e -> e.getValue(),(s1,s2)->{
             return s2.compareTo(s1);
         }));

		 List<Data> response = new ArrayList();
		 int Rank = 0;
		 for( Chart obj : percentList) {
			 Rank++;
			 response.add(Data.builder().headerName("Rank").headerValue(Rank).plots(Arrays.asList(Plot.builder().label("DSS_COMPLETION_RATE").name(obj.getName()).value(obj.getValue()).symbol("percentage").build())).headerSymbol("percentage").build());
		 };

		return response;
	}

	public List<Data> bottomUlbByPerformance(PayloadDetails payloadDetails) {
		BpaSearchCriteria criteria = getBpaSearchCriteria(payloadDetails);
		criteria.setExcludedTenantId(DashboardConstants.TESTING_TENANT);
		
		String[] businessService = {"BPA1","BPA2","BPA3","BPA4","BPA5","BPA6","BPA7","BPA8","BPA9","BPA10"};   
		Set <String> businessServiceIn = Arrays.asList(businessService).stream().collect(Collectors.toSet());
		criteria.setBusinessServices(businessServiceIn);
		
		String[] statusIncluded = {"APPROVED"};   
		Set <String> statusIn = Arrays.asList(statusIncluded).stream().collect(Collectors.toSet());
		criteria.setStatus(statusIn);
		
		HashMap<String, Long> tenantWisePermitsIssuedList = bpaRepository.getTenantWisePermitsIssuedList(criteria);
		criteria.setStatus(null);
		
		
		String[] status = {"INITIATED","CITIZEN_APPROVAL_INPROCESS","INPROGRESS","PENDING_APPL_FEE"};   
		Set <String> statusNotIn = Arrays.asList(status).stream().collect(Collectors.toSet());
		criteria.setStatusNotIn(statusNotIn);
		
		HashMap<String, Long> tenantWiseApplicationsReceivedList = bpaRepository.getTenantWiseApplicationsReceivedList(criteria);

		List<Chart> percentList = mapTenantsForPerformanceRate(tenantWisePermitsIssuedList, tenantWiseApplicationsReceivedList);

		 Collections.sort(percentList,Comparator.comparing(e -> e.getValue(),(s1,s2)->{
             return s1.compareTo(s2);
         }));

		 List<Data> response = new ArrayList();
		 int Rank = 0;
		 for( Chart obj : percentList) {
			 Rank++;
			 response.add(Data.builder().headerName("Rank").headerValue(Rank).plots(Arrays.asList(Plot.builder().label("DSS_COMPLETION_RATE").name(obj.getName()).value(obj.getValue()).symbol("percentage").build())).headerSymbol("percentage").build());
		 };

		return response;
	}
}
