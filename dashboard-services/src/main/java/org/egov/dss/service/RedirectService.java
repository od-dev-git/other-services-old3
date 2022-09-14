package org.egov.dss.service;

import java.util.List;

import org.egov.dss.util.Constants;
import org.egov.dss.web.model.Data;
import org.egov.dss.web.model.RequestInfoWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RedirectService {
	
	@Autowired
	private RevenueService revenueService;
	
	@Autowired
	private PTService ptService;
	
	@Autowired
	private WSService wsService;
	
	public List<Data> redirect(RequestInfoWrapper requestInfoWrapper) {
		String visualizationCode=requestInfoWrapper.getChartCriteria().getVisualizationCode();
		
		if(Constants.VisualizationCodes.REVENUE_TOTAL_COLLECTION.equalsIgnoreCase(visualizationCode)) {
			return revenueService.totalCollection(requestInfoWrapper.getChartCriteria());
		}
		if(Constants.VisualizationCodes.REVENUE_TODAYS_COLLECTION.equalsIgnoreCase(visualizationCode)) {
			return revenueService.todaysCollection(requestInfoWrapper.getChartCriteria());
		}
		if(Constants.VisualizationCodes.REVENUE_TARGET_COLLECTION.equalsIgnoreCase(visualizationCode)) {
			return revenueService.targetCollection(requestInfoWrapper.getChartCriteria());
		}
		if(Constants.VisualizationCodes.REVENUE_TARGET_ACHIEVED.equalsIgnoreCase(visualizationCode)) {
			return revenueService.targetAchieved(requestInfoWrapper.getChartCriteria());
		}
		if(Constants.VisualizationCodes.REVENUE_TOTAL_MUTATION_FEE_CALCULATION.equalsIgnoreCase(visualizationCode)) {
			return revenueService.totalMutationFeeCollection(requestInfoWrapper.getChartCriteria());
		}
		if(Constants.VisualizationCodes.REVENUE_CUMULATIVE_COLLECTION.equalsIgnoreCase(visualizationCode)) {
			return revenueService.cumulativeCollection(requestInfoWrapper.getChartCriteria());
		}
		if(Constants.VisualizationCodes.REVENUE_TOP_PERFORMING_ULBS.equalsIgnoreCase(visualizationCode)) {
			return revenueService.topPerformingUlbs(requestInfoWrapper.getChartCriteria());
		}
		if(Constants.VisualizationCodes.REVENUE_BOTTOM_PERFORMING_ULBS.equalsIgnoreCase(visualizationCode)) {
			return revenueService.bottomPerformingUlbs(requestInfoWrapper.getChartCriteria());
		}
		if(Constants.VisualizationCodes.REVENUE_COLLECTION_BY_USAGE_TYPE.equalsIgnoreCase(visualizationCode)) {
			return revenueService.collectionByUsageType(requestInfoWrapper.getChartCriteria());
		}
		if(Constants.VisualizationCodes.REVENUE_DEMAND_COLLECTION_INDEX_DDR_REVENUE.equalsIgnoreCase(visualizationCode)) {
			return revenueService.demandCollectionIndexDDRRevenue(requestInfoWrapper.getChartCriteria());
		}
		if(Constants.VisualizationCodes.REVENUE_TAXHEADS_BREAKUP_DDR_REVENUE.equalsIgnoreCase(visualizationCode)) {
			return revenueService.taxheadsBreakupDDRRevenue(requestInfoWrapper.getChartCriteria());
		}
		if(Constants.VisualizationCodes.SERVICE_TOTAL_PROPERTIES.equalsIgnoreCase(visualizationCode)) {
			return ptService.totalProprties(requestInfoWrapper.getChartCriteria());
		}
		if(Constants.VisualizationCodes.SERVICE_PROPERTIES_PAID.equalsIgnoreCase(visualizationCode)) {
			return ptService.propertiesPaid(requestInfoWrapper.getChartCriteria());
		}
		if(Constants.VisualizationCodes.SERVICE_PROPERTIES_ASSESSED.equalsIgnoreCase(visualizationCode)) {
			return ptService.propertiesAssessed(requestInfoWrapper.getChartCriteria());
		}
		if(Constants.VisualizationCodes.SERVICE_ACTIVE_ULBS.equalsIgnoreCase(visualizationCode)) {
			return ptService.activeUlbs(requestInfoWrapper.getChartCriteria());
		}
		if(Constants.VisualizationCodes.SERVICE_TOTAL_MUTATION_PROPERTIES.equalsIgnoreCase(visualizationCode)) {
			return ptService.totalMutationProperties(requestInfoWrapper.getChartCriteria());
		}
		if(Constants.VisualizationCodes.SERVICE_PT_TOTAL_APPLICATIONS.equalsIgnoreCase(visualizationCode)) {
			return ptService.ptTotalApplications(requestInfoWrapper.getChartCriteria());
		}
		if(Constants.VisualizationCodes.SERVICE_TOTAL_NO_OF_PROPERTIES.equalsIgnoreCase(visualizationCode)) {
			return ptService.totalnoOfProperties(requestInfoWrapper.getChartCriteria());
		}
		if(Constants.VisualizationCodes.SERVICE_CUMULATIVE_PROPERTIES_ASSESSED.equalsIgnoreCase(visualizationCode)) {
			return ptService.cumulativePropertiesAssessed(requestInfoWrapper.getChartCriteria());
		}
		if(Constants.VisualizationCodes.SERVICE_TOP_PERFORMING_ULBS_COMPLETION_RATE.equalsIgnoreCase(visualizationCode)) {
			return ptService.topPerformingUlbsCompletionRate(requestInfoWrapper.getChartCriteria());
		}
		
		
		return null;

	}

}
