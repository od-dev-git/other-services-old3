package org.egov.report.repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.egov.report.repository.builder.IssueFixQueryBuilder;
import org.egov.report.repository.rowmapper.TransactionsRowMapper;
import org.egov.report.web.model.Transaction;
import org.egov.report.web.model.issuefix.IssueFix;
import org.egov.report.web.model.issuefix.IssueFixRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.stereotype.Repository;

@Repository
public class IssueFixRepository {
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Autowired
	private IssueFixQueryBuilder queryBuilder;
	
	public List<Transaction> getTransactions(IssueFix issueFix){
		
		List<Object> preparedPropStmtList = new ArrayList<>();
		String query = queryBuilder.getTransactionsQuery(issueFix, preparedPropStmtList);
		return jdbcTemplate.query(query,preparedPropStmtList.toArray(), new TransactionsRowMapper());
		
	}

	public String getBillStatus(String billId) {
		
		List<Object> preparedPropStmtList = new ArrayList<>();
		String query = queryBuilder.getBillStatusQuery(billId, preparedPropStmtList);
		return jdbcTemplate.queryForObject(query,preparedPropStmtList.toArray(), String.class);
		
	}

	public void updateBillIdInTransaction(Transaction transaction, IssueFixRequest issueFixRequest) {
		
		String consumerCode = transaction.getConsumerCode();
		
		String txnId = transaction.getTxnId();
		
		String updateBillIdInTransactionQuery = queryBuilder.getUpdateBillIdInTransactionQuery();

		jdbcTemplate.update(updateBillIdInTransactionQuery, new PreparedStatementSetter() {

			@Override
			public void setValues(PreparedStatement ps) {
				try {
					ps.setString(1, consumerCode);
					ps.setString(2, txnId);
					ps.setString(3, txnId);
					ps.setString(4, consumerCode);
					ps.setString(5, txnId);
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		
	}

}
