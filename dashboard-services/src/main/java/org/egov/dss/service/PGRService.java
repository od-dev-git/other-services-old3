package org.egov.dss.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import org.egov.dss.config.ConfigurationLoader;
import org.egov.dss.constants.DashboardConstants;
import org.egov.dss.model.BpaSearchCriteria;
import org.egov.dss.model.Chart;
import org.egov.dss.model.PayloadDetails;
import org.egov.dss.model.PaymentSearchCriteria;
import org.egov.dss.model.PgrSearchCriteria;
import org.egov.dss.model.WaterSearchCriteria;
import org.egov.dss.repository.BPARepository;
import org.egov.dss.repository.PGRRepository;
import org.egov.dss.web.model.Data;
import org.egov.dss.web.model.Plot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.google.common.collect.Sets;

@Service
public class PGRService {
	
	@Autowired
	private PGRRepository pgrRepository;
	
	@Autowired
	private BPARepository bpaRepository;
	
	public PgrSearchCriteria getPgrSearchCriteria(PayloadDetails payloadDetails) {
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
		//avoiding divide by zero exception
		if(totalApplication == 0)
			totalApplication = Integer.valueOf(1);
		criteria.setStatus(Sets.newHashSet(DashboardConstants.STATUS_REJECTED.toLowerCase(),
				DashboardConstants.STATUS_RESOLVED.toLowerCase(), DashboardConstants.STATUS_CLOSED.toLowerCase()));	
		Integer slaAchievedAppCount = (Integer) pgrRepository.getSlaAchievedAppCount(criteria);
		return Arrays.asList(Data.builder()
				.headerValue((slaAchievedAppCount.doubleValue() / totalApplication.doubleValue()) * 100).build());
	}
	
	public Integer slaAchievedCount(PayloadDetails payloadDetails) {
		PgrSearchCriteria criteria = getPgrSearchCriteria(payloadDetails);
		criteria.setExcludedTenantId(DashboardConstants.TESTING_TENANT);
		criteria.setStatus(Sets.newHashSet(DashboardConstants.STATUS_REJECTED.toLowerCase(),
				DashboardConstants.STATUS_RESOLVED.toLowerCase(), DashboardConstants.STATUS_CLOSED.toLowerCase()));	
		if(pgrRepository.getSlaAchievedAppCount(criteria) == null) {
			return 0;
		}
		Integer slaAchievedAppCount = (Integer) pgrRepository.getSlaAchievedAppCount(criteria);
		return slaAchievedAppCount;
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
		
		if (CollectionUtils.isEmpty(response)) {
			List<Plot> plots = new ArrayList();
			plots.add(Plot.builder().name("S.N.").label(String.valueOf(serialNumber)).symbol("text").build());
			plots.add(Plot.builder().name("ULBs").label(payloadDetails.getTenantid()).symbol("text").build());
			plots.add(Plot.builder().name("Closed Complaints").value(BigDecimal.ZERO).symbol("number").build());
			plots.add(Plot.builder().name("Reopened Complaints").value(BigDecimal.ZERO).symbol("number").build());
			plots.add(Plot.builder().name("Open Complaints").value(BigDecimal.ZERO).symbol("number").build());
			plots.add(Plot.builder().name("Total Complaints").value(BigDecimal.ZERO).symbol("amount").build());
			plots.add(Plot.builder().name("Completion Rate").value(BigDecimal.ZERO).symbol("percentage").build());
			plots.add(Plot.builder().name("SLA Achieved").value(BigDecimal.ZERO).symbol("percentage").build());
			response.add(Data.builder().headerName(payloadDetails.getTenantid()).plots(plots).headerValue(serialNumber)
					.build());
		}

		return response;
	}
	
	public List<Data> complaintsByStatus(PayloadDetails payloadDetails) {
		PgrSearchCriteria criteria = getPgrSearchCriteria(payloadDetails);
		criteria.setExcludedTenantId(DashboardConstants.TESTING_TENANT);
		List<Chart> ComplaintsByCriteria = pgrRepository.getComplaintsByStatusCriteria(criteria);

		List<Plot> plots = new ArrayList();
		Long total = 0L;
		total = extractDataForChart(ComplaintsByCriteria, plots ,total);		 

		return Arrays.asList(Data.builder().headerName("DSS_PGR_COMPLAINTS_BY_STATUS").headerValue(total).plots(plots).build());
	}


	public List<Data> complaintsByDepartment(PayloadDetails payloadDetails) {
		PgrSearchCriteria criteria = getPgrSearchCriteria(payloadDetails);
		criteria.setExcludedTenantId(DashboardConstants.TESTING_TENANT);
		List<Chart> ComplaintsByCriteria = pgrRepository.getComplaintsByDepartmentCriteria(criteria);

		List<Plot> plots = new ArrayList();
		Long total = 0L;
		total = extractDataForChart(ComplaintsByCriteria, plots ,total);		 

		return Arrays.asList(Data.builder().headerName("DSS_PGR_COMPLAINTS_BY_DEPARTMENT").headerValue(total).plots(plots).build());
	}


	public List<Data> complaintsByChannel(PayloadDetails payloadDetails) {
		PgrSearchCriteria criteria = getPgrSearchCriteria(payloadDetails);
		criteria.setExcludedTenantId(DashboardConstants.TESTING_TENANT);
		List<Chart> ComplaintsByCriteria = pgrRepository.getComplaintsByChannelCriteria(criteria);

		List<Plot> plots = new ArrayList();
		Long total = 0L;
		total = extractDataForChart(ComplaintsByCriteria, plots ,total);		 

		return Arrays.asList(Data.builder().headerName("DSS_PGR_COMPLAINTS_BY_CHANNELS").headerValue(total).plots(plots).build());
	}

	private Long extractDataForChart(List<Chart> items, List<Plot> plots, Long total) {
		for (Chart item : items) {
			plots.add(Plot.builder().name(item.getName()).value(item.getValue()).symbol("number").build());
			total = total + Long.valueOf(String.valueOf(item.getValue()));
		}

		return total ;

	}


	public List<Data> eventDurationGraph(PayloadDetails payloadDetails) {
		List<Data> response = new ArrayList<>();

		//Getting Default Month Year Data
		BpaSearchCriteria criteria = getBpaSearchCriteria(payloadDetails);
		criteria.setExcludedTenantId(DashboardConstants.TESTING_TENANT);
		criteria.setBusinessServices(Sets.newHashSet(DashboardConstants.OBPS_ALL_SERVICES));
		LinkedHashMap<String,BigDecimal> monthYearData= bpaRepository.getMonthYearBigDecimalData(criteria);

		PgrSearchCriteria pgrCriteria = getPgrSearchCriteria(payloadDetails);
		pgrCriteria.setExcludedTenantId(DashboardConstants.TESTING_TENANT);
		List<Chart> eventDurationGraph = pgrRepository.getEventDurationGraph(pgrCriteria);

		List<Plot> plotsForEventDurationGraph = extractedMonthYearData(monthYearData,
				eventDurationGraph);

		BigDecimal total = monthYearData.values().stream().reduce(
				 BigDecimal.ZERO, BigDecimal::add);
		response.add(Data.builder().headerName("Event Average Turn Around Time").headerValue(total).plots(plotsForEventDurationGraph).build());



		return response;
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

	private List<Plot> extractedMonthYearData(LinkedHashMap<String, BigDecimal>  map,
			List<Chart> chart) {
		chart.forEach(item -> {
			if(map.containsKey(item.getName())) {
				BigDecimal value =  item.getValue();
				map.replace(item.getName(), item.getValue());
			}
		});
		List<Plot> plots = new ArrayList();
		map.forEach((key,value) ->{
			plots.add(Plot.builder().name(key).value(value).symbol("number").build());
		});
		return plots;
	}


	public List<Data> uniqueCitizens(PayloadDetails payloadDetails) {
		List<Data> response = new ArrayList<>();

		//Getting Default Month Year Data
		BpaSearchCriteria criteria = getBpaSearchCriteria(payloadDetails);
		criteria.setExcludedTenantId(DashboardConstants.TESTING_TENANT);
		criteria.setBusinessServices(Sets.newHashSet(DashboardConstants.OBPS_ALL_SERVICES));
		LinkedHashMap<String,BigDecimal> monthYearData= bpaRepository.getMonthYearBigDecimalData(criteria);

		PgrSearchCriteria pgrCriteria = getPgrSearchCriteria(payloadDetails);
		pgrCriteria.setExcludedTenantId(DashboardConstants.TESTING_TENANT);
		List<Chart> eventDurationGraph = pgrRepository.getUniqueCitizens(pgrCriteria);

		List<Plot> plotsForEventDurationGraph = extractedMonthYearData(monthYearData,
				eventDurationGraph);

		BigDecimal total = monthYearData.values().stream().reduce(
				 BigDecimal.ZERO, BigDecimal::add);
		response.add(Data.builder().headerName("Closed").headerValue(total).plots(plotsForEventDurationGraph).build());



		return response;
	}


	public List<Data> totalComplaintsByStatus(PayloadDetails payloadDetails) {
		List<Data> response = new ArrayList<>();

		//Getting Default Month Year Data
		BpaSearchCriteria criteria = getBpaSearchCriteria(payloadDetails);
		criteria.setExcludedTenantId(DashboardConstants.TESTING_TENANT);
		criteria.setBusinessServices(Sets.newHashSet(DashboardConstants.OBPS_ALL_SERVICES));
		LinkedHashMap<String,BigDecimal> monthYearDataClosedStatus= bpaRepository.getMonthYearBigDecimalData(criteria);
		LinkedHashMap<String,BigDecimal> monthYearDataOpenStatus= new LinkedHashMap<>();
		monthYearDataOpenStatus.putAll(monthYearDataClosedStatus);


		PgrSearchCriteria pgrCriteria = getPgrSearchCriteria(payloadDetails);
		pgrCriteria.setExcludedTenantId(DashboardConstants.TESTING_TENANT);


		List<Chart> totalOpenedComplaints = pgrRepository.getTotalOpenedComplaintsMonthWise(pgrCriteria);
		List<Plot> plotsForOpenedtatus = extractedMonthYearData(monthYearDataOpenStatus,
				totalOpenedComplaints);
		BigDecimal totalOpenedStatus = monthYearDataOpenStatus.values().stream().reduce(
				 BigDecimal.ZERO, BigDecimal::add);
		response.add(Data.builder().headerName("Open").headerValue(totalOpenedStatus).plots(plotsForOpenedtatus).build());

		List<Chart> totalClosedComplaints = pgrRepository.getTotalClosedComplaintsMonthWise(pgrCriteria);
		List<Plot> plotsForClosedStatus = extractedMonthYearData(monthYearDataClosedStatus,
				totalClosedComplaints);
		BigDecimal totalClosedStatus = monthYearDataClosedStatus.values().stream().reduce(
				 BigDecimal.ZERO, BigDecimal::add);
		response.add(Data.builder().headerName("Closed").headerValue(totalClosedStatus).plots(plotsForClosedStatus).build());

		return response;
	}


	public List<Data> totalComplaintsBySource(PayloadDetails payloadDetails) {
		List<Data> response = new ArrayList<>();

		//Getting Default Month Year Data
		BpaSearchCriteria criteria = getBpaSearchCriteria(payloadDetails);
		criteria.setExcludedTenantId(DashboardConstants.TESTING_TENANT);
		criteria.setBusinessServices(Sets.newHashSet(DashboardConstants.OBPS_ALL_SERVICES));
		LinkedHashMap<String,BigDecimal> monthYearDataMobileApp= bpaRepository.getMonthYearBigDecimalData(criteria);
		LinkedHashMap<String,BigDecimal> monthYearDataIvr  = new LinkedHashMap<>();
		monthYearDataIvr.putAll(monthYearDataMobileApp);
		LinkedHashMap<String,BigDecimal> monthYearDataWeb  = new LinkedHashMap<>();
		monthYearDataWeb.putAll(monthYearDataMobileApp);
		LinkedHashMap<String,BigDecimal> monthYearDataWhatsapp  = new LinkedHashMap<>();
		monthYearDataWhatsapp.putAll(monthYearDataMobileApp);


		PgrSearchCriteria pgrCriteria = getPgrSearchCriteria(payloadDetails);
		pgrCriteria.setExcludedTenantId(DashboardConstants.TESTING_TENANT);


		List<Chart> totalWhatsappComplaints = pgrRepository.totalComplaintsByWhatsapp(pgrCriteria);
		List<Plot> plotsForWhatsappComplaints = extractedMonthYearData(monthYearDataWhatsapp,
				totalWhatsappComplaints);
		BigDecimal totalWhatsapp = monthYearDataWhatsapp.values().stream().reduce(
				 BigDecimal.ZERO, BigDecimal::add);
		response.add(Data.builder().headerName("Whatsapp").headerValue(totalWhatsapp).plots(plotsForWhatsappComplaints).build());

		List<Chart> totalIvrComplaints = pgrRepository.totalComplaintsByIvr(pgrCriteria);
		List<Plot> plotsForIvrComplaints = extractedMonthYearData(monthYearDataIvr,
				totalIvrComplaints);
		BigDecimal totalIvr = monthYearDataIvr.values().stream().reduce(
				 BigDecimal.ZERO, BigDecimal::add);
		response.add(Data.builder().headerName("Ivr").headerValue(totalIvr).plots(plotsForIvrComplaints).build());

		List<Chart> totalWebComplaints = pgrRepository.totalComplaintsByWeb(pgrCriteria);
		List<Plot> plotsForWebComplaints = extractedMonthYearData(monthYearDataWeb,
				totalWebComplaints);
		BigDecimal totalWeb = monthYearDataWeb.values().stream().reduce(
				 BigDecimal.ZERO, BigDecimal::add);
		response.add(Data.builder().headerName("Web").headerValue(totalWeb).plots(plotsForWebComplaints).build());

		List<Chart> totalMobileAppComplaints = pgrRepository.totalComplaintsByMobileApp(pgrCriteria);
		List<Plot> plotsForMobileAppComplaints = extractedMonthYearData(monthYearDataMobileApp,
				totalMobileAppComplaints);
		BigDecimal totalMobileApp = monthYearDataMobileApp.values().stream().reduce(
				 BigDecimal.ZERO, BigDecimal::add);
		response.add(Data.builder().headerName("Mobileapp").headerValue(totalMobileApp).plots(plotsForMobileAppComplaints).build());


		return response;
	}

	public HashMap<String, Long> totalApplicationsTenantWise(PayloadDetails payloadDetails) {
		PgrSearchCriteria criteria = getPgrSearchCriteria(payloadDetails);
		criteria.setExcludedTenantId(DashboardConstants.TESTING_TENANT);
		HashMap<String, Long> totalApplication = pgrRepository.getTenantWiseTotalApplication(criteria);
        return totalApplication;
	}

}
