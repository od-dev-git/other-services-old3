package org.egov.usm.repository.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.egov.usm.web.model.AuditDetails;
import org.egov.usm.web.model.SDAMember;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class SDAMemberRowMapper implements ResultSetExtractor<List<SDAMember>> {

	@Override
	public List<SDAMember> extractData(ResultSet rs) throws SQLException, DataAccessException {
		Map<String,SDAMember> membersMap = new LinkedHashMap<>();

        while (rs.next()){
            String id = rs.getString("id");
            SDAMember sdaMember = membersMap.get(id);
            
            if(sdaMember == null) {
                AuditDetails auditdetails = AuditDetails.builder()
                        .createdBy(rs.getString("createdby"))
                        .createdTime(rs.getLong("createdtime"))
                        .lastModifiedBy(rs.getString("lastmodifiedby"))
                        .lastModifiedTime(rs.getLong("lastmodifiedtime"))
                        .build();

                sdaMember =  SDAMember.builder()
                        .id(id)
                        .userId(rs.getString("userid"))
                        .tenant(rs.getString("tenantid"))
                        .ward(rs.getString("ward"))
                        .slumCode(rs.getString("slumcode"))
                        .isActive(rs.getBoolean("active"))
                        .auditDetails(auditdetails)
                        .build();
            }
            log.info("SDAMember : ", sdaMember);
            membersMap.put(id, sdaMember);
        }
        return new ArrayList<>(membersMap.values());
	}

}
