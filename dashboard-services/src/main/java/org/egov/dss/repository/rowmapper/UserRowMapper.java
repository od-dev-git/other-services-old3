package org.egov.dss.repository.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.egov.dss.web.model.User;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Component;

@Component
public class UserRowMapper implements ResultSetExtractor<List<User>> {

	@Override
	public List<User> extractData(ResultSet rs) throws SQLException, DataAccessException {
		List<User> users = new ArrayList<>();
		while (rs.next()) {
			User user=new User();
			user.setId(rs.getLong("id"));
			user.setTenantId(rs.getString("tenantid"));
			users.add(user);
		}
		return users;
	}

}
