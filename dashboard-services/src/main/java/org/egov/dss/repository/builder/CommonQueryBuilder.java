package org.egov.dss.repository.builder;

import org.springframework.stereotype.Component;

@Component
public class CommonQueryBuilder {
	
	public static final String PAYLOAD_QUERY_SQL = " select edd.id, edd.visualizationcode, edd.modulelevel, edd.startdate, edd.enddate, edd.timeinterval,"
			+ "edd.charttype , edd.tenantid, edd.districtid, edd.city, edd.headername, edd.valuetype"
			+ " from eg_dss_response edd ";
	
    public static final String RESPONSE_DATA_UPDATE_QUERY = "Update eg_dss_response set responsedata = ?, lastmodifiedtime = ?, enddate = ? where id =? ";
    
   

}
