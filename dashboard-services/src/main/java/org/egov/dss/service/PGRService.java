package org.egov.dss.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.egov.dss.config.ConfigurationLoader;
import org.egov.dss.constants.DashboardConstants;
import org.egov.dss.model.Chart;
import org.egov.dss.model.PayloadDetails;
import org.egov.dss.model.PaymentSearchCriteria;
import org.egov.dss.model.PgrSearchCriteria;
import org.egov.dss.repository.PGRRepository;
import org.egov.dss.web.model.Data;
import org.egov.dss.web.model.Plot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.google.common.collect.Sets;

@Service
public class PGRService {
	
	@Autowired
	private PGRRepository pgrRepository;
	
	private PgrSearchCriteria getPgrSearchCriteria(PayloadDetails payloadDetails) {
		PgrSearchCriteria criteria = new PgrSearchCriteria();

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

	public List<Data> totalApplications(PayloadDetails payloadDetails) {
		PgrSearchCriteria criteria = getPgrSearchCriteria(payloadDetails);
		criteria.setExcludedTenantId(DashboardConstants.TESTING_TENANT);
		Integer totalApplication = (Integer) pgrRepository.getTotalApplications(criteria);
		return Arrays.asList(Data.builder().headerValue(totalApplication).build());
	}

	public List<Data> slaAchieved(PayloadDetails payloadDetails) {
		PgrSearchCriteria criteria = getPgrSearchCriteria(payloadDetails);
		criteria.setExcludedTenantId(DashboardConstants.TESTING_TENANT);
		Integer totalApplication = (Integer) pgrRepository.getTotalApplications(criteria);
		if(totalApplication == 0)
			return Arrays.asList(Data.builder().headerValue(0.0).build());
		criteria.setStatus(Sets.newHashSet(DashboardConstants.STATUS_REJECTED.toLowerCase(),
				DashboardConstants.STATUS_RESOLVED.toLowerCase(), DashboardConstants.STATUS_CLOSED.toLowerCase()));	
		Integer slaAchievedAppCount = (Integer) pgrRepository.getSlaAchievedAppCount(criteria);
		return Arrays.asList(Data.builder()
				.headerValue((slaAchievedAppCount.doubleValue() / totalApplication.doubleValue()) * 100).build());
	}

	public List<Data> closedApplications(PayloadDetails payloadDetails) {
		PgrSearchCriteria criteria = getPgrSearchCriteria(payloadDetails);
		criteria.setExcludedTenantId(DashboardConstants.TESTING_TENANT);
		criteria.setStatus(Sets.newHashSet(DashboardConstants.STATUS_REJECTED.toLowerCase(),
				DashboardConstants.STATUS_RESOLVED.toLowerCase(), DashboardConstants.STATUS_CLOSED.toLowerCase()));
		Integer closedApplications = (Integer) pgrRepository.getTotalApplications(criteria);
		return Arrays.asList(Data.builder().headerValue(closedApplications).build());
	}

	public List<Data> pgrCompletionRate(PayloadDetails payloadDetails) {
		
		PgrSearchCriteria criteria = getPgrSearchCriteria(payloadDetails);
		criteria.setExcludedTenantId(DashboardConstants.TESTING_TENANT);
		Integer totalApplication = (Integer) pgrRepository.getTotalApplications(criteria);
		
		criteria.setStatus(Sets.newHashSet(DashboardConstants.STATUS_REJECTED.toLowerCase(),
				DashboardConstants.STATUS_RESOLVED.toLowerCase(), DashboardConstants.STATUS_CLOSED.toLowerCase()));
		Integer closedApplications = (Integer) pgrRepository.getTotalApplications(criteria);
		
		//avoiding divide by zero exception
		if(totalApplication == 0)
			totalApplication = 1;
			
		Double completionRate = (double) ((closedApplications * 100)/totalApplication);
		
		return Arrays.asList(Data.builder().headerValue(completionRate).build());
	}

	public List<Data> pgrCumulativeClosedComplaints(PayloadDetails payloadDetails) {
		
		List<Data> response = new ArrayList<>();
		
		PgrSearchCriteria criteria = getPgrSearchCriteria(payloadDetails);
		criteria.setExcludedTenantId(DashboardConstants.TESTING_TENANT);
		
		List<Chart> monthYearData = pgrRepository.getMonthYearData(criteria);
		
		List<Chart> monthYearDataForClosed = monthYearData;
		
		List<Chart> monthYearDataForReOpened = monthYearData;
		
		List<Chart> totalApplications = pgrRepository.getCumulativeComplaints(criteria);
		
		List<Plot> plotsForTotalApplications = extractDataForChartComplaints(totalApplications, monthYearData);
		
		BigDecimal total = totalApplications.stream().map(application -> application.getValue()).reduce(BigDecimal.ZERO,
				BigDecimal::add);	
		
		response.add(Data.builder().headerName("Total Complaints").headerSymbol("number").headerValue(total).plots(plotsForTotalApplications).build());
		
		criteria.setStatus(Sets.newHashSet(DashboardConstants.STATUS_REJECTED.toLowerCase(),
				DashboardConstants.STATUS_RESOLVED.toLowerCase(), DashboardConstants.STATUS_CLOSED.toLowerCase()));
		
		List<Chart> closedApplications = pgrRepository.getCumulativeComplaints(criteria);
		
		List<Plot> plotsForClosedApplications = extractDataForChartComplaints(closedApplications, monthYearDataForClosed);
		
		BigDecimal totalClosedApplications = closedApplications.stream().map(application -> application.getValue())
				.reduce(BigDecimal.ZERO, BigDecimal::add);

		response.add(Data.builder().headerName("Closed Complaints").headerSymbol("number").headerValue(totalClosedApplications).plots(plotsForClosedApplications).build());
		
		
		criteria.setStatus(Sets.newHashSet(DashboardConstants.STATUS_REOPEN.toLowerCase()));
		
		List<Chart> reOpenedApplications = pgrRepository.getCumulativeComplaints(criteria);
		
		List<Plot> plotsForReOpenedApplications = extractDataForChartComplaints(reOpenedApplications, monthYearDataForReOpened);
		
		BigDecimal totalReOpenedApplications = reOpenedApplications.stream().map(application -> application.getValue())
				.reduce(BigDecimal.ZERO, BigDecimal::add);
		
		response.add(Data.builder().headerName("Reopened Complaints").headerSymbol("number").headerValue(totalReOpenedApplications).plots(plotsForReOpenedApplications).build());
		
		return response;
	}

	public List<Data> pgrTopComplaints(PayloadDetails payloadDetails) {
		
		PgrSearchCriteria criteria = getPgrSearchCriteria(payloadDetails);
		criteria.setExcludedTenantId(DashboardConstants.TESTING_TENANT);
		
		List<Chart> topFiveComplaints = pgrRepository.getTopFiveComplaints(criteria);

		List<Plot> plots = new ArrayList<Plot>();
		extractDataForChart(topFiveComplaints, plots);

		BigDecimal total = topFiveComplaints.stream().map(complaints -> complaints.getValue()).reduce(BigDecimal.ZERO,
				BigDecimal::add);

		return Arrays
				.asList(Data.builder().headerName("DSS_PGR_TOP_COMPLAINTS").headerValue(total).plots(plots).build());
	}
	
	private List<Plot> extractDataForChartComplaints(List<Chart> complaintsData, List<Chart> monthYearData) {
		
		BigDecimal commulativeValue = BigDecimal.ZERO;
		
		for(Chart monthYear : monthYearData) {		
			BigDecimal value = complaintsData.stream()
					.filter(complaint -> complaint.getName().equals(monthYear.getName())).map(item -> item.getValue())
					.reduce(BigDecimal.ZERO, BigDecimal::add);
			monthYear.setValue(commulativeValue.add(value));
			commulativeValue = commulativeValue.add(value);
		}
		
		List<Plot> plotsForComplaintsData = new ArrayList();
		monthYearData.forEach((key) -> {
			plotsForComplaintsData
					.add(Plot.builder().name(key.getName()).value(key.getValue()).symbol("number").build());
		});
		return plotsForComplaintsData;

	}
	
	private void extractDataForChart(List<Chart> items, List<Plot> plots) {
		items.stream().forEach(item ->{
			plots.add(Plot.builder().name(item.getName()).value(item.getValue()).symbol("number").build());
		});
	}

	public List<Data> pgrStatusByDDR(PayloadDetails payloadDetails) {
		
		PgrSearchCriteria criteria = getPgrSearchCriteria(payloadDetails);
		criteria.setExcludedTenantId(DashboardConstants.TESTING_TENANT);
		//set criteria for Closed Applications
		criteria.setStatus(Sets.newHashSet(DashboardConstants.STATUS_REJECTED.toLowerCase(),
				DashboardConstants.STATUS_RESOLVED.toLowerCase(), DashboardConstants.STATUS_CLOSED.toLowerCase()));
		
		HashMap<String, BigDecimal> tenantWiseClosedComplaints = pgrRepository.getTenantWiseApplications(criteria);
		
		//set criteria for Reopen Applications
		criteria.setStatus(Sets.newHashSet(DashboardConstants.STATUS_REOPEN.toLowerCase()));
		
		HashMap<String, BigDecimal> tenantWiseReOpenedComplaints = pgrRepository.getTenantWiseApplications(criteria);
		
		//set criteria for Applications other than Closed Applications basically all open applications
		criteria.setStatus(null);
		criteria.setStatusNotIn(Sets.newHashSet(DashboardConstants.STATUS_REJECTED.toLowerCase(),
				DashboardConstants.STATUS_RESOLVED.toLowerCase(), DashboardConstants.STATUS_CLOSED.toLowerCase()));
		
		HashMap<String, BigDecimal> tenantWiseOpenedComplaints = pgrRepository.getTenantWiseApplications(criteria);
		
		//set criteria for Total Applications
		criteria.setStatusNotIn(null);
		
		HashMap<String, BigDecimal> tenantWiseTotalComplaints = pgrRepository.getTenantWiseApplications(criteria);
		
		HashMap<String, BigDecimal> tenantWiseCompletionRate =  new HashMap<>();
		
		tenantWiseTotalComplaints.forEach((key, value) -> {

			if (tenantWiseClosedComplaints.containsKey(key)) {
				BigDecimal closedComplaints = tenantWiseClosedComplaints.get(key);
				BigDecimal completionRate = closedComplaints.multiply(new BigDecimal(100)).divide(value, 2,
						RoundingMode.HALF_UP);
				tenantWiseCompletionRate.put(key, completionRate);
			}
		});
		
		criteria.setStatus(Sets.newHashSet(DashboardConstants.STATUS_REJECTED.toLowerCase(),
				DashboardConstants.STATUS_RESOLVED.toLowerCase(), DashboardConstants.STATUS_CLOSED.toLowerCase()));	
		HashMap<String, BigDecimal> tenantWiseSlaAchieved = pgrRepository.getTenantWiseSlaAchieved(criteria);
		
		HashMap<String, BigDecimal> tenantWiseSlaAchievedPercentage =  new HashMap<>();
		
		tenantWiseTotalComplaints.forEach((key, value) -> {

			if (tenantWiseSlaAchieved.containsKey(key)) {
				BigDecimal slaAchieved = tenantWiseSlaAchieved.get(key);
				BigDecimal slaAchievedPercentage = slaAchieved.multiply(new BigDecimal(100)).divide(value, 2,
						RoundingMode.HALF_UP);
				tenantWiseSlaAchievedPercentage.put(key, slaAchievedPercentage);
			}
		});
		
		List<Data> response = new ArrayList<>();
		int serialNumber = 1;

		for (HashMap.Entry<String, BigDecimal> tenantWiseComplaint : tenantWiseTotalComplaints.entrySet()) {
			List<Plot> plots = new ArrayList();
			plots.add(Plot.builder().name("S.N.").label(String.valueOf(serialNumber)).symbol("text").build());

			plots.add(
					Plot.builder().name("ULBs").label(tenantWiseComplaint.getKey().toString()).symbol("text").build());

			plots.add(
					Plot.builder().name("Closed Complaints")
							.value(tenantWiseClosedComplaints.get(tenantWiseComplaint.getKey()) == null ? BigDecimal.ZERO
									: tenantWiseClosedComplaints.get(tenantWiseComplaint.getKey()))
							.symbol("number").build());

			plots.add(Plot.builder().name("Reopened Complaints")
					.value(tenantWiseReOpenedComplaints.get(tenantWiseComplaint.getKey()) == null ? BigDecimal.ZERO
							: tenantWiseReOpenedComplaints.get(tenantWiseComplaint.getKey()))
					.symbol("number").build());
			
			plots.add(Plot.builder().name("Open Complaints")
					.value(tenantWiseOpenedComplaints.get(tenantWiseComplaint.getKey()) == null ? BigDecimal.ZERO
							: tenantWiseOpenedComplaints.get(tenantWiseComplaint.getKey()))
					.symbol("number").build());
			
			plots.add(Plot.builder().name("Total Complaints").value(tenantWiseComplaint.getValue()).symbol("amount")
					.build());
			
			plots.add(Plot.builder().name("Completion Rate")
					.value(tenantWiseCompletionRate.get(tenantWiseComplaint.getKey()) == null ? BigDecimal.ZERO
							: tenantWiseCompletionRate.get(tenantWiseComplaint.getKey()))
					.symbol("percentage").build());
			
			plots.add(Plot.builder().name("SLA Achieved")
					.value(tenantWiseSlaAchievedPercentage.get(tenantWiseComplaint.getKey()) == null ? BigDecimal.ZERO
							: tenantWiseSlaAchievedPercentage.get(tenantWiseComplaint.getKey()))
					.symbol("percentage").build());

			response.add(Data.builder().headerName(tenantWiseComplaint.getKey()).plots(plots).headerValue(serialNumber)
					.headerName(tenantWiseComplaint.getKey()).build());

			serialNumber++;

		}
		return response;
	}

}
