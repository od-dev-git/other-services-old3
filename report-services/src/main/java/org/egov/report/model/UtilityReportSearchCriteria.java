package org.egov.report.model;

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

    private String reportType;

}
