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
import org.egov.dss.model.PaymentSearchCriteria;
import org.egov.dss.model.TLSearchCriteria;
import org.egov.dss.repository.TLRepository;
import org.egov.dss.web.model.Data;
import org.egov.dss.web.model.Plot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.google.common.collect.Sets;

@Service
public class TLService {
	
	@Autowired
	private TLRepository tlRepository;
	
	public List<Data> totalApplications(PayloadDetails payloadDetails) {
		TLSearchCriteria criteria = getTlSearchCriteria(payloadDetails);
	    criteria.setExcludedTenantId(DashboardConstants.TESTING_TENANT);
	    criteria.setIsApplicationDate(Boolean.TRUE);
		Integer totalApplication =  (Integer) tlRepository.getTotalApplications(criteria);
		return Arrays.asList(Data.builder().headerValue(totalApplication).build());
	}
	
	public List<Data> slaAchieved(PayloadDetails payloadDetails) {
		TLSearchCriteria criteria = getTlSearchCriteria(payloadDetails);
		criteria.setExcludedTenantId(DashboardConstants.TESTING_TENANT);
		Integer totalApplication = (Integer) tlRepository.getTotalApplications(criteria);
		criteria.setStatus(DashboardConstants.STATUS_APPROVED);
		Integer slaAchievedAppCount = (Integer) tlRepository.getSlaAchievedAppCount(criteria);
		return Arrays.asList(Data.builder()
				.headerValue((slaAchievedAppCount.doubleValue() / totalApplication.doubleValue()) * 100).build());
	}
	
	public List<Data> totalNewApplications(PayloadDetails payloadDetails) {
		TLSearchCriteria criteria = getTlSearchCriteria(payloadDetails);
	    criteria.setExcludedTenantId(DashboardConstants.TESTING_TENANT);
	    criteria.setApplicationType(DashboardConstants.APPLICATION_STATUS_NEW);
	    criteria.setIsApplicationDate(Boolean.TRUE);
		Integer totalApplication =  (Integer) tlRepository.getTotalApplications(criteria);
		return Arrays.asList(Data.builder().headerValue(totalApplication).build());
	}
	
	public List<Data> totalRenewalApplications(PayloadDetails payloadDetails) {
		TLSearchCriteria criteria = getTlSearchCriteria(payloadDetails);
	    criteria.setExcludedTenantId(DashboardConstants.TESTING_TENANT);
	    criteria.setApplicationType(DashboardConstants.APPLICATION_STATUS_RENEWAL);
	    criteria.setIsApplicationDate(Boolean.TRUE);
		Integer totalApplication =  (Integer) tlRepository.getTotalApplications(criteria);
		return Arrays.asList(Data.builder().headerValue(totalApplication).build());
	}
	
	public List<Data> licenseIssued(PayloadDetails payloadDetails) {
		TLSearchCriteria criteria = getTlSearchCriteria(payloadDetails);
	    criteria.setExcludedTenantId(DashboardConstants.TESTING_TENANT);
	    criteria.setStatus(DashboardConstants.STATUS_APPROVED);
	    Integer totalApplication =  (Integer) tlRepository.getTotalApplications(criteria);
		return Arrays.asList(Data.builder().headerValue(totalApplication).build());
	}
	
	public List<Data> activeUlbs(PayloadDetails payloadDetails) {
		TLSearchCriteria criteria = getTlSearchCriteria(payloadDetails);
	    criteria.setExcludedTenantId(DashboardConstants.TESTING_TENANT);
	    Integer totalApplication =  (Integer) tlRepository.getTotalActiveUlbs(criteria);
		return Arrays.asList(Data.builder().headerValue(totalApplication).build());
	}
	
	public List<Data> tlSlaComplience(PayloadDetails payloadDetails) {
		TLSearchCriteria criteria = getTlSearchCriteria(payloadDetails);
	    criteria.setExcludedTenantId(DashboardConstants.TESTING_TENANT);
	    criteria.setStatus(DashboardConstants.STATUS_APPROVED);
	    Integer slaAchievedApplication =  (Integer) tlRepository.getTotalApplications(criteria);
		return Arrays.asList(Data.builder().headerValue(slaAchievedApplication).build());
	}
	
	public List<Data> cumulativeLicenseIssued(PayloadDetails payloadDetails) {
		TLSearchCriteria criteria = getTlSearchCriteria(payloadDetails);
		criteria.setExcludedTenantId(DashboardConstants.TESTING_TENANT);
		criteria.setStatus(DashboardConstants.STATUS_APPROVED);
		List<Chart> cumulativeLicenseIssued = tlRepository.getCumulativeLicenseIssued(criteria);
		List<Plot> plots = new ArrayList();
		extractDataForChart(cumulativeLicenseIssued, plots);

		BigDecimal total = cumulativeLicenseIssued.stream().map(usageCategory -> usageCategory.getValue())
				.reduce(BigDecimal.ZERO, BigDecimal::add);

		return Arrays
				.asList(Data.builder().headerName("DSS_TRADE_LICENCE_ISSUED").headerValue(total).plots(plots).build());
	}
	

	public List<Data> topPerformingUlbsCompletionRate(PayloadDetails payloadDetails) {
		TLSearchCriteria criteria = getTlSearchCriteria(payloadDetails);
		criteria.setExcludedTenantId(DashboardConstants.TESTING_TENANT);
		HashMap<String, Long> totalApplication = tlRepository.getTenantWiseTotalApplication(criteria);
		HashMap<String, Long> licenseIssued = tlRepository.getTenantWiseLicenseIssued(criteria);
		List<Chart> percentList = mapTenantsForPerformanceRate(licenseIssued, totalApplication);

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

	public List<Data> bottomPerformingUlbsCompletionRate(PayloadDetails payloadDetails) {
		TLSearchCriteria criteria = getTlSearchCriteria(payloadDetails);
		criteria.setExcludedTenantId(DashboardConstants.TESTING_TENANT);
		HashMap<String, Long> totalApplication = tlRepository.getTenantWiseTotalApplication(criteria);
		HashMap<String, Long> licenseIssued = tlRepository.getTenantWiseLicenseIssued(criteria);

		List<Chart> percentList = mapTenantsForPerformanceRate(licenseIssued, totalApplication);

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
	
	public List<Data> tlApplicationByStatus(PayloadDetails payloadDetails) {
		TLSearchCriteria criteria = getTlSearchCriteria(payloadDetails);
		criteria.setExcludedTenantId(DashboardConstants.TESTING_TENANT);
		List<Chart> propertiesByUsageType = tlRepository.getLicenseByStatus(criteria);
        List<Plot> plots = new ArrayList();
		extractDataForChart(propertiesByUsageType, plots);	

		BigDecimal total = propertiesByUsageType.stream().map(usageCategory -> usageCategory.getValue()).reduce(BigDecimal.ZERO,
				BigDecimal::add);		 

		return Arrays.asList(Data.builder().headerName("DSS_TL_LICENSE_BY_STATUS").headerValue(total).plots(plots).build());
	}
	
	public List<Data> tlStatusByBoundary(PayloadDetails payloadDetails) {
		TLSearchCriteria criteria = getTlSearchCriteria(payloadDetails);
		List<HashMap<String, Object>> tlStatusByBoundary = tlRepository.getTlStatusByBoundary(criteria);

		List<Data> response = new ArrayList();
		int serailNumber = 0;
		for (HashMap<String, Object> tlStatus : tlStatusByBoundary) {
			serailNumber++;
			String tenantId = String.valueOf(tlStatus.get("tenantid"));
			String tenantIdStyled = tenantId.replace("od.", "");
			tenantIdStyled = tenantIdStyled.substring(0, 1).toUpperCase() + tenantIdStyled.substring(1).toLowerCase();
			List<Plot> row = new ArrayList<>();
			row.add(Plot.builder().label(String.valueOf(serailNumber)).name("S.N.").symbol("text").build());
			row.add(Plot.builder().label(tenantIdStyled).name("ULBs").symbol("text").build());

			row.add(Plot.builder().name("Approved").value(new BigDecimal(String.valueOf(tlStatus.get("approvedcnt"))))
					.symbol("number").build());
			row.add(Plot.builder().name("Initiated").value(new BigDecimal(String.valueOf(tlStatus.get("initiatedcnt"))))
					.symbol("number").build());
			row.add(Plot.builder().name("Rejected").value(new BigDecimal(String.valueOf(tlStatus.get("rejectedcnt"))))
					.symbol("number").build());
			row.add(Plot.builder().name("Pending Approval")
					.value(new BigDecimal(String.valueOf(tlStatus.get("approvalpendingcnt")))).symbol("number")
					.build());
			row.add(Plot.builder().name("Field Inspection")
					.value(new BigDecimal(String.valueOf(tlStatus.get("fieldinspectionpendingcnt")))).symbol("number")
					.build());
			row.add(Plot.builder().name("Cancelled").value(new BigDecimal(String.valueOf(tlStatus.get("cancelledcnt"))))
					.symbol("number").build());
			row.add(Plot.builder().name("Pending Payment")
					.value(new BigDecimal(String.valueOf(tlStatus.get("paymentpendingcnt")))).symbol("number").build());
			row.add(Plot.builder().name("Citizen Action Pending")
					.value(new BigDecimal(String.valueOf(tlStatus.get("citizenactionpendingcnt")))).symbol("number")
					.build());
			row.add(Plot.builder().name("Doc Verification Pending")
					.value(new BigDecimal(String.valueOf(tlStatus.get("docverificationpendingcnt")))).symbol("number")
					.build());
			row.add(Plot.builder().name("Expired").value(new BigDecimal(String.valueOf(tlStatus.get("expiredcnt"))))
					.symbol("number").build());

			response.add(Data.builder().headerName(tenantIdStyled).headerValue(serailNumber).plots(row).insight(null)
					.build());
		}

		return response;
	}
	
	
	private TLSearchCriteria getTlSearchCriteria(PayloadDetails payloadDetails) {
		TLSearchCriteria criteria = new TLSearchCriteria();

		if (StringUtils.hasText(payloadDetails.getModulelevel())) {
			if (payloadDetails.getModulelevel().equalsIgnoreCase(DashboardConstants.BS_HOME_REVENUE))
				criteria.setBusinessServices(null);
			else
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

}
