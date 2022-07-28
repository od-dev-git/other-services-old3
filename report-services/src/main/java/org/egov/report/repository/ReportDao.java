package org.egov.report.repository;

import java.util.ArrayList;
import java.util.List;

import org.egov.report.repository.builder.ReportQueryBuilder;
import org.egov.report.repository.rowmapper.UserRowMapper;
import org.egov.report.web.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class ReportDao {
	
	@Autowired
	private ReportQueryBuilder queryBuilder;
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Autowired
	private UserRowMapper userRowMapper;

	public List<User> getEmployeeBaseTenant(List<Long> userIds) {
		List<Object> prepareStatement = new ArrayList<>();
		String query = queryBuilder.getEmployeeBaseTenantQuery(userIds, prepareStatement);
		List<User> users = jdbcTemplate.query(query, prepareStatement.toArray(), userRowMapper);
		return users;
	}

}
