package org.egov.report.repository.rowmapper;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.egov.report.util.WSReportUtils;
import org.egov.report.web.model.ConsumerBillHistoryResponse;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

public class ConsumerBillHistoryRowMapper implements ResultSetExtractor<List<ConsumerBillHistoryResponse>> {

    WSReportUtils wsReportUtils = new WSReportUtils();

    @Override
    public List<ConsumerBillHistoryResponse> extractData(ResultSet rs) throws SQLException, DataAccessException {

        List<ConsumerBillHistoryResponse> responseList = new ArrayList<>();

        while (rs.next()) {

            String tenantId = rs.getString("tenantid");
            BigDecimal taxAmt = rs.getBigDecimal("totalTax");
            BigDecimal collectedAmt = rs.getBigDecimal("totalCollected");

            ConsumerBillHistoryResponse response = ConsumerBillHistoryResponse
                    .builder()
                    .ulb(tenantId.substring(3))
                    .consumerCode(rs.getString("consumercode"))
                    .periodFrom(wsReportUtils.getConvertedDate(rs.getLong("taxperiodfrom")))
                    .periodTo(wsReportUtils.getConvertedDate(rs.getLong("taxperiodto")))
                    .taxAmount(taxAmt)
                    .collectedAmount(collectedAmt)
                    .dueAmount(taxAmt.subtract(collectedAmt))
                    .build();

            Boolean isPaymentCompleted = rs.getBoolean("ispaymentcompleted");
            if (isPaymentCompleted)
                response.setPaymentCompleted("Yes");
            else
                response.setPaymentCompleted("No");

            responseList.add(response);
        }

        return responseList;
    }

}
