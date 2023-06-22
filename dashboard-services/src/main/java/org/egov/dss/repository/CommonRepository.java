package org.egov.dss.repository;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.egov.dss.config.ConfigurationLoader;
import org.egov.dss.constants.DashboardConstants;
import org.egov.dss.model.Chart;
import org.egov.dss.model.DemandPayload;
import org.egov.dss.model.PayloadDetails;
import org.egov.dss.model.PropertySerarchCriteria;
import org.egov.dss.repository.builder.CommonQueryBuilder;
import org.egov.dss.repository.rowmapper.ChartRowMapper;
import org.egov.dss.repository.rowmapper.PayloadDetailsRowMapper;
import org.egov.dss.repository.rowmapper.TableChartRowMapper;
import org.egov.dss.repository.rowmapper.TenantWiseCollectionRowMapper;
import org.egov.dss.util.DashboardUtils;
import org.egov.dss.web.model.ChartCriteria;
import org.egov.dss.web.model.ResponseData;
import org.egov.tracer.model.ServiceCallException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Repository
public class CommonRepository {
	 
	@Autowired
	private CommonQueryBuilder commonQueryBuilder;

	@Autowired
	private JdbcTemplate jdbcTemplate;
	
    @Autowired
	private DashboardUtils utils;
	
	@Autowired
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	
	@Autowired
	private ObjectMapper mapper;
	
	@Autowired
	private RestTemplate restTemplate;
	
	public List<PayloadDetails> fetchSchedulerPayloads(ChartCriteria criteria) {
		Map<String, Object> preparedStatementValues = new HashMap<>();
        String query  = commonQueryBuilder.fetchSchedulerPayloads(criteria , preparedStatementValues);
        log.info("query: "+query);
        log.info("startDate: "+criteria.getStartDate()+" endDate: "+criteria.getEndDate());
        return namedParameterJdbcTemplate.query(query, preparedStatementValues, new PayloadDetailsRowMapper());
       
    }

	public void update(PayloadDetails payloadDetails) {
		String finalQuery = commonQueryBuilder.RESPONSE_DATA_UPDATE_QUERY.replace("{tableName}",
				payloadDetails.getTableName());
		jdbcTemplate.update(finalQuery, new PreparedStatementSetter() {

			@Override
			public void setValues(PreparedStatement ps) throws SQLException {
				ps.setObject(1, utils.getPGObject(payloadDetails.getResponsedata()));
				ps.setLong(2, payloadDetails.getLastModifiedTime());
				ps.setLong(3, payloadDetails.getStartdate());
				ps.setLong(4, payloadDetails.getEnddate());
				ps.setString(5, payloadDetails.getId());

			}

		});
	}

	public List<Chart> getTotalProperties(PropertySerarchCriteria criteriaProperty) {
		
		Map<String, Object> preparedStatementValues = new HashMap<>();
        String query  = commonQueryBuilder.getTotalPropertiesQuery(criteriaProperty , preparedStatementValues);
        log.info("query for PT in common: "+query);
        return namedParameterJdbcTemplate.query(query, preparedStatementValues, new ChartRowMapper());
	}
	
	
	public void insertPayloadData(PayloadDetails payloadDetails,String tenantId, String tableName) {
		String finalQuery = commonQueryBuilder.PAYLOAD_DATA_INSERT_QUERY.replace("{tableName}",
				tableName);
		
		
		jdbcTemplate.update(finalQuery, new PreparedStatementSetter() {
         
			@Override
			public void setValues(PreparedStatement ps) throws SQLException {
				ps.setString(1, UUID.randomUUID().toString());
				ps.setString(2, payloadDetails.getVisualizationcode());
				ps.setString(3, payloadDetails.getModulelevel());
				ps.setLong(4, payloadDetails.getStartdate());
				ps.setLong(5, payloadDetails.getEnddate());
				ps.setString(6, payloadDetails.getTimeinterval());
				ps.setString(7, payloadDetails.getCharttype());
				if(tenantId.equalsIgnoreCase(DashboardConstants.STATE_TENANT)) {
					ps.setString(8, null);
				}else {
					ps.setString(8, tenantId);
				}
				ps.setString(9, payloadDetails.getHeadername());
				ps.setString(10, payloadDetails.getValuetype());

			}

		});
	}
	
	public List<HashMap<String, Object>> fetchDemandData(DemandPayload criteria) {
		Map<String, Object> preparedStatementValues = new HashMap<>();
		String query = commonQueryBuilder.fetchDemandData(criteria, preparedStatementValues);
		log.info("query: " + query);
		log.info("Module : " + criteria.getBusinessService() + " taxPeriodFrom: " + criteria.getTaxPeriodFrom()
				+ " taxPeriodTo: " + criteria.getTaxPeriodTo());
		return namedParameterJdbcTemplate.query(query, preparedStatementValues, new TableChartRowMapper());

	}
	
	
	public void updateDemand(DemandPayload demandPayload) {
		String finalQuery = commonQueryBuilder.DEMAND_UPDATE_QUERY;
		log.info(finalQuery);
		log.info(demandPayload.getTenantId()+","+demandPayload.getTaxAmount()+" , "+demandPayload.getBusinessService()+" , "+demandPayload.getFinancialYear());
		jdbcTemplate.update(finalQuery, new PreparedStatementSetter() {

			@Override
			public void setValues(PreparedStatement ps) throws SQLException {
				ps.setBigDecimal(1, demandPayload.getTaxAmount());
				ps.setBigDecimal(2, demandPayload.getCollectionAmount());
				ps.setLong(3, demandPayload.getLastModifiedTime());
				ps.setString(4, demandPayload.getTenantId());
				ps.setString(5, demandPayload.getBusinessService());
				ps.setString(6, demandPayload.getFinancialYear());
				
			}

		});
	}
	
	public Object fetchResult(StringBuilder uri, Object request) {
		Object response = null;
		log.info("URI: " + uri.toString());
		try {
			log.info("Request: " + mapper.writeValueAsString(request));
			response = restTemplate.postForObject(uri.toString(), request, String.class);
		} catch (HttpClientErrorException e) {
			log.error("External Service threw an Exception: ", e);
			throw new ServiceCallException(e.getResponseBodyAsString());
		} catch (Exception e) {
			log.error("Exception while fetching from searcher: ", e);
		}

		return response;
	}
	
	
}
