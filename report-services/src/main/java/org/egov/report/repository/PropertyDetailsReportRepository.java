package org.egov.report.repository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.validation.Valid;

import org.egov.report.web.model.DemandCriteria;
import org.egov.report.web.model.PTAssessmentSearchCriteria;
import org.egov.report.web.model.PropertyDemandResponse;
import org.egov.report.web.model.PropertyDetailsResponse;
import org.apache.commons.lang.StringUtils;
import org.egov.report.repository.builder.ReportQueryBuilder;
import org.egov.report.repository.rowmapper.BillSummaryRowMapper;
import org.egov.report.repository.rowmapper.DemandsRowMapper;
import org.egov.report.repository.rowmapper.OldPropertyIdRowMapper;
import org.egov.report.repository.rowmapper.PropertyDetailsRowMapper;
import org.egov.report.repository.rowmapper.UserRowMapper;
import org.egov.report.web.model.PropertyDetailsSearchCriteria;
import org.egov.report.web.model.User;
import org.egov.report.web.model.WSReportSearchCriteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.expression.MapAccessor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Repository
public class PropertyDetailsReportRepository {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Autowired
	private ReportQueryBuilder queryBuilder;
	
	public List<PropertyDetailsResponse> getPropertyDetails(PropertyDetailsSearchCriteria criteria)
	{
		List<Object> preparedPropStmtList = new ArrayList<>();
		String query = queryBuilder.getPropertyDetailsQuery(criteria, preparedPropStmtList);
		return jdbcTemplate.query(query,preparedPropStmtList.toArray(), new PropertyDetailsRowMapper());

	}

	public HashMap<String,List<PropertyDemandResponse>> getPropertyDemandDetails(PropertyDetailsSearchCriteria searchCriteria) {

		List<Object> preparedPropStmtList = new ArrayList<>();
		String query = queryBuilder.getPropertyWiseDemandQuery(searchCriteria, preparedPropStmtList);
		return jdbcTemplate.query(query,preparedPropStmtList.toArray(), new DemandsRowMapper());
	}

	public HashMap<String, List<PropertyDemandResponse>> getPropertyWiseDemandDetails(
			PropertyDetailsSearchCriteria searchCriteria ) {
		
		List<Object> preparedPropStmtList = new ArrayList<>();
		String query = queryBuilder.getPropertyWiseDemandQuery(searchCriteria, preparedPropStmtList);
		return jdbcTemplate.query(query,preparedPropStmtList.toArray(), new DemandsRowMapper());
		
	}

	public List<PropertyDetailsResponse> getPropertiesDetail(PropertyDetailsSearchCriteria searchCriteria) {

		List<Object> preparedPropStmtList = new ArrayList<>();
		String query = queryBuilder.getPropertiesDetail(searchCriteria, preparedPropStmtList);
		return jdbcTemplate.query(query,preparedPropStmtList.toArray(), new PropertyDetailsRowMapper());
	}

    public Long getPropertyDetailsCount(PropertyDetailsSearchCriteria searchCriteria) {
        List<Object> preparedPropStmtList = new ArrayList<>();
        String query = queryBuilder.getPropertyDetailsQueryCount(searchCriteria, preparedPropStmtList);
        return jdbcTemplate.queryForObject(query, preparedPropStmtList.toArray(), Long.class);
    }

    public Long getPropertyDemandDetailsCount(PropertyDetailsSearchCriteria searchCriteria) {
        List<Object> preparedPropStmtList = new ArrayList<>();

        String query = queryBuilder.getPropertyWiseDemandCountQuery(searchCriteria, preparedPropStmtList);

        return jdbcTemplate.queryForObject(query, preparedPropStmtList.toArray(), Long.class);
    }

    public Long getPropertiesDetailCount(PropertyDetailsSearchCriteria searchCriteria) {
        List<Object> preparedPropStmtList = new ArrayList<>();

        String query = queryBuilder.getPropertiesDetailCount(searchCriteria, preparedPropStmtList);

        return jdbcTemplate.queryForObject(query,preparedPropStmtList.toArray(), Long.class);
    }
    

    public Long getPropertyCount(PropertyDetailsSearchCriteria searchCriteria) {
        List<Object> preparedPropStmtList = new ArrayList<>();

        String query = queryBuilder.getPropertiesCount(searchCriteria, preparedPropStmtList);

        return jdbcTemplate.queryForObject(query,preparedPropStmtList.toArray(), Long.class);
    }

    public List<PropertyDetailsResponse> getPropertyDetail(PropertyDetailsSearchCriteria searchCriteria) {
        List<Object> preparedPropStmtList = new ArrayList<>();

        String query = queryBuilder.getPropertyDetailQuery(searchCriteria, preparedPropStmtList);

        return jdbcTemplate.query(query, preparedPropStmtList.toArray(), new PropertyDetailsRowMapper());

    }

    public List<String> getPropertyIds(PropertyDetailsSearchCriteria searchCriteria) {
        List<Object> preparedPropStmtList = new ArrayList<>();

        String query = queryBuilder.getPropertyIds(searchCriteria, preparedPropStmtList);

        return jdbcTemplate.queryForList(query, preparedPropStmtList.toArray(), String.class);
    }

    public HashMap<String, PropertyDetailsResponse> getOldPropertyIds(PropertyDetailsSearchCriteria searchCriteria) {
        List<Object> preparedPropStmtList = new ArrayList<>();
        String query = queryBuilder.getOldPropertyIdsQuery(searchCriteria, preparedPropStmtList);
        return jdbcTemplate.query(query,preparedPropStmtList.toArray(),new OldPropertyIdRowMapper());
    }

	public List<Map<String, Object>> createPTAssessmentReport(
			@Valid PTAssessmentSearchCriteria ptAssessmentSearchCriteria, String tenantid) {
		log.info("Search Criteria : " + ptAssessmentSearchCriteria.toString());
		
        String query = queryBuilder.getCreatePTAssessmentReportQuery(ptAssessmentSearchCriteria, tenantid);
	    
	    List<Map<String, Object>> paymentsDetailsList =  jdbcTemplate.queryForList(query.toString());
	    
	    if(paymentsDetailsList.isEmpty())
			return Collections.emptyList();
		return paymentsDetailsList;
	}

	public List<Map<String, Object>> createptDDNReport(@Valid PTAssessmentSearchCriteria searchCriteria,
			String tenantId) {
		log.info("Search Criteria : " + searchCriteria.toString());
		
        String query = queryBuilder.createptDDNReport(searchCriteria, tenantId);
	    
	    List<Map<String, Object>> ptDDNList =  jdbcTemplate.queryForList(query.toString());
	    
	    if(ptDDNList.isEmpty())
			return Collections.emptyList();
		return ptDDNList;
	}


}