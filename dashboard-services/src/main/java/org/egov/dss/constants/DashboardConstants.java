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

	public static final String BS_HOME_REVENUE = "HOME_REVENUE";

	public static final String TESTING_TENANT = "od.testing";

	public static final String WS_CONNECTION_ACTIVATED = "CONNECTION_ACTIVATED";

	public static final String BUSINESS_SERICE_BPA1 = "BPA1";

	public static final String BUSINESS_SERICE_BPA2 = "BPA2";

	public static final String BUSINESS_SERICE_BPA3 = "BPA3";

	public static final String BUSINESS_SERICE_BPA4 = "BPA4";

	public static final String BUSINESS_SERICE_BPA5 = "BPA5";

	public static final String BUSINESS_SERICE_BPA6 = "BPA6";

	public static final String BUSINESS_SERICE_BPA7 = "BPA7";

	public static final String BUSINESS_SERICE_BPA8 = "BPA8";

	public static final String BUSINESS_SERICE_BPA9 = "BPA9";

	public static final String BUSINESS_SERICE_BPA10 = "BPA10";
	
	public static final String MODULE_LEVEL_OBPS = "OBPS";
	
	public static final String MODULE_LEVEL_PT = "PT"; 
	
	public static final String MODULE_LEVEL_PGR = "PGR";  
	
	public static final String STATUS_APPROVED = "APPROVED";
	
	public static final String STATUS_CANCELLED = "CANCELLED";
	
	public static final String STATUS_DISHONOURED = "DISHONOURED";
	
	public static final String ONLINE_PAYMENT = "ONLINE";
	
	public static final String NEFT_PAYMENT = "ONLINE_NEFT";
	
	public static final String RTGS_PAYMENT = "ONLINE_RTGS";
	
	public static final String CARD_PAYMENT = "CARD";
	
	public static final String CHEQUE_PAYMENT = "CHEQUE";
	
	public static final String CASH_PAYMENT = "CASH";
	
	public static final String PERFORM = "perform";
	
	public static final String RANK = "Rank";
	
	public static final String STATUS_ACTIVE = "ACTIVE";
	
	public static final String PREVIOUS_YEAR_TOTAL_COLLECTION = "Previous Year Total Collection";
	
	public static final String PREVIOUS_YEAR_TARGET_COLLECTION = "Previous Year Target Collection";
	
	public static final String WS_CONNECTION_TYPE_METERED = "Metered";

	public static final String WS_CONNECTION_TYPE_NON_METERED = "Non Metered";

	public static final String WS_CONNECTIONFACILITY_WATER = "WATER";

	public static final String WS_CONNECTIONFACILITY_SEWERAGE = "SEWERAGE";

	public static final String WS_CONNECTIONFACILITY_WATER_SEWERAGE = "WATER-SEWERAGE";
	
	public static final List<String> OBPS_REVENUE_ALL_BS = Collections
			.unmodifiableList(Arrays.asList(BUSINESS_SERVICE_BPA_APP_FEE, BUSINESS_SERVICE_BPA_SAN_FEE));
	
	public static final List<String> PT_REVENUE_ALL_BS = Collections
			.unmodifiableList(Arrays.asList(BUSINESS_SERVICE_PT, BUSINESS_SERVICE_PT_MUTATION));

	public static final List<String> WS_REVENUE_ALL_BS = Collections.unmodifiableList(Arrays.asList(BUSINESS_SERVICE_WS,
			BUSINESS_SERVICE_SW, BUSINESS_SERVICE_WS_ONE_TIME_FEE, BUSINESS_SERVICE_SW_ONE_TIME_FEE));

	public static final List<String> OBPS_ALL_BUSINESS_SERVICES = Collections
			.unmodifiableList(Arrays.asList(BUSINESS_SERICE_BPA1, BUSINESS_SERICE_BPA2, BUSINESS_SERICE_BPA3,
					BUSINESS_SERICE_BPA4, BUSINESS_SERICE_BPA5, BUSINESS_SERICE_BPA6, BUSINESS_SERICE_BPA7,
					BUSINESS_SERICE_BPA8, BUSINESS_SERICE_BPA9, BUSINESS_SERICE_BPA10));

	public static final List<String> ALL_REVENUE_BUSINESS_SERVICES = Collections.unmodifiableList(Arrays.asList(
			BUSINESS_SERVICE_PT,BUSINESS_SERVICE_WS,BUSINESS_SERVICE_WS_ONE_TIME_FEE,BUSINESS_SERVICE_SW,BUSINESS_SERVICE_SW_ONE_TIME_FEE,
			BUSINESS_SERVICE_MR,BUSINESS_SERVICE_TL,BUSINESS_SERVICE_BPA_APP_FEE,BUSINESS_SERVICE_BPA_SAN_FEE,BUSINESS_SERVICE_OC_APP_FEE,BUSINESS_SERVICE_OC_SAN_FEE));
	
	public static final List<String> ALL_DIGITAL_PAYMENT_MODE = Collections.unmodifiableList(Arrays.asList(
			ONLINE_PAYMENT,NEFT_PAYMENT, RTGS_PAYMENT,CARD_PAYMENT));




}
