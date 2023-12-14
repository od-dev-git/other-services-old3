package org.egov.dss.service;

import java.util.List;

import org.egov.dss.constants.DashboardConstants;
import org.egov.dss.util.Constants;
import org.egov.dss.web.model.Data;
import org.egov.dss.web.model.RequestInfoWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Slf4j
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
	private RegularizationService regularizationService;
	
	@Autowired
	private PGRService pgrService;
	
	@Autowired
	private CommonService commonService;
	
	@Autowired
	private URCService urcService;
    @Autowired
	private USMService usmService;
	
	public List<Data> redirect(RequestInfoWrapper requestInfoWrapper) {
		String visualizationCode=requestInfoWrapper.getPayloadDetails().getVisualizationcode();
		log.info("Currently Processing :"+visualizationCode+"  Module level: "+requestInfoWrapper.getPayloadDetails().getModulelevel());
		if(Constants.VisualizationCodes.REVENUE_TOTAL_COLLECTION.equalsIgnoreCase(visualizationCode)) {
			return revenueService.totalCollection(requestInfoWrapper.getPayloadDetails());
		}
		
		if(Constants.VisualizationCodes.REVENUE_CURRENT_COLLECTION.equalsIgnoreCase(visualizationCode)) {
			return revenueService.currentCollection(requestInfoWrapper.getPayloadDetails());
		}
		
		if(Constants.VisualizationCodes.REVENUE_ARREAR_COLLECTION.equalsIgnoreCase(visualizationCode)) {
			return revenueService.arrearCollection(requestInfoWrapper.getPayloadDetails());
		}
		
		if(Constants.VisualizationCodes.REVENUE_PREVIOUS_YEAR_COLLECTION.equalsIgnoreCase(visualizationCode)) {
			return revenueService.previousYearCollection(requestInfoWrapper.getPayloadDetails());
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
		if (Constants.VisualizationCodes.REVENUE_CUMULATIVE_COLLECTION.equalsIgnoreCase(visualizationCode)) {
			if (requestInfoWrapper.getPayloadDetails().getModulelevel()
					.equalsIgnoreCase(DashboardConstants.BS_HOME_REVENUE)) {
				return revenueService.cumulativeCollectionOverview(requestInfoWrapper.getPayloadDetails());
			} else {
				return revenueService.cumulativeCollection(requestInfoWrapper.getPayloadDetails());
			}
		}
		if (Constants.VisualizationCodes.REVENUE_TOP_PERFORMING_ULBS.equalsIgnoreCase(visualizationCode)) {
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
		if(Constants.VisualizationCodes.REVENUE_PT_TAX_HEADS_BREAKUP.equalsIgnoreCase(visualizationCode)) {
			return revenueService.revenuePTTaxHeadsBreakup(requestInfoWrapper.getPayloadDetails());
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
		if(Constants.VisualizationCodes.SERVICE_TOTAL_NO_OF_DEACTIVATED_PROPERTIES.equalsIgnoreCase(visualizationCode)) {
			return ptService.totalNoOfDeactivatedProperties(requestInfoWrapper.getPayloadDetails());
		}
		if(Constants.VisualizationCodes.SERVICE_PT_NEW_ASSESSMENT_SHARE.equalsIgnoreCase(visualizationCode)) {
			return ptService.ptNewAssessmentShare(requestInfoWrapper.getPayloadDetails());
		}
		if(Constants.VisualizationCodes.SERVICE_PT_RE_ASSESSMENT_SHARE.equalsIgnoreCase(visualizationCode)) {
			return ptService.ptReAssessmentShare(requestInfoWrapper.getPayloadDetails());
		}
		if(Constants.VisualizationCodes.SERVICE_CUMULATIVE_PROPERTIES_ASSESSED.equalsIgnoreCase(visualizationCode)) {
			return ptService.cumulativePropertiesAssessed(requestInfoWrapper.getPayloadDetails());
		}
		if(Constants.VisualizationCodes.SERVICE_PROPERTIES_BY_USAGE_TYPE.equalsIgnoreCase(visualizationCode)) {
			return ptService.propertiesByUsageType(requestInfoWrapper.getPayloadDetails());
		}
		if (Constants.VisualizationCodes.SERVICE_TOP_PERFORMING_ULBS_COMPLETION_RATE.equalsIgnoreCase(visualizationCode)) {
			if (requestInfoWrapper.getPayloadDetails().getModulelevel()
					.equalsIgnoreCase(DashboardConstants.BUSINESS_SERVICE_PT)) {
				return ptService.topPerformingUlbsCompletionRate(requestInfoWrapper.getPayloadDetails());
			} else if (requestInfoWrapper.getPayloadDetails().getModulelevel()
					.equalsIgnoreCase(DashboardConstants.BUSINESS_SERVICE_TL)) {
				return tlService.topPerformingUlbsCompletionRate(requestInfoWrapper.getPayloadDetails());
			}

		}
		if(Constants.VisualizationCodes.SERVICE_BOTTOM_PERFORMING_ULBS_COMPLETION_RATE.equalsIgnoreCase(visualizationCode)) {
			if (requestInfoWrapper.getPayloadDetails().getModulelevel()
					.equalsIgnoreCase(DashboardConstants.BUSINESS_SERVICE_PT)) {
				return ptService.bottomPerformingUlbsCompletionRate(requestInfoWrapper.getPayloadDetails());
			} else if (requestInfoWrapper.getPayloadDetails().getModulelevel()
					.equalsIgnoreCase(DashboardConstants.BUSINESS_SERVICE_TL)) {
				return tlService.bottomPerformingUlbsCompletionRate(requestInfoWrapper.getPayloadDetails());
			}
		}
		if(Constants.VisualizationCodes.SERVICE_PT_SHARE_OF_NEW_ASSESSMENT.equalsIgnoreCase(visualizationCode)) {
			return ptService.ptShareOfNewAssessment(requestInfoWrapper.getPayloadDetails());
		}
		if(Constants.VisualizationCodes.SERVICE_PT_SHARE_OF_RE_ASSESSMENT.equalsIgnoreCase(visualizationCode)) {
			return ptService.ptShareOfReAssessment(requestInfoWrapper.getPayloadDetails());
		}
		if(Constants.VisualizationCodes.SERVICE_XPTFY_BY_DDR.equalsIgnoreCase(visualizationCode)) {
			return ptService.ptByFinancalYear(requestInfoWrapper.getPayloadDetails());
		}
		if(Constants.VisualizationCodes.SERVICE_PT_ASMT_STATUS_DDR.equalsIgnoreCase(visualizationCode)) {
            return ptService.ptAsmtStatusDDR(requestInfoWrapper.getPayloadDetails());
        }
		
		if(Constants.VisualizationCodes.SERVICE_PT_STATUS_BY_BOUNDARY.equalsIgnoreCase(visualizationCode)) {
            return ptService.ptStatusByBoundary(requestInfoWrapper.getPayloadDetails());
        }
		
		if(Constants.VisualizationCodes.SERVICE_PT_APPLICATIONS_AGEING.equalsIgnoreCase(visualizationCode)) {
            return ptService.ptApplicationsAgeing(requestInfoWrapper.getPayloadDetails());
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
		if(Constants.VisualizationCodes.SERVICE_OBPS_TOTAL_APPLICATIONS_RECEIVED.equalsIgnoreCase(visualizationCode)) {
			return bpaService.totalApplicationsReceived(requestInfoWrapper.getPayloadDetails());
		}
		if(Constants.VisualizationCodes.SERVICE_OBPS_TOTAL_APPLICATIONS_REJECTED.equalsIgnoreCase(visualizationCode)) {
			return bpaService.totalApplicationsRejected(requestInfoWrapper.getPayloadDetails());
		}
		if(Constants.VisualizationCodes.SERVICE_OBPS_TOTAL_APPLICATIONS_PENDING.equalsIgnoreCase(visualizationCode)) {
			return bpaService.totalApplicationsPending(requestInfoWrapper.getPayloadDetails());
		}
		if(Constants.VisualizationCodes.SERVICE_OBPS_AVG_DAYS_TO_ISSUE_PERMIT.equalsIgnoreCase(visualizationCode)) {
			return bpaService.avgDaysToIssuePermit(requestInfoWrapper.getPayloadDetails());
		}
		if(Constants.VisualizationCodes.SERVICE_OBPS_MIN_DAYS_TO_ISSUE_PERMIT.equalsIgnoreCase(visualizationCode)) {
			return bpaService.minDaysToIssuePermit(requestInfoWrapper.getPayloadDetails());
		}
		if(Constants.VisualizationCodes.SERVICE_OBPS_MAX_DAYS_TO_ISSUE_PERMIT.equalsIgnoreCase(visualizationCode)) {
			return bpaService.maxDaysToIssuePermit(requestInfoWrapper.getPayloadDetails());
		}
		if(Constants.VisualizationCodes.SERVICE_OBPS_SLA_COMPLIANCE_PERMIT.equalsIgnoreCase(visualizationCode)) {
			return bpaService.slaCompliancePermit(requestInfoWrapper.getPayloadDetails());
		}
		if(Constants.VisualizationCodes.SERVICE_OBPS_SLA_COMPLIANCE_OTHER_THAN_LOW_RISK.equalsIgnoreCase(visualizationCode)) {
			return bpaService.slaComplianceOtherThanLowRisk(requestInfoWrapper.getPayloadDetails());
		}
		if(Constants.VisualizationCodes.SERVICE_OBPS_SLA_COMPLIANCE_PREAPPROVED_PLAN.equalsIgnoreCase(visualizationCode)) {
			return bpaService.slaCompliancePreApprovedPlan(requestInfoWrapper.getPayloadDetails());
		}
		if(Constants.VisualizationCodes.SERVICE_OBPS_SLA_COMPLIANCE_BUILDING_PERMIT.equalsIgnoreCase(visualizationCode)) {
			return bpaService.slaComplianceBuildingPermit(requestInfoWrapper.getPayloadDetails());
		}
		if(Constants.VisualizationCodes.SERVICE_OBPS_TOP_ULB_BY_PERFORMANCE.equalsIgnoreCase(visualizationCode)) {
			return bpaService.topUlbByPerformance(requestInfoWrapper.getPayloadDetails());
		}
		if(Constants.VisualizationCodes.SERVICE_OBPS_BOTTOM_ULB_BY_PERFORMANCE.equalsIgnoreCase(visualizationCode)) {
			return bpaService.bottomUlbByPerformance(requestInfoWrapper.getPayloadDetails());
		}
		if(Constants.VisualizationCodes.SERVICE_OBPS_TOP_PERFORMING_ULBS_TABLE.equalsIgnoreCase(visualizationCode)) {
			return bpaService.topPerformingUlbsTable(requestInfoWrapper.getPayloadDetails());
		}
		if(Constants.VisualizationCodes.SERVICE_OBPS_BOTTOM_PERFORMING_ULBS_TABLE.equalsIgnoreCase(visualizationCode)) {
			return bpaService.bottomPerformingUlbsTable(requestInfoWrapper.getPayloadDetails());
		}
		if(Constants.VisualizationCodes.SERVICE_OBPS_PERMITS_AND_OC_ISSUED_AND_OC_SUBMITTED.equalsIgnoreCase(visualizationCode)) {
			return bpaService.permitsAndOcIssuedAndOcSubmitted(requestInfoWrapper.getPayloadDetails());
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
		if(Constants.VisualizationCodes.DIGITAL_COLLECTIONS_BY_VALUE.equals(visualizationCode)) {
			return revenueService.digitalCollectionsByValue(requestInfoWrapper.getPayloadDetails());
		}
		if(Constants.VisualizationCodes.DIGITAL_COLLECTIONS_BY_VOLUME.equals(visualizationCode)) {
			return revenueService.digitalCollectionsByVolume(requestInfoWrapper.getPayloadDetails());
		}
		
		if(Constants.VisualizationCodes.REVENUE_PT_GROWTH_RATE.equals(visualizationCode)) {
			return revenueService.revenueGrowthRate(requestInfoWrapper.getPayloadDetails());
		}
		if(Constants.VisualizationCodes.REVENUE_PT_GROWTH_RATE_TABLE.equals(visualizationCode)) {
			return revenueService.revenueGrowthRateTable(requestInfoWrapper.getPayloadDetails());
		}
		if(Constants.VisualizationCodes.REVENUE_PT_TOP_ULBS_BY_DIGITAL_COLLECTION.equals(visualizationCode)) {
			return revenueService.topUlbsDigitalCollectionByValue(requestInfoWrapper.getPayloadDetails());
		}
		if(Constants.VisualizationCodes.REVENUE_PT_BOTTOM_ULBS_BY_DIGITAL_COLLECTION.equals(visualizationCode)) {
			return revenueService.bottomUlbsDigitalCollectionByValue(requestInfoWrapper.getPayloadDetails());
		}
		if(Constants.VisualizationCodes.REVENUE_PT_TOP_ULBS_COLLECTION_BY_VOLUME.equals(visualizationCode)) {
			return revenueService.topUlbsDigitalCollectionByVolume(requestInfoWrapper.getPayloadDetails());
		}
		if(Constants.VisualizationCodes.REVENUE_PT_BOTTOM_ULBS_COLLECTION_BY_VOLUME.equals(visualizationCode)) {
			return revenueService.bottomUlbsDigitalCollectionByVolume(requestInfoWrapper.getPayloadDetails());
		}
		if(Constants.VisualizationCodes.REVENUE_PT_PREVIOUS_YEAR_TARGET_ACHIEVED.equals(visualizationCode)) {
			return revenueService.previousYearTargetAchieved(requestInfoWrapper.getPayloadDetails());
		}
		
		if(Constants.VisualizationCodes.REVENUE_PT_FINANCIAL_INDICATORS.equals(visualizationCode)) {
			return revenueService.ptFinancialIndicatorsData(requestInfoWrapper.getPayloadDetails());
		}
		
		if(Constants.VisualizationCodes.REVENUE_PT_PAYMENT_MODE_DDR.equals(visualizationCode)) {
			return revenueService.ptPaymentModeData(requestInfoWrapper.getPayloadDetails());
		}
		
		if(Constants.VisualizationCodes.REVENUE_ALL_PAYMENT_MODE_COLLECTION.equals(visualizationCode)) {
			return revenueService.paymentModeCollection(requestInfoWrapper.getPayloadDetails());
		}
		
		if(Constants.VisualizationCodes.REVENUE_WS_APP_FEE_COLLECTION.equals(visualizationCode)) {
			return revenueService.getWsApplicationFeeCollection(requestInfoWrapper.getPayloadDetails());
		}
		
		if(Constants.VisualizationCodes.REVENUE_WS_DEMAND_FEE_COLLECTION.equals(visualizationCode)) {
			return revenueService.getWsDemandFeeCollection(requestInfoWrapper.getPayloadDetails());
		}
		
		if(Constants.VisualizationCodes.REVENUE_WS_CUMULATIVE_COLLECTION.equals(visualizationCode)) {
			return revenueService.getWSCumulativeCollection(requestInfoWrapper.getPayloadDetails());
		}
		
		if(Constants.VisualizationCodes.REVENUE_WS_TOP_ULBS_BY_DIGITAL_COLLECTION.equals(visualizationCode)) {
			return revenueService.topUlbsDigitalCollectionByValue(requestInfoWrapper.getPayloadDetails());
		}
		
		if(Constants.VisualizationCodes.REVENUE_WS_BOTTOM_ULBS_BY_DIGITAL_COLLECTION.equals(visualizationCode)) {
			return revenueService.bottomUlbsDigitalCollectionByValue(requestInfoWrapper.getPayloadDetails());
		}
		
		if(Constants.VisualizationCodes.REVENUE_WS_TOP_ULBS_COLLECTION_BY_VOLUME.equals(visualizationCode)) {
			return revenueService.topUlbsDigitalCollectionByVolume(requestInfoWrapper.getPayloadDetails());
		}
		
		if(Constants.VisualizationCodes.REVENUE_WS_BOTTOM_ULBS_COLLECTION_BY_VOLUME.equals(visualizationCode)) {
			return revenueService.bottomUlbsDigitalCollectionByVolume(requestInfoWrapper.getPayloadDetails());
		}
		
		if(Constants.VisualizationCodes.REVENUE_WS_COLLECTION_BY_USAGE_TYPE.equalsIgnoreCase(visualizationCode)) {
			return revenueService.wsCollectionByUsageType(requestInfoWrapper.getPayloadDetails());
		}
		
		if(Constants.VisualizationCodes.REVENUE_WS_COLLECTION_BY_CHANNEL.equalsIgnoreCase(visualizationCode)) {
			return revenueService.wsCollectionByChannel(requestInfoWrapper.getPayloadDetails());
		}
		
		if(Constants.VisualizationCodes.REVENUE_WS_TAX_HEADS_BREAKUP.equalsIgnoreCase(visualizationCode)) {
			return revenueService.getWSTaxHeadsBreakup(requestInfoWrapper.getPayloadDetails());
		}
		
		if(Constants.VisualizationCodes.REVENUE_WS_KEY_FINANCIAL_INDICATOR.equalsIgnoreCase(visualizationCode)) {
			return revenueService.getWSKeyFinancialIndicators(requestInfoWrapper.getPayloadDetails());
		}
		
		if(Constants.VisualizationCodes.SERVICE_WS_ACTIVE_METERED_WATER_CONNECTIONS.equalsIgnoreCase(visualizationCode)) {
			return wsService.totalActiveMeteredWaterConnections(requestInfoWrapper.getPayloadDetails());
		}
		if(Constants.VisualizationCodes.SERVICE_WS_ACTIVE_NON_METERED_WATER_CONNECTIONS.equalsIgnoreCase(visualizationCode)) {
			return wsService.totalActiveNonMeteredWaterConnections(requestInfoWrapper.getPayloadDetails());
		}
		if(Constants.VisualizationCodes.SERVICE_WS_TOTAL_WATER_CONNECTIONS.equalsIgnoreCase(visualizationCode)) {
			return wsService.totalActiveWaterConnections(requestInfoWrapper.getPayloadDetails());
		}
		if(Constants.VisualizationCodes.SERVICE_WS_TOTAL_SEWERAGE_CONNECTIONS.equalsIgnoreCase(visualizationCode)) {
			return wsService.totalActiveSewerageConnections(requestInfoWrapper.getPayloadDetails());
		}
		if(Constants.VisualizationCodes.SERVICE_WS_TOTAL_WATER_SEWERAGE_CONNECTIONS.equalsIgnoreCase(visualizationCode)) {
			return wsService.totalActiveWaterSewerageConnections(requestInfoWrapper.getPayloadDetails());
		}
		if(Constants.VisualizationCodes.SERVICE_WS_CUMULATIVE_CONNECTIONS.equalsIgnoreCase(visualizationCode)) {
			return wsService.cumulativeConnections(requestInfoWrapper.getPayloadDetails());
		}
		if(Constants.VisualizationCodes.SERVICE_WS_TOTAL_CUMULATIVE_ACTIVE_CONNECTIONS.equalsIgnoreCase(visualizationCode)) {
			return wsService.totalCumulativeActiveConnections(requestInfoWrapper.getPayloadDetails());
		}
		if(Constants.VisualizationCodes.SERVICE_WS_CONNECTIONS_BY_USAGE_TYPE.equalsIgnoreCase(visualizationCode)) {
			return wsService.wsConnectionsByUsageType(requestInfoWrapper.getPayloadDetails());
		}
		if(Constants.VisualizationCodes.SERVICE_WS_CONNECTIONS_BY_TYPE.equalsIgnoreCase(visualizationCode)) {
			return wsService.wsConnectionsByType(requestInfoWrapper.getPayloadDetails());
		}
		if(Constants.VisualizationCodes.SERVICE_WS_CONNECTION_AGEING.equalsIgnoreCase(visualizationCode)) {
			return wsService.wsConnectionAgeing(requestInfoWrapper.getPayloadDetails());
		}
		if(Constants.VisualizationCodes.SERVICE_WS_SLA_ACHIEVED.equalsIgnoreCase(visualizationCode)) {
			return wsService.slaAchieved(requestInfoWrapper.getPayloadDetails());
		}
		if(Constants.VisualizationCodes.SERVICE_WS_TOTAL_APPLICATIONS.equalsIgnoreCase(visualizationCode)) {
			return wsService.wsTotalApplications(requestInfoWrapper.getPayloadDetails());
		}
		
		if(Constants.VisualizationCodes.REVENUE_BPA_APP_FEE_COLLECTIONS.equalsIgnoreCase(visualizationCode)) {
			return revenueService.bpaFeeCollection(requestInfoWrapper.getPayloadDetails());
		}
		
		if(Constants.VisualizationCodes.REVENUE_BPA_SANC_FEE_COLLECTIONS.equalsIgnoreCase(visualizationCode)) {
			return revenueService.bpaFeeCollection(requestInfoWrapper.getPayloadDetails());
		}
		
		if(Constants.VisualizationCodes.REVENUE_OC_APP_FEE_COLLECTIONS.equalsIgnoreCase(visualizationCode)) {
			return revenueService.bpaFeeCollection(requestInfoWrapper.getPayloadDetails());
		}
		
		if(Constants.VisualizationCodes.REVENUE_OC_SANC_FEE_COLLECTIONS.equalsIgnoreCase(visualizationCode)) {
			return revenueService.bpaFeeCollection(requestInfoWrapper.getPayloadDetails());
		}
		
		if(Constants.VisualizationCodes.REVENUE_BPA_CUMULATIVE_COLLECTION.equalsIgnoreCase(visualizationCode)) {
			return revenueService.getBPACumulativeCollection(requestInfoWrapper.getPayloadDetails());
		}
		
		if(Constants.VisualizationCodes.REVENUE_BPA_TOP_BY_VALUE.equalsIgnoreCase(visualizationCode)) {
			return revenueService.topUlbsDigitalCollectionByValue(requestInfoWrapper.getPayloadDetails());
		}
		
		if(Constants.VisualizationCodes.REVENUE_BPA_BOTTOM_BY_VALUE.equalsIgnoreCase(visualizationCode)) {
			return revenueService.bottomUlbsDigitalCollectionByValue(requestInfoWrapper.getPayloadDetails());
		}
		
		if(Constants.VisualizationCodes.REVENUE_BPA_TOP_BY_VOLUME.equalsIgnoreCase(visualizationCode)) {
			return revenueService.topUlbsDigitalCollectionByVolume(requestInfoWrapper.getPayloadDetails());
		}
		
		if(Constants.VisualizationCodes.REVENUE_BPA_BOTTOM_BY_VOLUME.equalsIgnoreCase(visualizationCode)) {
			return revenueService.bottomUlbsDigitalCollectionByVolume(requestInfoWrapper.getPayloadDetails());
		}
		
		if(Constants.VisualizationCodes.REVENUE_BPA_COLLECTION_PAYMENT_MODE.equalsIgnoreCase(visualizationCode)) {
			return revenueService.bpaCollectionByChannel(requestInfoWrapper.getPayloadDetails());
		}
		
		if(Constants.VisualizationCodes.REVENUE_BPA_COLLECTION_REPORT.equalsIgnoreCase(visualizationCode)) {
			return revenueService.bpaCollectionReport(requestInfoWrapper.getPayloadDetails());
		}
		if(Constants.VisualizationCodes.SERVICE_OBPS_TOTAL_OC_APPLICATION.equalsIgnoreCase(visualizationCode)) {
            return bpaService.totalOcApplicationsReceived(requestInfoWrapper.getPayloadDetails());
        }
        
        if(Constants.VisualizationCodes.SERVICE_OBPS_TOTAL_OC_ISSUED.equalsIgnoreCase(visualizationCode)) {
            return bpaService.totalOcIssued(requestInfoWrapper.getPayloadDetails());
        }
        
        if(Constants.VisualizationCodes.SERVICE_OBPS_TOTAL_OC_REJECTED_APPLICATION.equalsIgnoreCase(visualizationCode)) {
            return bpaService.totalOcRejected(requestInfoWrapper.getPayloadDetails());
        }
        
        if(Constants.VisualizationCodes.SERVICE_OBPS_TOTAL_OC_PENDING_APPLICATION.equalsIgnoreCase(visualizationCode)) {
            return bpaService.totalOcPending(requestInfoWrapper.getPayloadDetails());
        }
        
        if(Constants.VisualizationCodes.SERVICE_OBPS_AVG_DAYS_TO_ISSUE_OC.equalsIgnoreCase(visualizationCode)) {
            return bpaService.avgDaysToIssueOc(requestInfoWrapper.getPayloadDetails());
        }
        
        if(Constants.VisualizationCodes.SERVICE_OBPS_MIN_DAYS_TO_ISSUE_OC.equalsIgnoreCase(visualizationCode)) {
            return bpaService.minDaysToIssueOc(requestInfoWrapper.getPayloadDetails());
        }
        
        if(Constants.VisualizationCodes.SERVICE_OBPS_MAX_DAYS_TO_ISSUE_OC.equalsIgnoreCase(visualizationCode)) {
            return bpaService.maxDaysToIssueOc(requestInfoWrapper.getPayloadDetails());
        }
        
        if(Constants.VisualizationCodes.SERVICE_OBPS_OC_SLA_COMPLIANCE.equalsIgnoreCase(visualizationCode)) {
            return bpaService.slaComplianceOc(requestInfoWrapper.getPayloadDetails());
        }
        
        if(Constants.VisualizationCodes.SERVICE_OBPS_SERVICE_REPORT.equalsIgnoreCase(visualizationCode)) {
            return bpaService.serviceReport(requestInfoWrapper.getPayloadDetails());
        }
        
        if(Constants.VisualizationCodes.SERVICE_OBPS_SERVICE_SUMMARY.equalsIgnoreCase(visualizationCode)) {
            return bpaService.obpsServiceSummary(requestInfoWrapper.getPayloadDetails());
        }
        
        if(Constants.VisualizationCodes.SERVICE_OBPS_APP_PENDING_BREAKDOWN.equalsIgnoreCase(visualizationCode)) {
            return bpaService.obpsApplicationsPendingBreakdown(requestInfoWrapper.getPayloadDetails());
        }
        
        if(Constants.VisualizationCodes.SERVICE_TL_TOTAL_APPLICATION.equalsIgnoreCase(visualizationCode)) {
        	return tlService.totalApplications(requestInfoWrapper.getPayloadDetails());
        }
        
        if(Constants.VisualizationCodes.SERVICE_TL_TOTAL_NEW_APPLICATION.equalsIgnoreCase(visualizationCode)) {
        	return tlService.totalNewApplications(requestInfoWrapper.getPayloadDetails());
        }
        
        if(Constants.VisualizationCodes.SERVICE_TL_TOTAL_RENEWAL_APPLICATION.equalsIgnoreCase(visualizationCode)) {
        	return tlService.totalRenewalApplications(requestInfoWrapper.getPayloadDetails());
        }
        
        if(Constants.VisualizationCodes.SERVICE_TL_TOTAL_CORRECTION_APPLICATION.equalsIgnoreCase(visualizationCode)) {
        	return tlService.totalCorrectionApplications(requestInfoWrapper.getPayloadDetails());
        }
        
        if(Constants.VisualizationCodes.SERVICE_TL_TOTAL_TEMPORARY_APPLICATION.equalsIgnoreCase(visualizationCode)) {
        	return tlService.totalTemporaryApplications(requestInfoWrapper.getPayloadDetails());
        }
        
        if(Constants.VisualizationCodes.SERVICE_TL_LICENSE_ISSUED.equalsIgnoreCase(visualizationCode)) {
        	return tlService.licenseIssued(requestInfoWrapper.getPayloadDetails());
        }
        
        if(Constants.VisualizationCodes.SERVICE_TL_ACTIVE_ULBS.equalsIgnoreCase(visualizationCode)) {
        	return tlService.activeUlbs(requestInfoWrapper.getPayloadDetails());
        }
        
        if(Constants.VisualizationCodes.SERVICE_TL_SLA_ACHIEVED.equalsIgnoreCase(visualizationCode)) {
        	return tlService.tlSlaComplience(requestInfoWrapper.getPayloadDetails());
        }
        
        if(Constants.VisualizationCodes.SERVICE_TL_CUMULATIVE_LICENSE_ISSUED.equalsIgnoreCase(visualizationCode)) {
        	return tlService.cumulativeLicenseIssued(requestInfoWrapper.getPayloadDetails());
        }
        
        if(Constants.VisualizationCodes.SERVICE_TL_APPLICATION_BY_STATUS.equalsIgnoreCase(visualizationCode)) {
        	return tlService.tlApplicationByStatus(requestInfoWrapper.getPayloadDetails());
        }
        
        if(Constants.VisualizationCodes.SERVICE_TL_STATUS_BY_DDR.equalsIgnoreCase(visualizationCode)) {
        	return tlService.tlStatusByBoundary(requestInfoWrapper.getPayloadDetails());
        }
        
		
        
        if(Constants.VisualizationCodes.REVENUE_TL_TOP_ULBS_BY_VALUE.equalsIgnoreCase(visualizationCode)) {
            return revenueService.topUlbsDigitalCollectionByValue(requestInfoWrapper.getPayloadDetails());
        }
        
        if(Constants.VisualizationCodes.REVENUE_TL_BOTTOM_ULBS_BY_VALUE.equalsIgnoreCase(visualizationCode)) {
            return revenueService.bottomUlbsDigitalCollectionByValue(requestInfoWrapper.getPayloadDetails());
        }
        
        if(Constants.VisualizationCodes.REVENUE_TL_TOP_ULBS_BY_VOLUME.equalsIgnoreCase(visualizationCode)) {
            return revenueService.topUlbsDigitalCollectionByVolume(requestInfoWrapper.getPayloadDetails());
        }
        
        if(Constants.VisualizationCodes.REVENUE_TL_BOTTOM_ULBS_BY_VOLUME.equalsIgnoreCase(visualizationCode)) {
            return revenueService.bottomUlbsDigitalCollectionByVolume(requestInfoWrapper.getPayloadDetails());
        }
        
        if(Constants.VisualizationCodes.REVENUE_TL_COLLECTION_BY_LICENSE_TYPE.equalsIgnoreCase(visualizationCode)) {
            return revenueService.tlCollectionsByLicenseType(requestInfoWrapper.getPayloadDetails());
        }
        
        if(Constants.VisualizationCodes.REVENUE_TL_COLLECTION_BY_LICENSE_TYPE_LINE.equalsIgnoreCase(visualizationCode)) {
            return revenueService.tlCollectionsByLicenseTypeLine(requestInfoWrapper.getPayloadDetails());
        }
        
        if(Constants.VisualizationCodes.REVENUE_TL_KEY_FINALCIAL_INDICATORS.equalsIgnoreCase(visualizationCode)) {
            return revenueService.tlKeyFinancialIndicators(requestInfoWrapper.getPayloadDetails());
        }
        
        if(Constants.VisualizationCodes.SERVICE_MR_NEW_APPLICATION.equalsIgnoreCase(visualizationCode)) {
            return mrService.totalNewApplications(requestInfoWrapper.getPayloadDetails());
        }
        
        if(Constants.VisualizationCodes.SERVICE_MR_CORRECTION_APPLICATION.equalsIgnoreCase(visualizationCode)) {
            return mrService.totalCorrectionApplications(requestInfoWrapper.getPayloadDetails());
        }
        
        if(Constants.VisualizationCodes.SERVICE_MR_TATKAL_APPLICATION.equalsIgnoreCase(visualizationCode)) {
            return mrService.totalTatkalApplications(requestInfoWrapper.getPayloadDetails());
        }
        
        if(Constants.VisualizationCodes.SERVICE_MR_APPROVED_APPLICATION.equalsIgnoreCase(visualizationCode)) {
            return mrService.totalApprovedApplications(requestInfoWrapper.getPayloadDetails());
        }
        
        if(Constants.VisualizationCodes.SERVICE_MR_SLA_ACHIEVED.equalsIgnoreCase(visualizationCode)) {
            return mrService.slaAchievedCount(requestInfoWrapper.getPayloadDetails());
        }
        
        if(Constants.VisualizationCodes.SERVICE_MR_CUMULATIVE_APPLICATION.equalsIgnoreCase(visualizationCode)) {
            return mrService.cumulativeApplications(requestInfoWrapper.getPayloadDetails());
        }
        
        if(Constants.VisualizationCodes.SERVICE_MR_TOP_PERFORMANCE_ULBS.equalsIgnoreCase(visualizationCode)) {
            return mrService.topPerformingUlbs(requestInfoWrapper.getPayloadDetails());
        }
        
        if(Constants.VisualizationCodes.SERVICE_MR_BOTTOM_PERFORMANCE_ULBS.equalsIgnoreCase(visualizationCode)) {
            return mrService.bottomPerformingUlbs(requestInfoWrapper.getPayloadDetails());
        }
        
        if(Constants.VisualizationCodes.SERVICE_MR_APPLICATION_BY_STATUS.equalsIgnoreCase(visualizationCode)) {
            return mrService.mrApplicationsByStatus(requestInfoWrapper.getPayloadDetails());
        }
        
        if(Constants.VisualizationCodes.SERVICE_MR_STATUS_BY_BOUNDARY.equalsIgnoreCase(visualizationCode)) {
            return mrService.mrStatusByBoundary(requestInfoWrapper.getPayloadDetails());
        }
        
        if(Constants.VisualizationCodes.REVENUE_MR_CUMULATIVE_COLLECTIONS.equalsIgnoreCase(visualizationCode)) {
            return revenueService.cumulativeCollection(requestInfoWrapper.getPayloadDetails());
        }
        if(Constants.VisualizationCodes.REVENUE_MR_TOP_DIGITAL_BY_VALUE.equalsIgnoreCase(visualizationCode)) {
            return revenueService.topUlbsDigitalCollectionByValue(requestInfoWrapper.getPayloadDetails());
        }
        if(Constants.VisualizationCodes.REVENUE_MR_BOTTOM_DIGITAL_BY_VALUE.equalsIgnoreCase(visualizationCode)) {
            return revenueService.bottomUlbsDigitalCollectionByValue(requestInfoWrapper.getPayloadDetails());
        }
        if(Constants.VisualizationCodes.REVENUE_MR_TOP_DIGITAL_BY_VOLUME.equalsIgnoreCase(visualizationCode)) {
            return revenueService.topUlbsDigitalCollectionByVolume(requestInfoWrapper.getPayloadDetails());
        }
        if(Constants.VisualizationCodes.REVENUE_MR_BOTTOM_DIGITAL_BY_VOLUME.equalsIgnoreCase(visualizationCode)) {
            return revenueService.bottomUlbsDigitalCollectionByVolume(requestInfoWrapper.getPayloadDetails());
        }
        if(Constants.VisualizationCodes.REVENUE_MR_KEY_FINANCIAL_INDICATORS.equalsIgnoreCase(visualizationCode)) {
            return revenueService.mrKeyFinancialIndicators(requestInfoWrapper.getPayloadDetails());
        }
        if(Constants.VisualizationCodes.SERVICE_PGR_CLOSED_COMPLAINTS.equalsIgnoreCase(visualizationCode)) {
            return pgrService.closedApplications(requestInfoWrapper.getPayloadDetails());
        }
        if(Constants.VisualizationCodes.SERVICE_PGR_COMPLETION_RATE.equalsIgnoreCase(visualizationCode)) {
            return pgrService.pgrCompletionRate(requestInfoWrapper.getPayloadDetails());
        }
        if(Constants.VisualizationCodes.SERVICE_PGR_CUMULATIVE_CLOSED_COMPLAINTS.equalsIgnoreCase(visualizationCode)) {
            return pgrService.pgrCumulativeClosedComplaints(requestInfoWrapper.getPayloadDetails());
        }
        if(Constants.VisualizationCodes.SERVICE_PGR_TOP_COMPLAINTS.equalsIgnoreCase(visualizationCode)) {
            return pgrService.pgrTopComplaints(requestInfoWrapper.getPayloadDetails());
        }
        if(Constants.VisualizationCodes.SERVICE_PGR_STATUS_BY_DDR.equalsIgnoreCase(visualizationCode)) {
            return pgrService.pgrStatusByDDR(requestInfoWrapper.getPayloadDetails());
        }
        if(Constants.VisualizationCodes.SERVICE_PGR_COMPLAINTS_BY_STATUS.equalsIgnoreCase(visualizationCode)) {
			return pgrService.complaintsByStatus(requestInfoWrapper.getPayloadDetails());
		}
		if(Constants.VisualizationCodes.SERVICE_PGR_COMPLAINTS_BY_DEPARTMENT.equalsIgnoreCase(visualizationCode)) {
			return pgrService.complaintsByDepartment(requestInfoWrapper.getPayloadDetails());
		}
		if(Constants.VisualizationCodes.SERVICE_PGR_COMPLAINTS_BY_CHANNEL.equalsIgnoreCase(visualizationCode)) {
			return pgrService.complaintsByChannel(requestInfoWrapper.getPayloadDetails());
		}
		if(Constants.VisualizationCodes.SERVICE_PGR_EVENT_DURATION_GRAPH.equalsIgnoreCase(visualizationCode)) {
			return pgrService.eventDurationGraph(requestInfoWrapper.getPayloadDetails());
		}
		if(Constants.VisualizationCodes.SERVICE_PGR_UNIQUE_CITIZENS.equalsIgnoreCase(visualizationCode)) {
			return pgrService.uniqueCitizens(requestInfoWrapper.getPayloadDetails());
		}
		if(Constants.VisualizationCodes.SERVICE_PGR_TOTAL_COMPLAINTS_BY_STATUS.equalsIgnoreCase(visualizationCode)) {
			return pgrService.totalComplaintsByStatus(requestInfoWrapper.getPayloadDetails());
		}
		if(Constants.VisualizationCodes.SERVICE_PGR_TOTAL_COMPLAINTS_BY_SOURCE.equalsIgnoreCase(visualizationCode)) {
			return pgrService.totalComplaintsBySource(requestInfoWrapper.getPayloadDetails());
		}
		if(Constants.VisualizationCodes.REVENUE_TOTAL_COLLECTION_DEPT_WISE.equalsIgnoreCase(visualizationCode)) {
			return revenueService.totalCollectionDeptWise(requestInfoWrapper.getPayloadDetails());
		}
		if(Constants.VisualizationCodes.SERVICE_TOTAL_APPLICATION_AND_CLOSD_APPLICATION.equalsIgnoreCase(visualizationCode)) {
			return commonService.totalApplicationAndClosed(requestInfoWrapper.getPayloadDetails());
		}
		if (Constants.VisualizationCodes.SERVICE_CITIZEN_REGISTERED.equalsIgnoreCase(visualizationCode)) {
			  return commonService.citizenRegistered(requestInfoWrapper.getPayloadDetails());
		}
		if (Constants.VisualizationCodes.SERVICE_DSS_CLOSED_APPLICATION_OVERVIEW.equalsIgnoreCase(visualizationCode)) {
			  return commonService.totalClosedApplications(requestInfoWrapper.getPayloadDetails());
		}
		if (Constants.VisualizationCodes.SERVICE_TOTAL_APPLICATION_DEPT_WISE.equalsIgnoreCase(visualizationCode)) {
			  return commonService.totalApplcationsServiceWise(requestInfoWrapper.getPayloadDetails());
		}
		if (Constants.VisualizationCodes.SERVICE_TOP_PERFORMING_ULBS_COMPLETION_RATE.equalsIgnoreCase(visualizationCode)) {
			  return commonService.topPerformingULBsCompletionRate(requestInfoWrapper.getPayloadDetails());
		}
		if (Constants.VisualizationCodes.SERVICE_BOTTOM_PERFORMING_ULBS_COMPLETION_RATE.equalsIgnoreCase(visualizationCode)) {
			  return commonService.bottomPerformingULBsCompletionRate(requestInfoWrapper.getPayloadDetails());
		}
		if (Constants.VisualizationCodes.REVENUE_TOTAL_DEMAND.equalsIgnoreCase(visualizationCode)) {
			  return revenueService.totalDemand(requestInfoWrapper.getPayloadDetails());
		}
		if (Constants.VisualizationCodes.REVENUE_ARREAR_DEMAND.equalsIgnoreCase(visualizationCode)) {
			  return revenueService.arrearDemand(requestInfoWrapper.getPayloadDetails());
		}
		if (Constants.VisualizationCodes.REVENUE_CURRENT_DEMAND.equalsIgnoreCase(visualizationCode)) {
			  return revenueService.currentDemand(requestInfoWrapper.getPayloadDetails());
		}
		if (Constants.VisualizationCodes.SERVICE_WS_CUMULATIVE_CONNECTIONS_BY_USAGE.equalsIgnoreCase(visualizationCode)) {
			  return wsService.wsCumulativeConnectionByUsage(requestInfoWrapper.getPayloadDetails());
		}
		if (Constants.VisualizationCodes.SERVICE_WS_CUMULATIVE_CONNECTIONS_BY_TYPE.equalsIgnoreCase(visualizationCode)) {
			  return wsService.wsCumulativeConnectionByType(requestInfoWrapper.getPayloadDetails());
		}
		if (Constants.VisualizationCodes.REVENUE_OVERVIEW_TOP_PERFORMING_ULBS.equalsIgnoreCase(visualizationCode)) {
			return revenueService.topPerformingUlbsOverview(requestInfoWrapper.getPayloadDetails());			
		}
		if(Constants.VisualizationCodes.REVENUE_OVERVIEW_BOTTOM_PERFORMING_ULBS.equalsIgnoreCase(visualizationCode)) {
			return revenueService.bottomPerformingUlbsOverview(requestInfoWrapper.getPayloadDetails());
		}
		if (Constants.VisualizationCodes.SERVICE_WS_STATUS_BY_BOUNDARY.equalsIgnoreCase(visualizationCode)) {
			  return wsService.wsStatusByBoundary(requestInfoWrapper.getPayloadDetails());
		}
		if(Constants.VisualizationCodes.SERVICE_PGR_TOTAL_ESCALATED_COMPLAINTS.equalsIgnoreCase(visualizationCode)) {
            return pgrService.totalEscalatedComplaints(requestInfoWrapper.getPayloadDetails());
        }
		
		if(Constants.VisualizationCodes.REVENUE_URC_TOTALCOLLECTION.equalsIgnoreCase(visualizationCode)) {
            return urcService.urcTotalCollection(requestInfoWrapper.getPayloadDetails());
        }
		
		if(Constants.VisualizationCodes.REVENUE_URC_PTCOLLECTION.equalsIgnoreCase(visualizationCode)) {
            return urcService.ptTotalCollection(requestInfoWrapper.getPayloadDetails());
        }
		
		if(Constants.VisualizationCodes.REVENUE_URC_WNSCOLLECTION.equalsIgnoreCase(visualizationCode)) {
            return urcService.wsTotalCollection(requestInfoWrapper.getPayloadDetails());
        }
		
		if(Constants.VisualizationCodes.ULBS_UNDER_URC.equalsIgnoreCase(visualizationCode)) {
            return urcService.ulbsUnderUrc(requestInfoWrapper.getPayloadDetails());
        }
		
		if(Constants.VisualizationCodes.JAlSATHI_ONBOARDED_URC.equalsIgnoreCase(visualizationCode)) {
            return urcService.jalsathiOnboarded(requestInfoWrapper.getPayloadDetails());
        }
		
		if(Constants.VisualizationCodes.URC_PROPERTIES_PAID.equalsIgnoreCase(visualizationCode)) {
            return urcService.totalPropertiesPaid(requestInfoWrapper.getPayloadDetails());
        }
		
		if(Constants.VisualizationCodes.URC_WATER_CONSUMER_PAID.equalsIgnoreCase(visualizationCode)) {
            return urcService.totalWaterConsumerPaid(requestInfoWrapper.getPayloadDetails());
        }
		if(Constants.VisualizationCodes.URC_WATER_CONSUMER_PAID.equalsIgnoreCase(visualizationCode)) {
            return urcService.totalWaterConsumerPaid(requestInfoWrapper.getPayloadDetails());
        }
		
		if(Constants.VisualizationCodes.URC_PT_PAYMENT_MODE.equalsIgnoreCase(visualizationCode)) {
            return urcService.ptPaymentModeData(requestInfoWrapper.getPayloadDetails());
        }
		
		if(Constants.VisualizationCodes.URC_WS_PAYMENT_MODE.equalsIgnoreCase(visualizationCode)) {
            return urcService.wsPaymentModeData(requestInfoWrapper.getPayloadDetails());
        }
		
		if(Constants.VisualizationCodes.URC_REVENUE_PT_TARGET_ACHIEVEMENT.equalsIgnoreCase(visualizationCode)) {
            return urcService.revenuePTTargetAchievement(requestInfoWrapper.getPayloadDetails());
        }
		
		if(Constants.VisualizationCodes.URC_REVENUE_PT_DEMAND_EFFICIENCY.equalsIgnoreCase(visualizationCode)) {
            return urcService.ptDemandEfficiency(requestInfoWrapper.getPayloadDetails());
        }
		
		if(Constants.VisualizationCodes.URC_REVENUE_WS_DEMAND_EFFICIENCY.equalsIgnoreCase(visualizationCode)) {
            return urcService.wsDemandEfficiency(requestInfoWrapper.getPayloadDetails());
        }
		
		if(Constants.VisualizationCodes.URC_TOP_JALSATHI_PT_COLLECTION.equalsIgnoreCase(visualizationCode)) {
            return urcService.topJalSathiPTCollection(requestInfoWrapper.getPayloadDetails());
        }
		
		if(Constants.VisualizationCodes.URC_TOP_JALSATHI_WS_COLLECTION.equalsIgnoreCase(visualizationCode)) {
            return urcService.topJalSathiWSCollection(requestInfoWrapper.getPayloadDetails());
        }
		if(Constants.VisualizationCodes.URC_SERVICE_PROPERTIES_PAID.equalsIgnoreCase(visualizationCode)) {
            return urcService.servicePropertiesPaid(requestInfoWrapper.getPayloadDetails());
        }
		
		if(Constants.VisualizationCodes.URC_SERVICE_WATER_CONSUMER_PAID.equalsIgnoreCase(visualizationCode)) {
            return urcService.serviceWaterConsumerPaid(requestInfoWrapper.getPayloadDetails());
        }
		
		if(Constants.VisualizationCodes.URC_PROPERTIES_COVER_BY_JALSATHI.equalsIgnoreCase(visualizationCode)) {
            return urcService.propertiesCoverByJalsathi(requestInfoWrapper.getPayloadDetails());
        }		

		if(Constants.VisualizationCodes.URC_TOP_JALSATHI_UNIFIED_COLLECTION.equalsIgnoreCase(visualizationCode)) {
            return urcService.topJalSathiUnifiedCollection(requestInfoWrapper.getPayloadDetails());
        }
		 if (Constants.VisualizationCodes.USM_TOTAL_FEEDBACK_SUBMITTED.equalsIgnoreCase(visualizationCode)) {
			return usmService.totalFeedbackSubmitted(requestInfoWrapper.getPayloadDetails());
		}
		if (Constants.VisualizationCodes.USM_TOTAL_SLUM_SUBMITTED_FEEDBACK.equalsIgnoreCase(visualizationCode)) {
			return usmService.totalSlumSubmittedFeedback(requestInfoWrapper.getPayloadDetails());
		}
		if (Constants.VisualizationCodes.USM_TOTAL_OPEN_ISSUES.equalsIgnoreCase(visualizationCode)) {
			return usmService.totalOpenIssues(requestInfoWrapper.getPayloadDetails());
		}
		if (Constants.VisualizationCodes.USM_TOTAL_CLOSE_ISSUES.equalsIgnoreCase(visualizationCode)) {
			return usmService.totalClosedIssue(requestInfoWrapper.getPayloadDetails());
		}
		if (Constants.VisualizationCodes.USM_TOP_CATEGORY_ISSUES.equalsIgnoreCase(visualizationCode)) {
			return usmService.topIssueCategory(requestInfoWrapper.getPayloadDetails());
		}
		if (Constants.VisualizationCodes.USM_CATEGORY_WISE_ISSUE.equalsIgnoreCase(visualizationCode)) {
			return usmService.categoryWiseIssue(requestInfoWrapper.getPayloadDetails());
		}
		if (Constants.VisualizationCodes.USM_ISSUE_RESOLUTION_SUMMARY.equalsIgnoreCase(visualizationCode)) {
			return usmService.issueResolutionSummary(requestInfoWrapper.getPayloadDetails());
		}
		if (Constants.VisualizationCodes.USM_BOTTOM_ULB.equalsIgnoreCase(visualizationCode)) {
			return usmService.bottomUlbsByStatus(requestInfoWrapper.getPayloadDetails());
		}
		if (Constants.VisualizationCodes.USM_TOP_ULB.equalsIgnoreCase(visualizationCode)) {
			return usmService.topUlbsByStatus(requestInfoWrapper.getPayloadDetails());
		}
		if (Constants.VisualizationCodes.USM_TOTAL_CUMULATIVE.equalsIgnoreCase(visualizationCode)) {
			return usmService.cumulativeApplications(requestInfoWrapper.getPayloadDetails());
		}		
		if(Constants.VisualizationCodes.URC_MONTHWISE_UNIFIED_COLLECTION.equalsIgnoreCase(visualizationCode)) {
            return urcService.monthwiseUnifiedCollection(requestInfoWrapper.getPayloadDetails());
        }
		
		if(Constants.VisualizationCodes.URC_DEMAND_EFFICIENCY.equalsIgnoreCase(visualizationCode)) {
            return urcService.urcDemandEfficiency(requestInfoWrapper.getPayloadDetails());
        }
		
		if(Constants.VisualizationCodes.URC_ACTIVE_JALSATHI.equalsIgnoreCase(visualizationCode)) {
		    return urcService.activeJalSathi(requestInfoWrapper.getPayloadDetails());
		}
		
		if(Constants.VisualizationCodes.URC_PROPERTIES_COVERED_BY_JALSATHI.equalsIgnoreCase(visualizationCode)) {
		    return urcService.propertiesCoveredByJalsathi(requestInfoWrapper.getPayloadDetails());
		}
		        
		if(Constants.VisualizationCodes.URC_JALSATHI_PT_COLLECTION.equalsIgnoreCase(visualizationCode)) {
		    return urcService.jalsathiPTCollection(requestInfoWrapper.getPayloadDetails());
		}
		
		if(Constants.VisualizationCodes.URC_WATER_COVERED_BY_JALSATHI.equalsIgnoreCase(visualizationCode)) {
		    return urcService.waterConnectionCoveredByJalsathi (requestInfoWrapper.getPayloadDetails());
		}
		
		if(Constants.VisualizationCodes.URC_JALSATHI_WS_COLLECTION.equalsIgnoreCase(visualizationCode)) {
            return urcService.jalsathiWSCollection(requestInfoWrapper.getPayloadDetails());
        }
        
        if(Constants.VisualizationCodes.URC_JALSATHI_TOTAL_COLLECTION.equalsIgnoreCase(visualizationCode)) {
            return urcService.jalsathiTotalCollection(requestInfoWrapper.getPayloadDetails());
        }
        
        if(Constants.VisualizationCodes.URC_COLLECTOR_WISE_REVENUE.equalsIgnoreCase(visualizationCode)) {
            return urcService.collectorWiseRevenue(requestInfoWrapper.getPayloadDetails());
        }
        
		/*
		 * if (Constants.VisualizationCodes.JALSATHI_PT_INCENTIVES.equalsIgnoreCase(
		 * visualizationCode)) { return
		 * urcService.jalsathiPTIncentives(requestInfoWrapper.getPayloadDetails()); }
		 * 
		 * if (Constants.VisualizationCodes.JALSATHI_WS_INCENTIVES.equalsIgnoreCase(
		 * visualizationCode)) { return
		 * urcService.jalsathiWSIncentives(requestInfoWrapper.getPayloadDetails()); }
		 */
		
		if (Constants.VisualizationCodes.JALSATHI_CONTRIBUTION_TO_PT.equalsIgnoreCase(visualizationCode)) {
			return urcService.jalsathiContributionToPT(requestInfoWrapper.getPayloadDetails());
		}
		
		if (Constants.VisualizationCodes.JALSATHI_CONTRIBUTION_TO_WS.equalsIgnoreCase(visualizationCode)) {
			return urcService.jalsathiContributionToWS(requestInfoWrapper.getPayloadDetails());
		}
		
		if(Constants.VisualizationCodes.REGULARIZATION_OBPS_TOP_PERFORMING_ULBS_TABLE.equalsIgnoreCase(visualizationCode)) {
			return regularizationService.topPerformingUlbsTable(requestInfoWrapper.getPayloadDetails());
		}
		if(Constants.VisualizationCodes.REGULARIZATION_OBPS_BOTTOM_PERFORMING_ULBS_TABLE.equalsIgnoreCase(visualizationCode)) {
			return regularizationService.bottomPerformingUlbsTable(requestInfoWrapper.getPayloadDetails());
		}
		
		if(Constants.VisualizationCodes.SERVICE_REGULARIZATION_SERVICE_SUMMARY.equalsIgnoreCase(visualizationCode)) {
	        return regularizationService.RegularizationServiceSummary(requestInfoWrapper.getPayloadDetails());
	    }	
		
		if(Constants.VisualizationCodes.SERVICE_REGULARIZATION_TOTAL_APPLICATIONS_RECEIVED.equalsIgnoreCase(visualizationCode)) {
			return regularizationService.totalApplicationsReceived(requestInfoWrapper.getPayloadDetails());
		}
		
		if(Constants.VisualizationCodes.SERVICE_REGULARIZATION_TOTAL_CERTIFICATE_ISSUED.equalsIgnoreCase(visualizationCode)) {
			return regularizationService.totalRegularizationCertificateIssued(requestInfoWrapper.getPayloadDetails());
		}
		
		if(Constants.VisualizationCodes.SERVICE_REGULARIZATION_TOTAL_APPLICATIONS_REJECTED.equalsIgnoreCase(visualizationCode)) {
			return regularizationService.totalApplicationsRejected(requestInfoWrapper.getPayloadDetails());
		}
		
		if(Constants.VisualizationCodes.SERVICE_REGULARIZATION_TOTAL_APPLICATIONS_PENDING.equalsIgnoreCase(visualizationCode)) {
			return regularizationService.totalApplicationsPending(requestInfoWrapper.getPayloadDetails());
		} 
		
		if(Constants.VisualizationCodes.SERVICE_REGULARIZATION_AVG_DAYS_TO_ISSUE_CERTIFICATE.equalsIgnoreCase(visualizationCode)) {
			return regularizationService.avgDaysToIssueCertificate(requestInfoWrapper.getPayloadDetails());
		}
		
		if(Constants.VisualizationCodes.SERVICE_REGULARIZATION_MIN_DAYS_TO_ISSUE_CERTIFICATE.equalsIgnoreCase(visualizationCode)) {
			return regularizationService.minDaysToIssueCertificate(requestInfoWrapper.getPayloadDetails());
		}
		
		if(Constants.VisualizationCodes.SERVICE_REGULARIZATION_MAX_DAYS_TO_ISSUE_CERTIFICATE.equalsIgnoreCase(visualizationCode)) {
			return regularizationService.maxDaysToIssueCertificate(requestInfoWrapper.getPayloadDetails());
		}
		if (Constants.VisualizationCodes.SERVICE_BLR_APPLICATION_BREAKDOWN.equalsIgnoreCase(visualizationCode)) {
			return regularizationService.regularizationApplicationsPendingBreakdown(requestInfoWrapper.getPayloadDetails());
		}

		if(Constants.VisualizationCodes.REVENUE_REGULARIZATION_APP_FEE_COLLECTIONS.equalsIgnoreCase(visualizationCode)) {
			return revenueService.regularizationFeeCollection(requestInfoWrapper.getPayloadDetails());
		}
		
		if(Constants.VisualizationCodes.REVENUE_REGULARIZATION_SANC_FEE_COLLECTIONS.equalsIgnoreCase(visualizationCode)) {
			return revenueService.regularizationFeeCollection(requestInfoWrapper.getPayloadDetails());
		}
		
		if(Constants.VisualizationCodes.REVENUE_REGULARIZATION_CUMULATIVE_COLLECTION.equalsIgnoreCase(visualizationCode)) {
			return revenueService.getRegCumulativeCollection(requestInfoWrapper.getPayloadDetails());
		}
		
		if(Constants.VisualizationCodes.REVENUE_REGULARIZATION_COLLECTION_REPORT.equalsIgnoreCase(visualizationCode)) {
			return revenueService.regularizationCollectionReport(requestInfoWrapper.getPayloadDetails());
		}
		
		if(Constants.VisualizationCodes.SERVICE_REGULARIZATION_SERVICE_REPORT.equalsIgnoreCase(visualizationCode)) {
			return regularizationService.regularizationServiceReport(requestInfoWrapper.getPayloadDetails());
		}

		return null;

	}

}
