package org.egov.dss.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.egov.dss.constants.DashboardConstants;
import org.egov.dss.model.Chart;
import org.egov.dss.model.CommonSearchCriteria;
import org.egov.dss.model.MarriageSearchCriteria;
import org.egov.dss.model.PayloadDetails;
import org.egov.dss.model.WaterSearchCriteria;
import org.egov.dss.repository.WSRepository;
import org.egov.dss.web.model.Data;
import org.egov.dss.web.model.Plot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.google.common.collect.Sets;

@Service
public class WSService {
	
	@Autowired
	private WSRepository wsRepository;
   
	public List<Data> totalActiveConnections(PayloadDetails payloadDetails) {
		WaterSearchCriteria criteria = getWaterSearchCriteria(payloadDetails);
		criteria.setStatus(DashboardConstants.WS_CONNECTION_ACTIVATED);
		criteria.setExcludedTenantId(DashboardConstants.TESTING_TENANT);
		criteria.setIsOldApplication(Boolean.FALSE);
		Integer activeConnectionCount =  (Integer) wsRepository.getActiveWaterConnectionCount(criteria);
		return Arrays.asList(Data.builder().headerValue(activeConnectionCount).build());
	}
	
	public List<Data> slaAchieved(PayloadDetails payloadDetails) {
		WaterSearchCriteria criteria = getWaterSearchCriteria(payloadDetails);
		Integer slaAchievedAppCount = Integer.valueOf(1);
		criteria.setExcludedTenantId(DashboardConstants.TESTING_TENANT);
		criteria.setStatus(DashboardConstants.WS_CONNECTION_ACTIVATED);
		criteria.setApplicationType(Sets.newHashSet(DashboardConstants.WS_NEW_CONNECTIONS));
		criteria.setApplicationStatusExclude(Sets.newHashSet(DashboardConstants.STATUS_INITIATED));
		Integer totalApplication =  (Integer) wsTotalApplications(payloadDetails).get(0).getHeaderValue();
		slaAchievedAppCount = (Integer) wsRepository.getSlaAchievedAppCount(criteria);
		return Arrays.asList(Data.builder()
				.headerValue((slaAchievedAppCount.doubleValue() / totalApplication.doubleValue()) * 100).headerSymbol("percentage").build());
	}
	
	public Integer slaAchievedCount(PayloadDetails payloadDetails) {
		WaterSearchCriteria criteria = getWaterSearchCriteria(payloadDetails);
		criteria.setExcludedTenantId(DashboardConstants.TESTING_TENANT);
		criteria.setStatus(DashboardConstants.WS_CONNECTION_ACTIVATED);
		Object slaAchievedAppCountObject = wsRepository.getSlaAchievedAppCount(criteria);
		if(slaAchievedAppCountObject == null) {
			return 0;
		}
		Integer slaAchievedAppCount = (Integer) slaAchievedAppCountObject;
		return slaAchievedAppCount;
	}
	
	public WaterSearchCriteria getWaterSearchCriteria(PayloadDetails payloadDetails) {
		WaterSearchCriteria criteria = new WaterSearchCriteria();

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
	
	public List<Data> totalActiveMeteredWaterConnections(PayloadDetails payloadDetails) {
		WaterSearchCriteria criteria = getWaterSearchCriteria(payloadDetails);
		criteria.setStatus(DashboardConstants.WS_CONNECTION_ACTIVATED);
		criteria.setExcludedTenantId(DashboardConstants.TESTING_TENANT);
		criteria.setIsOldApplication(Boolean.FALSE);
		criteria.setConnectionType(DashboardConstants.WS_CONNECTION_TYPE_METERED);
		Integer activeMeteredWaterConnectionCount =  (Integer) wsRepository.getTotalActiveMeteredWaterConnectionsCount(criteria);
		return Arrays.asList(Data.builder().headerValue(activeMeteredWaterConnectionCount).build());
	}

	public List<Data> totalActiveNonMeteredWaterConnections(PayloadDetails payloadDetails) {
		WaterSearchCriteria criteria = getWaterSearchCriteria(payloadDetails);
		criteria.setStatus(DashboardConstants.WS_CONNECTION_ACTIVATED);
		criteria.setExcludedTenantId(DashboardConstants.TESTING_TENANT);
		criteria.setIsOldApplication(Boolean.FALSE);
		criteria.setConnectionType(DashboardConstants.WS_CONNECTION_TYPE_NON_METERED);
		Integer activeNonMeteredWaterConnectionCount =  (Integer) wsRepository.getTotalActiveNonMeteredWaterConnectionsCount(criteria);
		return Arrays.asList(Data.builder().headerValue(activeNonMeteredWaterConnectionCount).build());
	}

	public List<Data> totalActiveWaterConnections(PayloadDetails payloadDetails) {
		WaterSearchCriteria criteria = getWaterSearchCriteria(payloadDetails);
		criteria.setStatus(DashboardConstants.WS_CONNECTION_ACTIVATED);
		criteria.setExcludedTenantId(DashboardConstants.TESTING_TENANT);
		criteria.setIsOldApplication(Boolean.FALSE);
		criteria.setConnectionFacility(DashboardConstants.WS_CONNECTIONFACILITY_WATER);
		Integer activeNonMeteredWaterConnectionCount =  (Integer) wsRepository.getTotalActiveWaterConnectionsCount(criteria);
		return Arrays.asList(Data.builder().headerValue(activeNonMeteredWaterConnectionCount).build());
	}

	public List<Data> totalActiveSewerageConnections(PayloadDetails payloadDetails) {
		WaterSearchCriteria criteria = getWaterSearchCriteria(payloadDetails);
		criteria.setStatus(DashboardConstants.WS_CONNECTION_ACTIVATED);
		criteria.setExcludedTenantId(DashboardConstants.TESTING_TENANT);
		criteria.setIsOldApplication(Boolean.FALSE);
		criteria.setConnectionFacility(DashboardConstants.WS_CONNECTIONFACILITY_SEWERAGE);
		Integer activeNonMeteredWaterConnectionCount =  (Integer) wsRepository.getTotalActiveSewerageConnectionsCount(criteria);
		return Arrays.asList(Data.builder().headerValue(activeNonMeteredWaterConnectionCount).build());
	}

	public List<Data> totalActiveWaterSewerageConnections(PayloadDetails payloadDetails) {
		WaterSearchCriteria criteria = getWaterSearchCriteria(payloadDetails);
		criteria.setStatus(DashboardConstants.WS_CONNECTION_ACTIVATED);
		criteria.setExcludedTenantId(DashboardConstants.TESTING_TENANT);
		criteria.setIsOldApplication(Boolean.FALSE);
		criteria.setConnectionFacility(DashboardConstants.WS_CONNECTIONFACILITY_WATER_SEWERAGE);
		Integer activeNonMeteredWaterConnectionCount =  (Integer) wsRepository.getTotalActiveWaterSewerageConnectionsCount(criteria);
		return Arrays.asList(Data.builder().headerValue(activeNonMeteredWaterConnectionCount).build());
	}

	public List<Data> cumulativeConnections(PayloadDetails payloadDetails) {
		WaterSearchCriteria criteria = getWaterSearchCriteria(payloadDetails);
		criteria.setStatus(DashboardConstants.WS_CONNECTION_ACTIVATED);
		criteria.setExcludedTenantId(DashboardConstants.TESTING_TENANT);
		criteria.setIsOldApplication(Boolean.FALSE);
		List<Chart> cumulativeConnections = wsRepository.getCumulativeConnections(criteria);

		List<Plot> plots = new ArrayList();
		Long total = 0L;
		total = extractDataForChart(cumulativeConnections, plots ,total);	

		return Arrays.asList(Data.builder().headerName("Water & Sewerage Connections").headerValue(total).plots(plots).build());
	}


	private Long extractDataForChart(List<Chart> items, List<Plot> plots, Long total) {
		for (Chart item : items) {
			plots.add(Plot.builder().name(item.getName()).value(item.getValue()).symbol("number").build());
			total = total + Long.valueOf(String.valueOf(item.getValue()));
		}

		return total ;

	}

	public List<Data> wsConnectionsByUsageType(PayloadDetails payloadDetails) {
		WaterSearchCriteria criteria = getWaterSearchCriteria(payloadDetails);
		criteria.setStatus(DashboardConstants.WS_CONNECTION_ACTIVATED);
		criteria.setExcludedTenantId(DashboardConstants.TESTING_TENANT);
		criteria.setIsOldApplication(Boolean.FALSE);
		List<Chart> wsConnectionsByUsageType = wsRepository.getwsConnectionsByUsageType(criteria);

		List<Plot> plots = new ArrayList();
		Long total = 0L;
		total = extractDataForChart(wsConnectionsByUsageType, plots ,total);		 

		return Arrays.asList(Data.builder().headerName("DSS_WS_WATER_CONNECTION_BY_USAGE").headerValue(total).plots(plots).build());
	}

	public List<Data> wsConnectionsByType(PayloadDetails payloadDetails) {
		WaterSearchCriteria criteria = getWaterSearchCriteria(payloadDetails);
		criteria.setStatus(DashboardConstants.WS_CONNECTION_ACTIVATED);
		criteria.setExcludedTenantId(DashboardConstants.TESTING_TENANT);
		criteria.setIsOldApplication(Boolean.FALSE);
		List<Chart> wsConnectionsByType = wsRepository.getwsConnectionsByType(criteria);

		List<Plot> plots = new ArrayList();
		Long total = 0L;
		total = extractDataForChart(wsConnectionsByType, plots ,total);	 

		return Arrays.asList(Data.builder().headerName("DSS_WS_CONNECTION_BY_CHANNEL").headerValue(total).plots(plots).build());
	}

	public List<Data> wsConnectionAgeing(PayloadDetails payloadDetails) {
		WaterSearchCriteria criteria = getWaterSearchCriteria(payloadDetails);
		criteria.setExcludedTenantId(DashboardConstants.TESTING_TENANT);
		List<HashMap<String, Object>> wsConnectionAgeingBreakup = wsRepository.getWSConnectionAgeing(criteria);

			 List<Data> response = new ArrayList();
			 int serailNumber = 0 ;
			 for( HashMap<String, Object> tenantWiseRow : wsConnectionAgeingBreakup) {
				 serailNumber++;
		            String tenantId = String.valueOf(tenantWiseRow.get("tenantid"));
		            String tenantIdStyled = tenantId.replace("od.", "");
		            tenantIdStyled = tenantIdStyled.substring(0, 1).toUpperCase() + tenantIdStyled.substring(1).toLowerCase();
				 List<Plot> row = new ArrayList<>();
				row.add(Plot.builder().label(String.valueOf(serailNumber)).name("S.N.").symbol("text").build());
				row.add(Plot.builder().label(tenantIdStyled).name("DDRs").symbol("text").build());

				row.add(Plot.builder().name("Pending_from_0_to_3_days").value(new BigDecimal(String.valueOf(tenantWiseRow.get("pending_from_0_to_3_days")))).symbol("number").build());				
				row.add(Plot.builder().name("Pending_from_3_to_7_days").value(new BigDecimal(String.valueOf(tenantWiseRow.get("pending_from_3_to_7_days")))).symbol("number").build());				
				row.add(Plot.builder().name("Pending_from_7_to_15_days").value(new BigDecimal(String.valueOf(tenantWiseRow.get("pending_from_7_to_15_days")))).symbol("number").build());				
				row.add(Plot.builder().name("Pending_from_more_than_15_days").value(new BigDecimal(String.valueOf(tenantWiseRow.get("pending_from_more_than_15_days")))).symbol("number").build());				
				row.add(Plot.builder().name("Total_Pending_Applications").value(new BigDecimal(String.valueOf(tenantWiseRow.get("total_pending_applications")))).symbol("number").build());				

				 response.add(Data.builder().headerName(tenantIdStyled).headerValue(serailNumber).plots(row).insight(null).build());
			 }	

				if (CollectionUtils.isEmpty(response)) {
					serailNumber++;
					List<Plot> row = new ArrayList<>();
					row.add(Plot.builder().label(String.valueOf(serailNumber)).name("S.N.").symbol("text").build());
					row.add(Plot.builder().label(payloadDetails.getTenantid()).name("DDRs").symbol("text").build());
					row.add(Plot.builder().name("Pending_from_0_to_3_days").value(BigDecimal.ZERO).symbol("number")
							.build());
					row.add(Plot.builder().name("Pending_from_3_to_7_days").value(BigDecimal.ZERO).symbol("number")
							.build());
					row.add(Plot.builder().name("Pending_from_7_to_15_days").value(BigDecimal.ZERO).symbol("number")
							.build());
					row.add(Plot.builder().name("Pending_from_more_than_15_days").value(BigDecimal.ZERO)
							.symbol("number").build());
					row.add(Plot.builder().name("Total_Pending_Applications").value(BigDecimal.ZERO).symbol("number")
							.build());
					response.add(Data.builder().headerName(payloadDetails.getTenantid()).headerValue(serailNumber)
							.plots(row).insight(null).build());
				}

		return response;
	}

	public List<Data> wsTotalApplications(PayloadDetails payloadDetails) {
		WaterSearchCriteria criteria = getWaterSearchCriteria(payloadDetails);
		criteria.setExcludedTenantId(DashboardConstants.TESTING_TENANT);
		criteria.setApplicationType(Sets.newHashSet(DashboardConstants.WS_NEW_CONNECTIONS));
		criteria.setApplicationStatusExclude(Sets.newHashSet(DashboardConstants.STATUS_INITIATED));
		Integer wsTotalApplications =  (Integer) wsRepository.getWsTotalApplicationsCount(criteria);
		return Arrays.asList(Data.builder().headerValue(wsTotalApplications).build());
	}

	public List<Data> totalCumulativeActiveConnections(PayloadDetails payloadDetails) {
		WaterSearchCriteria criteria = getWaterSearchCriteria(payloadDetails);
		criteria.setStatus(DashboardConstants.WS_CONNECTION_ACTIVATED);
		criteria.setExcludedTenantId(DashboardConstants.TESTING_TENANT);
		criteria.setIsOldApplication(Boolean.FALSE);
		criteria.setFromDate(null);
		criteria.setToDate(null);
		Integer activeConnectionCount =  (Integer) wsRepository.getActiveWaterConnectionCount(criteria);
		return Arrays.asList(Data.builder().headerValue(activeConnectionCount).build());
	}

	public HashMap<String, Long> totalApplicationsTenantWise(PayloadDetails payloadDetails) {
		WaterSearchCriteria criteria = getWaterSearchCriteria(payloadDetails);
		criteria.setExcludedTenantId(DashboardConstants.TESTING_TENANT);
		HashMap<String, Long> totalApplication = wsRepository.getTenantWiseTotalApplication(criteria);
        return totalApplication;
	}

	public HashMap<String, Long> wsTotalCompletedApplicationsTenantWise(PayloadDetails payloadDetails) {
		WaterSearchCriteria criteria = getWaterSearchCriteria(payloadDetails);
		criteria.setExcludedTenantId(DashboardConstants.TESTING_TENANT);
		criteria.setStatus(DashboardConstants.WS_CONNECTION_ACTIVATED);
		HashMap<String, Long> totalApplication = wsRepository.getTenantWiseTotalApplication(criteria);
        return totalApplication;
	}
	
	public List<Data> wsCumulativeConnectionByUsage(PayloadDetails payloadDetails) {
		WaterSearchCriteria criteria = getWaterSearchCriteria(payloadDetails);
		criteria.setStatus(DashboardConstants.WS_CONNECTION_ACTIVATED);
		criteria.setExcludedTenantId(DashboardConstants.TESTING_TENANT);
		criteria.setIsOldApplication(Boolean.FALSE);
		criteria.setFromDate(null);
		criteria.setToDate(null);
		List<Chart> wsConnectionsByUsageType = wsRepository.getwsConnectionsByUsageType(criteria);

		List<Plot> plots = new ArrayList();
		Long total = 0L;
		total = extractDataForChart(wsConnectionsByUsageType, plots ,total);		 

		return Arrays.asList(Data.builder().headerName("DSS_WS_CUMULATIVE_CONNECTION_BY_USAGE").headerValue(total).plots(plots).build());
	}
	
	public List<Data> wsCumulativeConnectionByType(PayloadDetails payloadDetails) {
		WaterSearchCriteria criteria = getWaterSearchCriteria(payloadDetails);
		criteria.setStatus(DashboardConstants.WS_CONNECTION_ACTIVATED);
		criteria.setExcludedTenantId(DashboardConstants.TESTING_TENANT);
		criteria.setIsOldApplication(Boolean.FALSE);
		criteria.setFromDate(null);
		criteria.setToDate(null);
		List<Chart> wsConnectionsByType = wsRepository.getwsConnectionsByType(criteria);

		List<Plot> plots = new ArrayList();
		Long total = 0L;
		total = extractDataForChart(wsConnectionsByType, plots ,total);	 

		return Arrays.asList(Data.builder().headerName("DSS_WS_CUMULATIVE_CONNECTION_BY_TYPE").headerValue(total).plots(plots).build());
	}

}
