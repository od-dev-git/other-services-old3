package org.egov.report.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.egov.report.repository.builder.ReportQueryBuilder;
import org.egov.report.repository.rowmapper.TradeLicenseEscallationDetailsRowMapper;
import org.egov.report.web.model.TradeLicenseEscallationDetailsResponse;
import org.egov.report.web.model.TradeLicenseSearchCriteria;
import org.egov.tracer.model.ServiceCallException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Repository
public class TradeLicenseReportRepository {
	
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private ReportQueryBuilder queryBuilder;
    
    private ObjectMapper mapper;
    private RestTemplate restTemplate;

    @Autowired
    public TradeLicenseReportRepository(ObjectMapper mapper, RestTemplate restTemplate) {
        this.mapper = mapper;
        this.restTemplate = restTemplate;
    }
	
    public List<TradeLicenseEscallationDetailsResponse> getTLAutoEscallationReport(
            TradeLicenseSearchCriteria searchCriteria) {
        log.info("Executing getTLAutoEscallationReport with search criteria: {}", searchCriteria);
        List<Object> preparedPropStmtList = new ArrayList<>();
        String query = queryBuilder.getTLAutoEscallationReport(searchCriteria, preparedPropStmtList);
        log.info("Generated SQL query: {}", query);
        return jdbcTemplate.query(query, preparedPropStmtList.toArray(), new TradeLicenseEscallationDetailsRowMapper());
    }

    public Object fetchResult(StringBuilder uri, Object request) {
        log.info("Initiating fetchResult for URI: {}", uri);
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        Object response = null;
        try {
            log.info("Sending request to external service: {}", mapper.writeValueAsString(request));
            response = restTemplate.postForObject(uri.toString(), request, Map.class);
            log.info("Received response from external service");
        } catch (HttpClientErrorException e) {
            log.error("External Service threw an HttpClientErrorException: ", e);
            throw new ServiceCallException(e.getResponseBodyAsString());
        } catch (Exception e) {
            log.error("Exception while fetching from external service: ", e);
        }

        return response;
    }
}
