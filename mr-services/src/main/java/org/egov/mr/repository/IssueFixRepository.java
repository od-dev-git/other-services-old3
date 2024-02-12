package org.egov.mr.repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.egov.mr.repository.builder.IssueFixQueryBuilder;
import org.egov.mr.web.models.issuefix.IssueFix;
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
	
	public List<String> getDSC(IssueFix issueFix) {

		List<Object> preparedStmtList = new ArrayList<>();

        String query = issueFixQueryBuilder.getDSC(issueFix,preparedStmtList);
        System.out.println("Query: "+query);
        System.out.println("preparedStmtList: "+preparedStmtList);
        List<String> data = jdbcTemplate.queryForList(query,preparedStmtList.toArray(), String.class);
        System.out.println("Data: "+data);
        return data;
	}

	public void updateDSC(IssueFix issueFix) {

		String query = issueFixQueryBuilder.searchDscQuery();
		System.out.println("Query: "+query);
		jdbcTemplate.update(query, preparedStatement -> {
			try {
					preparedStatement.setString(1, issueFix.getApplicationNo());
					preparedStatement.setString(2, issueFix.getTenantId());
					preparedStatement.setString(3, issueFix.getApplicationNo());
			} catch (SQLException e) {
				e.printStackTrace();
			}
		});
	}

}
