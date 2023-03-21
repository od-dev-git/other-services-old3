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
		
		
		final static String SERVICE_DSS_TOTAL_APPLICATION_OVERVIEW = "dssTotalApplicationOverview";
		final static String SERVICE_DSS_CLOSED_APPLICATION_OVERVIEW = "dssClosedApplicationOverview";
		final static String SERVICE_DSS_SLA_ACHIEVED_OVERVIEW = "dssSlaAchievedOverview";
		final static String SERVICE_COTOZN_REGISTERED = "citizenRegistered";
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
		
		final static String SERVICE_WS_TOTAL_ACTIVE_CONNECTIONS = "wstotalConnectionOverview";
		final static String SERVICE_MR_TOTAL_APPLICATION = "mrtotalApplications";
		final static String SERVICE_PGR_TOTAL_COMPLAINTS = "totalComplaints";
		final static String SERVICE_OBPS_TOTAL_PERMITS = "bpaTotalPermitsIssued";
		final static String REVENUE_MR_TOTAL_COLLECTION = "mrtotalCollection";
		final static String REVENUE_BPA_TOTAL_COLLECTION = "bpaTotalCollection";
		final static String REVENUE_WS_TOTAL_COLLECTION = "wstotalCollection";
		final static String SERVICE_PGR_SLA_ACHIEVED = "slaAchieved";
	}

}
