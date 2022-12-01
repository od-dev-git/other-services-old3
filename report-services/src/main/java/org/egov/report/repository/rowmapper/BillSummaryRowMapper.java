package org.egov.report.repository.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import org.egov.report.web.model.BillSummaryResponses;
import org.egov.report.web.model.User;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Component;

public class BillSummaryRowMapper implements ResultSetExtractor<List<BillSummaryResponses>> {

    List<BillSummaryResponses> billSummaryResponseList = new ArrayList<>();

    @Override
    public List<BillSummaryResponses> extractData(ResultSet rs) throws SQLException, DataAccessException {

        while (rs.next()) {
            BillSummaryResponses billSummaryResponse = BillSummaryResponses
                    .builder()
                    .monthYear(rs.getString("monthYear"))
                    .ulb(rs.getString("ulb"))
                    .build();

            billSummaryResponseList.add(billSummaryResponse);
        }

        return billSummaryResponseList;
    }
}