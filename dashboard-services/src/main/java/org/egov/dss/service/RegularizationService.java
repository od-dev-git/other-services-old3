package org.egov.dss.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import org.egov.dss.config.ConfigurationLoader;
import org.egov.dss.constants.DashboardConstants;
import org.egov.dss.model.PayloadDetails;
import org.egov.dss.model.RegularizationSearchCriteria;
import org.egov.dss.repository.RegularizationRepository;
import org.egov.dss.util.DashboardUtils;
import org.egov.dss.web.model.Data;
import org.egov.dss.web.model.Plot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.google.common.collect.Sets;

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
	
	public HashMap<String,BigDecimal> getTenantWiseAvgDaysToIssuePermit(PayloadDetails payloadDetails) {
		 RegularizationSearchCriteria criteria = getRegularizationSearchCriteria(payloadDetails);
	        criteria.setExcludedTenantId(DashboardConstants.TESTING_TENANT);
	        criteria.setStatus(Sets.newHashSet(DashboardConstants.STATUS_APPROVED));
	        criteria.setBusinessServices(Sets.newHashSet(DashboardConstants.REGULARIZATION_ALL_BUSINESS_SERVICES));
	        return regularizationRepository.getTenantWiseAvgDaysPermitIssued(criteria);
	     }
	
    public List<Data> bottomUlbByPerformance(PayloadDetails payloadDetails) {
		RegularizationSearchCriteria criteria = getRegularizationSearchCriteria(payloadDetails);
		criteria.setExcludedTenantId(DashboardConstants.TESTING_TENANT);
		List<Data> response = new ArrayList();
		HashMap<String, BigDecimal> tenantWiseEbraAvgDaysPermitIssue = getTenantWiseAvgDaysToIssuePermit(payloadDetails);
		if (!CollectionUtils.isEmpty(tenantWiseEbraAvgDaysPermitIssue)) {
			Map<String, BigDecimal> tenantWiseSorted = tenantWiseEbraAvgDaysPermitIssue.entrySet().parallelStream()
					.sorted(Map.Entry.comparingByValue(Comparator.reverseOrder())).collect(Collectors.toMap(
							Map.Entry::getKey, Map.Entry::getValue, (oldValue, newValue) -> oldValue, LinkedHashMap::new));
			int Rank = tenantWiseSorted.size();
			for (Entry<String, BigDecimal> obj : tenantWiseSorted.entrySet()) {
				response.add(
						Data.builder().headerName("Rank").headerValue(Rank)
								.plots(Arrays.asList(Plot.builder().label("AVERAGE_DAYS").name(obj.getKey())
										.value(obj.getValue()).symbol("number").build()))
								.headerSymbol("number").build());
				Rank--;
			}
			;
		} else {
			response.add(
					Data.builder().headerName("Rank").headerValue(BigDecimal.ZERO)
							.plots(Arrays.asList(Plot.builder().label("AVERAGE_DAYS")
									.name(String.valueOf(payloadDetails.getTenantid())).value(BigDecimal.ZERO)
									.symbol("number").build()))
							.headerSymbol("number").build());
		}

		return response;
	}
	
	public List<Data> topUlbByPerformance(PayloadDetails payloadDetails) {
		RegularizationSearchCriteria criteria = getRegularizationSearchCriteria(payloadDetails);
		criteria.setExcludedTenantId(DashboardConstants.TESTING_TENANT);
		List<Data> response = new ArrayList();
		HashMap<String, BigDecimal> tenantWiseRegularizationAvgDaysPermitIssue = getTenantWiseAvgDaysToIssuePermit(payloadDetails);
		// Sort the HashMap in ascending order
		if (!CollectionUtils.isEmpty(tenantWiseRegularizationAvgDaysPermitIssue)) {
			Map<String, BigDecimal> tenantWiseSorted = tenantWiseRegularizationAvgDaysPermitIssue.entrySet().parallelStream()
					.sorted(Map.Entry.comparingByValue()).collect(Collectors.toMap(
							Map.Entry::getKey, Map.Entry::getValue, (oldValue, newValue) -> oldValue, LinkedHashMap::new));
			
			int Rank = 0;
			for (Entry<String, BigDecimal> obj : tenantWiseSorted.entrySet()) {
				Rank++;
				response.add(
						Data.builder().headerName("Rank").headerValue(Rank)
								.plots(Arrays.asList(Plot.builder().label("AVERAGE_DAYS").name(obj.getKey())
										.value(obj.getValue()).symbol("number").build()))
								.headerSymbol("number").build());
			}
			;
		} else {
			response.add(
					Data.builder().headerName("Rank").headerValue(BigDecimal.ZERO)
							.plots(Arrays.asList(Plot.builder().label("AVERAGE_DAYS")
									.name(String.valueOf(payloadDetails.getTenantid())).value(BigDecimal.ZERO)
									.symbol("number").build()))
							.headerSymbol("number").build());
		}

		return response;
	}
	
	public List<Data> RegularizationServiceSummary(PayloadDetails payloadDetails) {
		RegularizationSearchCriteria criteria = getRegularizationSearchCriteria(payloadDetails);
        criteria.setExcludedTenantId(DashboardConstants.TESTING_TENANT);
        HashMap<String, BigDecimal> totalAppicationReceived = getTotalApplicationReceivedByService(payloadDetails);
        HashMap<String, BigDecimal> totalAppicationApproved = getTotalApplicationApprovedByService(payloadDetails);
        HashMap<String, BigDecimal> avgDaysToIssuePermit =    getAvgDaysToIssuePermitByServiceType(payloadDetails);
        
        HashMap<String, BigDecimal> concatedHashMap = new HashMap<>(totalAppicationReceived);
        concatedHashMap.putAll(totalAppicationApproved);
        concatedHashMap.putAll(avgDaysToIssuePermit);
                
        List<Data> response = new ArrayList<>();
        int serialNumber = 1;

        for (HashMap.Entry<String, BigDecimal> totalApplication : concatedHashMap.entrySet()) {
            List<Plot> plots = new ArrayList();
            plots.add(Plot.builder().name("S.N.").label(String.valueOf(serialNumber)).symbol("text").build());

            plots.add(Plot.builder().name("Service").label(totalApplication.getKey().toString()).symbol("text")
                    .build());

			plots.add(Plot.builder().name("Application Received")
					.value(totalAppicationReceived.get(totalApplication.getKey()) == null ? BigDecimal.ZERO
							: totalAppicationReceived.get(totalApplication.getKey()))
					.symbol("number").build());

			plots.add(Plot.builder().name("Application Approved")
					.value(totalAppicationApproved.get(totalApplication.getKey()) == null ? BigDecimal.ZERO
							: totalAppicationApproved.get(totalApplication.getKey()))
					.symbol("number").build());
			
			plots.add(Plot.builder().name("Avg. Days to Issue Permit")
					.value(avgDaysToIssuePermit.get(totalApplication.getKey()) == null ? BigDecimal.ZERO
							: avgDaysToIssuePermit.get(totalApplication.getKey()))
					.symbol("number").build());

			response.add(Data.builder().headerName(totalApplication.getKey()).plots(plots).headerValue(serialNumber)
					.build());

            serialNumber++;

        }
        
        if (CollectionUtils.isEmpty(response)) {
			serialNumber++;
			List<Plot> plots = new ArrayList();
			plots.add(Plot.builder().name("S.N.").label(String.valueOf(serialNumber)).symbol("text").build());

			plots.add(Plot.builder().name("Service").label(payloadDetails.getTenantid()).symbol("text").build());

			plots.add(Plot.builder().name("Application Received").value(BigDecimal.ZERO).symbol("number")
					.build());

			plots.add(Plot.builder().name("Application Approved").value(BigDecimal.ZERO).symbol("number").build());
			
			plots.add(Plot.builder().name("Avg. Days to Issue Permit").value(BigDecimal.ZERO).symbol("number").build());

			response.add(Data.builder().headerName(payloadDetails.getTenantid()).plots(plots).headerValue(serialNumber)
					.build());

		}

         return response;
         
	}
	
	public HashMap<String,BigDecimal> getTotalApplicationReceivedByService(PayloadDetails payloadDetails) {
		RegularizationSearchCriteria criteria = getRegularizationSearchCriteria(payloadDetails);
        criteria.setExcludedTenantId(DashboardConstants.TESTING_TENANT);
        criteria.setStatusNotIn(Sets.newHashSet(DashboardConstants.REGULARIZATION_REJECTED_STATUSES));
        criteria.setBusinessServices(Sets.newHashSet(DashboardConstants.REGULARIZATION_ALL_BUSINESS_SERVICES));
        return regularizationRepository.getTotalApplicationByServiceType(criteria);
     }
	
	public HashMap<String,BigDecimal> getTotalApplicationApprovedByService(PayloadDetails payloadDetails) {
		RegularizationSearchCriteria criteria = getRegularizationSearchCriteria(payloadDetails);
        criteria.setExcludedTenantId(DashboardConstants.TESTING_TENANT);
        criteria.setStatus(Sets.newHashSet(DashboardConstants.STATUS_APPROVED));
        criteria.setBusinessServices(Sets.newHashSet(DashboardConstants.REGULARIZATION_ALL_BUSINESS_SERVICES));
        return regularizationRepository.getApprovedApplicationByServiceType(criteria);
     }
	
	public HashMap<String,BigDecimal> getAvgDaysToIssuePermitByServiceType(PayloadDetails payloadDetails) {
		RegularizationSearchCriteria criteria = getRegularizationSearchCriteria(payloadDetails);
        criteria.setExcludedTenantId(DashboardConstants.TESTING_TENANT);
        criteria.setStatus(Sets.newHashSet(DashboardConstants.STATUS_APPROVED));
        criteria.setBusinessServices(Sets.newHashSet(DashboardConstants.REGULARIZATION_ALL_BUSINESS_SERVICES));
        return regularizationRepository.getAvgDaysToIssuePermitByServiceType(criteria);
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

	public HashMap<String,BigDecimal> getTenantWiseRegularizationApplication(PayloadDetails payloadDetails) {
        RegularizationSearchCriteria criteria = getRegularizationSearchCriteria(payloadDetails);
        criteria.setExcludedTenantId(DashboardConstants.TESTING_TENANT);
        criteria.setStatusNotIn(Sets.newHashSet(DashboardConstants.REGULARIZATION_REJECTED_STATUSES));
        criteria.setBusinessServices(Sets.newHashSet(DashboardConstants.REGULARIZATION_ALL_BUSINESS_SERVICES));
        return regularizationRepository.getTenantWiseRegularizationApplication(criteria);
     }
    
    public HashMap<String,BigDecimal> getTenantWisePermitIssued(PayloadDetails payloadDetails) {
        RegularizationSearchCriteria criteria = getRegularizationSearchCriteria(payloadDetails);
        criteria.setExcludedTenantId(DashboardConstants.TESTING_TENANT);
        criteria.setStatus(Sets.newHashSet(DashboardConstants.STATUS_APPROVED));
        criteria.setBusinessServices(Sets.newHashSet(DashboardConstants.REGULARIZATION_ALL_BUSINESS_SERVICES));
        return regularizationRepository.getTenantWiseRegularizationPermitIssued(criteria);
     }
    
    public HashMap<String,BigDecimal> getTenantWiseApplicationPending(PayloadDetails payloadDetails) {
        RegularizationSearchCriteria criteria = getRegularizationSearchCriteria(payloadDetails);
        criteria.setExcludedTenantId(DashboardConstants.TESTING_TENANT);
        criteria.setStatusNotIn(Sets.newHashSet(DashboardConstants.REGULARIZATION_REJECTED_STATUS_TOTAL_APPLICATIONS_PENDING));
        criteria.setBusinessServices(Sets.newHashSet(DashboardConstants.REGULARIZATION_ALL_BUSINESS_SERVICES));
        criteria.setFromDate(null);
        criteria.setDeleteStatus(DashboardConstants.STATUS_DELETED);
        return regularizationRepository.getTenantWiseRegularizationPendingApplication(criteria);
     }
    
    public HashMap<String,BigDecimal> getAvgDaysToIssueCertificate(PayloadDetails payloadDetails) {
        RegularizationSearchCriteria criteria = getRegularizationSearchCriteria(payloadDetails);
        criteria.setExcludedTenantId(DashboardConstants.TESTING_TENANT);
        criteria.setStatus(Sets.newHashSet(DashboardConstants.STATUS_APPROVED));
        criteria.setBusinessServices(Sets.newHashSet(DashboardConstants.REGULARIZATION_ALL_BUSINESS_SERVICES));
        return regularizationRepository.getTenantWiseAvgDaysPermitIssued(criteria);
     }
	
	public List<Data> regularizationServiceReport(PayloadDetails payloadDetails) {
		RegularizationSearchCriteria criteria = getRegularizationSearchCriteria(payloadDetails);
        criteria.setExcludedTenantId(DashboardConstants.TESTING_TENANT);
        HashMap<String, BigDecimal> totalApplicationSubmitted = getTenantWiseRegularizationApplication(payloadDetails);
        HashMap<String, BigDecimal> tenantWiseRegularizationPermitIssued = getTenantWisePermitIssued(payloadDetails);
        HashMap<String, BigDecimal> tenantWiseRegularizationPendingApplication = getTenantWiseApplicationPending(payloadDetails);
        HashMap<String, BigDecimal> tenantWiseAvgDaysToIssueCertificate = getAvgDaysToIssueCertificate(payloadDetails);
        
        List<Data> response = new ArrayList<>();
        int serialNumber = 1;

        for (HashMap.Entry<String, BigDecimal> tenantWiseRegApplication : totalApplicationSubmitted.entrySet()) {
            List<Plot> plots = new ArrayList();
            plots.add(Plot.builder().name("S.N.").label(String.valueOf(serialNumber)).symbol("text").build());

            plots.add(Plot.builder().name("ULBs").label(tenantWiseRegApplication.getKey().toString()).symbol("text")
                    .build());
					
            plots.add(Plot.builder().name("Total Applications Submitted").value(tenantWiseRegApplication.getValue() == null ? BigDecimal.ZERO : tenantWiseRegApplication.getValue())
                    .symbol("number").build());
					
			plots.add(Plot.builder().name("Total Permits Issued")
                    .value(tenantWiseRegularizationPermitIssued.get(tenantWiseRegApplication.getKey()) == null ? BigDecimal.ZERO : tenantWiseRegularizationPermitIssued.get(tenantWiseRegApplication.getKey())).symbol("number").build());

            plots.add(Plot.builder().name("Total Regularization Application Pending")
                    .value(tenantWiseRegularizationPendingApplication.get(tenantWiseRegApplication.getKey()) == null ? BigDecimal.ZERO : tenantWiseRegularizationPendingApplication.get(tenantWiseRegApplication.getKey())).symbol("number").build());

            plots.add(Plot.builder().name("Average days to issue Permit")
                    .value(tenantWiseAvgDaysToIssueCertificate.get(tenantWiseRegApplication.getKey()) == null ? BigDecimal.ZERO : tenantWiseAvgDaysToIssueCertificate.get(tenantWiseRegApplication.getKey()) ).symbol("number").build());
			
            response.add(Data.builder().headerName(tenantWiseRegApplication.getKey()).plots(plots)
                    .headerValue(serialNumber).headerName(tenantWiseRegApplication.getKey()).build());

            serialNumber++;

        }
        
		if (CollectionUtils.isEmpty(response)) {
//			serialNumber++;
			List<Plot> plots = new ArrayList();
			plots.add(Plot.builder().name("S.N.").label(String.valueOf(serialNumber)).symbol("text").build());

			plots.add(Plot.builder().name("ULBs").label(payloadDetails.getTenantid()).symbol("text").build());
			
			plots.add(Plot.builder().name("Total Applications Submitted").value(BigDecimal.ZERO).symbol("number")
					.build());

			plots.add(Plot.builder().name("Total Certificates Issued").value(BigDecimal.ZERO).symbol("number").build());

			plots.add(Plot.builder().name("Total Regularization Application Pending").value(BigDecimal.ZERO).symbol("number")
					.build());

			plots.add(Plot.builder().name("Average days to issue certificate").value(BigDecimal.ZERO).symbol("number")
					.build());

			response.add(Data.builder().headerName(payloadDetails.getTenantid()).plots(plots).headerValue(serialNumber)
					.build());
					

		}

        return response;
    }

	
}
