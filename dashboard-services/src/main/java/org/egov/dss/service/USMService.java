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
import org.egov.dss.model.TargetSearchCriteria;
import org.egov.dss.model.UsmSearchCriteria;
import org.egov.dss.repository.USMRepository;
import org.egov.dss.web.model.Data;
import org.egov.dss.web.model.Plot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.google.common.collect.Sets;

@Service
public class USMService {

	@Autowired
	private USMRepository usmRepository;

	public List<Data> totalFeedbackSubmitted(PayloadDetails payloadDetails) {
		UsmSearchCriteria criteria = getUSMSearchCriteria(payloadDetails);
		criteria.setExcludedTenantId(DashboardConstants.TESTING_TENANT);
		Integer totalFeedbackSubmitted = (Integer) usmRepository.getTotalFeedbackSubmitted(criteria);
		return Arrays.asList(Data.builder().headerValue(totalFeedbackSubmitted).build());
	}

	public List<Data> totalOpenIssues(PayloadDetails payloadDetails) {
		UsmSearchCriteria criteria = getUSMSearchCriteria(payloadDetails);
		criteria.setExcludedTenantId(DashboardConstants.TESTING_TENANT);
		Integer totalOpenIssues = (Integer) usmRepository.getTotalOpenIssue(criteria);
		return Arrays.asList(Data.builder().headerValue(totalOpenIssues).build());
	}

	public List<Data> totalClosedIssue(PayloadDetails payloadDetails) {
		UsmSearchCriteria criteria = getUSMSearchCriteria(payloadDetails);
		criteria.setExcludedTenantId(DashboardConstants.TESTING_TENANT);
		Integer totalCloseIssues = (Integer) usmRepository.getTotalClosedIssue(criteria);
		return Arrays.asList(Data.builder().headerValue(totalCloseIssues).build());
	}

	public List<Data> totalClosedWithSatisfactory(PayloadDetails payloadDetails) {
		UsmSearchCriteria criteria = getUSMSearchCriteria(payloadDetails);
		criteria.setExcludedTenantId(DashboardConstants.TESTING_TENANT);
		Integer totalCloseIssuesSatisfactory = (Integer) usmRepository.getTotalClosedSatisfiedIssue(criteria);
		return Arrays.asList(Data.builder().headerValue(totalCloseIssuesSatisfactory).build());
	}

	public List<Data> totalUnattendedIssue(PayloadDetails payloadDetails) {
		UsmSearchCriteria criteria = getUSMSearchCriteria(payloadDetails);
		criteria.setExcludedTenantId(DashboardConstants.TESTING_TENANT);
		Integer totalCloseIssuesSatisfactory = (Integer) usmRepository.getTotalUnattendedIssue(criteria);
		return Arrays.asList(Data.builder().headerValue(totalCloseIssuesSatisfactory).build());
	}

	public List<Data> totalEscalatedIssue(PayloadDetails payloadDetails) {
		UsmSearchCriteria criteria = getUSMSearchCriteria(payloadDetails);
		criteria.setExcludedTenantId(DashboardConstants.TESTING_TENANT);
		Integer totalCloseIssuesSatisfactory = (Integer) usmRepository.getTotalEscalatedIssue(criteria);
		return Arrays.asList(Data.builder().headerValue(totalCloseIssuesSatisfactory).build());
	}

	public List<Data> totalRespondedEscalatedIssue(PayloadDetails payloadDetails) {
		UsmSearchCriteria criteria = getUSMSearchCriteria(payloadDetails);
		criteria.setExcludedTenantId(DashboardConstants.TESTING_TENANT);
		Integer totalCloseIssuesSatisfactory = (Integer) usmRepository.getTotalRespondedEscalatedIssue(criteria);
		return Arrays.asList(Data.builder().headerValue(totalCloseIssuesSatisfactory).build());
	}

	public List<Data> totalSlumSubmittedFeedback(PayloadDetails payloadDetails) {
		UsmSearchCriteria criteria = getUSMSearchCriteria(payloadDetails);
		criteria.setExcludedTenantId(DashboardConstants.TESTING_TENANT);
		Integer totalSlumSubmittedFeedback = (Integer) usmRepository.getTotalSlumFeedbackSubmitted(criteria);
		return Arrays.asList(Data.builder().headerValue(totalSlumSubmittedFeedback).build());
	}

	public List<Data> cumulativeApplications(PayloadDetails payloadDetails) {
		UsmSearchCriteria criteria = getUSMSearchCriteria(payloadDetails);
		criteria.setExcludedTenantId(DashboardConstants.TESTING_TENANT);
		List<Chart> cumulativeApplications = usmRepository.getCumulativeApplications(criteria);
		List<Plot> plots = new ArrayList();
		extractDataForChart(cumulativeApplications, plots);

		BigDecimal total = cumulativeApplications.stream()
				.map(cumulativeApplication -> cumulativeApplication.getValue())
				.reduce(BigDecimal.ZERO, BigDecimal::add);

		return Arrays.asList(
				Data.builder().headerName("DSS_USM_CUMULATIVE_APPLICATIONS").headerValue(total).plots(plots).build());
	}

	public List<Data> topIssueCategory(PayloadDetails payloadDetails) {

		UsmSearchCriteria criteria = getUSMSearchCriteria(payloadDetails);
		criteria.setExcludedTenantId(DashboardConstants.TESTING_TENANT);

		List<Chart> topFiveComplaints = usmRepository.topIssueCategory(criteria);

		List<Plot> plots = new ArrayList<Plot>();
		extractDataForChart(topFiveComplaints, plots);

		BigDecimal total = topFiveComplaints.stream().map(complaints -> complaints.getValue()).reduce(BigDecimal.ZERO,
				BigDecimal::add);

		return Arrays.asList(
				Data.builder().headerName("DSS_USM_TOP_ISSUE_CATEGORY").headerValue(total).plots(plots).build());
	}

	public List<Data> categoryWiseIssue(PayloadDetails payloadDetails) {

		UsmSearchCriteria criteria = getUSMSearchCriteria(payloadDetails);
		criteria.setExcludedTenantId(DashboardConstants.TESTING_TENANT);

		List<Chart> issueCategoryWise = usmRepository.getCategoryWiseCount(criteria);

		List<Plot> plots = new ArrayList<Plot>();
		extractDataForChart(issueCategoryWise, plots);

		BigDecimal total = issueCategoryWise.stream().map(complaints -> complaints.getValue()).reduce(BigDecimal.ZERO,
				BigDecimal::add);

		return Arrays.asList(
				Data.builder().headerName("DSS_USM_CATEGORY_WISE_ISSUE").headerValue(total).plots(plots).build());
	}

	public List<Data> issueResolutionSummary(PayloadDetails payloadDetails) {

		UsmSearchCriteria criteria = getUSMSearchCriteria(payloadDetails);
		criteria.setExcludedTenantId(DashboardConstants.TESTING_TENANT);
		TargetSearchCriteria targetSearchCriteria = getTargetSearchCriteria(payloadDetails);

		HashMap<String, BigDecimal> tenantWiseFeedback = usmRepository.getTenantWiseFeedback(criteria);
		HashMap<String, BigDecimal> tenantWiseClosedIssue = usmRepository.getTenantWiseClosedTicket(criteria);
		HashMap<String, BigDecimal> tenantWiseOpenIssue = usmRepository.getTenantWiseOpenTicket(criteria);
		List<Data> response = new ArrayList<>();
		int serialNumber = 1;

		for (HashMap.Entry<String, BigDecimal> tenantWiseTotalFeedback : tenantWiseFeedback.entrySet()) {
			List<Plot> plots = new ArrayList();
			plots.add(Plot.builder().name("S.N.").label(String.valueOf(serialNumber)).symbol("text").build());

			plots.add(Plot.builder().name("ULBs").label(tenantWiseTotalFeedback.getKey().toString()).symbol("text")
					.build());

			plots.add(Plot.builder().name("Total Feedback").value(tenantWiseTotalFeedback.getValue()).symbol("amount")
					.build());

			plots.add(
					Plot.builder().name("Total open issue")
							.value(tenantWiseOpenIssue.get(tenantWiseTotalFeedback.getKey()) == null ? BigDecimal.ZERO
									: tenantWiseOpenIssue.get(tenantWiseTotalFeedback.getKey()))
							.symbol("number").build());

			plots.add(
					Plot.builder().name("Total Closed Issue")
							.value(tenantWiseClosedIssue.get(tenantWiseTotalFeedback.getKey()) == null ? BigDecimal.ZERO
									: tenantWiseClosedIssue.get(tenantWiseTotalFeedback.getKey()))
							.symbol("number").build());

			response.add(Data.builder().headerName(tenantWiseTotalFeedback.getKey()).plots(plots)
					.headerValue(serialNumber).headerName(tenantWiseTotalFeedback.getKey()).build());

			serialNumber++;

		}

		if (CollectionUtils.isEmpty(response)) {

			List<Plot> plots = new ArrayList();
			plots.add(Plot.builder().name("S.N.").label(String.valueOf(serialNumber)).symbol("text").build());

			plots.add(Plot.builder().name("ULBs").label(payloadDetails.getTenantid()).symbol("text").build());

			plots.add(Plot.builder().name("Total Feedback").value(BigDecimal.ZERO).symbol("amount").build());

			plots.add(Plot.builder().name("Total open issue").value(BigDecimal.ZERO).symbol("number").build());

			plots.add(Plot.builder().name("Total Closed Issu").value(BigDecimal.ZERO).symbol("number").build());

			response.add(Data.builder().headerName(payloadDetails.getTenantid()).plots(plots).headerValue(serialNumber)
					.build());
		}

		return response;
	}

	public List<Data> topUlbsByStatus(PayloadDetails payloadDetails) {

		UsmSearchCriteria criteria = getUSMSearchCriteria(payloadDetails);

		criteria.setExcludedTenantId(DashboardConstants.TESTING_TENANT);

		HashMap<String, Long> totalTicket = usmRepository.getTenantWiseIssue(criteria);
//

		HashMap<String, Long> totalClosedTicket = usmRepository.getTenantWiseTotalClosedTicket(criteria);
		List<Chart> percentList = mapTenantsForPerformanceRate(totalTicket, totalClosedTicket);

		Collections.sort(percentList, Comparator.comparing(e -> e.getValue(), (s1, s2) -> {
			return s2.compareTo(s1);
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

	public List<Data> bottomUlbsByStatus(PayloadDetails payloadDetails) {

		UsmSearchCriteria criteria = getUSMSearchCriteria(payloadDetails);

		criteria.setExcludedTenantId(DashboardConstants.TESTING_TENANT);

		HashMap<String, Long> totalTicket = usmRepository.getTenantWiseIssue(criteria);
//
//		criteria.setStatus(DashboardConstants.TICKET_STATUS);
		HashMap<String, Long> totalClosedTicket = usmRepository.getTenantWiseTotalClosedTicket(criteria);
		List<Chart> percentList = mapTenantsForPerformanceRate(totalTicket, totalClosedTicket);

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

	private List<Chart> mapTenantsForPerformanceRate(HashMap<String, Long> numeratorMap,
			HashMap<String, Long> denominatorMap) {
		List<Chart> percentList = new ArrayList();
		numeratorMap.entrySet().stream().forEach(item -> {
			Long denominator = item.getValue();
			Long numerator = denominatorMap.get(item.getKey());
			BigDecimal percent = new BigDecimal(numerator * 100).divide(new BigDecimal(denominator), 2,
					RoundingMode.HALF_EVEN);
			percentList.add(Chart.builder().name(item.getKey()).value(percent).build());
		});
		return percentList;
	}

	private TargetSearchCriteria getTargetSearchCriteria(PayloadDetails payloadDetails) {
		TargetSearchCriteria criteria = new TargetSearchCriteria();

		if (StringUtils.hasText(payloadDetails.getModulelevel())) {
			if (payloadDetails.getModulelevel().equalsIgnoreCase(DashboardConstants.MODULE_LEVEL_USM))
				criteria.setBusinessServices(null);
			else
				criteria.setBusinessServices(Sets.newHashSet(payloadDetails.getModulelevel()));
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

		if (StringUtils.hasText(payloadDetails.getTimeinterval())) {
			criteria.setFinancialYear(payloadDetails.getTimeinterval());
		}

		return criteria;
	}

	public UsmSearchCriteria getUSMSearchCriteria(PayloadDetails payloadDetails) {
		UsmSearchCriteria criteria = new UsmSearchCriteria();

		if (StringUtils.hasText(payloadDetails.getModulelevel())) {
			if (payloadDetails.getModulelevel().equalsIgnoreCase(DashboardConstants.MODULE_LEVEL_USM))
				criteria.setBusinessServices(null);
			else
				criteria.setBusinessServices(Sets.newHashSet(payloadDetails.getModulelevel()));
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

	private void extractDataForChart(List<Chart> items, List<Plot> plots) {
		items.stream().forEach(item -> {
			plots.add(Plot.builder().name(item.getName()).value(item.getValue()).symbol("number").build());
		});
	}

}
