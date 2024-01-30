package org.egov.mr.repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.egov.mr.repository.builder.IssueFixQueryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.stereotype.Repository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Repository
public class IssueFixRepository {
	
	@Autowired
	private IssueFixQueryBuilder issueFixQueryBuilder;
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	public void deleteCertificate(String applicationNumber) {

		String certificateDeletionQuery = issueFixQueryBuilder.getCertificateDeletionQuery();

		jdbcTemplate.update(certificateDeletionQuery, new PreparedStatementSetter() {

			@Override
			public void setValues(PreparedStatement ps) {
				try {
					ps.setString(1, applicationNumber);
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
	}

}
