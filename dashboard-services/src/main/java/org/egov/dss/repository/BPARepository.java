package org.egov.dss.repository;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.egov.dss.config.ConfigurationLoader;
import org.egov.dss.model.BpaSearchCriteria;
import org.egov.dss.repository.builder.BpaQueryBuilder;
import org.egov.dss.repository.rowmapper.ULBPerformanceRateRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.SingleColumnRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Repository
public class BPARepository {
	
	@Autowired
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	
	@Autowired
	private BpaQueryBuilder bpaQueryBuilder;
	
	@Autowired
	private ConfigurationLoader config;
	
	public Object getTotalPermitsIssued(BpaSearchCriteria bpaSearchCriteria) {
        Map<String, Object> preparedStatementValues = new HashMap<>();
        String query = bpaQueryBuilder.getTotalPermitIssued(bpaSearchCriteria, preparedStatementValues);
        log.info("query: "+query);
        List<Integer> result = namedParameterJdbcTemplate.query(query, preparedStatementValues, new SingleColumnRowMapper<>(Integer.class));
        return result.get(0);
    }
	
	public Object getSlaAchievedAppCount(BpaSearchCriteria bpaSearchCriteria) {
        Map<String, Object> preparedStatementValues = new HashMap<>();
        bpaSearchCriteria.setSlaThreshold(config.getSlaBpaThreshold());
        String query = bpaQueryBuilder.getTotalPermitIssued(bpaSearchCriteria, preparedStatementValues);
        log.info("query: "+query);
        List<Integer> result = namedParameterJdbcTemplate.query(query, preparedStatementValues, new SingleColumnRowMapper<>(Integer.class));
        return result.get(0);
    }

	public Integer totalApplicationsReceived(BpaSearchCriteria bpaSearchCriteria) {
        Map<String, Object> preparedStatementValues = new HashMap<>();
        String query = bpaQueryBuilder.getGeneralQuery(bpaSearchCriteria, preparedStatementValues);
        log.info("query: "+query);
        List<Integer> result = namedParameterJdbcTemplate.query(query, preparedStatementValues, new SingleColumnRowMapper<>(Integer.class));
        return result.get(0);
	}
	

	public Integer totalApplicationsRejected(BpaSearchCriteria bpaSearchCriteria) {
        Map<String, Object> preparedStatementValues = new HashMap<>();
        String query = bpaQueryBuilder.getGeneralQuery(bpaSearchCriteria, preparedStatementValues);
        log.info("query: "+query);
        List<Integer> result = namedParameterJdbcTemplate.query(query, preparedStatementValues, new SingleColumnRowMapper<>(Integer.class));
        return result.get(0);
	}

	public Integer totalApplicationsPending(BpaSearchCriteria bpaSearchCriteria) {
        Map<String, Object> preparedStatementValues = new HashMap<>();
        String query = bpaQueryBuilder.getGeneralQuery(bpaSearchCriteria, preparedStatementValues);
        log.info("query: "+query);
        List<Integer> result = namedParameterJdbcTemplate.query(query, preparedStatementValues, new SingleColumnRowMapper<>(Integer.class));
        return result.get(0);
	}

	public Integer getMaxDaysToIssuePermit(BpaSearchCriteria bpaSearchCriteria) {
        Map<String, Object> preparedStatementValues = new HashMap<>();
        String query = bpaQueryBuilder.getMaxDaysToIssuePermitQuery(bpaSearchCriteria, preparedStatementValues);
        log.info("query: "+query);
        List<Integer> result = namedParameterJdbcTemplate.query(query, preparedStatementValues, new SingleColumnRowMapper<>(Integer.class));
        return result.get(0);
	}

	public Integer getMinDaysToIssuePermit(BpaSearchCriteria bpaSearchCriteria) {
        Map<String, Object> preparedStatementValues = new HashMap<>();
        String query = bpaQueryBuilder.getMinDaysToIssuePermitQuery(bpaSearchCriteria, preparedStatementValues);
        log.info("query: "+query);
        List<Integer> result = namedParameterJdbcTemplate.query(query, preparedStatementValues, new SingleColumnRowMapper<>(Integer.class));
        return result.get(0);
	}

	public BigDecimal getAvgDaysToIssuePermit(BpaSearchCriteria bpaSearchCriteria) {
        Map<String, Object> preparedStatementValues = new HashMap<>();
        String query = bpaQueryBuilder.getAvgDaysToIssuePermitQuery(bpaSearchCriteria, preparedStatementValues);
        log.info("query: "+query);
        List<BigDecimal> result = namedParameterJdbcTemplate.query(query, preparedStatementValues, new SingleColumnRowMapper<>(BigDecimal.class));
        return result.get(0);
	}

	public Integer getSlaCompliancePermit(BpaSearchCriteria bpaSearchCriteria) {
        Map<String, Object> preparedStatementValues = new HashMap<>();
        String query = bpaQueryBuilder.getSLAComplianceGeneralQuery(bpaSearchCriteria, preparedStatementValues);
        log.info("query: "+query);
        List<Integer> result = namedParameterJdbcTemplate.query(query, preparedStatementValues, new SingleColumnRowMapper<>(Integer.class));
        return result.get(0);
	}

	public Integer getSlaComplianceOtherThanLowRisk(BpaSearchCriteria bpaSearchCriteria) {
        Map<String, Object> preparedStatementValues = new HashMap<>();
        String query = bpaQueryBuilder.getSLAComplianceGeneralQuery(bpaSearchCriteria, preparedStatementValues);
        log.info("query: "+query);
        List<Integer> result = namedParameterJdbcTemplate.query(query, preparedStatementValues, new SingleColumnRowMapper<>(Integer.class));
        return result.get(0);
	}

	public Integer getSlaCompliancePreApprovedPlan(BpaSearchCriteria bpaSearchCriteria) {
        Map<String, Object> preparedStatementValues = new HashMap<>();
        String query = bpaQueryBuilder.getSLAComplianceGeneralQuery(bpaSearchCriteria, preparedStatementValues);
        log.info("query: "+query);
        List<Integer> result = namedParameterJdbcTemplate.query(query, preparedStatementValues, new SingleColumnRowMapper<>(Integer.class));
        return result.get(0);
	}

	public Integer getSlaComplianceBuildingPermit(BpaSearchCriteria bpaSearchCriteria) {
        Map<String, Object> preparedStatementValues = new HashMap<>();
        String query = bpaQueryBuilder.getSLAComplianceGeneralQuery(bpaSearchCriteria, preparedStatementValues);
        log.info("query: "+query);
        List<Integer> result = namedParameterJdbcTemplate.query(query, preparedStatementValues, new SingleColumnRowMapper<>(Integer.class));
        return result.get(0);
	}

	public HashMap<String, Long> getTenantWisePermitsIssuedList(BpaSearchCriteria bpaSearchCriteria) {
		Map<String, Object> preparedStatementValues = new HashMap<>();
        String query = bpaQueryBuilder.getTenantWisePermitsIssuedListQuery(bpaSearchCriteria, preparedStatementValues);
        log.info("query for BPA Tenant Wise Permits Issued List Query : "+query);
        HashMap<String, Long> result = namedParameterJdbcTemplate.query(query, preparedStatementValues, new ULBPerformanceRateRowMapper());
        return result;
	}

	public HashMap<String, Long> getTenantWiseApplicationsReceivedList(BpaSearchCriteria bpaSearchCriteria) {
		Map<String, Object> preparedStatementValues = new HashMap<>();
        String query = bpaQueryBuilder.getTenantWiseApplicationsReceivedListQuery(bpaSearchCriteria, preparedStatementValues);
        log.info("query for Tenant Wise Applications Received List : "+query);
        HashMap<String, Long> result = namedParameterJdbcTemplate.query(query, preparedStatementValues, new ULBPerformanceRateRowMapper());
        return result;
	}


}
