package org.egov.dss.repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.egov.dss.config.ConfigurationLoader;
import org.egov.dss.model.Chart;
import org.egov.dss.model.WaterSearchCriteria;
import org.egov.dss.repository.builder.WaterServiceQueryBuilder;
import org.egov.dss.repository.rowmapper.ChartRowMapper;
import org.egov.dss.repository.rowmapper.TableChartRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.SingleColumnRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Repository
public class WSRepository {
	
	@Autowired
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	
	@Autowired
	private WaterServiceQueryBuilder wsQueryBuilder;
	
	@Autowired
	private ConfigurationLoader config;

	public Object getActiveWaterConnectionCount(WaterSearchCriteria waterSearchCriteria) {
        Map<String, Object> preparedStatementValues = new HashMap<>();
        String query = wsQueryBuilder.getActiveConnectionCount(waterSearchCriteria, preparedStatementValues);
        log.info("query FOR get Active Water Connection Count : "+query);
        List<Integer> result = namedParameterJdbcTemplate.query(query, preparedStatementValues, new SingleColumnRowMapper<>(Integer.class));
        return result.get(0);
    }
	
	public Object getSlaAchievedAppCount(WaterSearchCriteria waterSearchCriteria) {
        Map<String, Object> preparedStatementValues = new HashMap<>();
        waterSearchCriteria.setSlaThreshold(config.getSlaWsThreshold());
        String query = wsQueryBuilder.getWsTotalApplicationsCount(waterSearchCriteria, preparedStatementValues);
        log.info("query: "+query);
        List<Integer> result = namedParameterJdbcTemplate.query(query, preparedStatementValues, new SingleColumnRowMapper<>(Integer.class));
        return result.get(0);
    }
	
	public Integer getTotalActiveMeteredWaterConnectionsCount(WaterSearchCriteria waterSearchCriteria) {
        Map<String, Object> preparedStatementValues = new HashMap<>();
        String query = wsQueryBuilder.getTotalActiveXConnectionTypeWaterConnectionsCount(waterSearchCriteria, preparedStatementValues);
        log.info("query FOR get Total Active Metered Water Connection Count : "+query);
        List<Integer> result = namedParameterJdbcTemplate.query(query, preparedStatementValues, new SingleColumnRowMapper<>(Integer.class));
        return result.get(0);
	}

	public Integer getTotalActiveNonMeteredWaterConnectionsCount(WaterSearchCriteria waterSearchCriteria) {
        Map<String, Object> preparedStatementValues = new HashMap<>();
        String query = wsQueryBuilder.getTotalActiveXConnectionTypeWaterConnectionsCount(waterSearchCriteria, preparedStatementValues);
        log.info("query FOR get Total Active Non Metered Water Connection Count : "+query);
        List<Integer> result = namedParameterJdbcTemplate.query(query, preparedStatementValues, new SingleColumnRowMapper<>(Integer.class));
        return result.get(0);
	}

	public Integer getTotalActiveWaterConnectionsCount(WaterSearchCriteria waterSearchCriteria) {
        Map<String, Object> preparedStatementValues = new HashMap<>();
        String query = wsQueryBuilder.getTotalActiveXConnectionFacilityTypeConnectionsCount(waterSearchCriteria, preparedStatementValues);
        log.info("query FOR get Total Active  Water Connectionfacility Type Connection Count : "+query);
        List<Integer> result = namedParameterJdbcTemplate.query(query, preparedStatementValues, new SingleColumnRowMapper<>(Integer.class));
        return result.get(0);
	}

	public Integer getTotalActiveSewerageConnectionsCount(WaterSearchCriteria waterSearchCriteria) {
        Map<String, Object> preparedStatementValues = new HashMap<>();
        String query = wsQueryBuilder.getTotalActiveXConnectionFacilityTypeConnectionsCount(waterSearchCriteria, preparedStatementValues);
        log.info("query FOR get Total Active  Sewerage Connectionfacility Type Connection Count : "+query);
        List<Integer> result = namedParameterJdbcTemplate.query(query, preparedStatementValues, new SingleColumnRowMapper<>(Integer.class));
        return result.get(0);
	}

	public Integer getTotalActiveWaterSewerageConnectionsCount(WaterSearchCriteria waterSearchCriteria) {
        Map<String, Object> preparedStatementValues = new HashMap<>();
        String query = wsQueryBuilder.getTotalActiveXConnectionFacilityTypeConnectionsCount(waterSearchCriteria, preparedStatementValues);
        log.info("query FOR get Total Active  Sewerage Connectionfacility Type Connection Count : "+query);
        List<Integer> result = namedParameterJdbcTemplate.query(query, preparedStatementValues, new SingleColumnRowMapper<>(Integer.class));
        return result.get(0);
	}

	public List<Chart> getCumulativeConnections(WaterSearchCriteria waterSearchCriteria) {
		Map<String, Object> preparedStatementValues = new HashMap<>();
        String query = wsQueryBuilder.getCumulativeConnectionsQuery(waterSearchCriteria, preparedStatementValues);
        log.info("query for Cumulative Connections Count : "+query);
        List<Chart> result = namedParameterJdbcTemplate.query(query, preparedStatementValues, new ChartRowMapper());
        return result;
	}

	public List<Chart> getwsConnectionsByUsageType(WaterSearchCriteria waterSearchCriteria) {
		Map<String, Object> preparedStatementValues = new HashMap<>();
        String query = wsQueryBuilder.getwsConnectionsByUsageTypeQuery(waterSearchCriteria, preparedStatementValues);
        log.info("query get ws Connections By Usage Type : "+query);
        List<Chart> result = namedParameterJdbcTemplate.query(query, preparedStatementValues, new ChartRowMapper());
        return result;
	}

	public List<Chart> getwsConnectionsByType(WaterSearchCriteria waterSearchCriteria) {
		Map<String, Object> preparedStatementValues = new HashMap<>();
        String query = wsQueryBuilder.getwsConnectionsByTypeQuery(waterSearchCriteria, preparedStatementValues);
        log.info("query get ws Connections By Type : "+query);
        List<Chart> result = namedParameterJdbcTemplate.query(query, preparedStatementValues, new ChartRowMapper());
        return result;
	}

	public List<HashMap<String, Object>> getWSConnectionAgeing(WaterSearchCriteria waterSearchCriteria) {
		Map<String, Object> preparedStatementValues = new HashMap<>();
        String query = wsQueryBuilder.getWSConnectionAgeingQuery(waterSearchCriteria, preparedStatementValues);
        log.info("query for WS Connection Ageing  : "+query);
        List<HashMap<String, Object>> result = namedParameterJdbcTemplate.query(query, preparedStatementValues, new TableChartRowMapper());
        return result;
	}

	public Integer getWsTotalApplicationsCount(WaterSearchCriteria waterSearchCriteria) {
        Map<String, Object> preparedStatementValues = new HashMap<>();
        String query = wsQueryBuilder.getWsTotalApplicationsCount(waterSearchCriteria, preparedStatementValues);
        log.info("query FOR WS total  Connection Count : "+query);
        List<Integer> result = namedParameterJdbcTemplate.query(query, preparedStatementValues, new SingleColumnRowMapper<>(Integer.class));
        return result.get(0);
	}

}
