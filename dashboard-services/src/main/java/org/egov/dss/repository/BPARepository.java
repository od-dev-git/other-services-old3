package org.egov.dss.repository;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.egov.dss.config.ConfigurationLoader;
import org.egov.dss.model.BpaSearchCriteria;
import org.egov.dss.model.Chart;
import org.egov.dss.repository.builder.BpaQueryBuilder;
import org.egov.dss.repository.rowmapper.BPAPerformanceRateRowMapper;
import org.egov.dss.repository.rowmapper.ChartRowMapper;
import org.egov.dss.repository.rowmapper.RateRowMapper;
import org.egov.dss.repository.rowmapper.TenantWiseCollectionRowMapper;
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
        log.info("query for getTotalPermitsIssued: "+query);
        List<Integer> result = namedParameterJdbcTemplate.query(query, preparedStatementValues, new SingleColumnRowMapper<>(Integer.class));
        return result.get(0);
    }
	
	public Object getSlaAchievedAppCount(BpaSearchCriteria bpaSearchCriteria) {
        Map<String, Object> preparedStatementValues = new HashMap<>();
        bpaSearchCriteria.setSlaThreshold(config.getSlaBpaThreshold());
        String query = bpaQueryBuilder.getTotalPermitIssued(bpaSearchCriteria, preparedStatementValues);
        log.info("query for getSlaAchievedAppCount: "+query);
        List<Integer> result = namedParameterJdbcTemplate.query(query, preparedStatementValues, new SingleColumnRowMapper<>(Integer.class));
        return result.get(0);
    }

	public Integer totalApplicationsReceived(BpaSearchCriteria bpaSearchCriteria) {
        Map<String, Object> preparedStatementValues = new HashMap<>();
        String query = bpaQueryBuilder.getGeneralQuery(bpaSearchCriteria, preparedStatementValues);
        log.info("query for totalApplicationsReceived: "+query);
        List<Integer> result = namedParameterJdbcTemplate.query(query, preparedStatementValues, new SingleColumnRowMapper<>(Integer.class));
        return result.get(0);
	}
	

	public Integer totalApplicationsRejected(BpaSearchCriteria bpaSearchCriteria) {
        Map<String, Object> preparedStatementValues = new HashMap<>();
        String query = bpaQueryBuilder.getGeneralQuery(bpaSearchCriteria, preparedStatementValues);
        log.info("query for totalApplicationsRejected: "+query);
        List<Integer> result = namedParameterJdbcTemplate.query(query, preparedStatementValues, new SingleColumnRowMapper<>(Integer.class));
        return result.get(0);
	}

	public Integer totalApplicationsPending(BpaSearchCriteria bpaSearchCriteria) {
        Map<String, Object> preparedStatementValues = new HashMap<>();
        String query = bpaQueryBuilder.getPendingApplicationQuery(bpaSearchCriteria, preparedStatementValues);
        log.info("query for totalApplicationsPending: "+query);
        List<Integer> result = namedParameterJdbcTemplate.query(query, preparedStatementValues, new SingleColumnRowMapper<>(Integer.class));
        return result.get(0);
	}

	public Integer getMaxDaysToIssuePermit(BpaSearchCriteria bpaSearchCriteria) {
        Map<String, Object> preparedStatementValues = new HashMap<>();
        String query = bpaQueryBuilder.getMaxDaysToIssuePermitQuery(bpaSearchCriteria, preparedStatementValues);
        log.info("query for getMaxDaysToIssuePermit: "+query);
        List<Integer> result = namedParameterJdbcTemplate.query(query, preparedStatementValues, new SingleColumnRowMapper<>(Integer.class));
        return result.get(0);
	}

	public Integer getMinDaysToIssuePermit(BpaSearchCriteria bpaSearchCriteria) {
        Map<String, Object> preparedStatementValues = new HashMap<>();
        String query = bpaQueryBuilder.getMinDaysToIssuePermitQuery(bpaSearchCriteria, preparedStatementValues);
        log.info("query for getMinDaysToIssuePermit: "+query);
        List<Integer> result = namedParameterJdbcTemplate.query(query, preparedStatementValues, new SingleColumnRowMapper<>(Integer.class));
        return result.get(0);
	}

	public BigDecimal getAvgDaysToIssuePermit(BpaSearchCriteria bpaSearchCriteria) {
        Map<String, Object> preparedStatementValues = new HashMap<>();
        String query = bpaQueryBuilder.getAvgDaysToIssuePermitQuery(bpaSearchCriteria, preparedStatementValues);
        log.info("query for getAvgDaysToIssuePermit : "+query);
        List<BigDecimal> result = namedParameterJdbcTemplate.query(query, preparedStatementValues, new SingleColumnRowMapper<>(BigDecimal.class));
        return result.get(0);
	}

	public Integer getSlaCompliancePermit(BpaSearchCriteria bpaSearchCriteria) {
        Map<String, Object> preparedStatementValues = new HashMap<>();
        String query = bpaQueryBuilder.getSLAComplianceGeneralQuery(bpaSearchCriteria, preparedStatementValues);
        log.info("query for getSlaCompliancePermit : "+query);
        List<Integer> result = namedParameterJdbcTemplate.query(query, preparedStatementValues, new SingleColumnRowMapper<>(Integer.class));
        return result.get(0);
	}

	public Integer getSlaComplianceOtherThanLowRisk(BpaSearchCriteria bpaSearchCriteria) {
        Map<String, Object> preparedStatementValues = new HashMap<>();
        String query = bpaQueryBuilder.getSLAComplianceGeneralQuery(bpaSearchCriteria, preparedStatementValues);
        log.info("query for getSlaComplianceOtherThanLowRisk : "+query);
        List<Integer> result = namedParameterJdbcTemplate.query(query, preparedStatementValues, new SingleColumnRowMapper<>(Integer.class));
        return result.get(0);
	}

	public Integer getSlaCompliancePreApprovedPlan(BpaSearchCriteria bpaSearchCriteria) {
        Map<String, Object> preparedStatementValues = new HashMap<>();
        String query = bpaQueryBuilder.getSLAComplianceGeneralQuery(bpaSearchCriteria, preparedStatementValues);
        log.info("query for getSlaCompliancePreApprovedPlan : "+query);
        List<Integer> result = namedParameterJdbcTemplate.query(query, preparedStatementValues, new SingleColumnRowMapper<>(Integer.class));
        return result.get(0);
	}

	public Integer getSlaComplianceBuildingPermit(BpaSearchCriteria bpaSearchCriteria) {
        Map<String, Object> preparedStatementValues = new HashMap<>();
        String query = bpaQueryBuilder.getSLAComplianceGeneralQuery(bpaSearchCriteria, preparedStatementValues);
        log.info("query for getSlaComplianceBuildingPermit : "+query);
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

	public List<Chart> getTotalPermitsIssuedVsTotalOcIssuedVsTotalOcSubmitted(BpaSearchCriteria bpaSearchCriteria) {
		Map<String, Object> preparedStatementValues = new HashMap<>();
        String query = bpaQueryBuilder.getTotalPermitsIssuedVsTotalOcIssuedVsTotalOcSubmittedQuery(bpaSearchCriteria, preparedStatementValues);
        log.info("query for Total Permits Issued Vs Total Oc Issued Vs Total Oc Submitted Query : "+query);
        List<Chart> result = namedParameterJdbcTemplate.query(query, preparedStatementValues, new ChartRowMapper());
        return result;
	}

	public LinkedHashMap<String, Long> getMonthYearData(BpaSearchCriteria bpaSearchCriteria) {
		Map<String, Object> preparedStatementValues = new HashMap<>();
        String query = bpaQueryBuilder.getMonthYearDataQuery(bpaSearchCriteria, preparedStatementValues);
        log.info("query for Month Year Query : "+query);
        LinkedHashMap<String, Long> result = namedParameterJdbcTemplate.query(query, preparedStatementValues, new BPAPerformanceRateRowMapper());
        return result;
	}
	
	public HashMap<String, BigDecimal> getTenantWiseBpaTotalApplication(BpaSearchCriteria bpaSearchCriteria) {
        Map<String, Object> preparedStatementValues = new HashMap<>();
        String query = bpaQueryBuilder.getTenantWiseBpaApplicationQuery(bpaSearchCriteria, preparedStatementValues);
        log.info("query for BPA Tenant Wise Application List : "+query);
        HashMap<String, BigDecimal> result = namedParameterJdbcTemplate.query(query, preparedStatementValues, new TenantWiseCollectionRowMapper());
        return result;
    }
	
	public HashMap<String, BigDecimal> getTenantWiseBpaPermitIssued(BpaSearchCriteria bpaSearchCriteria) {
        Map<String, Object> preparedStatementValues = new HashMap<>();
        String query = bpaQueryBuilder.getTenantWisePermitIssuedQuery(bpaSearchCriteria, preparedStatementValues);
        log.info("query for BPA Tenant Wise Application List : "+query);
        HashMap<String, BigDecimal> result = namedParameterJdbcTemplate.query(query, preparedStatementValues, new TenantWiseCollectionRowMapper());
        return result;
    }
	
	public HashMap<String, BigDecimal> getTenantWiseBpaPendingApplication(BpaSearchCriteria bpaSearchCriteria) {
        Map<String, Object> preparedStatementValues = new HashMap<>();
        String query = bpaQueryBuilder.getTenantWiseBpaPendingApplication(bpaSearchCriteria, preparedStatementValues);
        log.info("query for BPA Tenant Wise Pending Application List : "+query);
        HashMap<String, BigDecimal> result = namedParameterJdbcTemplate.query(query, preparedStatementValues, new TenantWiseCollectionRowMapper());
        return result;
    }
    
    public HashMap<String, BigDecimal> getTenantWiseAvgDaysPermitIssued(BpaSearchCriteria bpaSearchCriteria) {
        Map<String, Object> preparedStatementValues = new HashMap<>();
        String query = bpaQueryBuilder.getTenantWiseAvgPermitIssue(bpaSearchCriteria, preparedStatementValues);
        log.info("query for BPA Tenant Wise Permit Issued Avg Days : "+query);
        HashMap<String, BigDecimal> result = namedParameterJdbcTemplate.query(query, preparedStatementValues, new TenantWiseCollectionRowMapper());
        return result;
    }
    
    public LinkedHashMap<String, BigDecimal> getMonthYearBigDecimalData(BpaSearchCriteria bpaSearchCriteria) {
		Map<String, Object> preparedStatementValues = new HashMap<>();
        String query = bpaQueryBuilder.getMonthYearDataQuery(bpaSearchCriteria, preparedStatementValues);
        log.info("query for Month Year Query : "+query);
        LinkedHashMap<String, BigDecimal> result = namedParameterJdbcTemplate.query(query, preparedStatementValues, new RateRowMapper());
        return result;
	}
    
    public HashMap<String, BigDecimal> getTotalApplicationByServiceType(BpaSearchCriteria bpaSearchCriteria) {
        Map<String, Object> preparedStatementValues = new HashMap<>();
        String query = bpaQueryBuilder.getTotalApplicationByServiceType(bpaSearchCriteria, preparedStatementValues);
        log.info("query for BPA Total Application by Service Type : "+query);
        HashMap<String, BigDecimal> result = namedParameterJdbcTemplate.query(query, preparedStatementValues, new TenantWiseCollectionRowMapper());
        return result;
    }
    
    public HashMap<String, BigDecimal> getApprovedApplicationByServiceType(BpaSearchCriteria bpaSearchCriteria) {
        Map<String, Object> preparedStatementValues = new HashMap<>();
        String query = bpaQueryBuilder.getApprovedApplicationByServiceType(bpaSearchCriteria, preparedStatementValues);
        log.info("query for BPA Total Approved Application by Service Type : "+query);
        HashMap<String, BigDecimal> result = namedParameterJdbcTemplate.query(query, preparedStatementValues, new TenantWiseCollectionRowMapper());
        return result;
    }
    
   
}
