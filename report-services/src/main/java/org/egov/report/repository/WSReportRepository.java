package org.egov.report.repository;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.auth.UsernamePasswordCredentials;
import org.egov.common.contract.request.RequestInfo;
import org.egov.report.repository.builder.ReportQueryBuilder;
import org.egov.report.repository.rowmapper.ConsumerMasterRowMapper;
import org.egov.report.service.UserService;
import org.egov.report.web.model.ConsumerMasterWSReportResponse;
import org.egov.report.web.model.OwnerInfo;
import org.egov.report.web.model.UserDetailResponse;
import org.egov.report.web.model.WSReportSearchCriteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

@Repository
public class WSReportRepository {
	
		@Autowired
		ReportQueryBuilder queryBuilder;
		
		@Autowired
		JdbcTemplate jdbcTemplate;
		
		@Autowired
		UserService userService;
	
	
		public List<ConsumerMasterWSReportResponse> getComsumerMasterWSReport(RequestInfo requestInfo, WSReportSearchCriteria criteria){
		
			List<Object> preparedStatement = new ArrayList<>();
		
			String query = queryBuilder.getQueryForConsumerMasterWSReport(preparedStatement,criteria); 
		
			return jdbcTemplate.query(query, preparedStatement.toArray(), new ConsumerMasterRowMapper());
		
		}

}
