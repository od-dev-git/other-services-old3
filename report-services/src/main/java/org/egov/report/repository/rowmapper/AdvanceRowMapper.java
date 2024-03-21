package org.egov.report.repository.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.egov.report.model.Advance;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Component;

@Component
public class AdvanceRowMapper implements ResultSetExtractor<Map<String, Advance>> {

	@Override
	public Map<String, Advance> extractData(ResultSet rs) throws SQLException, DataAccessException {

		Map<String, Advance> totalAdvanceAmounts = new HashMap<>();

		while (rs.next()) {

			String consumerCode = rs.getString("consumercode");

			Advance advanceAmount = Advance.builder().consumerCode(consumerCode)
					.totalAdvanceAmount(rs.getBigDecimal("totaladvanceamount")).build();

			totalAdvanceAmounts.put(consumerCode, advanceAmount);

		}

		return totalAdvanceAmounts;
	}

}
