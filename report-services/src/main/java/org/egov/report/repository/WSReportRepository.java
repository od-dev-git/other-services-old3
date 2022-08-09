package org.egov.report.repository;

import java.util.ArrayList;
import java.util.List;

import org.egov.common.contract.request.RequestInfo;
import org.egov.report.repository.builder.ReportQueryBuilder;
import org.egov.report.repository.rowmapper.BillSummaryRowMapper;
import org.egov.report.repository.rowmapper.ConsumerMasterRowMapper;
import org.egov.report.service.UserService;
import org.egov.report.web.model.BillSummaryQueryResponse;
import org.egov.report.web.model.ConsumerMasterWSReportResponse;
import org.egov.report.web.model.WSReportSearchCriteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

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


	public List<BillSummaryQueryResponse> getBillSummaryDetails(WSReportSearchCriteria criteria)
	{
		List<Object> preparedStmtList = new ArrayList<>();
		
		String query = queryBuilder.getBillSummaryDetailsQuery(criteria, preparedStmtList);

		return jdbcTemplate.query(query,preparedStmtList.toArray(), new BillSummaryRowMapper());
		
	}
}
