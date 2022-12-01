package org.egov.report.repository.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.egov.report.web.model.ULBWiseWaterConnectionDetails;
import org.egov.report.web.model.WsSchedulerBasedDemandsGenerationReponse;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

public class SchedulerGeneratedDemandsRowMapper
        implements ResultSetExtractor<List<WsSchedulerBasedDemandsGenerationReponse>> {

    List<WsSchedulerBasedDemandsGenerationReponse> schedulerDemandResponse = new ArrayList<>();

    @Override
    public List<WsSchedulerBasedDemandsGenerationReponse> extractData(ResultSet rs)
            throws SQLException, DataAccessException {

        while (rs.next()) {

            String ulb = (rs.getString("tenantid")).replace("od.", "");
            ulb = ulb.substring(0, 1).toUpperCase() + ulb.substring(1).toLowerCase();

            WsSchedulerBasedDemandsGenerationReponse schedulerDemands = WsSchedulerBasedDemandsGenerationReponse
                    .builder()
                    .ulb(ulb)
                    .ward(rs.getString("ward"))
                    .consumerCode(rs.getString("consumercode"))
                    .oldConnectionNo(rs.getString("oldconnectionno"))
                    .connectionType(rs.getString("connectiontype"))
                    .demandGenerationDate(rs.getString("createdtime"))
                    .taxPeriodFrom(rs.getString("taxperiodfrom"))
                    .taxPeriodto(rs.getString("taxperiodto"))
                    .build();

            schedulerDemandResponse.add(schedulerDemands);

        }

        return schedulerDemandResponse;

    }

}