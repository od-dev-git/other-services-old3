package org.egov.report.repository.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.egov.report.repository.DemandRepository;
import org.egov.report.model.AuditDetails;
import org.egov.report.model.Demand;
import org.egov.report.model.Demand.StatusEnum;
import org.egov.report.model.DemandDetail;
import org.egov.report.util.Util;
import org.egov.report.web.contract.User;
//import org.egov.report.web.model.User;
//import org.egov.demand.model.AuditDetails;
//import org.egov.demand.model.Demand;
//import org.egov.demand.model.DemandDetail;
//import org.egov.demand.model.Demand.StatusEnum;
import org.egov.report.util.Util;
//import org.egov.demand.web.contract.User;
import org.postgresql.util.PGobject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Repository;

import com.fasterxml.jackson.databind.JsonNode;

import lombok.extern.slf4j.Slf4j;

@Repository
@Slf4j
public class DemandRowMapper implements ResultSetExtractor<List<Demand>> {
   
    @Autowired
    private Util util;
    
    @Override
    public List<Demand> extractData(ResultSet rs) throws SQLException, DataAccessException {

        Map<String, Demand> demandMap = new LinkedHashMap<>();
        String demandIdRsName = "did";

        while (rs.next()) {

            String demandId = rs.getString(demandIdRsName);
            Demand demand = demandMap.get(demandId);

            if (demand == null) {

                demand = new Demand();
                demand.setId(demandId);
                demand.setBusinessService(rs.getString("dbusinessservice"));
                demand.setConsumerCode(rs.getString("dconsumerCode"));
                demand.setConsumerType(rs.getString("dconsumerType"));
                demand.setTaxPeriodFrom(rs.getLong("dtaxPeriodFrom"));
                demand.setTaxPeriodTo(rs.getLong("dtaxPeriodTo"));
                demand.setTenantId(rs.getString("dtenantid"));
                demand.setBillExpiryTime(rs.getLong("dbillexpirytime"));
                demand.setFixedBillExpiryDate(rs.getLong("dfixedBillExpiryDate"));
                demand.setStatus(StatusEnum.fromValue(rs.getString("status")));
                demand.setIsPaymentCompleted(rs.getBoolean("ispaymentcompleted"));
                demand.setMinimumAmountPayable(rs.getBigDecimal("dminimumAmountPayable"));
                
                PGobject adDetail = (PGobject) rs.getObject("demandadditionaldetails"); 
                JsonNode json = util.getJsonValue(adDetail);
                demand.setAdditionalDetails(json);
                
                String payerId = rs.getString("payer");
                if (null != payerId) {
                    demand.setPayer(User.builder().uuid(payerId).build());
                }

                AuditDetails auditDetail = new AuditDetails();
                auditDetail.setCreatedBy(rs.getString("dcreatedby"));
                auditDetail.setLastModifiedBy(rs.getString("dlastModifiedby"));
                auditDetail.setCreatedTime(rs.getLong("dcreatedtime"));
                auditDetail.setLastModifiedTime(rs.getLong("dlastModifiedtime"));
                demand.setAuditDetails(auditDetail);

                demand.setDemandDetails(new ArrayList<>());
                demandMap.put(demand.getId(), demand);
            }

            DemandDetail demandDetail = new DemandDetail();
            demandDetail.setId(rs.getString("dlid"));
            demandDetail.setDemandId(rs.getString("dldemandid"));

            demandDetail.setTaxHeadMasterCode(rs.getString("dltaxheadcode"));
            ;
            demandDetail.setTenantId(rs.getString("dltenantid"));
            demandDetail.setTaxAmount(rs.getBigDecimal("dltaxamount"));
            demandDetail.setCollectionAmount(rs.getBigDecimal("dlcollectionamount"));

            AuditDetails dlauditDetail = new AuditDetails();
            dlauditDetail.setCreatedBy(rs.getString("dlcreatedby"));
            dlauditDetail.setCreatedTime(rs.getLong("dlcreatedtime"));
            dlauditDetail.setLastModifiedBy(rs.getString("dllastModifiedby"));
            dlauditDetail.setLastModifiedTime(rs.getLong("dllastModifiedtime"));
            demandDetail.setAuditDetails(dlauditDetail);

            if (demand.getId().equals(demandDetail.getDemandId()))
                demand.getDemandDetails().add(demandDetail);
        }
        return new ArrayList<>(demandMap.values());
    }

}
