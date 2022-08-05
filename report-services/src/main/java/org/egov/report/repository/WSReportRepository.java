package org.egov.report.repository;

import java.util.ArrayList;
import java.util.List;

import org.egov.report.repository.builder.ReportQueryBuilder;
import org.egov.report.repository.rowmapper.BillSummaryRowMapper;
import org.egov.report.web.model.BillSummaryQueryResponse;
import org.egov.report.web.model.WSReportSearchCriteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class WSReportRepository {
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Autowired
	private ReportQueryBuilder queryBuilder;

	public List<BillSummaryQueryResponse> getBillSummaryDetails(WSReportSearchCriteria criteria)
	{
		List<Object> preparedStmtList = new ArrayList<>();
		
		String query = queryBuilder.getBillSummaryDetailsQuery(criteria, preparedStmtList);

		return jdbcTemplate.query(query,preparedStmtList.toArray(), new BillSummaryRowMapper());
		
	}
}
