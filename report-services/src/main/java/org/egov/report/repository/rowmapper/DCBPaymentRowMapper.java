package org.egov.report.repository.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.egov.report.model.DCBPayment;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

public class DCBPaymentRowMapper implements ResultSetExtractor<Map<String, DCBPayment>> {

	@Override
	public Map<String, DCBPayment> extractData(ResultSet rs) throws SQLException, DataAccessException {

		Map<String, DCBPayment> payments = new HashMap<>();

		while (rs.next()) {

			DCBPayment payment = DCBPayment.builder().consumerCode(rs.getString("consumercode"))
					.totalPaid(rs.getBigDecimal("totalpaid")).build();

			payments.put(rs.getString("consumercode"), payment);

		}

		return payments;

	}

}
