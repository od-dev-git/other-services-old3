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
import org.egov.tracer.model.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.stereotype.Repository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Repository
public class IssueFixRepository {
	
	
	private final JdbcTemplate jdbcTemplate;
	
    private final JdbcTemplate jdbcTemplateEmas;
	
	@Autowired
    public IssueFixRepository(@Qualifier("jdbcTemplateEmas") JdbcTemplate jdbcTemplateEmas,
    		@Qualifier("jdbcTemplateDigit") JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.jdbcTemplateEmas = jdbcTemplateEmas;
    }

	
	@Autowired
	private IssueFixQueryBuilder queryBuilder;
	
	public List<Transaction> getTransactions(IssueFix issueFix){
		
		List<Object> preparedPropStmtList = new ArrayList<>();
		String query = queryBuilder.getTransactionsQuery(issueFix, preparedPropStmtList);
		return jdbcTemplate.query(query,preparedPropStmtList.toArray(), new TransactionsRowMapper());
		
	}

	public String getBillStatus(String billId, String tenantId) {
		
		List<Object> preparedPropStmtList = new ArrayList<>();
		String query = queryBuilder.getBillStatusQuery(billId, preparedPropStmtList, tenantId);
		log.info("query to get bills: "+query);
		return jdbcTemplate.queryForObject(query,preparedPropStmtList.toArray(), String.class);
		
	}

	public void updateBillIdInTransaction(Transaction transaction, IssueFixRequest issueFixRequest) {
		
		String consumerCode = transaction.getConsumerCode();
		
		String txnId = transaction.getTxnId();
		
		String tenantId = transaction.getTenantId();
		
		String updateBillIdInTransactionQuery = queryBuilder.getUpdateBillIdInTransactionQuery();
		log.info("query for bill update : "+updateBillIdInTransactionQuery);

		jdbcTemplate.update(updateBillIdInTransactionQuery, new PreparedStatementSetter() {

			@Override
			public void setValues(PreparedStatement ps) {
				try {
					ps.setString(1, consumerCode);
					ps.setString(2, tenantId);
					ps.setString(3, tenantId);
					ps.setString(4, txnId);
					ps.setString(5, txnId);
					ps.setString(6, consumerCode);
					ps.setString(7, tenantId);
					ps.setString(8, tenantId);
					ps.setString(9, txnId);
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		
	}

	public int getUserIdFromHRMS(String tenantId, String empId) {
		
		List<Object> preparedPropStmtList = new ArrayList<>();
		
		String query = queryBuilder.getHRMSSearchQuery(tenantId, empId, preparedPropStmtList);
		
		log.info("query to get userID from Employee ID: "+query);
		log.info("prepared statement to get userID from Employee ID "+ String.valueOf(preparedPropStmtList));
		
		return jdbcTemplate.queryForObject(query,preparedPropStmtList.toArray(), Integer.class);
	}

	public String getNameFromEMAS(int userId) {
		
		List<Object> preparedPropStmtList = new ArrayList<>();
		
		String name = "";
		
		String query = queryBuilder.getNameFromEMASDBQuery(userId, preparedPropStmtList);
		
		log.info("query to get Name from User ID from EMAS: "+query);
		log.info("prepared statement to get Name from User ID from EMAS "+ String.valueOf(preparedPropStmtList));
		
		try {
			name =  jdbcTemplateEmas.queryForObject(query,preparedPropStmtList.toArray(), String.class);
		} catch(Exception e) {
			log.info("No Records found in EMAS Table for mentioned data...");
			throw new CustomException("ERROR", "DSC Token Not Registered for mentioned data !");
			
		}
		return name;
	}

	public Boolean deleteDSCTokenEMAS(int userId) {
		
		List<Object> preparedPropStmtList = new ArrayList<>();
		
		String deletionQuery = queryBuilder.getEMASDeletionQuery(userId, preparedPropStmtList);
		
		log.info("query to delete the token from EMAS: "+deletionQuery);
		log.info("prepared statement for dsc deletion "+ String.valueOf(preparedPropStmtList));
		
		return jdbcTemplateEmas.queryForObject(deletionQuery,preparedPropStmtList.toArray(), Boolean.class);		
	}
	
	public void expireBill(String consumerCode) {

        String billExpireQuery = queryBuilder.getBillExpireQuery();

        jdbcTemplate.update(billExpireQuery, new PreparedStatementSetter() {

            @Override
            public void setValues(PreparedStatement ps) {
                try {
                    ps.setString(1, consumerCode);
                } catch (SQLException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        });

	}

	
}
