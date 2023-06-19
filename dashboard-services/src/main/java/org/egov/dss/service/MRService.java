package org.egov.dss.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import org.egov.dss.constants.DashboardConstants;
import org.egov.dss.model.Chart;
import org.egov.dss.model.MarriageSearchCriteria;
import org.egov.dss.model.PayloadDetails;
import org.egov.dss.repository.MRRepository;
import org.egov.dss.web.model.Data;
import org.egov.dss.web.model.Plot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.google.common.collect.Sets;

@Service
public class MRService {
	
	@Autowired
	private MRRepository mrRepository;
	
	public List<Data> totalApplications(PayloadDetails payloadDetails) {
		MarriageSearchCriteria criteria = getMarriageSearchCriteria(payloadDetails);
	    criteria.setExcludedTenantId(DashboardConstants.TESTING_TENANT);
	    criteria.setIsApplicationDate(Boolean.TRUE);
	    criteria.setStatusNotIn(Sets.newHashSet(DashboardConstants.STATUS_INITIATED));
		Integer totalApplication =  (Integer) mrRepository.getTotalApplications(criteria);
		return Arrays.asList(Data.builder().headerValue(totalApplication).build());
	}
	
	public List<Data> slaAchieved(PayloadDetails payloadDetails) {
		MarriageSearchCriteria criteria = getMarriageSearchCriteria(payloadDetails);
		criteria.setExcludedTenantId(DashboardConstants.TESTING_TENANT);
		Integer totalApplication = (Integer) mrRepository.getTotalApplications(criteria);
		Integer slaAchievedAppCount = (Integer) mrRepository.getSlaAchievedAppCount(criteria);
		return Arrays.asList(Data.builder()
				.headerValue((slaAchievedAppCount.doubleValue() / totalApplication.doubleValue()) * 100).build());
	}
	
	public Integer slaAchievedCounts(PayloadDetails payloadDetails) {
		MarriageSearchCriteria criteria = getMarriageSearchCriteria(payloadDetails);
		criteria.setExcludedTenantId(DashboardConstants.TESTING_TENANT);
		Object slaAchievedAppCountObject = mrRepository.getSlaAchievedAppCount(criteria);
		if(slaAchievedAppCountObject == null) {
			return 0;
		}
		Integer slaAchievedAppCount = (Integer) slaAchievedAppCountObject;
		return slaAchievedAppCount;
	}
	
	public List<Data> totalNewApplications(PayloadDetails payloadDetails) {
		MarriageSearchCriteria criteria = getMarriageSearchCriteria(payloadDetails);
	    criteria.setExcludedTenantId(DashboardConstants.TESTING_TENANT);
	    criteria.setIsApplicationDate(Boolean.TRUE);
	    criteria.setApplicationType(DashboardConstants.APPLICATION_STATUS_NEW);
	    criteria.setStatusNotIn(Sets.newHashSet(DashboardConstants.STATUS_INITIATED));
		Integer totalApplication =  (Integer) mrRepository.getTotalApplications(criteria);
		return Arrays.asList(Data.builder().headerValue(totalApplication).build());
	}
	
	public List<Data> totalCorrectionApplications(PayloadDetails payloadDetails) {
		MarriageSearchCriteria criteria = getMarriageSearchCriteria(payloadDetails);
	    criteria.setExcludedTenantId(DashboardConstants.TESTING_TENANT);
	    criteria.setIsApplicationDate(Boolean.TRUE);
	    criteria.setApplicationType(DashboardConstants.APPLICATION_STATUS_CORRECTION);
	    criteria.setStatusNotIn(Sets.newHashSet(DashboardConstants.STATUS_INITIATED));
		Integer totalApplication =  (Integer) mrRepository.getTotalApplications(criteria);
		return Arrays.asList(Data.builder().headerValue(totalApplication).build());
	}
	
	public List<Data> totalApprovedApplications(PayloadDetails payloadDetails) {
		MarriageSearchCriteria criteria = getMarriageSearchCriteria(payloadDetails);
	    criteria.setExcludedTenantId(DashboardConstants.TESTING_TENANT);
	    criteria.setStatus(DashboardConstants.STATUS_APPROVED);
	    criteria.setIsApplicationDate(Boolean.TRUE);
	    Integer totalApplication =  (Integer) mrRepository.getTotalApplications(criteria);
		return Arrays.asList(Data.builder().headerValue(totalApplication).build());
	}
	
	public List<Data> totalTatkalApplications(PayloadDetails payloadDetails) {
		MarriageSearchCriteria criteria = getMarriageSearchCriteria(payloadDetails);
	    criteria.setExcludedTenantId(DashboardConstants.TESTING_TENANT);	   
	    criteria.setIsTatkalApplication(Boolean.TRUE);
	    criteria.setIsApplicationDate(Boolean.TRUE);
	    criteria.setStatusNotIn(Sets.newHashSet(DashboardConstants.STATUS_INITIATED));
	    Integer totalApplication =  (Integer) mrRepository.getTotalApplications(criteria);
		return Arrays.asList(Data.builder().headerValue(totalApplication).build());
	}
	
	public List<Data> slaAchievedCount(PayloadDetails payloadDetails) {
		MarriageSearchCriteria criteria = getMarriageSearchCriteria(payloadDetails);
		criteria.setExcludedTenantId(DashboardConstants.TESTING_TENANT);
		criteria.setIsApplicationDate(Boolean.TRUE);
		criteria.setStatus(DashboardConstants.STATUS_APPROVED);
		Integer slaAchievedAppCount = (Integer) mrRepository.getSlaAchievedAppCount(criteria);
		return Arrays.asList(Data.builder().headerValue(slaAchievedAppCount).build());
	}
	
	public List<Data> cumulativeApplications(PayloadDetails payloadDetails) {
		MarriageSearchCriteria criteria = getMarriageSearchCriteria(payloadDetails);
		criteria.setExcludedTenantId(DashboardConstants.TESTING_TENANT);
		criteria.setStatus(DashboardConstants.STATUS_APPROVED);
		criteria.setIsApplicationDate(Boolean.TRUE);
		List<Chart> cumulativeApplications = mrRepository.getCumulativeApplications(criteria);
		List<Plot> plots = new ArrayList();
		extractDataForChart(cumulativeApplications, plots);

		BigDecimal total = cumulativeApplications.stream().map(cumulativeApplication -> cumulativeApplication.getValue())
				.reduce(BigDecimal.ZERO, BigDecimal::add);

		return Arrays
				.asList(Data.builder().headerName("DSS_MR_CUMULATIVE_APPLICATIONS").headerValue(total).plots(plots).build());
	}
	
	public List<Data> topPerformingUlbs(PayloadDetails payloadDetails) {
		MarriageSearchCriteria criteria = getMarriageSearchCriteria(payloadDetails);
		criteria.setExcludedTenantId(DashboardConstants.TESTING_TENANT);
		criteria.setIsApplicationDate(Boolean.TRUE);
		criteria.setStatusNotIn(Sets.newHashSet(DashboardConstants.STATUS_INITIATED));
		HashMap<String, Long> totalApplication = mrRepository.getTenantWiseTotalApplication(criteria);
		criteria.setStatus(DashboardConstants.STATUS_APPROVED);
		HashMap<String, Long> approvedApplication = mrRepository.getTenantWiseTotalApplication(criteria);
		List<Chart> percentList = mapTenantsForPerformanceRate(approvedApplication, totalApplication);

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
		};
		
        return response;
	}

	public List<Data> bottomPerformingUlbs(PayloadDetails payloadDetails) {
		MarriageSearchCriteria criteria = getMarriageSearchCriteria(payloadDetails);
		criteria.setExcludedTenantId(DashboardConstants.TESTING_TENANT);
		criteria.setIsApplicationDate(Boolean.TRUE);
		criteria.setStatusNotIn(Sets.newHashSet(DashboardConstants.STATUS_INITIATED));
		HashMap<String, Long> totalApplication = mrRepository.getTenantWiseTotalApplication(criteria);
		criteria.setStatus(DashboardConstants.STATUS_APPROVED);
		HashMap<String, Long> approvedApplication = mrRepository.getTenantWiseTotalApplication(criteria);
		List<Chart> percentList = mapTenantsForPerformanceRate(approvedApplication, totalApplication);

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
		};
		
        return response;
	}
	
	public List<Data> mrApplicationsByStatus(PayloadDetails payloadDetails) {
		MarriageSearchCriteria criteria = getMarriageSearchCriteria(payloadDetails);
		criteria.setExcludedTenantId(DashboardConstants.TESTING_TENANT);
		criteria.setIsApplicationDate(Boolean.TRUE);
		List<Chart> applications = mrRepository.getApplicationsByStatus(criteria);
		List<Plot> plots = new ArrayList();
		extractDataForChart(applications, plots);	

		BigDecimal total = applications.stream().map(application -> application.getValue()).reduce(BigDecimal.ZERO,
				BigDecimal::add);		 

		return Arrays.asList(Data.builder().headerName("DSS_MR_APPLICATION_BY_STATUS").headerValue(total).plots(plots).build());
	}
	
	public List<Data> mrStatusByBoundary(PayloadDetails payloadDetails) {
		MarriageSearchCriteria criteria = getMarriageSearchCriteria(payloadDetails);
		criteria.setIsApplicationDate(Boolean.TRUE);
		criteria.setExcludedTenantId(DashboardConstants.TESTING_TENANT);
		List<HashMap<String, Object>> mrStatusByBoundary = mrRepository.getMrStatusByBoundary(criteria);

		List<Data> response = new ArrayList();
		int serailNumber = 0;
		for (HashMap<String, Object> mrStatus : mrStatusByBoundary) {
			serailNumber++;
			String tenantId = String.valueOf(mrStatus.get("tenantid"));
			String tenantIdStyled = tenantId.replace("od.", "");
			tenantIdStyled = tenantIdStyled.substring(0, 1).toUpperCase() + tenantIdStyled.substring(1).toLowerCase();
			List<Plot> row = new ArrayList<>();
			row.add(Plot.builder().label(String.valueOf(serailNumber)).name("S.N.").symbol("text").build());
			row.add(Plot.builder().label(tenantIdStyled).name("ULBs").symbol("text").build());

			row.add(Plot.builder().name("Initiated").value(new BigDecimal(String.valueOf(mrStatus.get("initiatedcnt"))))
					.symbol("number").build());
			row.add(Plot.builder().name("Approved").value(new BigDecimal(String.valueOf(mrStatus.get("approvedcnt"))))
					.symbol("number").build());
			row.add(Plot.builder().name("Rejected").value(new BigDecimal(String.valueOf(mrStatus.get("rejectedcnt"))))
					.symbol("number").build());
			row.add(Plot.builder().name("Pending Approval")
					.value(new BigDecimal(String.valueOf(mrStatus.get("approvalpendingcnt")))).symbol("number")
					.build());
			row.add(Plot.builder().name("Cancelled").value(new BigDecimal(String.valueOf(mrStatus.get("cancelledcnt"))))
					.symbol("number").build());
			row.add(Plot.builder().name("Pending Payment")
					.value(new BigDecimal(String.valueOf(mrStatus.get("paymentpendingcnt")))).symbol("number").build());
			row.add(Plot.builder().name("Pending Schedule")
					.value(new BigDecimal(String.valueOf(mrStatus.get("schedulependingcnt")))).symbol("number")
					.build());
			row.add(Plot.builder().name("Citizen Action Pending")
					.value(new BigDecimal(String.valueOf(mrStatus.get("citizenactionpendingcnt")))).symbol("number")
					.build());
			row.add(Plot.builder().name("Doc Verification Pending")
					.value(new BigDecimal(String.valueOf(mrStatus.get("docverificationpendingcnt")))).symbol("number")
					.build());

			response.add(Data.builder().headerName(tenantIdStyled).headerValue(serailNumber).plots(row).insight(null)
					.build());
		}
		
		if (CollectionUtils.isEmpty(response)) {
			serailNumber++;
			List<Plot> row = new ArrayList<>();
			row.add(Plot.builder().label(String.valueOf(serailNumber)).name("S.N.").symbol("text").build());
			row.add(Plot.builder().label(payloadDetails.getTenantid()).name("ULBs").symbol("text").build());
            row.add(Plot.builder().name("Initiated").value(BigDecimal.ZERO).symbol("number").build());
			row.add(Plot.builder().name("Approved").value(BigDecimal.ZERO).symbol("number").build());
			row.add(Plot.builder().name("Rejected").value(BigDecimal.ZERO).symbol("number").build());
			row.add(Plot.builder().name("Pending Approval").value(BigDecimal.ZERO).symbol("number").build());
			row.add(Plot.builder().name("Cancelled").value(BigDecimal.ZERO).symbol("number").build());
			row.add(Plot.builder().name("Pending Payment").value(BigDecimal.ZERO).symbol("number").build());
			row.add(Plot.builder().name("Pending Schedule").value(BigDecimal.ZERO).symbol("number").build());
			row.add(Plot.builder().name("Citizen Action Pending").value(BigDecimal.ZERO).symbol("number").build());
			row.add(Plot.builder().name("Doc Verification Pending").value(BigDecimal.ZERO).symbol("number").build());
			response.add(Data.builder().headerName(payloadDetails.getTenantid()).headerValue(serailNumber).plots(row)
					.insight(null).build());
		}

		return response;
	}
	
	
	public MarriageSearchCriteria getMarriageSearchCriteria(PayloadDetails payloadDetails) {
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
	
	private void extractDataForChart(List<Chart> items, List<Plot> plots) {
		items.stream().forEach(item -> {
			plots.add(Plot.builder().name(item.getName()).value(item.getValue()).symbol("number").build());
		});
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

	public HashMap<String, Long> totalApplicationsTenantWise(PayloadDetails payloadDetails) {
		MarriageSearchCriteria criteria = getMarriageSearchCriteria(payloadDetails);
		criteria.setExcludedTenantId(DashboardConstants.TESTING_TENANT);
		criteria.setIsApplicationDate(Boolean.TRUE);
		HashMap<String, Long> totalApplication = mrRepository.getTenantWiseTotalApplication(criteria);
        return totalApplication;
	}

	public HashMap<String, Long> totalCompletedApplicationsTenantWise(PayloadDetails payloadDetails) {
		MarriageSearchCriteria criteria = getMarriageSearchCriteria(payloadDetails);
		criteria.setExcludedTenantId(DashboardConstants.TESTING_TENANT);
		criteria.setIsApplicationDate(Boolean.TRUE);
		criteria.setStatus(DashboardConstants.STATUS_APPROVED);
		HashMap<String, Long> totalApplication = mrRepository.getTenantWiseTotalApplication(criteria);
        return totalApplication;
	}

}
