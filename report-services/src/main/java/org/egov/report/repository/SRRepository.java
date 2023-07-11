package org.egov.report.repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.egov.common.contract.request.RequestInfo;
import org.egov.report.repository.builder.SRQueryBuilder;
import org.egov.report.repository.rowmapper.SRTicketDetailsRowMapper;
import org.egov.report.web.model.SRReportSearchCriteria;
import org.egov.report.web.model.TicketDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import lombok.extern.slf4j.Slf4j;

@Repository
@Slf4j
public class SRRepository {
	
	@Autowired
	private SRQueryBuilder queryBuilder;
	
	@Autowired
	NamedParameterJdbcTemplate jdbcTemplate;
	
	public List<TicketDetails> getTicketDetails(RequestInfo requestInfo, SRReportSearchCriteria criteria) {
		Map<String,Object> preparedStatement = new HashMap();

		String query = queryBuilder.getTicketDetailsQuery(preparedStatement, criteria);

		log.info(" Query " + query);
		log.info(" Prepared Statement : " + preparedStatement.toString());
		log.info(" Search Criteria : " + criteria.toString());

		return jdbcTemplate.query(query, preparedStatement, new SRTicketDetailsRowMapper());
	}

}
