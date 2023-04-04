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
		criteria.setStatus(Sets.newHashSet(DashboardConstants.STATUS_APPROVED));
		criteria.setBusinessServices(Sets.newHashSet(DashboardConstants.OBPS_ALL_BUSINESS_SERVICES));
		Integer totalApplication = (Integer) bpaRepository.getTotalPermitsIssued(criteria);
		return Arrays.asList(Data.builder().headerValue(totalApplication).build());
	}

	public List<Data> slaAchieved(PayloadDetails payloadDetails) {
		BpaSearchCriteria criteria = getBpaSearchCriteria(payloadDetails);
		criteria.setExcludedTenantId(DashboardConstants.TESTING_TENANT);
		criteria.setStatus(Sets.newHashSet(DashboardConstants.STATUS_APPROVED));
		Integer totalApplication = (Integer) bpaRepository.getTotalPermitsIssued(criteria);
		Integer slaAchievedAppCount = (Integer) bpaRepository.getSlaAchievedAppCount(criteria);
		return Arrays.asList(Data.builder()
				.headerValue((slaAchievedAppCount.doubleValue() / totalApplication.doubleValue()) * 100).build());
	}

	public BpaSearchCriteria getBpaSearchCriteria(PayloadDetails payloadDetails) {
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
		criteria.setStatusNotIn(Sets.newHashSet(DashboardConstants.OBPS_REJECTED_STATUSES));
		criteria.setBusinessServices(Sets.newHashSet(DashboardConstants.OBPS_ALL_BUSINESS_SERVICES));
		Integer totalApplication = (Integer) bpaRepository.totalApplicationsReceived(criteria);
		return Arrays.asList(Data.builder().headerValue(totalApplication).build());
	}

	public List<Data> totalApplicationsRejected(PayloadDetails payloadDetails) {
		BpaSearchCriteria criteria = getBpaSearchCriteria(payloadDetails);
		criteria.setExcludedTenantId(DashboardConstants.TESTING_TENANT);
		criteria.setStatus(Sets.newHashSet(DashboardConstants.STATUS_REJECTED));
		criteria.setBusinessServices(Sets.newHashSet(DashboardConstants.OBPS_ALL_BUSINESS_SERVICES));
		Integer totalApplication = (Integer) bpaRepository.totalApplicationsRejected(criteria);
		return Arrays.asList(Data.builder().headerValue(totalApplication).build());
	}

	public List<Data> totalApplicationsPending(PayloadDetails payloadDetails) {
		BpaSearchCriteria criteria = getBpaSearchCriteria(payloadDetails);
		criteria.setExcludedTenantId(DashboardConstants.TESTING_TENANT);
		criteria.setStatusNotIn(Sets.newHashSet(DashboardConstants.OBPS_REJECTED_STATUS_TOTAL_APPLICATIONS_PENDING));
		criteria.setBusinessServices(Sets.newHashSet(DashboardConstants.OBPS_ALL_BUSINESS_SERVICES));
		Integer totalApplication = (Integer) bpaRepository.totalApplicationsPending(criteria);
		return Arrays.asList(Data.builder().headerValue(totalApplication).build());
	}

	public List<Data> avgDaysToIssuePermit(PayloadDetails payloadDetails) {
		BpaSearchCriteria criteria = getBpaSearchCriteria(payloadDetails);
		criteria.setExcludedTenantId(DashboardConstants.TESTING_TENANT);
		criteria.setStatus(Sets.newHashSet(DashboardConstants.STATUS_APPROVED));
		criteria.setBusinessServices(Sets.newHashSet(DashboardConstants.OBPS_ALL_BUSINESS_SERVICES));
		BigDecimal totalApplication = (BigDecimal) bpaRepository.getAvgDaysToIssuePermit(criteria);// change it
		return Arrays.asList(Data.builder().headerValue(totalApplication).build());
	}

	public List<Data> minDaysToIssuePermit(PayloadDetails payloadDetails) {
		BpaSearchCriteria criteria = getBpaSearchCriteria(payloadDetails);
		criteria.setExcludedTenantId(DashboardConstants.TESTING_TENANT);
		criteria.setStatus(Sets.newHashSet(DashboardConstants.STATUS_APPROVED));
		criteria.setBusinessServices(Sets.newHashSet(DashboardConstants.OBPS_ALL_BUSINESS_SERVICES));
		Integer totalApplication = (Integer) bpaRepository.getMinDaysToIssuePermit(criteria);
		return Arrays.asList(Data.builder().headerValue(totalApplication).build());
	}

	public List<Data> maxDaysToIssuePermit(PayloadDetails payloadDetails) {
		BpaSearchCriteria criteria = getBpaSearchCriteria(payloadDetails);
		criteria.setExcludedTenantId(DashboardConstants.TESTING_TENANT);
		criteria.setStatus(Sets.newHashSet(DashboardConstants.STATUS_APPROVED));
		criteria.setBusinessServices(Sets.newHashSet(DashboardConstants.OBPS_ALL_BUSINESS_SERVICES));
		Integer totalApplication = (Integer) bpaRepository.getMaxDaysToIssuePermit(criteria);
		return Arrays.asList(Data.builder().headerValue(totalApplication).build());
	}

	public List<Data> slaCompliancePermit(PayloadDetails payloadDetails) {
		BpaSearchCriteria criteria = getBpaSearchCriteria(payloadDetails);
		criteria.setExcludedTenantId(DashboardConstants.TESTING_TENANT);
		criteria.setStatus(Sets.newHashSet(DashboardConstants.STATUS_APPROVED));
		criteria.setBusinessServices(Sets.newHashSet(DashboardConstants.OBPS_ALL_BUSINESS_SERVICES));
		criteria.setSlaThreshold(config.getSlaBpaPermitsThreshold());
		Integer totalApplication = (Integer) bpaRepository.getSlaCompliancePermit(criteria);
		return Arrays.asList(Data.builder().headerValue(totalApplication).build());
	}

	public List<Data> slaComplianceOtherThanLowRisk(PayloadDetails payloadDetails) {
		BpaSearchCriteria criteria = getBpaSearchCriteria(payloadDetails);
		criteria.setExcludedTenantId(DashboardConstants.TESTING_TENANT);
		criteria.setStatus(Sets.newHashSet(DashboardConstants.STATUS_APPROVED));
		criteria.setBusinessServices(
				Sets.newHashSet(DashboardConstants.OBPS_SLA_COMPLIANCE_OTHER_THAN_LOW_RISK_STATUS));
		criteria.setSlaThreshold(config.getSlaBpaOtherThanLowRiskThreshold());
		Integer totalApplication = (Integer) bpaRepository.getSlaComplianceOtherThanLowRisk(criteria);
		return Arrays.asList(Data.builder().headerValue(totalApplication).build());
	}

	public List<Data> slaCompliancePreApprovedPlan(PayloadDetails payloadDetails) {
		BpaSearchCriteria criteria = getBpaSearchCriteria(payloadDetails);
		criteria.setExcludedTenantId(DashboardConstants.TESTING_TENANT);
		criteria.setStatus(Sets.newHashSet(DashboardConstants.STATUS_APPROVED));
		criteria.setBusinessServices(Sets.newHashSet(DashboardConstants.BUSINESS_SERICE_BPA6));
		criteria.setSlaThreshold(config.getSlaBpaPreApprovedPlanThreshold());
		Integer totalApplication = (Integer) bpaRepository.getSlaCompliancePreApprovedPlan(criteria);
		return Arrays.asList(Data.builder().headerValue(totalApplication).build());
	}

	public List<Data> slaComplianceBuildingPermit(PayloadDetails payloadDetails) {
		BpaSearchCriteria criteria = getBpaSearchCriteria(payloadDetails);
		criteria.setExcludedTenantId(DashboardConstants.TESTING_TENANT);
		criteria.setStatus(Sets.newHashSet(DashboardConstants.STATUS_APPROVED));
		criteria.setBusinessServices(Sets.newHashSet(DashboardConstants.OBPS_SLA_COMPLIANCE_BUILDING_PERMIT_STATUS));
		criteria.setSlaThreshold(config.getSlaBpaBuildingPermitThreshold());
		Integer totalApplication = (Integer) bpaRepository.getSlaComplianceBuildingPermit(criteria);
		return Arrays.asList(Data.builder().headerValue(totalApplication).build());
	}

	private List<Chart> mapTenantsForPerformanceRate(HashMap<String, Long> numeratorMap,
			HashMap<String, Long> denominatorMap) {
		List<Chart> percentList = new ArrayList();
		numeratorMap.entrySet().stream().forEach(item -> {
			Long numerator = item.getValue();
			Long denominator = denominatorMap.get(item.getKey());
			BigDecimal percent = new BigDecimal(numerator * 100).divide(new BigDecimal(denominator), 2,
					RoundingMode.HALF_EVEN);
			percentList.add(Chart.builder().name(item.getKey()).value(percent).build());
		});
		return percentList;
	}

	public List<Data> topUlbByPerformance(PayloadDetails payloadDetails) {
		BpaSearchCriteria criteria = getBpaSearchCriteria(payloadDetails);
		criteria.setExcludedTenantId(DashboardConstants.TESTING_TENANT);
		criteria.setBusinessServices(Sets.newHashSet(DashboardConstants.OBPS_ALL_BUSINESS_SERVICES));
		criteria.setStatus(Sets.newHashSet(DashboardConstants.STATUS_APPROVED));
		HashMap<String, Long> tenantWisePermitsIssuedList = bpaRepository.getTenantWisePermitsIssuedList(criteria);
		criteria.setStatus(null);
		criteria.setStatusNotIn(Sets.newHashSet(DashboardConstants.OBPS_REJECTED_STATUSES));
		HashMap<String, Long> tenantWiseApplicationsReceivedList = bpaRepository
				.getTenantWiseApplicationsReceivedList(criteria);
		List<Chart> percentList = mapTenantsForPerformanceRate(tenantWisePermitsIssuedList,
				tenantWiseApplicationsReceivedList);
		Collections.sort(percentList, Comparator.comparing(e -> e.getValue(), (s1, s2) -> {
			return s2.compareTo(s1);
		}));

		List<Data> response = new ArrayList();
		int Rank = 0;
		for (Chart obj : percentList) {
			Rank++;
			response.add(
					Data.builder().headerName("Rank").headerValue(Rank)
							.plots(Arrays.asList(Plot.builder().label("DSS_COMPLETION_RATE").name(obj.getName())
									.value(obj.getValue()).symbol("percentage").build()))
							.headerSymbol("percentage").build());
		}
		;

		return response;
	}

	public List<Data> bottomUlbByPerformance(PayloadDetails payloadDetails) {
		BpaSearchCriteria criteria = getBpaSearchCriteria(payloadDetails);
		criteria.setExcludedTenantId(DashboardConstants.TESTING_TENANT);
		criteria.setBusinessServices(Sets.newHashSet(DashboardConstants.OBPS_ALL_BUSINESS_SERVICES));
		criteria.setStatus(Sets.newHashSet(DashboardConstants.STATUS_APPROVED));
		HashMap<String, Long> tenantWisePermitsIssuedList = bpaRepository.getTenantWisePermitsIssuedList(criteria);
		criteria.setStatus(null);
		criteria.setStatusNotIn(Sets.newHashSet(DashboardConstants.OBPS_REJECTED_STATUSES));
		HashMap<String, Long> tenantWiseApplicationsReceivedList = bpaRepository
				.getTenantWiseApplicationsReceivedList(criteria);
		List<Chart> percentList = mapTenantsForPerformanceRate(tenantWisePermitsIssuedList,
				tenantWiseApplicationsReceivedList);
		Collections.sort(percentList, Comparator.comparing(e -> e.getValue(), (s1, s2) -> {
			return s1.compareTo(s2);
		}));

		List<Data> response = new ArrayList();
		int Rank = percentList.size();
		for (Chart obj : percentList) {
			response.add(
					Data.builder().headerName("Rank").headerValue(Rank)
							.plots(Arrays.asList(Plot.builder().label("DSS_COMPLETION_RATE").name(obj.getName())
									.value(obj.getValue()).symbol("percentage").build()))
							.headerSymbol("percentage").build());
			Rank--;
		}
		;

		return response;
	}

	public List<Data> permitsAndOcIssuedAndOcSubmitted(PayloadDetails payloadDetails) {
		List<Data> response = new ArrayList<>();
		BpaSearchCriteria criteria = getBpaSearchCriteria(payloadDetails);
		criteria.setExcludedTenantId(DashboardConstants.TESTING_TENANT);
		criteria.setBusinessServices(Sets.newHashSet(DashboardConstants.OBPS_ALL_SERVICES));

		LinkedHashMap<String,Long> monthYearPermit= bpaRepository.getMonthYearData(criteria);
		LinkedHashMap<String,Long> monthYearOCIssued= bpaRepository.getMonthYearData(criteria);
		LinkedHashMap<String,Long> monthYearOCSubmitted= bpaRepository.getMonthYearData(criteria);


		criteria.setStatus(Sets.newHashSet(DashboardConstants.STATUS_APPROVED));
		criteria.setBusinessServices(Sets.newHashSet(DashboardConstants.OBPS_ALL_BUSINESS_SERVICES));
		List<Chart> totalPermitsIssuedMonthWise = bpaRepository.getTotalPermitsIssuedVsTotalOcIssuedVsTotalOcSubmitted(criteria);
		
		List<Plot> plotsForTotalPermitsIssuedMonthWise = extractedMonthYearData(monthYearPermit,
				totalPermitsIssuedMonthWise);
		
		Long totalPermitsIssued = monthYearPermit.values().stream().mapToLong(Long::longValue).sum();
		response.add(Data.builder().headerName("TotalPermitIssued").headerValue(totalPermitsIssued).plots(plotsForTotalPermitsIssuedMonthWise).build());
		

		
		criteria.setStatus(Sets.newHashSet(DashboardConstants.STATUS_APPROVED));
		criteria.setBusinessServices(Sets.newHashSet(DashboardConstants.OBPS_ALL_OC_BUSINESS_SERVICES));
		List<Chart> totalOcIssuedMonthWise = bpaRepository.getTotalPermitsIssuedVsTotalOcIssuedVsTotalOcSubmitted(criteria);
		
		List<Plot> plotsForTotalOcIssuedMonthWise = extractedMonthYearData(monthYearOCIssued,
				totalOcIssuedMonthWise);
		
		
		Long totalOcIssued = monthYearOCIssued.values().stream().mapToLong(Long::longValue).sum();
		response.add(Data.builder().headerName("TotalOCissued").headerValue(totalOcIssued).plots(plotsForTotalOcIssuedMonthWise).build());

		

		
		criteria.setStatus(null);
		criteria.setStatusNotIn(Sets.newHashSet(DashboardConstants.OBPS_REJECTED_STATUSES));
		criteria.setBusinessServices(Sets.newHashSet(DashboardConstants.OBPS_ALL_OC_BUSINESS_SERVICES));
		List<Chart> totalOcSubmittedMonthWise = bpaRepository.getTotalPermitsIssuedVsTotalOcIssuedVsTotalOcSubmitted(criteria);
		
		List<Plot> plotsForTotalOcSubmittedMonthWise = extractedMonthYearData(monthYearOCSubmitted,
				totalOcSubmittedMonthWise);
		
		
		Long totalOcSubmitted = monthYearOCSubmitted.values().stream().mapToLong(Long::longValue).sum();
		response.add(Data.builder().headerName("TotalOCSubmitted").headerValue(totalOcSubmitted).plots(plotsForTotalOcSubmittedMonthWise).build());


		return response;		

	}

	private List<Plot> extractedMonthYearData(LinkedHashMap<String, Long> monthYearPermit,
			List<Chart> totalPermitsIssuedMonthWise) {
		totalPermitsIssuedMonthWise.forEach(item -> {
			if(monthYearPermit.containsKey(item.getName())) {
				BigDecimal value =  item.getValue();
				monthYearPermit.replace(item.getName(), value.longValue());
			}
		});
		List<Plot> plotsForTotalPermitsIssuedMonthWise = new ArrayList();
		monthYearPermit.forEach((key,value) ->{
			plotsForTotalPermitsIssuedMonthWise.add(Plot.builder().name(key).value(new BigDecimal(value)).symbol("number").build());
		});
		return plotsForTotalPermitsIssuedMonthWise;
	}
	
	private Long extractDataForChart(List<Chart> items, List<Plot> plots, Long total) {
		for (Chart item : items) {
			plots.add(Plot.builder().name(item.getName()).value(item.getValue()).symbol("number").build());
			total = total + Long.valueOf(String.valueOf(item.getValue()));
		}
		return total ;
	}
	
	public List<Data> totalOcApplicationsReceived(PayloadDetails payloadDetails) {
        BpaSearchCriteria criteria = getBpaSearchCriteria(payloadDetails);
        criteria.setExcludedTenantId(DashboardConstants.TESTING_TENANT);
        criteria.setStatusNotIn(Sets.newHashSet(DashboardConstants.OBPS_REJECTED_STATUSES));
        criteria.setBusinessServices(Sets.newHashSet(DashboardConstants.OBPS_OC_BUSINESS_SERVICES));
        Integer totalApplication = (Integer) bpaRepository.totalApplicationsReceived(criteria);
        return Arrays.asList(Data.builder().headerValue(totalApplication).build());
    }
    
    public List<Data> totalOcIssued(PayloadDetails payloadDetails) {
        BpaSearchCriteria criteria = getBpaSearchCriteria(payloadDetails);
        criteria.setExcludedTenantId(DashboardConstants.TESTING_TENANT);
        criteria.setStatus(Sets.newHashSet(DashboardConstants.STATUS_APPROVED));
        criteria.setBusinessServices(Sets.newHashSet(DashboardConstants.OBPS_OC_BUSINESS_SERVICES));
        Integer totalApplication = (Integer) bpaRepository.getTotalPermitsIssued(criteria);
        return Arrays.asList(Data.builder().headerValue(totalApplication).build());
    }
    
    public List<Data> totalOcRejected(PayloadDetails payloadDetails) {
        BpaSearchCriteria criteria = getBpaSearchCriteria(payloadDetails);
        criteria.setExcludedTenantId(DashboardConstants.TESTING_TENANT);
        criteria.setStatus(Sets.newHashSet(DashboardConstants.STATUS_REJECTED));
        criteria.setBusinessServices(Sets.newHashSet(DashboardConstants.OBPS_OC_BUSINESS_SERVICES));
        Integer totalApplication = (Integer) bpaRepository.totalApplicationsRejected(criteria);
        return Arrays.asList(Data.builder().headerValue(totalApplication).build());
    }
    
    public List<Data> totalOcPending(PayloadDetails payloadDetails) {
        BpaSearchCriteria criteria = getBpaSearchCriteria(payloadDetails);
        criteria.setExcludedTenantId(DashboardConstants.TESTING_TENANT);
        criteria.setStatusNotIn(Sets.newHashSet(DashboardConstants.OBPS_REJECTED_STATUS_TOTAL_APPLICATIONS_PENDING));
        criteria.setBusinessServices(Sets.newHashSet(DashboardConstants.OBPS_OC_BUSINESS_SERVICES));
        Integer totalApplication = (Integer) bpaRepository.totalApplicationsPending(criteria);
        return Arrays.asList(Data.builder().headerValue(totalApplication).build());
    }
    
    public List<Data> avgDaysToIssueOc(PayloadDetails payloadDetails) {
        BpaSearchCriteria criteria = getBpaSearchCriteria(payloadDetails);
        criteria.setExcludedTenantId(DashboardConstants.TESTING_TENANT);
        criteria.setStatus(Sets.newHashSet(DashboardConstants.STATUS_APPROVED));
        criteria.setBusinessServices(Sets.newHashSet(DashboardConstants.OBPS_OC_BUSINESS_SERVICES));
        BigDecimal totalApplication = (BigDecimal) bpaRepository.getAvgDaysToIssuePermit(criteria);
        return Arrays.asList(Data.builder().headerValue(totalApplication).build());
    }
    
    public List<Data> minDaysToIssueOc(PayloadDetails payloadDetails) {
        BpaSearchCriteria criteria = getBpaSearchCriteria(payloadDetails);
        criteria.setExcludedTenantId(DashboardConstants.TESTING_TENANT);
        criteria.setStatus(Sets.newHashSet(DashboardConstants.STATUS_APPROVED));
        criteria.setBusinessServices(Sets.newHashSet(DashboardConstants.OBPS_OC_BUSINESS_SERVICES));
        Integer totalApplication = (Integer) bpaRepository.getMinDaysToIssuePermit(criteria);
        return Arrays.asList(Data.builder().headerValue(totalApplication).build());
    }
    
    public List<Data> maxDaysToIssueOc(PayloadDetails payloadDetails) {
        BpaSearchCriteria criteria = getBpaSearchCriteria(payloadDetails);
        criteria.setExcludedTenantId(DashboardConstants.TESTING_TENANT);
        criteria.setStatus(Sets.newHashSet(DashboardConstants.STATUS_APPROVED));
        criteria.setBusinessServices(Sets.newHashSet(DashboardConstants.OBPS_OC_BUSINESS_SERVICES));
        Integer totalApplication = (Integer) bpaRepository.getMaxDaysToIssuePermit(criteria);
        return Arrays.asList(Data.builder().headerValue(totalApplication).build());
    }
    
    public List<Data> slaComplianceOc(PayloadDetails payloadDetails) {
        BpaSearchCriteria criteria = getBpaSearchCriteria(payloadDetails);
        criteria.setExcludedTenantId(DashboardConstants.TESTING_TENANT);
        criteria.setStatus(Sets.newHashSet(DashboardConstants.STATUS_APPROVED));
        criteria.setBusinessServices(Sets.newHashSet(DashboardConstants.OBPS_OC_BUSINESS_SERVICES));
        criteria.setSlaThreshold(config.getSlaOcPermitThreshold());
        Integer totalApplication = (Integer) bpaRepository.getSlaCompliancePermit(criteria);
        return Arrays.asList(Data.builder().headerValue(totalApplication).build());
    }
    
    public List<Data> serviceReport(PayloadDetails payloadDetails) {
        BpaSearchCriteria criteria = getBpaSearchCriteria(payloadDetails);
        criteria.setExcludedTenantId(DashboardConstants.TESTING_TENANT);
        HashMap<String, BigDecimal> tenantWiseBpaApplicationSubmitted = getTenantWiseBpaApplication(payloadDetails);
        HashMap<String, BigDecimal> tenantWiseBpaPermitIssued = getTenantWisePermitIssued(payloadDetails);
        HashMap<String, BigDecimal> tenantWiseBpaPendingApplication = getTenantWiseApplicationPending(payloadDetails);
        HashMap<String, BigDecimal> tenantWiseBpaAvgDaysPermitIssue = getTenantWiseAvgDaysToIssuePermit(payloadDetails);
        HashMap<String, BigDecimal> tenantWiseBpaSlaCompliance = getTenantWiseSlaCompliancePermit(payloadDetails);
        HashMap<String, BigDecimal> tenantWiseOcApplicationSubmitted = getTenantWiseOcApplication(payloadDetails);
        HashMap<String, BigDecimal> tenantWiseOcPendingApplication = getTenantWiseOcApplicationPending(payloadDetails);
        HashMap<String, BigDecimal> tenantWiseOcIssued = getTenantWiseOcIssue(payloadDetails);
        HashMap<String, BigDecimal> tenantWiseOcAvgDaysPermitIssue = getTenantWiseAvgDaysToOcIssue(payloadDetails);
        HashMap<String, BigDecimal> tenantWiseOcSlaCompliance = getTenantWiseOcSlaCompliance(payloadDetails);
        
        List<Data> response = new ArrayList<>();
        int serialNumber = 1;

        for (HashMap.Entry<String, BigDecimal> tenantWiseBpaApplication : tenantWiseBpaApplicationSubmitted.entrySet()) {
            List<Plot> plots = new ArrayList();
            plots.add(Plot.builder().name("S.N.").label(String.valueOf(serialNumber)).symbol("text").build());

            plots.add(Plot.builder().name("ULBs").label(tenantWiseBpaApplication.getKey().toString()).symbol("text")
                    .build());

            plots.add(Plot.builder().name("Total Applications Submitted").value(tenantWiseBpaApplication.getValue() == null ? BigDecimal.ZERO : tenantWiseBpaApplication.getValue())
                    .symbol("number").build());

            plots.add(Plot.builder().name("Total Permits Issued")
                    .value(tenantWiseBpaPermitIssued.get(tenantWiseBpaApplication.getKey()) == null ? BigDecimal.ZERO : tenantWiseBpaPermitIssued.get(tenantWiseBpaApplication.getKey())).symbol("number").build());

            plots.add(Plot.builder().name("Total BPA Application Pending")
                    .value(tenantWiseBpaPendingApplication.get(tenantWiseBpaApplication.getKey()) == null ? BigDecimal.ZERO : tenantWiseBpaPendingApplication.get(tenantWiseBpaApplication.getKey())).symbol("number").build());

            plots.add(Plot.builder().name("Average days to issue Permit")
                    .value(tenantWiseBpaAvgDaysPermitIssue.get(tenantWiseBpaApplication.getKey()) == null ? BigDecimal.ZERO : tenantWiseBpaAvgDaysPermitIssue.get(tenantWiseBpaApplication.getKey()) ).symbol("number").build());

            plots.add(Plot.builder().name("SLA Compliance Permit")
                    .value(tenantWiseBpaSlaCompliance.get(tenantWiseBpaApplication.getKey()) == null ? BigDecimal.ZERO : tenantWiseBpaSlaCompliance.get(tenantWiseBpaApplication.getKey())).symbol("number").build());
            
            plots.add(Plot.builder().name("Total OC Submitted")
                    .value(tenantWiseOcApplicationSubmitted.get(tenantWiseBpaApplication.getKey()) == null ? BigDecimal.ZERO : tenantWiseOcApplicationSubmitted.get(tenantWiseBpaApplication.getKey())).symbol("number").build());
            
            plots.add(Plot.builder().name("Total OC Application Pending")
                    .value(tenantWiseOcPendingApplication.get(tenantWiseBpaApplication.getKey()) == null ? BigDecimal.ZERO : tenantWiseOcPendingApplication.get(tenantWiseBpaApplication.getKey())).symbol("number").build());
            
            plots.add(Plot.builder().name("Total OC Issued")
                    .value(tenantWiseOcIssued.get(tenantWiseBpaApplication.getKey()) == null ? BigDecimal.ZERO : tenantWiseOcIssued.get(tenantWiseBpaApplication.getKey())).symbol("number").build());
            
            plots.add(Plot.builder().name("Average days to issue OC")
                    .value(tenantWiseOcAvgDaysPermitIssue.get(tenantWiseBpaApplication.getKey()) == null ? BigDecimal.ZERO : tenantWiseOcAvgDaysPermitIssue.get(tenantWiseBpaApplication.getKey())).symbol("number").build());
            
            plots.add(Plot.builder().name("SLA Compliance OC")
                    .value(tenantWiseOcSlaCompliance.get(tenantWiseBpaApplication.getKey()) == null ? BigDecimal.ZERO : tenantWiseOcSlaCompliance.get(tenantWiseBpaApplication.getKey())).symbol("number").build());

            response.add(Data.builder().headerName(tenantWiseBpaApplication.getKey()).plots(plots)
                    .headerValue(serialNumber).headerName(tenantWiseBpaApplication.getKey()).build());

            serialNumber++;

        }

        return response;
    }
    
    public HashMap<String,BigDecimal> getTenantWiseBpaApplication(PayloadDetails payloadDetails) {
        BpaSearchCriteria criteria = getBpaSearchCriteria(payloadDetails);
        criteria.setExcludedTenantId(DashboardConstants.TESTING_TENANT);
        criteria.setStatusNotIn(Sets.newHashSet(DashboardConstants.OBPS_REJECTED_STATUSES));
        criteria.setBusinessServices(Sets.newHashSet(DashboardConstants.OBPS_ALL_BUSINESS_SERVICES));
        return bpaRepository.getTenantWiseBpaTotalApplication(criteria);
     }
    
    public HashMap<String,BigDecimal> getTenantWisePermitIssued(PayloadDetails payloadDetails) {
        BpaSearchCriteria criteria = getBpaSearchCriteria(payloadDetails);
        criteria.setExcludedTenantId(DashboardConstants.TESTING_TENANT);
        criteria.setStatus(Sets.newHashSet(DashboardConstants.STATUS_APPROVED));
        criteria.setBusinessServices(Sets.newHashSet(DashboardConstants.OBPS_ALL_BUSINESS_SERVICES));
        return bpaRepository.getTenantWiseBpaTotalApplication(criteria);
     }
    
    public HashMap<String,BigDecimal> getTenantWiseApplicationPending(PayloadDetails payloadDetails) {
        BpaSearchCriteria criteria = getBpaSearchCriteria(payloadDetails);
        criteria.setExcludedTenantId(DashboardConstants.TESTING_TENANT);
        criteria.setStatusNotIn(Sets.newHashSet(DashboardConstants.OBPS_REJECTED_STATUS_TOTAL_APPLICATIONS_PENDING));
        criteria.setBusinessServices(Sets.newHashSet(DashboardConstants.OBPS_ALL_BUSINESS_SERVICES));
        return bpaRepository.getTenantWiseBpaTotalApplication(criteria);
     }
    
    public HashMap<String,BigDecimal> getTenantWiseAvgDaysToIssuePermit(PayloadDetails payloadDetails) {
        BpaSearchCriteria criteria = getBpaSearchCriteria(payloadDetails);
        criteria.setExcludedTenantId(DashboardConstants.TESTING_TENANT);
        criteria.setStatus(Sets.newHashSet(DashboardConstants.STATUS_APPROVED));
        criteria.setBusinessServices(Sets.newHashSet(DashboardConstants.OBPS_ALL_BUSINESS_SERVICES));
        return bpaRepository.getTenantWiseAvgDaysPermitIssued(criteria);
     }
    
    public HashMap<String,BigDecimal> getTenantWiseSlaCompliancePermit(PayloadDetails payloadDetails) {
        BpaSearchCriteria criteria = getBpaSearchCriteria(payloadDetails);
        criteria.setExcludedTenantId(DashboardConstants.TESTING_TENANT);
        criteria.setStatus(Sets.newHashSet(DashboardConstants.STATUS_APPROVED));
        criteria.setBusinessServices(Sets.newHashSet(DashboardConstants.OBPS_ALL_BUSINESS_SERVICES));
        criteria.setSlaThreshold(config.getSlaBpaPermitsThreshold());
        return bpaRepository.getTenantWiseBpaTotalApplication(criteria);
    }
   
    public HashMap<String,BigDecimal> getTenantWiseOcApplication(PayloadDetails payloadDetails) {
        BpaSearchCriteria criteria = getBpaSearchCriteria(payloadDetails);
        criteria.setExcludedTenantId(DashboardConstants.TESTING_TENANT);
        criteria.setStatusNotIn(Sets.newHashSet(DashboardConstants.OBPS_REJECTED_STATUSES));
        criteria.setBusinessServices(Sets.newHashSet(DashboardConstants.OBPS_OC_BUSINESS_SERVICES));
        return bpaRepository.getTenantWiseBpaTotalApplication(criteria);
     }
    
    public HashMap<String,BigDecimal> getTenantWiseOcIssue(PayloadDetails payloadDetails) {
        BpaSearchCriteria criteria = getBpaSearchCriteria(payloadDetails);
        criteria.setExcludedTenantId(DashboardConstants.TESTING_TENANT);
        criteria.setStatus(Sets.newHashSet(DashboardConstants.STATUS_APPROVED));
        criteria.setBusinessServices(Sets.newHashSet(DashboardConstants.OBPS_OC_BUSINESS_SERVICES));
        return bpaRepository.getTenantWiseBpaTotalApplication(criteria);
     }
    
    public HashMap<String,BigDecimal> getTenantWiseOcApplicationPending(PayloadDetails payloadDetails) {
        BpaSearchCriteria criteria = getBpaSearchCriteria(payloadDetails);
        criteria.setExcludedTenantId(DashboardConstants.TESTING_TENANT);
        criteria.setStatusNotIn(Sets.newHashSet(DashboardConstants.OBPS_REJECTED_STATUS_TOTAL_APPLICATIONS_PENDING));
        criteria.setBusinessServices(Sets.newHashSet(DashboardConstants.OBPS_OC_BUSINESS_SERVICES));
        return bpaRepository.getTenantWiseBpaTotalApplication(criteria);
     }
    
    public HashMap<String,BigDecimal> getTenantWiseAvgDaysToOcIssue(PayloadDetails payloadDetails) {
        BpaSearchCriteria criteria = getBpaSearchCriteria(payloadDetails);
        criteria.setExcludedTenantId(DashboardConstants.TESTING_TENANT);
        criteria.setStatus(Sets.newHashSet(DashboardConstants.STATUS_APPROVED));
        criteria.setBusinessServices(Sets.newHashSet(DashboardConstants.OBPS_OC_BUSINESS_SERVICES));
        return bpaRepository.getTenantWiseAvgDaysPermitIssued(criteria);
     }
    
    public HashMap<String,BigDecimal> getTenantWiseOcSlaCompliance(PayloadDetails payloadDetails) {
        BpaSearchCriteria criteria = getBpaSearchCriteria(payloadDetails);
        criteria.setExcludedTenantId(DashboardConstants.TESTING_TENANT);
        criteria.setStatus(Sets.newHashSet(DashboardConstants.STATUS_APPROVED));
        criteria.setBusinessServices(Sets.newHashSet(DashboardConstants.OBPS_OC_BUSINESS_SERVICES));
        criteria.setSlaThreshold(config.getSlaOcPermitThreshold());
        return bpaRepository.getTenantWiseBpaTotalApplication(criteria);
    }

}
