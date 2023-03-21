package org.egov.dss.service;

import java.util.List;

import org.egov.dss.constants.DashboardConstants;
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
	
	@Autowired
	private TLService tlService;
	
	@Autowired
	private MRService mrService;
	
	@Autowired
	private BPAService bpaService;
	
	@Autowired
	private PGRService pgrService;
	
	@Autowired
	private CommonService commonService;
	
	public List<Data> redirect(RequestInfoWrapper requestInfoWrapper) {
		String visualizationCode=requestInfoWrapper.getPayloadDetails().getVisualizationcode();
		
		if(Constants.VisualizationCodes.REVENUE_TOTAL_COLLECTION.equalsIgnoreCase(visualizationCode)) {
			return revenueService.totalCollection(requestInfoWrapper.getPayloadDetails());
		}
		
    	if(Constants.VisualizationCodes.REVENUE_TODAYS_COLLECTION.equalsIgnoreCase(visualizationCode)) {
			return revenueService.todaysCollection(requestInfoWrapper.getPayloadDetails());
		}
		if(Constants.VisualizationCodes.REVENUE_TARGET_COLLECTION.equalsIgnoreCase(visualizationCode)) {
			return revenueService.targetCollection(requestInfoWrapper.getPayloadDetails());
		}
		if(Constants.VisualizationCodes.REVENUE_TARGET_ACHIEVED.equalsIgnoreCase(visualizationCode)) {
			return revenueService.targetAchieved(requestInfoWrapper.getPayloadDetails());
		}
		if(Constants.VisualizationCodes.REVENUE_TOTAL_MUTATION_FEE_CALCULATION.equalsIgnoreCase(visualizationCode)) {
			return revenueService.totalMutationFeeCollection(requestInfoWrapper.getPayloadDetails());
		}
		if(Constants.VisualizationCodes.REVENUE_CUMULATIVE_COLLECTION.equalsIgnoreCase(visualizationCode)) {
			return revenueService.cumulativeCollection(requestInfoWrapper.getPayloadDetails());
		}
		if(Constants.VisualizationCodes.REVENUE_TOP_PERFORMING_ULBS.equalsIgnoreCase(visualizationCode)) {
			return revenueService.topPerformingUlbs(requestInfoWrapper.getPayloadDetails());
		}
		if(Constants.VisualizationCodes.REVENUE_BOTTOM_PERFORMING_ULBS.equalsIgnoreCase(visualizationCode)) {
			return revenueService.bottomPerformingUlbs(requestInfoWrapper.getPayloadDetails());
		}
		if(Constants.VisualizationCodes.REVENUE_COLLECTION_BY_USAGE_TYPE.equalsIgnoreCase(visualizationCode)) {
			return revenueService.collectionByUsageType(requestInfoWrapper.getPayloadDetails());
		}
		if(Constants.VisualizationCodes.REVENUE_DEMAND_COLLECTION_INDEX_DDR_REVENUE.equalsIgnoreCase(visualizationCode)) {
			return revenueService.demandCollectionIndexDDRRevenue(requestInfoWrapper.getPayloadDetails());
		}
		if(Constants.VisualizationCodes.REVENUE_TAXHEADS_BREAKUP_DDR_REVENUE.equalsIgnoreCase(visualizationCode)) {
			return revenueService.taxheadsBreakupDDRRevenue(requestInfoWrapper.getPayloadDetails());
		}
		if(Constants.VisualizationCodes.REVENUE_TAXHEADS_BREAKUP_USAGE.equalsIgnoreCase(visualizationCode)) {
			return revenueService.taxHeadsBreakupUsage(requestInfoWrapper.getPayloadDetails());
		}
		if(Constants.VisualizationCodes.REVENUE_DEMAND_COLLECTION_INDEX_USAGE_REVENUE.equalsIgnoreCase(visualizationCode)) {
			return revenueService.demandCollectionIndexUsageRevenue(requestInfoWrapper.getPayloadDetails());
		}
		if(Constants.VisualizationCodes.SERVICE_TOTAL_PROPERTIES.equalsIgnoreCase(visualizationCode)) {
			return ptService.totalProprties(requestInfoWrapper.getPayloadDetails());
		}
		if(Constants.VisualizationCodes.SERVICE_PROPERTIES_PAID.equalsIgnoreCase(visualizationCode)) {
			return ptService.propertiesPaid(requestInfoWrapper.getPayloadDetails());
		}
		if(Constants.VisualizationCodes.SERVICE_PROPERTIES_ASSESSED.equalsIgnoreCase(visualizationCode)) {
			return ptService.propertiesAssessed(requestInfoWrapper.getPayloadDetails());
		}
		if(Constants.VisualizationCodes.SERVICE_ACTIVE_ULBS.equalsIgnoreCase(visualizationCode)) {
			return ptService.activeUlbs(requestInfoWrapper.getPayloadDetails());
		}
		if(Constants.VisualizationCodes.SERVICE_TOTAL_MUTATION_PROPERTIES.equalsIgnoreCase(visualizationCode)) {
			return ptService.totalMutationProperties(requestInfoWrapper.getPayloadDetails());
		}
		if(Constants.VisualizationCodes.SERVICE_PT_TOTAL_APPLICATIONS.equalsIgnoreCase(visualizationCode)) {
			return ptService.ptTotalApplications(requestInfoWrapper.getPayloadDetails());
		}
		if(Constants.VisualizationCodes.SERVICE_TOTAL_NO_OF_PROPERTIES.equalsIgnoreCase(visualizationCode)) {
			return ptService.totalnoOfProperties(requestInfoWrapper.getPayloadDetails());
		}
		if(Constants.VisualizationCodes.SERVICE_CUMULATIVE_PROPERTIES_ASSESSED.equalsIgnoreCase(visualizationCode)) {
			return ptService.cumulativePropertiesAssessed(requestInfoWrapper.getPayloadDetails());
		}
		if(Constants.VisualizationCodes.SERVICE_TOP_PERFORMING_ULBS_COMPLETION_RATE.equalsIgnoreCase(visualizationCode)) {
			return ptService.topPerformingUlbsCompletionRate(requestInfoWrapper.getPayloadDetails());
		}
		if(Constants.VisualizationCodes.SERVICE_WS_TOTAL_ACTIVE_CONNECTIONS.equalsIgnoreCase(visualizationCode)) {
			return wsService.totalActiveConnections(requestInfoWrapper.getPayloadDetails());
		}
		if (Constants.VisualizationCodes.SERVICE_DSS_TOTAL_APPLICATION_OVERVIEW.equalsIgnoreCase(visualizationCode)) {
			if (requestInfoWrapper.getPayloadDetails().getModulelevel()
					.equalsIgnoreCase(DashboardConstants.BUSINESS_SERVICE_TL))
				return tlService.totalApplications(requestInfoWrapper.getPayloadDetails());
			else
				return commonService.totalApplication(requestInfoWrapper.getPayloadDetails());
		}
		if(Constants.VisualizationCodes.SERVICE_MR_TOTAL_APPLICATION.equalsIgnoreCase(visualizationCode)) {
			return mrService.totalApplications(requestInfoWrapper.getPayloadDetails());
		}
		if(Constants.VisualizationCodes.SERVICE_PGR_TOTAL_COMPLAINTS.equalsIgnoreCase(visualizationCode)) {
			return pgrService.totalApplications(requestInfoWrapper.getPayloadDetails());
		}
		if(Constants.VisualizationCodes.SERVICE_OBPS_TOTAL_PERMITS.equalsIgnoreCase(visualizationCode)) {
			return bpaService.totalPermitIssued(requestInfoWrapper.getPayloadDetails());
		}
		if(Constants.VisualizationCodes.REVENUE_MR_TOTAL_COLLECTION.equalsIgnoreCase(visualizationCode)) {
			return revenueService.totalCollection(requestInfoWrapper.getPayloadDetails());
		}
		if(Constants.VisualizationCodes.REVENUE_BPA_TOTAL_COLLECTION.equalsIgnoreCase(visualizationCode)) {
			return revenueService.totalCollection(requestInfoWrapper.getPayloadDetails());
		}
		if(Constants.VisualizationCodes.REVENUE_WS_TOTAL_COLLECTION.equalsIgnoreCase(visualizationCode)) {
			return revenueService.totalCollection(requestInfoWrapper.getPayloadDetails());
		}
		if (Constants.VisualizationCodes.SERVICE_PGR_SLA_ACHIEVED.equalsIgnoreCase(visualizationCode)) {
			return pgrService.slaAchieved(requestInfoWrapper.getPayloadDetails());
		}
		if (Constants.VisualizationCodes.SERVICE_DSS_SLA_ACHIEVED_OVERVIEW.equalsIgnoreCase(visualizationCode)) {
			  return commonService.slaAchieved(requestInfoWrapper.getPayloadDetails());
		}
		
		return null;

	}

}
