package org.egov.report.repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.egov.report.web.model.PropertyDemandResponse;
import org.egov.report.web.model.PropertyDetailsResponse;

import org.egov.report.repository.builder.ReportQueryBuilder;
import org.egov.report.repository.rowmapper.BillSummaryRowMapper;
import org.egov.report.repository.rowmapper.DemandsRowMapper;
import org.egov.report.repository.rowmapper.PropertyDetailsRowMapper;
import org.egov.report.repository.rowmapper.UserRowMapper;
import org.egov.report.web.model.PropertyDetailsSearchCriteria;
import org.egov.report.web.model.User;
import org.egov.report.web.model.WSReportSearchCriteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class PropertyDetailsReportRepository {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Autowired
	private ReportQueryBuilder queryBuilder;
	
	public List<PropertyDetailsResponse> getPropertyDetails(PropertyDetailsSearchCriteria criteria)
	{
		List<Object> preparedPropStmtList = new ArrayList<>();
		
//		criteria.setLimit(limit);
//		criteria.setOffset(offset);

		String query = queryBuilder.getPropertyDetailsQuery(criteria, preparedPropStmtList);

		return jdbcTemplate.query(query,preparedPropStmtList.toArray(), new PropertyDetailsRowMapper());

	}

	public HashMap<String,List<PropertyDemandResponse>> getPropertyDemandDetails(PropertyDetailsSearchCriteria searchCriteria) {

		List<Object> preparedPropStmtList = new ArrayList<>();
		
//		searchCriteria.setLimit(limit);
//		searchCriteria.setOffset(offset);

		String query = queryBuilder.getPropertyWiseDemandQuery(searchCriteria, preparedPropStmtList);

		return jdbcTemplate.query(query,preparedPropStmtList.toArray(), new DemandsRowMapper());
	}

	public HashMap<String, List<PropertyDemandResponse>> getPropertyWiseDemandDetails(
			PropertyDetailsSearchCriteria searchCriteria ) {
		
		List<Object> preparedPropStmtList = new ArrayList<>();
		
//		searchCriteria.setLimit(limit);
//      searchCriteria.setOffset(offset);

		String query = queryBuilder.getPropertyWiseDemandQuery(searchCriteria, preparedPropStmtList);

		return jdbcTemplate.query(query,preparedPropStmtList.toArray(), new DemandsRowMapper());
		
	}

	public List<PropertyDetailsResponse> getPropertiesDetail(PropertyDetailsSearchCriteria searchCriteria) {

		List<Object> preparedPropStmtList = new ArrayList<>();

//		searchCriteria.setLimit(Limit);
//		searchCriteria.setOffset(Offset);
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

}