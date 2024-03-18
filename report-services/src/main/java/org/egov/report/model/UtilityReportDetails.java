package org.egov.report.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UtilityReportDetails {
	
    @JsonProperty("id")
    private String id;

    @JsonProperty("tenantId")
    private String tenantId;
    
    @JsonProperty("reportType")
    private String reportType;

    @Default
    @JsonProperty("fileStoreId")
    private String fileStoreId = null;
    
    @JsonProperty("fileName")
    private String fileName;

    @Default
    @JsonProperty("auditDetails")
	private AuditDetails auditDetails = null;
    
    @JsonProperty("additionalDetails")
	private Object additionalDetails;

}
