package org.egov.integration.repository.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.egov.integration.model.BPAVerification;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Component;

@Component
public class BPAVerificationRowMapper implements ResultSetExtractor<List<BPAVerification>> {

	@Override
	public List<BPAVerification> extractData(ResultSet rs) throws SQLException, DataAccessException {

		List<BPAVerification> response = new ArrayList<>();

		while (rs.next()) {

			BPAVerification verification = new BPAVerification();

			verification.setPermitApprovalDate(rs.getLong("approvaldate"));
			verification.setPermitNumber(rs.getString("approvalno"));

			response.add(verification);
		}

		return response;
	}

}
