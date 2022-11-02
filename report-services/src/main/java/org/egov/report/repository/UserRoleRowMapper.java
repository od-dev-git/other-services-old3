package org.egov.report.repository;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.egov.report.user.Role;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

@Component
public class UserRoleRowMapper implements RowMapper<Role>{

	@Override
    public Role mapRow(final ResultSet rs, final int rowNum) throws SQLException {
        return Role.builder().tenantId(rs.getString("roleidtenantid")).build();
    }
}
