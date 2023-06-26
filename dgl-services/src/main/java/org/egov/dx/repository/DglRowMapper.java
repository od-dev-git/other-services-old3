package org.egov.dx.repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.egov.dx.web.models.DGLModel;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Component;

@Component
public class DglRowMapper implements ResultSetExtractor<DGLModel>{

	@Override
	public DGLModel extractData(ResultSet rs) throws SQLException, DataAccessException {

		DGLModel dglModel = new DGLModel();
		
		while (rs.next()) {
			
			dglModel.setConsumerCode(rs.getString("consumercode"));
			dglModel.setFilestore(rs.getString("filestore"));
			dglModel.setMaskedId(rs.getString("maskedid"));
			dglModel.setTenantId(rs.getString("tenantid"));
			
		}
		
		return dglModel;
		
	}

}
