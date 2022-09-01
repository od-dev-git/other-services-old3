package org.egov.report.repository.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.egov.report.web.model.ULBWiseWaterConnectionDetails;
import org.egov.report.web.model.WsSchedulerBasedDemandsGenerationReponse;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

public class SchedulerGeneratedDemandsRowMapper implements ResultSetExtractor<List<WsSchedulerBasedDemandsGenerationReponse>>{

List<WsSchedulerBasedDemandsGenerationReponse> schedulerDemandResponse = new ArrayList<>();

	@Override
	public List<WsSchedulerBasedDemandsGenerationReponse> extractData(ResultSet rs) throws SQLException, DataAccessException {

		while(rs.next()) {

			WsSchedulerBasedDemandsGenerationReponse schedulerDemands = new WsSchedulerBasedDemandsGenerationReponse();

			String ulb = (rs.getString("tenantid")).replace("od.", "");
			ulb = ulb.substring(0,1).toUpperCase() + ulb.substring(1).toLowerCase();
			schedulerDemands.setUlb(ulb);

			schedulerDemands.setWard(rs.getString("ward"));
			schedulerDemands.setConsumerCode(rs.getString("consumercode"));
			schedulerDemands.setOldConnectionNo(rs.getString("oldconnectionno"));
			schedulerDemands.setConnectionType(rs.getString("connectiontype"));
			schedulerDemands.setDemandGenerationDate(rs.getString("createdtime"));
			schedulerDemands.setTaxPeriodFrom(rs.getString("taxperiodfrom"));
			schedulerDemands.setTaxPeriodto(rs.getString("taxperiodto"));


		    schedulerDemandResponse.add(schedulerDemands);

		}

		return schedulerDemandResponse;

}


}