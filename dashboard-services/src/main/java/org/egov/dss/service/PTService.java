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
import org.egov.dss.model.PayloadDetails;
import org.egov.dss.model.PropertySerarchCriteria;
import org.egov.dss.repository.PTRepository;
import org.egov.dss.web.model.Data;
import org.egov.dss.web.model.Plot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.google.common.collect.Sets;

@Service
public class PTService {
	
	@Autowired
	private PTRepository ptRepository;

	public List<Data> totalProprties(PayloadDetails payloadDetails) {
		PropertySerarchCriteria criteria = getPropertySearchCriteria(payloadDetails);
		criteria.setExcludedTenantId(DashboardConstants.TESTING_TENANT);
		Integer totalProperties = (Integer) ptRepository.getTotalProperties(criteria);
		return Arrays.asList(Data.builder().headerValue(totalProperties).build());
	}

	public List<Data> propertiesPaid(PayloadDetails payloadDetails) {
		PropertySerarchCriteria criteria = getPropertySearchCriteria(payloadDetails);
		criteria.setExcludedTenantId(DashboardConstants.TESTING_TENANT);
		Integer totalPropertiesPaid = (Integer) ptRepository.getTotalPropertiesPaid(criteria);
		return Arrays.asList(Data.builder().headerValue(totalPropertiesPaid).build());
	}

	public List<Data> propertiesAssessed(PayloadDetails payloadDetails) {
		PropertySerarchCriteria criteria = getPropertySearchCriteria(payloadDetails);
		criteria.setExcludedTenantId(DashboardConstants.TESTING_TENANT);
		Integer assessedPropertiesCount = (Integer) ptRepository.getAssessedPropertiesCount(criteria);
		return Arrays.asList(Data.builder().headerValue(assessedPropertiesCount).build());
	}

	public List<Data> activeUlbs(PayloadDetails payloadDetails) {
		PropertySerarchCriteria criteria = getPropertySearchCriteria(payloadDetails);
		criteria.setExcludedTenantId(DashboardConstants.TESTING_TENANT);
		Integer activePropertyULBs = (Integer) ptRepository.getActivePRopertyULBs(criteria);
		return Arrays.asList(Data.builder().headerValue(activePropertyULBs).build());
	}

	public List<Data> totalMutationProperties(PayloadDetails payloadDetails) {
		PropertySerarchCriteria criteria = getPropertySearchCriteria(payloadDetails);
		criteria.setExcludedTenantId(DashboardConstants.TESTING_TENANT);
		Integer totalMutationPropertiesCount = (Integer) ptRepository.getTotalMutationPropertiesCount(criteria);
		return Arrays.asList(Data.builder().headerValue(totalMutationPropertiesCount).build());
	}

	public List<Data> ptTotalApplications(PayloadDetails payloadDetails) {
		PropertySerarchCriteria criteria = getPropertySearchCriteria(payloadDetails);
		criteria.setExcludedTenantId(DashboardConstants.TESTING_TENANT);
		Integer assessedPropertiesCount = (Integer) ptRepository.getTotalApplicationsCount(criteria);
		return Arrays.asList(Data.builder().headerValue(assessedPropertiesCount).build());
	}

	public List<Data> totalnoOfProperties(PayloadDetails payloadDetails) {
		PropertySerarchCriteria criteria = getPropertySearchCriteria(payloadDetails);
		criteria.setExcludedTenantId(DashboardConstants.TESTING_TENANT);
		Integer totalPropertiesCount = ptRepository.getTotalPropertiesCount(criteria);
		return Arrays.asList(Data.builder().headerValue(totalPropertiesCount).build());
	}

	public List<Data> ptNewAssessmentShare(PayloadDetails payloadDetails) {
		PropertySerarchCriteria criteria = getPropertySearchCriteria(payloadDetails);
		criteria.setExcludedTenantId(DashboardConstants.TESTING_TENANT);
		Integer ptTotalAssessmentsCount = (Integer) ptRepository.getPtTotalAssessmentsCount(criteria);
		Integer ptTotalNewAssessmentsCount = (Integer) ptRepository.getPtTotalNewAssessmentsCount(criteria);
		return Arrays.asList(Data.builder()
				.headerValue(Math.round((ptTotalNewAssessmentsCount.doubleValue() / ptTotalAssessmentsCount.doubleValue()) * 100)).build());
	}

	public List<Data> ptReAssessmentShare(PayloadDetails payloadDetails) {
		PropertySerarchCriteria criteria = getPropertySearchCriteria(payloadDetails);
		criteria.setExcludedTenantId(DashboardConstants.TESTING_TENANT);
		Integer ptTotalAssessmentsCount = (Integer) ptRepository.getPtTotalAssessmentsCount(criteria);
		Integer ptTotalNewAssessmentsCount = (Integer) ptRepository.getPtTotalNewAssessmentsCount(criteria);
		return Arrays.asList(Data.builder()
				.headerValue(Math.round((1 -(ptTotalNewAssessmentsCount.doubleValue() / ptTotalAssessmentsCount.doubleValue())) * 100)).build());
	}
	
	public List<Data> cumulativePropertiesAssessed(PayloadDetails payloadDetails) {
		getPropertySearchCriteria(payloadDetails);
		PropertySerarchCriteria criteria = getPropertySearchCriteria(payloadDetails);
		criteria.setExcludedTenantId(DashboardConstants.TESTING_TENANT);
		List<Chart> cumulativePropertiesAssessed = ptRepository.getCumulativePropertiesAssessed(criteria);

		List<Plot> plots = new ArrayList();
		extractDataForChart(cumulativePropertiesAssessed, plots);	

		BigDecimal total = cumulativePropertiesAssessed.stream().map(usageCategory -> usageCategory.getValue()).reduce(BigDecimal.ZERO,
				BigDecimal::add);		 

		return Arrays.asList(Data.builder().headerName("Collections").headerValue(total).plots(plots).build());
	}
	
	public List<Data> propertiesByUsageType(PayloadDetails payloadDetails) {
		PropertySerarchCriteria criteria = getPropertySearchCriteria(payloadDetails);
		criteria.setExcludedTenantId(DashboardConstants.TESTING_TENANT);
		List<Chart> propertiesByUsageType = ptRepository.getpropertiesByUsageType(criteria);

		List<Plot> plots = new ArrayList();
		extractDataForChart(propertiesByUsageType, plots);	

		BigDecimal total = propertiesByUsageType.stream().map(usageCategory -> usageCategory.getValue()).reduce(BigDecimal.ZERO,
				BigDecimal::add);		 

		return Arrays.asList(Data.builder().headerName("DSS_PT_PROPERTIES_BY_USAGE_TYPE").headerValue(total).plots(plots).build());
	}

	public List<Data> topPerformingUlbsCompletionRate(PayloadDetails payloadDetails) {
		PropertySerarchCriteria criteria = getPropertySearchCriteria(payloadDetails);
		criteria.setExcludedTenantId(DashboardConstants.TESTING_TENANT);
		HashMap<String, Long> slaCompletionCount = ptRepository.getSlaCompletionCountList(criteria);
		HashMap<String, Long> totalApplicationCompletionCount = ptRepository.getTotalApplicationCompletionCountList(criteria);

		List<Chart> percentList = mapTenantsForPerformanceRate(slaCompletionCount, totalApplicationCompletionCount);

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

	public List<Data> bottomPerformingUlbsCompletionRate(PayloadDetails payloadDetails) {
		PropertySerarchCriteria criteria = getPropertySearchCriteria(payloadDetails);
		criteria.setExcludedTenantId(DashboardConstants.TESTING_TENANT);
		HashMap<String, Long> slaCompletionCount = ptRepository.getSlaCompletionCountList(criteria);
		HashMap<String, Long> totalApplicationCompletionCount = ptRepository.getTotalApplicationCompletionCountList(criteria);

		List<Chart> percentList = mapTenantsForPerformanceRate(slaCompletionCount, totalApplicationCompletionCount);

		 Collections.sort(percentList,Comparator.comparing(e -> e.getValue(),(s1,s2)->{
             return s1.compareTo(s2);
         }));

		 List<Data> response = new ArrayList();
		 int Rank = percentList.size();
		 for( Chart obj : percentList) {
			 response.add(Data.builder().headerName("Rank").headerValue(Rank).plots(Arrays.asList(Plot.builder().label("DSS_COMPLETION_RATE").name(obj.getName()).value(obj.getValue()).symbol("percentage").build())).headerSymbol("percentage").build());
			 Rank--;
		 };

		return response;
	}
	
	public List<Data> ptShareOfNewAssessment(PayloadDetails payloadDetails) {
		PropertySerarchCriteria criteria = getPropertySearchCriteria(payloadDetails);
		criteria.setExcludedTenantId(DashboardConstants.TESTING_TENANT);
		HashMap<String, Long> ptTotalAssessmentsTenantwiseCount = ptRepository.getPtTotalAssessmentsTenantwiseCount(criteria);
		HashMap<String, Long> ptTotalNewAssessmentsTenantwiseCount = ptRepository.getPtTotalNewAssessmentsTenantwiseCount(criteria);

		List<Chart> percentList = mapTenantsForPerformanceRate(ptTotalNewAssessmentsTenantwiseCount, ptTotalAssessmentsTenantwiseCount);

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

	public List<Data> ptShareOfReAssessment(PayloadDetails payloadDetails) {
		PropertySerarchCriteria criteria = getPropertySearchCriteria(payloadDetails);
		criteria.setExcludedTenantId(DashboardConstants.TESTING_TENANT);
		HashMap<String, Long> ptTotalAssessmentsTenantwiseCount = ptRepository.getPtTotalAssessmentsTenantwiseCount(criteria);
		HashMap<String, Long> ptTotalNewAssessmentsTenantwiseCount = ptRepository.getPtTotalNewAssessmentsTenantwiseCount(criteria);

		List<Chart> percentList = mapTenantsForSharePerformanceRate(ptTotalNewAssessmentsTenantwiseCount, ptTotalAssessmentsTenantwiseCount);

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
	
	private List<Chart> mapTenantsForSharePerformanceRate(HashMap<String, Long> numeratorMap,
			HashMap<String, Long> denominatorMap) {
		List<Chart> percentList = new ArrayList();
		numeratorMap.entrySet().stream().forEach(item ->{
			Long numerator = item.getValue();
			Long denominator = denominatorMap.get(item.getKey());
			BigDecimal percent = (new BigDecimal(denominator*100).subtract(new BigDecimal(numerator*100))).divide(new BigDecimal(denominator), 2, RoundingMode.HALF_EVEN);
			percentList.add(Chart.builder().name(item.getKey()).value(percent).build());
		});
		return percentList;
	}
	
	public List<Data> slaAchieved(PayloadDetails payloadDetails) {
		PropertySerarchCriteria criteria = getPropertySearchCriteria(payloadDetails);
		criteria.setExcludedTenantId(DashboardConstants.TESTING_TENANT);
		Integer totalApplication = (Integer) ptRepository.getAssessedPropertiesCount(criteria);
		Integer slaAchievedAppCount = (Integer) ptRepository.getSlaAchievedAppCount(criteria);
		return Arrays.asList(Data.builder()
				.headerValue((slaAchievedAppCount.doubleValue() / totalApplication.doubleValue()) * 100).build());
	}
    
	private PropertySerarchCriteria getPropertySearchCriteria(PayloadDetails payloadDetails) {
		PropertySerarchCriteria criteria = new PropertySerarchCriteria();

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
		
//		Long total = 0L;
//		for(Chart item : items) {
//			plots.add(Plot.builder().name(item.getName()).value(item.getValue()).symbol("number").build());
//			total = total + Long.valueOf(String.valueOf(item.getValue()));
//		}
		items.stream().forEach(item ->{
			plots.add(Plot.builder().name(item.getName()).value(item.getValue()).symbol("number").build());
		});
	}
}
