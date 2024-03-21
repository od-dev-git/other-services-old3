package org.egov.report.repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.egov.report.config.ReportServiceConfiguration;
import org.egov.report.model.Advance;
import org.egov.report.model.DCBArrearDue;
import org.egov.report.model.DCBDemand;
import org.egov.report.model.DCBPayment;
import org.egov.report.model.DCBProperty;
import org.egov.report.model.UtilityReportDetails;
import org.egov.report.producer.KafkaProducer;
import org.egov.report.repository.builder.DCBQueryBuilder;
import org.egov.report.repository.rowmapper.AdvanceRowMapper;
import org.egov.report.repository.rowmapper.DCBArrearDueRowMapper;
import org.egov.report.repository.rowmapper.DCBDemandRowMapper;
import org.egov.report.repository.rowmapper.DCBPaymentRowMapper;
import org.egov.report.repository.rowmapper.DCBPropertyRowMapper;
import org.egov.report.repository.rowmapper.UtilityReportDetailsRowMapper;
import org.egov.report.web.model.UtilityReportRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import lombok.extern.slf4j.Slf4j;

@Repository
@Slf4j
public class DCBRepository {

	@Autowired
	private DCBQueryBuilder queryBuilder;
	
	@Autowired
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Autowired
	private KafkaProducer producer;
	
	@Autowired
	private ReportServiceConfiguration config;
	
	
	public Map<String,DCBProperty> getPropertyDetails(String tenantId) {

		Map<String, Object> preparedStatementValues = new HashMap<>();

		String propertiesDetailsQuery = queryBuilder.getPropertiesDetialsQuery(tenantId, preparedStatementValues);

		Map<String,DCBProperty> properties = namedParameterJdbcTemplate.query(propertiesDetailsQuery, preparedStatementValues,
				new DCBPropertyRowMapper());

		return properties;

	}

	public Map<String,DCBPayment> getCollections(String tenantId, Long startDate, Long endDate) {

		Map<String, Object> preparedStatementValues = new HashMap<>();

		String collectionsQuery = queryBuilder.getCollectionsQuery(tenantId, startDate, endDate,
				preparedStatementValues);

		Map<String,DCBPayment> payments = namedParameterJdbcTemplate.query(collectionsQuery, preparedStatementValues,
				new DCBPaymentRowMapper());

		return payments;
	}

	public Map<String,DCBDemand> getDemands(String tenantId, Long startDate, Long endDate) {
		
		Map<String, Object> preparedStatementValues = new HashMap<>();

		String demandsQuery = queryBuilder.getDemandsQuery(tenantId, startDate, endDate,
				preparedStatementValues);

		Map<String,DCBDemand> demands = namedParameterJdbcTemplate.query(demandsQuery, preparedStatementValues,
				new DCBDemandRowMapper());

		return demands;
		
	}

	public Map<String,DCBArrearDue> getArrearDue(String tenantId, Long startDate) {
		
		Map<String, Object> preparedStatementValues = new HashMap<>();

		String arrearDueQuery = queryBuilder.getArrearDueQuery(tenantId, startDate,
				preparedStatementValues);

		Map<String,DCBArrearDue> arrearDue = namedParameterJdbcTemplate.query(arrearDueQuery, preparedStatementValues,
				new DCBArrearDueRowMapper());

		return arrearDue;
	}
	
	public List<UtilityReportDetails> isReportExist(String reportType, String financialYear, String tenantId) {
		String query = "SELECT * FROM eg_bpa_utility_reports WHERE reporttype = '" + reportType
				+ "' and financialyear = '" + financialYear + "' and tenantid = '" + tenantId
				+ "' ORDER BY lastmodifiedtime DESC;";
		log.info("Query for DCB Report search:", query);

		List<UtilityReportDetails> reportDetailsList = jdbcTemplate.query(query, new UtilityReportDetailsRowMapper());
		if (reportDetailsList.isEmpty())
			return new ArrayList<>();
		return reportDetailsList;
	}
	
	public void saveReportDetails(UtilityReportRequest utilityReportRequest) {
		producer.push(config.getSaveUtilityReportTopic(), utilityReportRequest);
	}



	public void updateReportDetails(UtilityReportRequest utilityReportRequest) {
		producer.push(config.getUpdateUtilityReportTopic(), utilityReportRequest);
	}

	public Map<String, Advance> getTotalAdvanceAmount(String tenantId) {

		Map<String, Object> preparedStatementValues = new HashMap<>();

		String advanceQuery = queryBuilder.getTotalAdvanceQuery(tenantId, preparedStatementValues);

		Map<String, Advance> advanceAmounts = namedParameterJdbcTemplate.query(advanceQuery, preparedStatementValues,
				new AdvanceRowMapper());

		return advanceAmounts;

	}

}
