package org.egov.report.repository.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.egov.report.web.model.Transaction;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

public class TransactionsRowMapper implements ResultSetExtractor<List<Transaction>> {

	@Override
	public List<Transaction> extractData(ResultSet rs) throws SQLException, DataAccessException {

		List<Transaction> transactions = new ArrayList<>();
		
		while(rs.next()) {
			
			Transaction transaction = new Transaction();
			
			transaction.setConsumerCode(rs.getString("consumer_code"));
			transaction.setTxnId(rs.getString("txn_id"));
			transaction.setGateway(rs.getString("gateway"));
			transaction.setGatewayStatusCode(rs.getString("gateway_status_code"));
			transaction.setGatewayPaymentMode(rs.getString("gateway_payment_mode"));
			transaction.setGatewayTxnId(rs.getString("gateway_txn_id"));
			transaction.setTxnStatus(rs.getString("txn_status"));
			transaction.setTxnAmount(rs.getLong("txn_amount"));
			transaction.setBillId(rs.getString("bill_id"));
			transaction.setTenantId(rs.getString("tenant_id"));
			transaction.setModule(rs.getString("module"));
			
			transactions.add(transaction);
			
		}
		return transactions;
		
	}

}
