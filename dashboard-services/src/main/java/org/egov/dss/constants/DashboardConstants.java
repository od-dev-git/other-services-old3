package org.egov.dss.constants;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class DashboardConstants {
	
	public static  final String COMMON = "COMMON";
	
	public static  final String BUSINESS_SERVICE_PT = "PT";
	
	public static  final String BUSINESS_SERVICE_PT_MUTATION = "PT.MUTATION";
	
	public static  final String BUSINESS_SERVICE_WS = "WS";
	
	public static  final String BUSINESS_SERVICE_SW = "SW";
	
	public static  final String BUSINESS_SERVICE_WS_ONE_TIME_FEE = "WS.ONE_TIME_FEE";
	
	public static  final String BUSINESS_SERVICE_SW_ONE_TIME_FEE = "SW.ONE_TIME_FEE";
	
	public static  final String BUSINESS_SERVICE_TL = "TL";
	
	public static  final String BUSINESS_SERVICE_MR = "MR";
	
	public static  final String BUSINESS_SERVICE_BPA_APP_FEE = "BPA.NC_APP_FEE";
	
	public static  final String BUSINESS_SERVICE_BPA_SAN_FEE = "BPA.NC_SAN_FEE";
	
	public static  final String BUSINESS_SERVICE_OC_APP_FEE = "BPA.NC_OC_APP_FEE";
	
	public static  final String BUSINESS_SERVICE_OC_SAN_FEE = "BPA.NC_OC_SAN_FEE";
	
	public static final String EG_BS_JSON_EXCEPTION_KEY = "EG_BS_JSON_EXCEPTION";
	
	public static final String EG_BS_JSON_EXCEPTION_MSG = "Exception occured while parsing additional details";
	
	public static final String DB_TYPE_JSONB = "jsonb";
	
	public static final List<String> ALL_REVENUE_BUSINESS_SERVICES = Collections.unmodifiableList(Arrays.asList(
			BUSINESS_SERVICE_PT,BUSINESS_SERVICE_WS,BUSINESS_SERVICE_WS_ONE_TIME_FEE,BUSINESS_SERVICE_SW,BUSINESS_SERVICE_SW_ONE_TIME_FEE,
			BUSINESS_SERVICE_MR,BUSINESS_SERVICE_TL,BUSINESS_SERVICE_BPA_APP_FEE,BUSINESS_SERVICE_BPA_SAN_FEE,BUSINESS_SERVICE_OC_APP_FEE,BUSINESS_SERVICE_OC_SAN_FEE));

}
