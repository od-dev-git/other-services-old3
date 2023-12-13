package org.egov.dss.util;

public class Constants {
	
	public interface JsonPaths { 
		final static String CHART_TYPE = "chartType"; 
		final static String QUERIES = "queries"; 
		final static String AGGREGATION_QUERY= "aggrQuery";
		final static String INDEX_NAME = "indexName";
		final static String REQUEST_QUERY_MAP = "requestQueryMap"; 
		final static String DATE_REF_FIELD = "dateRefField"; 
		final static String AGGS = "aggs";
		final static String AGGREGATIONS = "aggregations" ;
		final static String MODULE = "module";
		final static String INTERVAL_VAL = "intervalvalue";
		final static String INTERVAL = "interval";
		final static String IS_MDMS_ENABALED = "isMdmsEnabled";
		final static String INSIGHT = "insight";
		final static String DAYS = "days";
		final static String TRANSLATE_CODE = "translateTenantCode";
		final static String VALUE_TYPE = "valueType";
		final static String CHART_NAME = "chartName";
		final static String DRILL_CHART = "drillChart";
	}
	
	public interface ConfigurationFiles { 
		final static String CHART_API_CONFIG = "ChartApiConfig.json"; 
		final static String MASTER_DASHBOARD_CONFIG = "MasterDashboardConfig.json";
	}
	
	public interface VisualizationCodes {
		final static String REVENUE_TODAYS_COLLECTION = "todaysCollection";
		final static String REVENUE_TOTAL_COLLECTION = "totalCollection";
		final static String REVENUE_TARGET_COLLECTION = "targetCollection";
		final static String REVENUE_TARGET_ACHIEVED = "targetAchieved";
		final static String REVENUE_TOTAL_DEMAND = "totalDemand";
		final static String REVENUE_ARREAR_DEMAND = "arrearDemand";
		final static String REVENUE_CURRENT_DEMAND = "currentDemand";
		final static String REVENUE_CUMULATIVE_COLLECTION = "cumulativeCollection";
		final static String REVENUE_TOP_PERFORMING_ULBS = "topPerformingUlbs";
		final static String REVENUE_BOTTOM_PERFORMING_ULBS = "bottomPerformingUlbs";
		final static String REVENUE_TOTAL_COLLECTION_DEPT_WISE = "totalCollectionDeptWise";
		final static String REVENUE_COLLECTION_BY_USAGE_TYPE = "collectionByUsageType";
		final static String REVENUE_DEMAND_COLLECTION_INDEX_DDR_REVENUE = "demandCollectionIndexDDRRevenue";
		final static String REVENUE_DEMAND_COLLECTION_INDEX_USAGE_REVENUE = "demandCollectionIndexUsageRevenue";
		final static String REVENUE_TAXHEADS_BREAKUP_DDR_REVENUE = "taxHeadsBreakupDDRRevenue";
		final static String REVENUE_TAXHEADS_BREAKUP_USAGE = "taxHeadsBreakupUsage";
		final static String REVENUE_TOTAL_MUTATION_FEE_CALCULATION = "totalMutationFeeCalculation";
		final static String DIGITAL_COLLECTIONS_BY_VALUE = "ptDigitalCollectionByValue";
		final static String DIGITAL_COLLECTIONS_BY_VOLUME = "ptDigitalCollectionByVolume";
		final static String REVENUE_PT_TAX_HEADS_BREAKUP = "ptTaxHeadTable";
		final static String REVENUE_CURRENT_COLLECTION = "currentCollection";
		final static String REVENUE_ARREAR_COLLECTION = "arrearCollection";
		final static String REVENUE_PREVIOUS_YEAR_COLLECTION = "previousYearCollection";		
		
		final static String SERVICE_DSS_TOTAL_APPLICATION_OVERVIEW = "dssTotalApplicationOverview";
		final static String SERVICE_DSS_CLOSED_APPLICATION_OVERVIEW = "dssClosedApplicationOverview";
		final static String SERVICE_DSS_SLA_ACHIEVED_OVERVIEW = "dssSlaAchievedOverview";
		final static String SERVICE_CITIZEN_REGISTERED = "citizenRegistered";
		final static String SERVICE_TOTAL_APPLICATION_AND_CLOSD_APPLICATION = "totalApplication&ClosedApplication";
		final static String SERVICE_TOP_PERFORMING_ULBS_COMPLETION_RATE = "topPerformingUlbsCompletionRate";
		final static String SERVICE_BOTTOM_PERFORMING_ULBS_COMPLETION_RATE = "bottomPerformingUlbsCompletionRate";
		final static String SERVICE_TOTAL_APPLICATION_DEPT_WISE = "totalApplicationDeptWise";
		final static String SERVICE_TOTAL_PROPERTIES = "totalProperties";
		final static String SERVICE_PROPERTIES_PAID = "propertiesPaid";
		final static String SERVICE_PROPERTIES_ASSESSED = "propertiesAssessed";
		final static String SERVICE_ACTIVE_ULBS = "activeUlbs";
		final static String SERVICE_TOTAL_MUTATION_PROPERTIES = "totalMutationProperties";
		final static String SERVICE_PT_TOTAL_APPLICATIONS = "ptTotalApplications";
		final static String SERVICE_TOTAL_NO_OF_PROPERTIES = "totalNoOfProperties";
		final static String SERVICE_CUMULATIVE_PROPERTIES_ASSESSED = "cumulativePropertiesAssessed";
		final static String SERVICE_PROPERTIES_BY_USAGE_TYPE = "propertiesByUsageType";
		final static String SERVICE_XPTFY_BY_DDR = "xptFyByDDR";
		final static String SERVICE_PT_NEW_ASSESSMENT_SHARE = "ptNewAssessmentShare";
		final static String SERVICE_PT_RE_ASSESSMENT_SHARE = "ptReAssessmentShare";
		final static String SERVICE_PT_SHARE_OF_NEW_ASSESSMENT = "ptShareOfNewAssessments";
		final static String SERVICE_PT_SHARE_OF_RE_ASSESSMENT = "ptShareOfReAssessments";
		final static String SERVICE_PT_ASMT_STATUS_DDR = "ptAssessmentStatusDDR";
		final static String SERVICE_PT_STATUS_BY_BOUNDARY = "ptStatusByBoundary";
		final static String SERVICE_TOTAL_NO_OF_DEACTIVATED_PROPERTIES = "totalNoOfDeactivatedProperties";
		final static String SERVICE_PT_APPLICATIONS_AGEING = "ptConnectionAgeingDDR";
		
		final static String SERVICE_MR_TOTAL_APPLICATION = "mrtotalApplications";
		final static String SERVICE_PGR_TOTAL_COMPLAINTS = "totalComplaints";
		final static String SERVICE_OBPS_TOTAL_PERMITS = "bpaTotalPermitsIssued";
		final static String REVENUE_MR_TOTAL_COLLECTION = "mrtotalCollection";
		final static String REVENUE_BPA_TOTAL_COLLECTION = "bpaTotalCollection";
		final static String REVENUE_WS_TOTAL_COLLECTION = "wstotalCollection";
		final static String SERVICE_PGR_SLA_ACHIEVED = "slaAchieved";
		final static String REVENUE_PT_GROWTH_RATE = "propertyTaxGrowthRate";
		final static String REVENUE_PT_GROWTH_RATE_TABLE = "propertyTaxGrowthRateTable";
		final static String REVENUE_PT_TOP_ULBS_BY_DIGITAL_COLLECTION = "ptTopDigitalCollectionByValue";
		final static String REVENUE_PT_BOTTOM_ULBS_BY_DIGITAL_COLLECTION = "ptBottomDigitalCollectionByValue";
		final static String REVENUE_PT_TOP_ULBS_COLLECTION_BY_VOLUME = "ptTopDigitalCollectionByVolume";
		final static String REVENUE_PT_BOTTOM_ULBS_COLLECTION_BY_VOLUME = "ptBottomDigitalCollectionByVolume";
		final static String REVENUE_PT_PREVIOUS_YEAR_TARGET_ACHIEVED = "previousTargetAchieved";
		final static String REVENUE_PT_FINANCIAL_INDICATORS = "ptFinancialIndicatorTable";
		final static String REVENUE_PT_PAYMENT_MODE_DDR = "ptPaymentModeDDR";		
		
		final static String REVENUE_WS_APP_FEE_COLLECTION = "wstotalApplicationFeesCollection";
		final static String REVENUE_WS_DEMAND_FEE_COLLECTION = "wstotalDemandFeesCollection";
		final static String REVENUE_WS_CUMULATIVE_COLLECTION = "wscumulativeCollections";
		final static String REVENUE_WS_TOP_ULBS_BY_DIGITAL_COLLECTION = "wsTopDigitalCollectionByValue";
		final static String REVENUE_WS_BOTTOM_ULBS_BY_DIGITAL_COLLECTION = "wsBottomDigitalCollectionByValue";
		final static String REVENUE_WS_TOP_ULBS_COLLECTION_BY_VOLUME = "wsTopDigitalCollectionByVolume";
		final static String REVENUE_WS_BOTTOM_ULBS_COLLECTION_BY_VOLUME = "wsBottomDigitalCollectionByVolume";
		final static String REVENUE_WS_COLLECTION_BY_USAGE_TYPE = "wscollectionByUsage";
		final static String REVENUE_WS_COLLECTION_BY_CHANNEL = "wscollectionByChannel";
		final static String REVENUE_WS_TAX_HEADS_BREAKUP = "wsTaxHeadDDR";
		final static String REVENUE_WS_KEY_FINANCIAL_INDICATOR = "wsFinancialIndicatorDDR";
		
		final static String SERVICE_WS_TOTAL_ACTIVE_CONNECTIONS = "wstotalConnection";
		final static String SERVICE_WS_ACTIVE_METERED_WATER_CONNECTIONS = "wsActiveMeteredWaterConnection";
		final static String SERVICE_WS_ACTIVE_NON_METERED_WATER_CONNECTIONS = "wsActiveNonMeteredWaterConnection";
		final static String SERVICE_WS_TOTAL_WATER_CONNECTIONS = "wstotalWaterConnection";
		final static String SERVICE_WS_TOTAL_SEWERAGE_CONNECTIONS = "wstotalSewerageConnection";
		final static String SERVICE_WS_TOTAL_WATER_SEWERAGE_CONNECTIONS = "wstotalWaterSewerageConnection";
		final static String SERVICE_WS_TOTAL_CUMULATIVE_ACTIVE_CONNECTIONS = "wstotalCumulativeActiveConnection";
		final static String SERVICE_WS_CUMULATIVE_CONNECTIONS = "wscumulativeConnections";
		final static String SERVICE_WS_CONNECTIONS_BY_USAGE_TYPE = "wsWaterConsumersByUsageType";
		final static String SERVICE_WS_CONNECTIONS_BY_TYPE = "wsConsumersByChannel";
		final static String SERVICE_WS_CONNECTION_AGEING = "wsConnectionAgeingDDR";
		final static String SERVICE_WS_SLA_ACHIEVED = "wsSlaAchieved";
		final static String SERVICE_WS_TOTAL_APPLICATIONS = "wstotalApplications";
		final static String SERVICE_WS_CUMULATIVE_CONNECTIONS_BY_USAGE = "wsCumulativeConnectionByUsage";
		final static String SERVICE_WS_CUMULATIVE_CONNECTIONS_BY_TYPE = "wsCumulativeConnectionByType";
		final static String SERVICE_WS_STATUS_BY_BOUNDARY = "wsServiceStatusByBoundary";
		
		final static String SERVICE_OBPS_TOTAL_APPLICATIONS_RECEIVED = "bpaTotalApplicationsSubmitted";
		final static String SERVICE_OBPS_TOTAL_APPLICATIONS_REJECTED = "bpaTotalRejectedApplications";
		final static String SERVICE_OBPS_TOTAL_APPLICATIONS_PENDING = "bpaTotalPendingApplications";
		final static String SERVICE_OBPS_AVG_DAYS_TO_ISSUE_PERMIT = "bpaAverageDaysToIssuePermit";
		final static String SERVICE_OBPS_MIN_DAYS_TO_ISSUE_PERMIT = "bpaMinDaysToIssuePermit";
		final static String SERVICE_OBPS_MAX_DAYS_TO_ISSUE_PERMIT = "bpaMaxDaysToIssuePermit";
		final static String SERVICE_OBPS_SLA_COMPLIANCE_PERMIT = "bpaSLACompliance";
		final static String SERVICE_OBPS_SLA_COMPLIANCE_OTHER_THAN_LOW_RISK = "bpaSLAComplianceOtherThanLowRisk";
		final static String SERVICE_OBPS_SLA_COMPLIANCE_PREAPPROVED_PLAN = "bpaSLACompliancePreApprovedPlan";
		final static String SERVICE_OBPS_SLA_COMPLIANCE_BUILDING_PERMIT = "bpaSLAComplianceBuildingPermit";
		final static String SERVICE_OBPS_TOP_ULB_BY_PERFORMANCE = "obpsTopUlbByPerformance";
		final static String SERVICE_OBPS_BOTTOM_ULB_BY_PERFORMANCE = "obpsBottomUlbByPerformance";
		final static String SERVICE_OBPS_TOP_PERFORMING_ULBS_TABLE = "obpsTopPerformingUlbsTable";
		final static String SERVICE_OBPS_BOTTOM_PERFORMING_ULBS_TABLE = "obpsBottomPerformingUlbsTable";
		
		final static String REVENUE_BPA_APP_FEE_COLLECTIONS = "bpaApplicationFeesCollection";
		final static String REVENUE_BPA_SANC_FEE_COLLECTIONS = "bpaSanctionFeesCollection";
		final static String REVENUE_OC_APP_FEE_COLLECTIONS = "ocApplicationFeesCollection";
		final static String REVENUE_OC_SANC_FEE_COLLECTIONS = "ocSanctionFeesCollection";
		final static String REVENUE_BPA_CUMULATIVE_COLLECTION = "bpaCumulativeCollections";
		final static String REVENUE_BPA_TOP_BY_VALUE = "obpsTopDigitalCollectionByValue";
		final static String REVENUE_BPA_BOTTOM_BY_VALUE = "obpsBottomDigitalCollectionByValue";
		final static String REVENUE_BPA_TOP_BY_VOLUME = "obpsTopDigitalCollectionByVolume";
		final static String REVENUE_BPA_BOTTOM_BY_VOLUME = "obpsBottomDigitalCollectionByVolume";
		final static String REVENUE_BPA_COLLECTION_PAYMENT_MODE = "obpsCollectionByPaymentMode";
		final static String REVENUE_BPA_COLLECTION_REPORT = "obpsCollectionReport";
        final static String SERVICE_OBPS_TOTAL_OC_APPLICATION = "totalOCApplications";
        final static String SERVICE_OBPS_TOTAL_OC_ISSUED = "bpaTotalOCIssued";
        final static String SERVICE_OBPS_TOTAL_OC_REJECTED_APPLICATION = "totalOCRejectedApplications";
        final static String SERVICE_OBPS_TOTAL_OC_PENDING_APPLICATION = "totalOCPendingApplications";
        final static String SERVICE_OBPS_AVG_DAYS_TO_ISSUE_OC = "bpaAverageDaysToIssueOC";
        final static String SERVICE_OBPS_MIN_DAYS_TO_ISSUE_OC = "bpaMinimumDaysToIssueOC";
        final static String SERVICE_OBPS_MAX_DAYS_TO_ISSUE_OC = "bpaMaximumDaysToIssueOC";
        final static String SERVICE_OBPS_OC_SLA_COMPLIANCE = "bpaSLAComplianceOC";
        final static String SERVICE_OBPS_SERVICE_REPORT = "obpsServiceReport";
		final static String SERVICE_OBPS_PERMITS_AND_OC_ISSUED_AND_OC_SUBMITTED = "permitsandOCissued";
		final static String SERVICE_OBPS_SERVICE_SUMMARY = "obpsServiceSummary";
		final static String SERVICE_OBPS_APP_PENDING_BREAKDOWN = "totalApplicationsPendingBreakdown";
		
		final static String REVENUE_TL_TOP_ULBS_BY_VALUE = "tlTopDigitalCollectionByValue";
		final static String REVENUE_TL_BOTTOM_ULBS_BY_VALUE = "tlBottomDigitalCollectionByValue";
		final static String REVENUE_TL_TOP_ULBS_BY_VOLUME = "tlTopDigitalCollectionByVolume";
		final static String REVENUE_TL_BOTTOM_ULBS_BY_VOLUME = "tlBottomDigitalCollectionByVolume";
		final static String REVENUE_TL_COLLECTION_BY_LICENSE_TYPE = "licenseByLicenseType";
		final static String REVENUE_TL_KEY_FINALCIAL_INDICATORS = "licenseIssuedDDRRevenue";
		final static String REVENUE_TL_COLLECTION_BY_LICENSE_TYPE_LINE = "collectionByLicenseTypeLine";
		
		final static String SERVICE_TL_TOTAL_APPLICATION = "totalApplication";
		final static String SERVICE_TL_TOTAL_NEW_APPLICATION = "tlTotalNewApplication";
		final static String SERVICE_TL_TOTAL_RENEWAL_APPLICATION = "tlTotalRenewalApplication";
		final static String SERVICE_TL_TOTAL_CORRECTION_APPLICATION = "tlTotalCorrectionApplication";
		final static String SERVICE_TL_TOTAL_TEMPORARY_APPLICATION = "tlTotalTemporaryApplication";
		final static String SERVICE_TL_LICENSE_ISSUED = "licenseIssued";
		final static String SERVICE_TL_ACTIVE_ULBS = "activeUlbs";
		final static String SERVICE_TL_SLA_ACHIEVED = "tlSlaAchieved";
		final static String SERVICE_TL_CUMULATIVE_LICENSE_ISSUED = "cumulativeLicenseIssued";
		final static String SERVICE_TL_APPLICATION_BY_STATUS = "licenseApplicationByStatus";
		final static String SERVICE_TL_STATUS_BY_DDR = "tlStatusByDDR";
		
		final static String REVENUE_MR_CUMULATIVE_COLLECTIONS = "mrcumulativeCollections";
		final static String REVENUE_MR_TOP_DIGITAL_BY_VALUE = "mrTopDigitalCollectionByValue";
		final static String REVENUE_MR_BOTTOM_DIGITAL_BY_VALUE = "mrBottomDigitalCollectionByValue";
		final static String REVENUE_MR_TOP_DIGITAL_BY_VOLUME = "mrTopDigitalCollectionByVolume";
		final static String REVENUE_MR_BOTTOM_DIGITAL_BY_VOLUME = "mrBottomDigitalCollectionByVolume";
		final static String REVENUE_MR_KEY_FINANCIAL_INDICATORS = "mrFinancialIndicatorDDR";
		
		final static String SERVICE_MR_NEW_APPLICATION = "mrtotalNewApplications";
		final static String SERVICE_MR_CORRECTION_APPLICATION = "mrtotalCorrectionApplications";
		final static String SERVICE_MR_TATKAL_APPLICATION = "mrtotalTatkalApplications";
		final static String SERVICE_MR_APPROVED_APPLICATION = "mrtotalApprovedApplications";
		final static String SERVICE_MR_SLA_ACHIEVED = "mrSlaAchieved";
		final static String SERVICE_MR_CUMULATIVE_APPLICATION = "mrCumulativeApplications";
		final static String SERVICE_MR_TOP_PERFORMANCE_ULBS = "mrTopUlbByPerformance";
		final static String SERVICE_MR_BOTTOM_PERFORMANCE_ULBS = "mrBottomUlbByPerformance";
		final static String SERVICE_MR_APPLICATION_BY_STATUS = "mrApplicationByStatus";
		final static String SERVICE_MR_STATUS_BY_BOUNDARY = "mrStatusByDDR";
		
		final static String SERVICE_PGR_CLOSED_COMPLAINTS = "closedComplaints";
		final static String SERVICE_PGR_COMPLETION_RATE = "completionRate";
		final static String SERVICE_PGR_CUMULATIVE_CLOSED_COMPLAINTS = "cumulativeClosedComplaints";
		final static String SERVICE_PGR_TOP_COMPLAINTS = "topFiveComplaints";
		final static String SERVICE_PGR_STATUS_BY_DDR = "xpgrStatusByDDR";
		final static String SERVICE_PGR_COMPLAINTS_BY_STATUS = "complaintsByStatus";
		final static String SERVICE_PGR_COMPLAINTS_BY_DEPARTMENT = "complaintsByDepartment";
		final static String SERVICE_PGR_COMPLAINTS_BY_CHANNEL = "complaintsByChannel";
		final static String SERVICE_PGR_EVENT_DURATION_GRAPH = "eventDurationGraph";
		final static String SERVICE_PGR_UNIQUE_CITIZENS = "pgrCitizenOnDateRange";
		final static String SERVICE_PGR_TOTAL_COMPLAINTS_BY_STATUS = "totalComplaintsbyStatus";
		final static String SERVICE_PGR_TOTAL_COMPLAINTS_BY_SOURCE = "totalComplaintsbySource";
		final static String SERVICE_PGR_TOTAL_ESCALATED_COMPLAINTS = "totalEscalatedComplaints";
		
		final static String REVENUE_OVERVIEW_TOP_PERFORMING_ULBS = "topPerformingUlbsOverview";
		final static String REVENUE_OVERVIEW_BOTTOM_PERFORMING_ULBS = "bottomPerformingUlbsOverview";
		final static String REVENUE_ALL_PAYMENT_MODE_COLLECTION = "paymentModeCollections";
		
		final static String REVENUE_URC_TOTALCOLLECTION = "totalCollections";
		final static String REVENUE_URC_WNSCOLLECTION = "waterSewerageCollections";
		final static String REVENUE_URC_PTCOLLECTION = "propertyTaxCollections";
		final static String ULBS_UNDER_URC = "ulbsUnderUrc";
		final static String JAlSATHI_ONBOARDED_URC = "jalsathiOnboardedUrc";
		final static String URC_PROPERTIES_PAID = "urcPropertiesPaid";
		final static String URC_WATER_CONSUMER_PAID = "urcWaterConsumerPaid";
		final static String URC_PT_PAYMENT_MODE = "urcPTPaymentmode";
		final static String URC_WS_PAYMENT_MODE = "urcWSPaymentmode";
		final static String URC_REVENUE_PT_TARGET_ACHIEVEMENT = "revenuePTTargetAchievement";
		final static String URC_REVENUE_PT_DEMAND_EFFICIENCY = "revenuePTDemandEfficiency";
		final static String URC_REVENUE_WS_DEMAND_EFFICIENCY = "revenueWSDemandEfficiency";
		final static String URC_SERVICE_PROPERTIES_PAID = "servicePropertiesPaid";
		final static String URC_SERVICE_WATER_CONSUMER_PAID = "serviceWaterConsumerPaid";
		final static String URC_TOP_JALSATHI_PT_COLLECTION = "topJalSathiPTCollection";
		final static String URC_TOP_JALSATHI_WS_COLLECTION = "topJalSathiWSCollection";
		final static String URC_TOP_JALSATHI_UNIFIED_COLLECTION = "topJalSathiUnifiedCollection";		
		final static String URC_PROPERTIES_COVER_BY_JALSATHI = "propertiesCoverByJalsathi";
		final static String URC_MONTHWISE_UNIFIED_COLLECTION = "monthwiseUnifiedCollection";
		final static String URC_DEMAND_EFFICIENCY = "urcDemandEfficiency";
		final static String URC_ACTIVE_JALSATHI = "activeJalSathi";
		final static String URC_PROPERTIES_COVERED_BY_JALSATHI = "propertiesCoveredByJalsathi";
		final static String URC_JALSATHI_PT_COLLECTION = "jalsathiPTCollection";
		final static String URC_WATER_COVERED_BY_JALSATHI = "waterConnCoveredByJalsathi";
		final static String URC_JALSATHI_WS_COLLECTION = "jalsathiWSCollection";
		final static String URC_JALSATHI_TOTAL_COLLECTION = "jalsathiTotalCollection";
		final static String URC_COLLECTOR_WISE_REVENUE = "collectorWiseRevenue";
		final static String JALSATHI_PT_INCENTIVES = "jalsathiPTIncentives";
	    final static String JALSATHI_WS_INCENTIVES = "jalsathiWSIncentives";
	    final static String JALSATHI_CONTRIBUTION_TO_PT = "jalsathiContributionToPT";
	    final static String JALSATHI_CONTRIBUTION_TO_WS = "jalsathiContributionToWS";


		final static String USM_TOTAL_FEEDBACK_SUBMITTED = "totalFeedbackSubmitted";
		final static String USM_TOTAL_SLUM_SUBMITTED_FEEDBACK = "totalSlumSubmittedFeedback";
		final static String USM_TOTAL_OPEN_ISSUES = "totalOpenIssues";
		final static String USM_TOTAL_CLOSE_ISSUES = "totalCloseIssues";
		final static String USM_ISSUE_RESOLUTION_SUMMARY = "issueResolutionSummery";
		final static String USM_TOP_CATEGORY_ISSUES = "topIssueCatgory";
		final static String USM_CATEGORY_WISE_ISSUE = "categoryWiseIssuesReported";
		final static String USM_TOP_ULB = "topPerformingULBS";
		final static String USM_BOTTOM_ULB = "bottomPerformingULBS";
		final static String USM_TOTAL_CUMULATIVE = "cumulativeFeedbackSubmitted";
		

		final static String SERVICE_REGULARIZATION_TOP_ULB_BY_PERFORMANCE = "regularizationTopUlbByPerformance";
		final static String SERVICE_REGULARIZATION_BOTTOM_ULB_BY_PERFORMANCE = "regularizationBottomUlbByPerformance";
		final static String SERVICE_REGULARIZATION_SERVICE_SUMMARY = "regularizationServiceSummary";
		final static String SERVICE_REGULARIZATION_TOTAL_APPLICATIONS_RECEIVED = "regularizationTotalApplicationsSubmitted";
		final static String SERVICE_REGULARIZATION_TOTAL_CERTIFICATE_ISSUED = "regularizationTotalCertificateIssued";
		final static String SERVICE_REGULARIZATION_TOTAL_APPLICATIONS_REJECTED = "regularizationTotalRejectedApplications";
		final static String SERVICE_REGULARIZATION_TOTAL_APPLICATIONS_PENDING = "regularizationTotalPendingApplications";
		final static String SERVICE_REGULARIZATION_AVG_DAYS_TO_ISSUE_CERTIFICATE = "regularizationAverageDaysToIssueCertificate";
		final static String SERVICE_REGULARIZATION_MIN_DAYS_TO_ISSUE_CERTIFICATE = "regularizationMinDaysToIssueCertificate";
		final static String SERVICE_REGULARIZATION_MAX_DAYS_TO_ISSUE_CERTIFICATE = "regularizationMaxDaysToIssueCertificate";

		final static String REVENUE_REGULARIZATION_TOTAL_COLLECTION = "regularizationTotalCollection";
		final static String REVENUE_REGULARIZATION_TODAYS_COLLECTION = "regularizationTodaysCollection";
		final static String REVENUE_REGULARIZATION_APP_FEE_COLLECTIONS = "regularizationApplicationFeesCollection";
		final static String REVENUE_REGULARIZATION_SANC_FEE_COLLECTIONS = "regularizationSanctionFeesCollection";
		final static String REVENUE_REGULARIZATION_CUMULATIVE_COLLECTION = "regularizationCumulativeCollections";
		final static String REVENUE_REGULARIZATION_COLLECTION_REPORT = "regularizationCollectionReport";
        final static String SERVICE_REGULARIZATION_SERVICE_REPORT = "regularizationServiceReport";
		

		final static String SERVICE_BLR_APPLICATION_BREAKDOWN = "regularizationApplicationsPendingBreakdown";


	}

}
