package org.egov.report.repository.rowmapper;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.egov.report.model.DemandDetailAudit;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Component;

@Component
public class DemandDetailAuditRowMapper implements ResultSetExtractor<List<DemandDetailAudit>> {

    @Override
    public List<DemandDetailAudit> extractData(ResultSet rs) throws SQLException, DataAccessException {
        List<DemandDetailAudit> demandDetailList = new ArrayList<>();

        while (rs.next()) {
            DemandDetailAudit currentDemandDetail = DemandDetailAudit.builder()
                    .id(getString(rs, "id"))
                    .consumercode(getString(rs, "consumercode"))
                    .businessservice(getString(rs, "businessservice"))
                    .taxperiodfrom(getLong(rs, "taxperiodfrom"))
                    .taxperiodto(getLong(rs, "taxperiodto"))
                    .entryRank(getInt(rs, "entryRank"))
                    .taxheadcode(getString(rs, "taxheadcode"))
                    .demandTaxamount(getBigDecimal(rs, "demandTaxamount"))
                    .cumulativeTaxamount(getBigDecimal(rs, "cumulativeTaxamount"))
                    .demandCollectionamount(getBigDecimal(rs, "demandCollectionamount"))
                    .cumulativeCollectionamount(getBigDecimal(rs, "cumulativeCollectionamount"))
                    .demandDetailCreatedby(getString(rs, "demandDetailCreatedby"))
                    .demandDetailCreatedtime(getLong(rs, "demandDetailCreatedtime"))
                    .demandDetailLastmodifiedby(getString(rs, "demandDetailLastmodifiedby"))
                    .demandDetailLastmodifiedtime(getLong(rs, "demandDetailLastmodifiedtime"))
                    .build();

            demandDetailList.add(currentDemandDetail);
        }

        return demandDetailList;
    }

    private String getString(ResultSet rs, String columnLabel) throws SQLException {
        return rs.getString(columnLabel) != null ? rs.getString(columnLabel) : "";
    }

    private Long getLong(ResultSet rs, String columnLabel) throws SQLException {
        long value = rs.getLong(columnLabel);
        return rs.wasNull() ? null : value;
    }

    private Integer getInt(ResultSet rs, String columnLabel) throws SQLException {
        int value = rs.getInt(columnLabel);
        return rs.wasNull() ? null : value;
    }

    private BigDecimal getBigDecimal(ResultSet rs, String columnLabel) throws SQLException {
        return rs.getBigDecimal(columnLabel) != null ? rs.getBigDecimal(columnLabel) : BigDecimal.ZERO;
    }
}


