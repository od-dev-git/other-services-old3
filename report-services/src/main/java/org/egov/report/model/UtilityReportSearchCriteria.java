package org.egov.report.model;

import java.math.BigDecimal;
import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UtilityReportSearchCriteria {

    private Set<String> ids;

    private String tenantId;
    
    private BigDecimal fromDate;
    
    private BigDecimal toDate;

    private String reportType;

}
