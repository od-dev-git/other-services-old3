package org.egov.dss.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import org.egov.dss.constants.DashboardConstants;
import org.egov.dss.model.BpaSearchCriteria;
import org.egov.dss.model.Chart;
import org.egov.dss.model.MarriageSearchCriteria;
import org.egov.dss.model.BpaSearchCriteria;
import org.egov.dss.model.Chart;
import org.egov.dss.model.CommonSearchCriteria;
import org.egov.dss.model.PayloadDetails;
import org.egov.dss.model.PgrSearchCriteria;
import org.egov.dss.model.PropertySerarchCriteria;
import org.egov.dss.model.TLSearchCriteria;
import org.egov.dss.model.WaterSearchCriteria;
import org.egov.dss.repository.BPARepository;
import org.egov.dss.repository.CommonRepository;
import org.egov.dss.repository.MRRepository;
import org.egov.dss.model.PropertySerarchCriteria;
import org.egov.dss.repository.BPARepository;
import org.egov.dss.repository.CommonServiceRepository;
import org.egov.dss.repository.PGRRepository;
import org.egov.dss.repository.TLRepository;
import org.egov.dss.repository.WSRepository;
import org.egov.dss.repository.PTRepository;
import org.egov.dss.web.model.Data;
import org.egov.dss.web.model.Plot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.Sets;
import org.springframework.util.StringUtils;

import com.google.common.collect.Sets;

@Service
public class CommonService {

	@Autowired
	private PGRService pgrService;

	@Autowired
	private PTService ptService;

	@Autowired
	private WSService wsService;

	@Autowired
	private TLService tlService;

	@Autowired
	private MRService mrService;

	@Autowired
	private BPAService bpaService;
	
	@Autowired
	private PGRRepository pgrRepository;
	
	@Autowired
	private WSRepository wsRepository;
	
	@Autowired
	private TLRepository tlRepository;
	
	@Autowired
	private MRRepository mrRepository;
	
	@Autowired
	private BPARepository bpaRepository;
	
	@Autowired
	private CommonRepository commonRepository;

	@Autowired
	private CommonServiceRepository csRepository;

	public List<Data> slaAchieved(PayloadDetails payloadDetails) {

		payloadDetails.setModulelevel(DashboardConstants.MODULE_LEVEL_PGR);
		Integer pgrSla = pgrService.slaAchievedCount(payloadDetails);
		payloadDetails.setModulelevel(DashboardConstants.MODULE_LEVEL_PT);
		Integer ptSla = ptService.slaAchievedCount(payloadDetails);
		payloadDetails.setModulelevel(DashboardConstants.MODULE_LEVEL_OBPS);
		Integer bpaSla = bpaService.slaAchievedCount(payloadDetails);
		payloadDetails.setModulelevel(DashboardConstants.BUSINESS_SERVICE_WS);
		Integer wsSla = wsService.slaAchievedCount(payloadDetails);
		payloadDetails.setModulelevel(DashboardConstants.BUSINESS_SERVICE_TL);
		Integer tlSla = tlService.slaAchievedCount(payloadDetails);
		payloadDetails.setModulelevel(DashboardConstants.BUSINESS_SERVICE_MR);
		Integer mrSla = mrService.slaAchievedCounts(payloadDetails);

		// change it
		List<Data> totalApplicationsList = totalApplication(payloadDetails);
		Double totalApplications = new Double(totalApplicationsList.get(0).getHeaderValue().toString());
		Double slaAchieved = ((pgrSla + ptSla + bpaSla + wsSla + tlSla + mrSla) * 100) / totalApplications;

		return Arrays.asList(Data.builder().headerValue(slaAchieved).build());
	}

	public List<Data> totalApplication(PayloadDetails payloadDetails) {
		payloadDetails.setModulelevel(DashboardConstants.MODULE_LEVEL_PGR);
		Integer pgrCount = (Integer) pgrService.totalApplications(payloadDetails).get(0).getHeaderValue();
		payloadDetails.setModulelevel(DashboardConstants.MODULE_LEVEL_PT);
		Integer ptCount = (Integer) ptService.ptTotalApplications(payloadDetails).get(0).getHeaderValue();
		payloadDetails.setModulelevel(DashboardConstants.MODULE_LEVEL_OBPS);
		Integer bpaCount = (Integer) bpaService.totalApplicationsReceived(payloadDetails).get(0).getHeaderValue();
		payloadDetails.setModulelevel(DashboardConstants.MODULE_LEVEL_OBPS);
		Integer bpaOcCount = (Integer) bpaService.totalOcApplicationsReceived(payloadDetails).get(0).getHeaderValue();
		payloadDetails.setModulelevel(DashboardConstants.BUSINESS_SERVICE_WS);
		Integer wsCount = (Integer) wsService.wsTotalApplications(payloadDetails).get(0).getHeaderValue();
		payloadDetails.setModulelevel(DashboardConstants.BUSINESS_SERVICE_TL);
		Integer tlCount = (Integer) tlService.totalApplications(payloadDetails).get(0).getHeaderValue();
		payloadDetails.setModulelevel(DashboardConstants.BUSINESS_SERVICE_MR);
		Integer mrCount = (Integer) mrService.totalApplications(payloadDetails).get(0).getHeaderValue();
		Integer totalApplications = (pgrCount + ptCount + bpaCount + bpaOcCount + wsCount + tlCount + mrCount);

		return Arrays.asList(Data.builder().headerValue(totalApplications).build());
	}

	public List<Data> citizenRegistered(PayloadDetails payloadDetails) {
		CommonSearchCriteria criteria = getCommonSearchCriteria(payloadDetails);
		Integer citizensRegistered = (Integer) csRepository.totalCitizensRegistered(criteria);
		return Arrays.asList(Data.builder().headerValue(citizensRegistered).build());
	}

	private CommonSearchCriteria getCommonSearchCriteria(PayloadDetails payloadDetails) {
		CommonSearchCriteria criteria = new CommonSearchCriteria();

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

	public List<Data> totalClosedApplications(PayloadDetails payloadDetails) {
		CommonSearchCriteria criteria = getCommonSearchCriteria(payloadDetails);

		criteria.setExcludedTenantId(DashboardConstants.TESTING_TENANT);
		criteria.setStatus(Sets.newHashSet(DashboardConstants.STATUS_ACTIVE));
		Integer ptTotal = (Integer) csRepository.ptTotalCompletionCount(criteria);

		criteria.setStatus(Sets.newHashSet(DashboardConstants.WS_CONNECTION_ACTIVATED));
		Integer wsTotal = (Integer) csRepository.wsTotalCompletionCount(criteria);

		criteria.setStatus(Sets.newHashSet(DashboardConstants.STATUS_APPROVED));
		criteria.setBusinessServices(Sets.newHashSet(DashboardConstants.OBPS_ALL_BUSINESS_SERVICES));
		Integer permitTotal = (Integer) csRepository.permitTotalCompletionCount(criteria);

		criteria.setBusinessServices(Sets.newHashSet(DashboardConstants.OBPS_OC_BUSINESS_SERVICES));
		Integer ocTotal = (Integer) csRepository.ocTotalCompletionCount(criteria);

		criteria.setStatus(Sets.newHashSet(DashboardConstants.STATUS_APPROVED));
		criteria.setBusinessServices(Sets.newHashSet(DashboardConstants.BUSINESS_SERVICE_TL));
		Integer tlTotal = (Integer) csRepository.tlTotalCompletionCount(criteria);

		criteria.setBusinessServices(null);
		Integer mrTotal = (Integer) csRepository.mrTotalCompletionCount(criteria);

		criteria.setBusinessServices(null);
		criteria.setStatus(Sets.newHashSet(DashboardConstants.PGR_REQUEST_COMPLETED_STATUS));
		Integer pgrTotal = (Integer) csRepository.pgrTotalCompletionCount(criteria);

		Integer totalApplications = (ptTotal + wsTotal + permitTotal + ocTotal + tlTotal + mrTotal + pgrTotal);

		return Arrays.asList(Data.builder().headerValue(totalApplications).build());
	}

	public List<Data> totalApplcationsServiceWise(PayloadDetails payloadDetails) {
		payloadDetails.setModulelevel(DashboardConstants.MODULE_LEVEL_PGR);
		Integer pgrCount = (Integer) pgrService.totalApplications(payloadDetails).get(0).getHeaderValue();
		payloadDetails.setModulelevel(DashboardConstants.MODULE_LEVEL_PT);
		Integer ptCount = (Integer) ptService.ptTotalApplications(payloadDetails).get(0).getHeaderValue();
		payloadDetails.setModulelevel(DashboardConstants.MODULE_LEVEL_OBPS);
		Integer bpaCount = (Integer) bpaService.totalPermitIssued(payloadDetails).get(0).getHeaderValue();
		payloadDetails.setModulelevel(DashboardConstants.MODULE_LEVEL_OBPS);
		Integer bpaOcCount = (Integer) bpaService.totalOcApplicationsReceived(payloadDetails).get(0).getHeaderValue();
		payloadDetails.setModulelevel(DashboardConstants.BUSINESS_SERVICE_WS);
		Integer wsCount = (Integer) wsService.wsTotalApplications(payloadDetails).get(0).getHeaderValue();
		payloadDetails.setModulelevel(DashboardConstants.BUSINESS_SERVICE_TL);
		Integer tlCount = (Integer) tlService.totalApplications(payloadDetails).get(0).getHeaderValue();
		payloadDetails.setModulelevel(DashboardConstants.BUSINESS_SERVICE_MR);
		Integer mrCount = (Integer) mrService.totalApplications(payloadDetails).get(0).getHeaderValue();

		List<Chart> totalApplcationsServiceWiseChart = new ArrayList<>();

		Chart pgr = Chart.builder().name("PGR Service").value(new BigDecimal(pgrCount)).build();
		totalApplcationsServiceWiseChart.add(pgr);
		Chart pt = Chart.builder().name("Property Tax").value(new BigDecimal(ptCount)).build();
		totalApplcationsServiceWiseChart.add(pt);
		Chart bpa = Chart.builder().name("BPA Permit").value(new BigDecimal(bpaCount)).build();
		totalApplcationsServiceWiseChart.add(bpa);
		Chart bpaOc = Chart.builder().name("BPA OC").value(new BigDecimal(bpaOcCount)).build();
		totalApplcationsServiceWiseChart.add(bpaOc);
		Chart ws = Chart.builder().name("Water Service").value(new BigDecimal(wsCount)).build();
		totalApplcationsServiceWiseChart.add(ws);
		Chart tl = Chart.builder().name("Trade License").value(new BigDecimal(tlCount)).build();
		totalApplcationsServiceWiseChart.add(tl);
		Chart mr = Chart.builder().name("Marriage Registration").value(new BigDecimal(mrCount)).build();
		totalApplcationsServiceWiseChart.add(mr);

		List<Plot> plots = new ArrayList();
		Long total = 0L;
		total = extractDataForChart(totalApplcationsServiceWiseChart, plots, total);
		return Arrays.asList(Data.builder().headerName("DSS_TOTAL_APPLICATIONS:_DEPARTMENT_WISE").headerValue(total)
				.plots(plots).build());
	}

	private Long extractDataForChart(List<Chart> items, List<Plot> plots, Long total) {
		for (Chart item : items) {
			plots.add(Plot.builder().name(item.getName()).value(item.getValue()).symbol("number").build());
			total = total + Long.valueOf(String.valueOf(item.getValue()));
		}

		return total;

	}

	public List<Data> topPerformingULBsCompletionRate(PayloadDetails payloadDetails) {
		payloadDetails.setModulelevel(DashboardConstants.MODULE_LEVEL_PGR);
		HashMap<String, Long> pgrCount = pgrService.totalApplicationsTenantWise(payloadDetails);

		payloadDetails.setModulelevel(DashboardConstants.MODULE_LEVEL_PT);
		HashMap<String, Long> ptCount = ptService.totalApplicationsTenantWise(payloadDetails);

		payloadDetails.setModulelevel(DashboardConstants.MODULE_LEVEL_OBPS);
		HashMap<String, Long> bpaCount = bpaService.totalApplicationsTenantWise(payloadDetails);

		payloadDetails.setModulelevel(DashboardConstants.MODULE_LEVEL_OBPS);
		HashMap<String, Long> bpaOcCount = bpaService.totalBPAOcApplicationsTenantWise(payloadDetails);

		payloadDetails.setModulelevel(DashboardConstants.BUSINESS_SERVICE_WS);
		HashMap<String, Long> wsCount = wsService.totalApplicationsTenantWise(payloadDetails);

		payloadDetails.setModulelevel(DashboardConstants.BUSINESS_SERVICE_TL);
		HashMap<String, Long> tlCount = tlService.totalApplicationsTenantWise(payloadDetails);

		payloadDetails.setModulelevel(DashboardConstants.BUSINESS_SERVICE_MR);
		HashMap<String, Long> mrCount = mrService.totalApplicationsTenantWise(payloadDetails);

		HashMap<String, Long> totalApplicationMap = new HashMap<>();

		wsCount.forEach((k, v) -> {
			System.out.format("key: %s, value: %d%n", k, v);

			Long wsValue = 0L;
			if (wsCount.get(k) != null) {
				wsValue = wsCount.get(k);
			}
			Long pgrValue = 0L;
			if (pgrCount.get(k) != null) {
				pgrValue = pgrCount.get(k);
			}
			Long ptValue = 0L;
			if (ptCount.get(k) != null) {
				ptValue = ptCount.get(k);
			}
			Long bpaValue = 0L;
			if (bpaCount.get(k) != null) {
				bpaValue = bpaCount.get(k);
			}
			Long bpaOcValue = 0L;
			if (bpaOcCount.get(k) != null) {
				bpaOcValue = bpaOcCount.get(k);
			}
			Long tlValue = 0L;
			if (tlCount.get(k) != null) {
				tlValue = tlCount.get(k);
			}
			Long mrValue = 0L;
			if (mrCount.get(k) != null) {
				mrValue = mrCount.get(k);
			}

			Long total = wsValue + pgrValue + ptValue + bpaValue + bpaOcValue + tlValue + mrValue;
			totalApplicationMap.put(k, total);
		});

		// Total Completion Count Tenantwise

		CommonSearchCriteria criteria = getCommonSearchCriteria(payloadDetails);

		payloadDetails.setModulelevel(DashboardConstants.MODULE_LEVEL_PT);
		criteria.setExcludedTenantId(DashboardConstants.TESTING_TENANT);

		payloadDetails.setModulelevel(DashboardConstants.MODULE_LEVEL_PT);
		HashMap<String, Long> ptCompletedCount = ptService.totalCompletedApplicationsTenantWise(payloadDetails);

		payloadDetails.setModulelevel(DashboardConstants.MODULE_LEVEL_OBPS);
		HashMap<String, Long> bpaCompletedCount = bpaService.bpaTotalApplicationsTenantWise(payloadDetails);

		payloadDetails.setModulelevel(DashboardConstants.MODULE_LEVEL_OBPS);
		HashMap<String, Long> bpaOcCompletedCount = bpaService.bpaOcTotalApplicationsTenantWise(payloadDetails);

		payloadDetails.setModulelevel(DashboardConstants.BUSINESS_SERVICE_WS);
		HashMap<String, Long> wsCompletedTotal = wsService.wsTotalCompletedApplicationsTenantWise(payloadDetails);

		criteria.setStatus(Sets.newHashSet(DashboardConstants.STATUS_APPROVED));
		criteria.setBusinessServices(Sets.newHashSet(DashboardConstants.BUSINESS_SERVICE_TL));
		payloadDetails.setModulelevel(DashboardConstants.BUSINESS_SERVICE_TL);
		HashMap<String, Long> tlCompletedCount = csRepository.tlTotalCompletedApplicationsTenantWise(criteria);

		payloadDetails.setModulelevel(DashboardConstants.BUSINESS_SERVICE_MR);
		HashMap<String, Long> mrCompletedCount = mrService.totalCompletedApplicationsTenantWise(payloadDetails);

		payloadDetails.setModulelevel(DashboardConstants.MODULE_LEVEL_PGR);
		criteria.setBusinessServices(null);
		criteria.setStatus(Sets.newHashSet(DashboardConstants.PGR_REQUEST_COMPLETED_STATUS));
		criteria.setExcludedTenantId(DashboardConstants.TESTING_TENANT);
		HashMap<String, Long> pgrCompletedCount = csRepository.pgrTotalApplicationsTenantWise(criteria);

		HashMap<String, Long> totalApplicationCompletionMap = new HashMap<>();

		wsCompletedTotal.forEach((k, v) -> {
			System.out.format("key: %s, value: %d%n", k, v);

			Long wsValue = 0L;
			if (wsCompletedTotal.get(k) != null) {
				wsValue = wsCompletedTotal.get(k);
			}
			Long pgrValue = 0L;
			if (pgrCompletedCount.get(k) != null) {
				pgrValue = pgrCompletedCount.get(k);
			}
			Long ptValue = 0L;
			if (ptCompletedCount.get(k) != null) {
				ptValue = ptCompletedCount.get(k);
			}
			Long bpaValue = 0L;
			if (bpaCompletedCount.get(k) != null) {
				bpaValue = bpaCompletedCount.get(k);
			}
			Long bpaOcValue = 0L;
			if (bpaOcCompletedCount.get(k) != null) {
				bpaOcValue = bpaOcCompletedCount.get(k);
			}
			Long tlValue = 0L;
			if (tlCompletedCount.get(k) != null) {
				tlValue = tlCompletedCount.get(k);
			}
			Long mrValue = 0L;
			if (mrCompletedCount.get(k) != null) {
				mrValue = mrCompletedCount.get(k);
			}

			Long totalCompletion = wsValue + pgrValue + ptValue + bpaValue + bpaOcValue + tlValue + mrValue;
			totalApplicationCompletionMap.put(k, totalCompletion);
		});


		List<Chart> percentList = mapTenantsForPerformanceRate(totalApplicationCompletionMap, totalApplicationMap);

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
	
	public List<Data> totalApplicationAndClosed(PayloadDetails payloadDetails) {

		//Get PGR applications here
		payloadDetails.setModulelevel(DashboardConstants.MODULE_LEVEL_PGR);
		PgrSearchCriteria criteriaPGR = pgrService.getPgrSearchCriteria(payloadDetails);
		criteriaPGR.setExcludedTenantId(DashboardConstants.TESTING_TENANT);	
		List<Chart> totalPGRApplications = pgrRepository.getCumulativeComplaints(criteriaPGR);
		
		criteriaPGR.setStatus(Sets.newHashSet(DashboardConstants.STATUS_REJECTED.toLowerCase(),
				DashboardConstants.STATUS_RESOLVED.toLowerCase(), DashboardConstants.STATUS_CLOSED.toLowerCase()));
		
		List<Chart> closedPGRApplications = pgrRepository.getCumulativeComplaints(criteriaPGR);
		
		//Get PT applications here
		payloadDetails.setModulelevel(DashboardConstants.MODULE_LEVEL_PT);
		PropertySerarchCriteria criteriaProperty = ptService.getPropertySearchCriteria(payloadDetails);
		criteriaProperty.setExcludedTenantId(DashboardConstants.TESTING_TENANT);	
		
		List<Chart> totalPropertyApplications = commonRepository.getTotalProperties(criteriaProperty);
		criteriaProperty.setStatus(DashboardConstants.STATUS_ACTIVE);
		List<Chart> totalPropertyClosedApplications = commonRepository.getTotalProperties(criteriaProperty);
		
		//Get WS applications here
		payloadDetails.setModulelevel(DashboardConstants.BUSINESS_SERVICE_WS);
		WaterSearchCriteria criteriaWS = wsService.getWaterSearchCriteria(payloadDetails);
		criteriaWS.setExcludedTenantId(DashboardConstants.TESTING_TENANT);
		
		List<Chart> totalWSApplications = wsRepository.getCumulativeConnections(criteriaWS);
		
		criteriaWS.setStatus(DashboardConstants.WS_CONNECTION_ACTIVATED);		
		
		List<Chart> closedWSApplications = wsRepository.getCumulativeConnections(criteriaWS);
		
		//Get TL applications here
		payloadDetails.setModulelevel(DashboardConstants.BUSINESS_SERVICE_TL);
		TLSearchCriteria criteriaTL = tlService.getTlSearchCriteria(payloadDetails);
		criteriaTL.setExcludedTenantId(DashboardConstants.TESTING_TENANT);
		
		List<Chart> totalTLApplications = tlRepository.getCumulativeLicenseIssued(criteriaTL);
		
		criteriaTL.setStatus(DashboardConstants.STATUS_APPROVED);
		List<Chart> totalTLApprovedApplications = tlRepository.getCumulativeLicenseIssued(criteriaTL);
		
		//Get MR applications here
		payloadDetails.setModulelevel(DashboardConstants.BUSINESS_SERVICE_MR);
		MarriageSearchCriteria criteriaMR = mrService.getMarriageSearchCriteria(payloadDetails);
		criteriaMR.setExcludedTenantId(DashboardConstants.TESTING_TENANT);
		
		List<Chart> totalMRApplications = mrRepository.getCumulativeApplications(criteriaMR);
		
		criteriaMR.setStatus(DashboardConstants.STATUS_APPROVED);
		List<Chart> closedMRApplications = mrRepository.getCumulativeApplications(criteriaMR);
		
		//Get BPA applications here
		payloadDetails.setModulelevel(DashboardConstants.MODULE_LEVEL_OBPS);
		BpaSearchCriteria criteriaBPA = bpaService.getBpaSearchCriteria(payloadDetails);
		criteriaBPA.setExcludedTenantId(DashboardConstants.TESTING_TENANT);
		criteriaBPA.setBusinessServices(Sets.newHashSet(DashboardConstants.OBPS_ALL_SERVICES));
		
		criteriaBPA.setBusinessServices(Sets.newHashSet(DashboardConstants.OBPS_ALL_BUSINESS_SERVICES));
		List<Chart> totalBpaApplications = bpaRepository.getTotalPermitsIssuedVsTotalOcIssuedVsTotalOcSubmitted(criteriaBPA);
		
		criteriaBPA.setStatus(Sets.newHashSet(DashboardConstants.STATUS_APPROVED));
		List<Chart> totalClosedBpaApplications = bpaRepository.getTotalPermitsIssuedVsTotalOcIssuedVsTotalOcSubmitted(criteriaBPA);
		
		criteriaBPA.setStatus(null);
		criteriaBPA.setBusinessServices(Sets.newHashSet(DashboardConstants.OBPS_ALL_OC_BUSINESS_SERVICES));
		List<Chart> totalOCApplications = bpaRepository.getTotalPermitsIssuedVsTotalOcIssuedVsTotalOcSubmitted(criteriaBPA);
		
		criteriaBPA.setStatus(Sets.newHashSet(DashboardConstants.STATUS_APPROVED));
		List<Chart> totalClosedOCApplications = bpaRepository.getTotalPermitsIssuedVsTotalOcIssuedVsTotalOcSubmitted(criteriaBPA);
		
		List<Chart> responseListTotalApplications =  new ArrayList<>();
		List<Chart> responseListClosedApplications =  new ArrayList<>();
		List<Data> responseToReturn = new ArrayList<>();
		
		//Add all applications and closed applications in one variable
		for(Chart wsApplication : totalWSApplications) {
			String month = wsApplication.getName();
			Chart response = new Chart() ;
			response.setName(month);	
			BigDecimal valuePGR = totalPGRApplications.stream().filter(complaint -> complaint.getName().equals(month))
					.map(item -> item.getValue()).reduce(BigDecimal.ZERO, BigDecimal::add);

			BigDecimal valuePT = totalPropertyApplications.stream()
					.filter(complaint -> complaint.getName().equals(month)).map(item -> item.getValue())
					.reduce(BigDecimal.ZERO, BigDecimal::add);

			BigDecimal valueMR = totalMRApplications.stream().filter(complaint -> complaint.getName().equals(month))
					.map(item -> item.getValue()).reduce(BigDecimal.ZERO, BigDecimal::add);

			BigDecimal valueTL = totalTLApplications.stream().filter(complaint -> complaint.getName().equals(month))
					.map(item -> item.getValue()).reduce(BigDecimal.ZERO, BigDecimal::add);

			BigDecimal valueBPA = totalBpaApplications.stream().filter(complaint -> complaint.getName().equals(month))
					.map(item -> item.getValue()).reduce(BigDecimal.ZERO, BigDecimal::add);

			BigDecimal valueOC = totalOCApplications.stream().filter(complaint -> complaint.getName().equals(month))
					.map(item -> item.getValue()).reduce(BigDecimal.ZERO, BigDecimal::add);

			response.setValue(valuePGR.add(valuePT).add(valueMR).add(valueTL).add(valueBPA).add(valueOC)
					.add(wsApplication.getValue()));
			responseListTotalApplications.add(response);
		}
		
		for(Chart wsApplication : closedWSApplications) {
			String month = wsApplication.getName();
			Chart response = new Chart() ;
			response.setName(month);	
			BigDecimal valuePGR = closedPGRApplications.stream().filter(complaint -> complaint.getName().equals(month))
					.map(item -> item.getValue()).reduce(BigDecimal.ZERO, BigDecimal::add);

			BigDecimal valuePT = totalPropertyClosedApplications.stream()
					.filter(complaint -> complaint.getName().equals(month)).map(item -> item.getValue())
					.reduce(BigDecimal.ZERO, BigDecimal::add);

			BigDecimal valueMR = closedMRApplications.stream().filter(complaint -> complaint.getName().equals(month))
					.map(item -> item.getValue()).reduce(BigDecimal.ZERO, BigDecimal::add);

			BigDecimal valueTL = totalTLApprovedApplications.stream().filter(complaint -> complaint.getName().equals(month))
					.map(item -> item.getValue()).reduce(BigDecimal.ZERO, BigDecimal::add);

			BigDecimal valueBPA = totalClosedBpaApplications.stream().filter(complaint -> complaint.getName().equals(month))
					.map(item -> item.getValue()).reduce(BigDecimal.ZERO, BigDecimal::add);

			BigDecimal valueOC = totalClosedOCApplications.stream().filter(complaint -> complaint.getName().equals(month))
					.map(item -> item.getValue()).reduce(BigDecimal.ZERO, BigDecimal::add);

			response.setValue(valuePGR.add(valuePT).add(valueMR).add(valueTL).add(valueBPA).add(valueOC)
					.add(wsApplication.getValue()));
			responseListClosedApplications.add(response);
		}
		
		List<Plot> plotsTotalApplications = new ArrayList<Plot>();
		extractDataForChart(responseListTotalApplications, plotsTotalApplications);
		
		BigDecimal totalApplications = plotsTotalApplications.stream().map(complaints -> complaints.getValue()).reduce(BigDecimal.ZERO,
				BigDecimal::add);
		
		responseToReturn.add(Data.builder().headerName("Total Applications").headerSymbol("number").headerValue(totalApplications).plots(plotsTotalApplications).build());
		
		List<Plot> plotsClosedApplications = new ArrayList<Plot>();
		extractDataForChart(responseListClosedApplications, plotsClosedApplications);
		
		BigDecimal closedApplications = plotsClosedApplications.stream().map(complaints -> complaints.getValue()).reduce(BigDecimal.ZERO,
				BigDecimal::add);
		
		responseToReturn.add(Data.builder().headerName("Closed Applications").headerSymbol("number").headerValue(closedApplications).plots(plotsClosedApplications).build());
		
		return responseToReturn;
		
	}
	
	private void extractDataForChart(List<Chart> items, List<Plot> plots) {
		items.stream().forEach(item ->{
			plots.add(Plot.builder().name(item.getName()).value(item.getValue()).symbol("number").build());
		});
	}

	public List<Data> bottomPerformingULBsCompletionRate(PayloadDetails payloadDetails) {
		payloadDetails.setModulelevel(DashboardConstants.MODULE_LEVEL_PGR);
		HashMap<String, Long> pgrCount = pgrService.totalApplicationsTenantWise(payloadDetails);

		payloadDetails.setModulelevel(DashboardConstants.MODULE_LEVEL_PT);
		HashMap<String, Long> ptCount = ptService.totalApplicationsTenantWise(payloadDetails);

		payloadDetails.setModulelevel(DashboardConstants.MODULE_LEVEL_OBPS);
		HashMap<String, Long> bpaCount = bpaService.totalApplicationsTenantWise(payloadDetails);

		payloadDetails.setModulelevel(DashboardConstants.MODULE_LEVEL_OBPS);
		HashMap<String, Long> bpaOcCount = bpaService.totalBPAOcApplicationsTenantWise(payloadDetails);

		payloadDetails.setModulelevel(DashboardConstants.BUSINESS_SERVICE_WS);
		HashMap<String, Long> wsCount = wsService.totalApplicationsTenantWise(payloadDetails);

		payloadDetails.setModulelevel(DashboardConstants.BUSINESS_SERVICE_TL);
		HashMap<String, Long> tlCount = tlService.totalApplicationsTenantWise(payloadDetails);

		payloadDetails.setModulelevel(DashboardConstants.BUSINESS_SERVICE_MR);
		HashMap<String, Long> mrCount = mrService.totalApplicationsTenantWise(payloadDetails);

		HashMap<String, Long> totalApplicationMap = new HashMap<>();

		wsCount.forEach((k, v) -> {
			System.out.format("key: %s, value: %d%n", k, v);

			Long wsValue = 0L;
			if (wsCount.get(k) != null) {
				wsValue = wsCount.get(k);
			}
			Long pgrValue = 0L;
			if (pgrCount.get(k) != null) {
				pgrValue = pgrCount.get(k);
			}
			Long ptValue = 0L;
			if (ptCount.get(k) != null) {
				ptValue = ptCount.get(k);
			}
			Long bpaValue = 0L;
			if (bpaCount.get(k) != null) {
				bpaValue = bpaCount.get(k);
			}
			Long bpaOcValue = 0L;
			if (bpaOcCount.get(k) != null) {
				bpaOcValue = bpaOcCount.get(k);
			}
			Long tlValue = 0L;
			if (tlCount.get(k) != null) {
				tlValue = tlCount.get(k);
			}
			Long mrValue = 0L;
			if (mrCount.get(k) != null) {
				mrValue = mrCount.get(k);
			}

			Long total = wsValue + pgrValue + ptValue + bpaValue + bpaOcValue + tlValue + mrValue;
			totalApplicationMap.put(k, total);
		});

		// Total Completion Count Tenantwise

		CommonSearchCriteria criteria = getCommonSearchCriteria(payloadDetails);

		payloadDetails.setModulelevel(DashboardConstants.MODULE_LEVEL_PT);
		criteria.setExcludedTenantId(DashboardConstants.TESTING_TENANT);

		payloadDetails.setModulelevel(DashboardConstants.MODULE_LEVEL_PT);
		HashMap<String, Long> ptCompletedCount = ptService.totalCompletedApplicationsTenantWise(payloadDetails);

		payloadDetails.setModulelevel(DashboardConstants.MODULE_LEVEL_OBPS);
		HashMap<String, Long> bpaCompletedCount = bpaService.bpaTotalApplicationsTenantWise(payloadDetails);

		payloadDetails.setModulelevel(DashboardConstants.MODULE_LEVEL_OBPS);
		HashMap<String, Long> bpaOcCompletedCount = bpaService.bpaOcTotalApplicationsTenantWise(payloadDetails);

		payloadDetails.setModulelevel(DashboardConstants.BUSINESS_SERVICE_WS);
		HashMap<String, Long> wsCompletedTotal = wsService.wsTotalCompletedApplicationsTenantWise(payloadDetails);

		criteria.setStatus(Sets.newHashSet(DashboardConstants.STATUS_APPROVED));
		criteria.setBusinessServices(Sets.newHashSet(DashboardConstants.BUSINESS_SERVICE_TL));
		payloadDetails.setModulelevel(DashboardConstants.BUSINESS_SERVICE_TL);
		HashMap<String, Long> tlCompletedCount = csRepository.tlTotalCompletedApplicationsTenantWise(criteria);

		payloadDetails.setModulelevel(DashboardConstants.BUSINESS_SERVICE_MR);
		HashMap<String, Long> mrCompletedCount = mrService.totalCompletedApplicationsTenantWise(payloadDetails);

		payloadDetails.setModulelevel(DashboardConstants.MODULE_LEVEL_PGR);
		criteria.setBusinessServices(null);
		criteria.setStatus(Sets.newHashSet(DashboardConstants.PGR_REQUEST_COMPLETED_STATUS));
		criteria.setExcludedTenantId(DashboardConstants.TESTING_TENANT);
		HashMap<String, Long> pgrCompletedCount = csRepository.pgrTotalApplicationsTenantWise(criteria);

		HashMap<String, Long> totalApplicationCompletionMap = new HashMap<>();

		wsCompletedTotal.forEach((k, v) -> {
			System.out.format("key: %s, value: %d%n", k, v);

			Long wsValue = 0L;
			if (wsCompletedTotal.get(k) != null) {
				wsValue = wsCompletedTotal.get(k);
			}
			Long pgrValue = 0L;
			if (pgrCompletedCount.get(k) != null) {
				pgrValue = pgrCompletedCount.get(k);
			}
			Long ptValue = 0L;
			if (ptCompletedCount.get(k) != null) {
				ptValue = ptCompletedCount.get(k);
			}
			Long bpaValue = 0L;
			if (bpaCompletedCount.get(k) != null) {
				bpaValue = bpaCompletedCount.get(k);
			}
			Long bpaOcValue = 0L;
			if (bpaOcCompletedCount.get(k) != null) {
				bpaOcValue = bpaOcCompletedCount.get(k);
			}
			Long tlValue = 0L;
			if (tlCompletedCount.get(k) != null) {
				tlValue = tlCompletedCount.get(k);
			}
			Long mrValue = 0L;
			if (mrCompletedCount.get(k) != null) {
				mrValue = mrCompletedCount.get(k);
			}

			Long totalCompletion = wsValue + pgrValue + ptValue + bpaValue + bpaOcValue + tlValue + mrValue;
			totalApplicationCompletionMap.put(k, totalCompletion);
		});


		List<Chart> percentList = mapTenantsForPerformanceRate(totalApplicationCompletionMap, totalApplicationMap);

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
