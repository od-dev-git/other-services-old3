package org.egov.report.repository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.egov.report.config.ReportServiceConfiguration;
import org.egov.report.model.UtilityReportDetails;
import org.egov.report.model.UtilityReportSearchCriteria;
import org.egov.report.producer.KafkaProducer;
import org.egov.report.repository.rowmapper.DataMartRowMapper;
import org.egov.report.repository.rowmapper.UtilityReportDetailsRowMapper;
import org.egov.report.web.model.UtilityReportRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.util.ObjectUtils;

import lombok.extern.slf4j.Slf4j;

@Repository
@Slf4j
public class FSMReportRepository {
	
	@Autowired
	private UtilityReportDetailsRowMapper rowMapper;
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Autowired
	private KafkaProducer producer;
	
	@Autowired
	private ReportServiceConfiguration config;
	
	private static final String DATA_MART_QUERY = "select INITCAP(SPLIT_PART(fsm.tenantid,'.',2)) as ulb, fsm.applicationno as ApplicationId,"
			+ " coalesce(fsm.applicationStatus, 'N/A') as ApplicationStatus, coalesce(fsm.nooftrips, 0) as Nooftrips,"
			+ " split_part(propertyusage::text, '.', 1) as PropertyType, case when split_part(propertyusage::text, '.', 2)!= '' then split_part(propertyusage::text, '.', 2) else 'N/A' end as PropertySubType,"
			+ " coalesce(fsm.sanitationType, 'N/A') as OnSiteSanitationType, coalesce(replace(fsmaddress.doorno, ',', '#'), 'N/A') as DoorNumber,"
			+ " coalesce(replace(fsmaddress.street, ',', '#'), 'N/A') as StreetName, coalesce(fsmaddress.city, 'N/A') as City,"
			+ " coalesce(fsmaddress.pincode, 'N/A') as Pincode, coalesce(fsmaddress.additionaldetails->'boundaryType', '{}') as urbanRuralFlag,"
			+ " coalesce(fsmaddress.locality, 'N/A') as Locality, coalesce(fsmaddress.additionaldetails->'gramPanchayat'->'name', '{}') as gp,"
			+ " coalesce(fsmaddress.additionaldetails->'village'->'name', '{}') as village, coalesce(fsmaddress.district, 'N/A') as District,"
			+ " coalesce(fsmaddress.state, 'N/A') as State, coalesce(fsmaddress.slumname, 'N/A') as SlumName, coalesce(fsm.source, 'N/A') as ApplicationSource,"
			+ " coalesce(fsmdso.name, 'N/A') as DesludgingEntity, coalesce(fsmgeolocation.longitude, 0) as Longitude, coalesce(fsmgeolocation.latitude, 0) as Latitude,"
			+ " case when fsmgeolocation.longitude>0 then 'Yes' else 'No' end as GeoLocationProvided, coalesce(fsmvehicle.registrationNumber, 'N/A') as DesludgingVehicleNumber,"
			+ " coalesce(fsm.vehicleType, 'N/A') as VehicleType, coalesce(fsmvehicle.tankcapicity, 0) as VehicleCapacity , coalesce(fsm.additionaldetails->'tripAmount', '0') as tripamount,"
			+ " coalesce(fsm.advanceamount, 0) as advanceAmount, coalesce(fsmpayment.totalamountpaid, 0) as PaymentAmount, coalesce(fsmpayment.paymentmode, 'N/A') as PaymentSource,"
			+ " coalesce(fsmpayment.paymentmode, 'N/A') as PaymentInstrumentType, to_char((to_timestamp(fsm.createdtime / 1000)::timestamp at time zone 'Asia/Kolkata'), 'mm/dd/yyyy HH24:MI:SS') as ApplicationSumbitDate"
			+ " from eg_fsm_application as fsm join eg_fsm_address as fsmaddress on ( fsmaddress.fsm_id = fsm.id ) join eg_fsm_geolocation as fsmgeolocation on ( fsmaddress.id = fsmgeolocation.address_id )"
			+ " left join eg_vendor as fsmdso on ( fsmdso.id = fsm.dso_id) left join eg_vehicle as fsmvehicle on ( fsm.vehicle_id = fsmvehicle.id) "
			+ " left join eg_vehicle_trip_detail as fsmvehicleTripdetail on ( fsmvehicleTripdetail.referenceNo = fsm.applicationNo) left join eg_vehicle_trip as fsmvehicleTrip on ( fsmvehicleTripdetail.trip_id = fsmvehicleTrip.id)"
			+ " left join egcl_bill as egbill on ( egbill.consumercode = fsm.applicationno) left join egcl_paymentdetail as paymentdl on ( paymentdl.billid = egbill.id )"
			+ " left join egcl_payment as fsmpayment on ( fsmpayment.id = paymentdl.paymentid) where fsm.tenantid not in ('od.testing') ";

	public List<UtilityReportDetails> isReportExist(String reportType) {
		
		String query = "SELECT * FROM eg_bpa_utility_reports WHERE reporttype = '" + reportType +"' ORDER BY lastmodifiedtime DESC;";
	    log.info("Query for Applications Report search:", query);
	    
	    List<UtilityReportDetails> reportDetailsList =  jdbcTemplate.query(query, rowMapper);
	    if(reportDetailsList.isEmpty())
			return new ArrayList<>();
	    return reportDetailsList;
	}
	
	public void saveReportDetails(UtilityReportRequest utilityReportRequest) {
		producer.push(config.getSaveUtilityReportTopic(), utilityReportRequest);
	}



	public void updateReportDetails(UtilityReportRequest utilityReportRequest) {
		producer.push(config.getUpdateUtilityReportTopic(), utilityReportRequest);
	}

	public List<Map<String, Object>> getDataMartReport(UtilityReportSearchCriteria searchCriteria) {
		
		log.info("Search Criteria : " + searchCriteria.toString());
		StringBuilder query =  new StringBuilder(DATA_MART_QUERY);
	    
		if (!ObjectUtils.isEmpty(searchCriteria.getTenantId())) {
			query.append(" and fsm.tenantid = '" + searchCriteria.getTenantId() + "'");
		}
		
	    log.info("Query for FSM DATA MART Report : " + query);
	    
	    List<Map<String, Object>> dataMartReportList =  jdbcTemplate.query(query.toString(), new DataMartRowMapper());
	    
	    if(dataMartReportList.isEmpty())
			return Collections.emptyList();
		return dataMartReportList;
	}

}
