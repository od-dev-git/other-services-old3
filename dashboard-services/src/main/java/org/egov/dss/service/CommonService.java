package org.egov.dss.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.egov.dss.constants.DashboardConstants;
import org.egov.dss.model.BpaSearchCriteria;
import org.egov.dss.model.Chart;
import org.egov.dss.model.MarriageSearchCriteria;
import org.egov.dss.model.PayloadDetails;
import org.egov.dss.model.PgrSearchCriteria;
import org.egov.dss.model.PropertySerarchCriteria;
import org.egov.dss.model.TLSearchCriteria;
import org.egov.dss.model.WaterSearchCriteria;
import org.egov.dss.repository.BPARepository;
import org.egov.dss.repository.CommonRepository;
import org.egov.dss.repository.MRRepository;
import org.egov.dss.repository.PGRRepository;
import org.egov.dss.repository.TLRepository;
import org.egov.dss.repository.WSRepository;
import org.egov.dss.web.model.Data;
import org.egov.dss.web.model.Plot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
	
	public List<Data> slaAchieved(PayloadDetails payloadDetails) {
		 payloadDetails.setModulelevel(DashboardConstants.MODULE_LEVEL_PGR);
	     Double pgrSla =	(Double) pgrService.slaAchieved(payloadDetails).get(0).getHeaderValue();
	     payloadDetails.setModulelevel(DashboardConstants.MODULE_LEVEL_PT);
	     Double ptSla =	(Double) ptService.slaAchieved(payloadDetails).get(0).getHeaderValue();
	     payloadDetails.setModulelevel(DashboardConstants.MODULE_LEVEL_OBPS);
	     Double bpaSla =	(Double) bpaService.slaAchieved(payloadDetails).get(0).getHeaderValue();
	     payloadDetails.setModulelevel(DashboardConstants.BUSINESS_SERVICE_WS);
	     Double wsSla =	(Double) wsService.slaAchieved(payloadDetails).get(0).getHeaderValue();
	     payloadDetails.setModulelevel(DashboardConstants.BUSINESS_SERVICE_TL);
	     Double tlSla =	(Double) tlService.slaAchieved(payloadDetails).get(0).getHeaderValue();
	     payloadDetails.setModulelevel(DashboardConstants.BUSINESS_SERVICE_MR);
	     Double mrSla =	(Double) mrService.slaAchieved(payloadDetails).get(0).getHeaderValue();
	     Double slaAchieved = (pgrSla+ptSla+bpaSla+wsSla+tlSla+mrSla)/6;
	     
		return Arrays.asList(Data.builder()
				.headerValue(slaAchieved).build());
	}
	
	public List<Data> totalApplication(PayloadDetails payloadDetails) {
		 payloadDetails.setModulelevel(DashboardConstants.MODULE_LEVEL_PGR);
	     Integer pgrSla =	(Integer) pgrService.totalApplications(payloadDetails).get(0).getHeaderValue();
	     payloadDetails.setModulelevel(DashboardConstants.MODULE_LEVEL_PT);
	     Integer ptSla =	(Integer) ptService.propertiesAssessed(payloadDetails).get(0).getHeaderValue();
	     payloadDetails.setModulelevel(DashboardConstants.MODULE_LEVEL_OBPS);
	     Integer bpaSla =	(Integer) bpaService.totalPermitIssued(payloadDetails).get(0).getHeaderValue();
	     payloadDetails.setModulelevel(DashboardConstants.BUSINESS_SERVICE_WS);
	     Integer wsSla =	(Integer) wsService.totalActiveConnections(payloadDetails).get(0).getHeaderValue();
	     payloadDetails.setModulelevel(DashboardConstants.BUSINESS_SERVICE_TL);
	     Integer tlSla =	(Integer) tlService.totalApplications(payloadDetails).get(0).getHeaderValue();
	     payloadDetails.setModulelevel(DashboardConstants.BUSINESS_SERVICE_MR);
	     Integer mrSla =	(Integer) mrService.totalApplications(payloadDetails).get(0).getHeaderValue();
	     Integer totalApplications = (pgrSla+ptSla+bpaSla+wsSla+tlSla+mrSla)/5;
	     
		return Arrays.asList(Data.builder()
				.headerValue(totalApplications).build());
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

	

}
