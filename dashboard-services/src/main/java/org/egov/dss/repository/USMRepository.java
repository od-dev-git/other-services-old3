package org.egov.dss.repository;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.egov.dss.model.Chart;
import org.egov.dss.model.UsmSearchCriteria;
import org.egov.dss.repository.builder.USMQueryBuilder;
import org.egov.dss.repository.rowmapper.CalculateTotalRowmapper;
import org.egov.dss.repository.rowmapper.ChartRowMapper;
import org.egov.dss.repository.rowmapper.TenantWiseCollectionRowMapper;
import org.egov.dss.repository.rowmapper.ULBPerformanceRateRowMapper;
import org.egov.dss.repository.rowmapper.UsmRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.SingleColumnRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Repository
public class USMRepository {
	@Autowired
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	@Autowired
	private USMQueryBuilder usmQueryBuilder;


	public Object getTotalFeedbackSubmitted(UsmSearchCriteria usmSearchCriteria) {
		Map<String, Object> preparedStatementValues = new HashMap<>();
		String query = USMQueryBuilder.getTotalFeebackSubmitted(usmSearchCriteria, preparedStatementValues);
		log.info("query: " + query);
		List<Integer> result = namedParameterJdbcTemplate.query(query, preparedStatementValues,
				new SingleColumnRowMapper<>(Integer.class));
		return result.get(0);
	}

	public Object getTotalOpenIssue(UsmSearchCriteria usmSearchCriteria) {
		Map<String, Object> preparedStatementValues = new HashMap<>();
		String query = USMQueryBuilder.getTotalOpenIssue(usmSearchCriteria, preparedStatementValues);
		log.info("query: " + query);
		List<Integer> result = namedParameterJdbcTemplate.query(query, preparedStatementValues,
				new SingleColumnRowMapper<>(Integer.class));
		return result.get(0);
	}

	public Object getTotalClosedIssue(UsmSearchCriteria usmSearchCriteria) {
		Map<String, Object> preparedStatementValues = new HashMap<>();
		String query = USMQueryBuilder.getTotalClosedIssue(usmSearchCriteria, preparedStatementValues);
		log.info("query: " + query);
		List<Integer> result = namedParameterJdbcTemplate.query(query, preparedStatementValues,
				new SingleColumnRowMapper<>(Integer.class));
		return result.get(0);
	}

	public Object getTotalClosedSatisfiedIssue(UsmSearchCriteria usmSearchCriteria) {
		Map<String, Object> preparedStatementValues = new HashMap<>();
		String query = USMQueryBuilder.getTotalClosedSatisfactoryIssue(usmSearchCriteria, preparedStatementValues);
		log.info("query: " + query);
		List<Integer> result = namedParameterJdbcTemplate.query(query, preparedStatementValues,
				new SingleColumnRowMapper<>(Integer.class));
		return result.get(0);
	}

	public Object getTotalUnattendedIssue(UsmSearchCriteria usmSearchCriteria) {
		Map<String, Object> preparedStatementValues = new HashMap<>();
		String query = USMQueryBuilder.getTotalUnattendedIssue(usmSearchCriteria, preparedStatementValues);
		log.info("query: " + query);
		List<Integer> result = namedParameterJdbcTemplate.query(query, preparedStatementValues,
				new SingleColumnRowMapper<>(Integer.class));
		return result.get(0);
	}

	public Object getTotalEscalatedIssue(UsmSearchCriteria usmSearchCriteria) {
		Map<String, Object> preparedStatementValues = new HashMap<>();
		String query = USMQueryBuilder.getTotalEscalatedIssue(usmSearchCriteria, preparedStatementValues);
		log.info("query: " + query);
		List<Integer> result = namedParameterJdbcTemplate.query(query, preparedStatementValues,
				new SingleColumnRowMapper<>(Integer.class));
		return result.get(0);
	}

	public Object getTotalRespondedEscalatedIssue(UsmSearchCriteria usmSearchCriteria) {
		Map<String, Object> preparedStatementValues = new HashMap<>();
		String query = USMQueryBuilder.getTotalEscalatedRespondedIssue(usmSearchCriteria, preparedStatementValues);
		log.info("query: " + query);
		List<Integer> result = namedParameterJdbcTemplate.query(query, preparedStatementValues,
				new SingleColumnRowMapper<>(Integer.class));
		return result.get(0);
	}

	public Object getTotalSlumFeedbackSubmitted(UsmSearchCriteria usmSearchCriteria) {
		Map<String, Object> preparedStatementValues = new HashMap<>();
		String query = USMQueryBuilder.getTotalSlumSubmittedFeedback(usmSearchCriteria, preparedStatementValues);
		log.info("query: " + query);
		List<Integer> result = namedParameterJdbcTemplate.query(query, preparedStatementValues,
				new SingleColumnRowMapper<>(Integer.class));

		return result.get(0);
	}

	public List<Chart> topIssueCategory(UsmSearchCriteria criteria) {

		Map<String, Object> preparedStatementValues = new HashMap<>();
		String query = USMQueryBuilder.getTopIssueByCategory(criteria, preparedStatementValues);
		log.info("query for get Top Five Complaints: " + query);
		return namedParameterJdbcTemplate.query(query, preparedStatementValues, new UsmRowMapper());
	}

	public HashMap<String, BigDecimal> getTenantWiseFeedback(UsmSearchCriteria criteria) {
		Map<String, Object> preparedStatementValues = new HashMap<>();
		String query = usmQueryBuilder.getTotalFeedbackByTenantWise(criteria, preparedStatementValues);
		log.info("query for Tenant Wise Feedback: " + query);
		return namedParameterJdbcTemplate.query(query, preparedStatementValues, new TenantWiseCollectionRowMapper());
	}

	public HashMap<String, BigDecimal> getTenantWiseClosedTicket(UsmSearchCriteria criteria) {
		Map<String, Object> preparedStatementValues = new HashMap<>();
		String query = usmQueryBuilder.getTotalClosedTicketByTenantWise(criteria, preparedStatementValues);
		log.info("query for Tenant Wise Closed Tikcets: " + query);
		return namedParameterJdbcTemplate.query(query, preparedStatementValues, new TenantWiseCollectionRowMapper());
	}

	public HashMap<String, Long> getTenantWiseTotalClosedTicket(UsmSearchCriteria criteria) {
		Map<String, Object> preparedStatementValues = new HashMap<>();
		String query = usmQueryBuilder.getTotalClosedTicketByTenantWise(criteria, preparedStatementValues);
		log.info("query for Tenant Wise Closed Tikcets: " + query);
		return namedParameterJdbcTemplate.query(query, preparedStatementValues, new CalculateTotalRowmapper());
	}

	public HashMap<String, BigDecimal> getTenantWiseOpenTicket(UsmSearchCriteria criteria) {
		Map<String, Object> preparedStatementValues = new HashMap<>();
		String query = usmQueryBuilder.getTotalOpenTicketByTenantWise(criteria, preparedStatementValues);
		log.info("query for Tenant Wise Open ticket: " + query);
		return namedParameterJdbcTemplate.query(query, preparedStatementValues, new TenantWiseCollectionRowMapper());
	}

	public HashMap<String, BigDecimal> getTenantWiseUnattendedTicket(UsmSearchCriteria criteria) {
		Map<String, Object> preparedStatementValues = new HashMap<>();
		String query = usmQueryBuilder.getTotalUnattendedTicketByTenantWise(criteria, preparedStatementValues);
		log.info("query for Tenant Wise Open ticket: " + query);
		return namedParameterJdbcTemplate.query(query, preparedStatementValues, new TenantWiseCollectionRowMapper());
	}

	public HashMap<String, BigDecimal> getTenantWiseClosedSatisfacotryTicket(UsmSearchCriteria criteria) {
		Map<String, Object> preparedStatementValues = new HashMap<>();
		String query = usmQueryBuilder.getTotalSatisfiedTicketByTenantWise(criteria, preparedStatementValues);
		log.info("query for Tenant Wise Open ticket: " + query);
		return namedParameterJdbcTemplate.query(query, preparedStatementValues, new TenantWiseCollectionRowMapper());
	}

	public HashMap<String, BigDecimal> getTenantWiseClosedDissatisfacotryTicket(UsmSearchCriteria criteria) {
		Map<String, Object> preparedStatementValues = new HashMap<>();
		String query = usmQueryBuilder.getTotalDissatisfiedTicketByTenantWise(criteria, preparedStatementValues);
		log.info("query for Tenant Wise Open ticket: " + query);
		return namedParameterJdbcTemplate.query(query, preparedStatementValues, new TenantWiseCollectionRowMapper());
	}

	public List<Chart> getCategoryWiseCount(UsmSearchCriteria criteria) {
		Map<String, Object> preparedStatementValues = new HashMap<>();
		String query = USMQueryBuilder.getCategoryWiseIssueCount(criteria, preparedStatementValues);
		log.info("query for Category wise issue count: " + query);
		return namedParameterJdbcTemplate.query(query, preparedStatementValues, new UsmRowMapper());
	}

	public HashMap<String, Long> getTenantWiseIssue(UsmSearchCriteria criteria) {
		Map<String, Object> preparedStatementValues = new HashMap<>();
		String query = usmQueryBuilder.getTotalIssueTenantWise(criteria, preparedStatementValues);
		log.info("query for Tenant Wise  Issue: " + query);
		return namedParameterJdbcTemplate.query(query, preparedStatementValues, new ULBPerformanceRateRowMapper());
	}

	public List<Chart> getCumulativeApplications(UsmSearchCriteria criteria) {
		Map<String, Object> preparedStatementValues = new HashMap<>();
		String query = USMQueryBuilder.getCumulativeApplications(criteria, preparedStatementValues);
		log.info("query for USM Cumulative Applications : " + query);
		List<Chart> result = namedParameterJdbcTemplate.query(query, preparedStatementValues, new ChartRowMapper());
		return result;
	}

	public HashMap<String, BigDecimal> getTotalNoWaterFeedbackSubmitted(UsmSearchCriteria usmSearchCriteria) {
		Map<String, Object> preparedStatementValues = new HashMap<>();
		String query = USMQueryBuilder.getTotalWaterFeedbackSubmitted(usmSearchCriteria, preparedStatementValues);
		log.info("query: " + query);
		return namedParameterJdbcTemplate.query(query, preparedStatementValues, new TenantWiseCollectionRowMapper());

	}

	public HashMap<String, BigDecimal> getTotalNoClosedIssueForWater(UsmSearchCriteria usmSearchCriteria) {
		Map<String, Object> preparedStatementValues = new HashMap<>();
		String query = USMQueryBuilder.getTotalClosedIssueWater(usmSearchCriteria, preparedStatementValues);
		log.info("query: " + query);
		return namedParameterJdbcTemplate.query(query, preparedStatementValues, new TenantWiseCollectionRowMapper());
	}
	
	public HashMap<String, BigDecimal> getTotalNoOpenIssueForWater(UsmSearchCriteria usmSearchCriteria) {
		Map<String, Object> preparedStatementValues = new HashMap<>();
		String query = USMQueryBuilder.getTotalOpenIssueWater(usmSearchCriteria, preparedStatementValues);
		log.info("query: " + query);
		return namedParameterJdbcTemplate.query(query, preparedStatementValues, new TenantWiseCollectionRowMapper());
	}

	public HashMap<String, BigDecimal> getTotalIssueForWater(UsmSearchCriteria usmSearchCriteria) {
		Map<String, Object> preparedStatementValues = new HashMap<>();
		String query = USMQueryBuilder.getTotalNoOfWaterIssue(usmSearchCriteria, preparedStatementValues);
		log.info("query: " + query);
		return namedParameterJdbcTemplate.query(query, preparedStatementValues, new TenantWiseCollectionRowMapper());
	}

	public HashMap<String, BigDecimal> getTotalUnattendedIssueForWater(UsmSearchCriteria usmSearchCriteria) {
		Map<String, Object> preparedStatementValues = new HashMap<>();
		String query = USMQueryBuilder.getTotalNoOfUnattendedWaterIssue(usmSearchCriteria, preparedStatementValues);
		log.info("query: " + query);
		return namedParameterJdbcTemplate.query(query, preparedStatementValues, new TenantWiseCollectionRowMapper());
	}

	public HashMap<String, BigDecimal> getTotalSatisfactoryIssueForWater(UsmSearchCriteria usmSearchCriteria) {
		Map<String, Object> preparedStatementValues = new HashMap<>();
		String query = USMQueryBuilder.getTotalNoOfSatisfactoryWaterIssue(usmSearchCriteria, preparedStatementValues);
		log.info("query: " + query);
		return namedParameterJdbcTemplate.query(query, preparedStatementValues, new TenantWiseCollectionRowMapper());
	}

	public HashMap<String, BigDecimal> getTotalDissatisfactoryIssueForWater(UsmSearchCriteria usmSearchCriteria) {
		Map<String, Object> preparedStatementValues = new HashMap<>();
		String query = USMQueryBuilder.getTotalNoOfDissatisfactoryWaterIssue(usmSearchCriteria,
				preparedStatementValues);
		log.info("query: " + query);
		return namedParameterJdbcTemplate.query(query, preparedStatementValues, new TenantWiseCollectionRowMapper());
	}

	public HashMap<String, BigDecimal> getTotalNoStreetlightFeedbackSubmitted(UsmSearchCriteria usmSearchCriteria) {
		Map<String, Object> preparedStatementValues = new HashMap<>();
		String query = USMQueryBuilder.getTotalStreetlightFeedbackSubmitted(usmSearchCriteria, preparedStatementValues);
		log.info("query: " + query);
		return namedParameterJdbcTemplate.query(query, preparedStatementValues, new TenantWiseCollectionRowMapper());

	}

	public HashMap<String, BigDecimal> getTotalNoClosedIssueForStreetlight(UsmSearchCriteria usmSearchCriteria) {
		Map<String, Object> preparedStatementValues = new HashMap<>();
		String query = USMQueryBuilder.getTotalClosedIssueStreetlight(usmSearchCriteria, preparedStatementValues);
		log.info("query: " + query);
		return namedParameterJdbcTemplate.query(query, preparedStatementValues, new TenantWiseCollectionRowMapper());
	}

	public HashMap<String, BigDecimal> getTotalNoOpenIssueForStreetlight(UsmSearchCriteria usmSearchCriteria) {
		Map<String, Object> preparedStatementValues = new HashMap<>();
		String query = USMQueryBuilder.getTotalOpenIssueStreetlight(usmSearchCriteria, preparedStatementValues);
		log.info("query: " + query);
		return namedParameterJdbcTemplate.query(query, preparedStatementValues, new TenantWiseCollectionRowMapper());
	}
	
	public HashMap<String, BigDecimal> getTotalIssueForStreetlight(UsmSearchCriteria usmSearchCriteria) {
		Map<String, Object> preparedStatementValues = new HashMap<>();
		String query = USMQueryBuilder.getTotalNoOfStreetlighIssue(usmSearchCriteria, preparedStatementValues);
		log.info("query: " + query);
		return namedParameterJdbcTemplate.query(query, preparedStatementValues, new TenantWiseCollectionRowMapper());
	}

	public HashMap<String, BigDecimal> getTotalUnattendedIssueForStreetlight(UsmSearchCriteria usmSearchCriteria) {
		Map<String, Object> preparedStatementValues = new HashMap<>();
		String query = USMQueryBuilder.getTotalNoOfUnattendedstreetlightIssue(usmSearchCriteria,
				preparedStatementValues);
		log.info("query: " + query);
		return namedParameterJdbcTemplate.query(query, preparedStatementValues, new TenantWiseCollectionRowMapper());
	}

	public HashMap<String, BigDecimal> getTotalSatisfactoryIssueForStreetlight(UsmSearchCriteria usmSearchCriteria) {
		Map<String, Object> preparedStatementValues = new HashMap<>();
		String query = USMQueryBuilder.getTotalNoOfSatisfactoryStreetlightIssue(usmSearchCriteria,
				preparedStatementValues);
		log.info("query: " + query);
		return namedParameterJdbcTemplate.query(query, preparedStatementValues, new TenantWiseCollectionRowMapper());
	}

	public HashMap<String, BigDecimal> getTotalDissatisfactoryIssueForStreetlight(UsmSearchCriteria usmSearchCriteria) {
		Map<String, Object> preparedStatementValues = new HashMap<>();
		String query = USMQueryBuilder.getTotalNoOfDissatisfactoryStreetlightIssue(usmSearchCriteria,
				preparedStatementValues);
		log.info("query: " + query);
		return namedParameterJdbcTemplate.query(query, preparedStatementValues, new TenantWiseCollectionRowMapper());
	}

	public HashMap<String, BigDecimal> getTotalNoSanitationFeedbackSubmitted(UsmSearchCriteria usmSearchCriteria) {
		Map<String, Object> preparedStatementValues = new HashMap<>();
		String query = USMQueryBuilder.getTotalSanitationFeedbackSubmitted(usmSearchCriteria, preparedStatementValues);
		log.info("query: " + query);
		return namedParameterJdbcTemplate.query(query, preparedStatementValues, new TenantWiseCollectionRowMapper());

	}

	public HashMap<String, BigDecimal> getTotalNoClosedIssueForSanitation(UsmSearchCriteria usmSearchCriteria) {
		Map<String, Object> preparedStatementValues = new HashMap<>();
		String query = USMQueryBuilder.getTotalClosedIssueSanitation(usmSearchCriteria, preparedStatementValues);
		log.info("query: " + query);
		return namedParameterJdbcTemplate.query(query, preparedStatementValues, new TenantWiseCollectionRowMapper());
	}

	public HashMap<String, BigDecimal> getTotalNoOpenIssueForSanitation(UsmSearchCriteria usmSearchCriteria) {
		Map<String, Object> preparedStatementValues = new HashMap<>();
		String query = usmQueryBuilder.getTotalOpenIssueSanitation(usmSearchCriteria, preparedStatementValues);
		log.info("query: " + query);
		return namedParameterJdbcTemplate.query(query, preparedStatementValues, new TenantWiseCollectionRowMapper());
	}
	
	public HashMap<String, BigDecimal> getTotalIssueForSanitation(UsmSearchCriteria usmSearchCriteria) {
		Map<String, Object> preparedStatementValues = new HashMap<>();
		String query = USMQueryBuilder.getTotalNoOfSanitationIssue(usmSearchCriteria, preparedStatementValues);
		log.info("query: " + query);
		return namedParameterJdbcTemplate.query(query, preparedStatementValues, new TenantWiseCollectionRowMapper());
	}

	public HashMap<String, BigDecimal> getTotalUnattendedIssueForSanitation(UsmSearchCriteria usmSearchCriteria) {
		Map<String, Object> preparedStatementValues = new HashMap<>();
		String query = USMQueryBuilder.getTotalNoOfUnattendedSanitationIssue(usmSearchCriteria,
				preparedStatementValues);
		log.info("query: " + query);
		return namedParameterJdbcTemplate.query(query, preparedStatementValues, new TenantWiseCollectionRowMapper());
	}

	public HashMap<String, BigDecimal> getTotalSatisfactoryIssueForSanitation(UsmSearchCriteria usmSearchCriteria) {
		Map<String, Object> preparedStatementValues = new HashMap<>();
		String query = USMQueryBuilder.getTotalNoOfSatisfactorySanitationIssue(usmSearchCriteria,
				preparedStatementValues);
		log.info("query: " + query);
		return namedParameterJdbcTemplate.query(query, preparedStatementValues, new TenantWiseCollectionRowMapper());
	}

	public HashMap<String, BigDecimal> getTotalDissatisfactoryIssueForSanitation(UsmSearchCriteria usmSearchCriteria) {
		Map<String, Object> preparedStatementValues = new HashMap<>();
		String query = USMQueryBuilder.getTotalNoOfDissatisfactorySanitationIssue(usmSearchCriteria,
				preparedStatementValues);
		log.info("query: " + query);
		return namedParameterJdbcTemplate.query(query, preparedStatementValues, new TenantWiseCollectionRowMapper());
	}

}
