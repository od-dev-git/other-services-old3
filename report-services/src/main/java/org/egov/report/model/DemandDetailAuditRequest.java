package org.egov.report.model;


import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class DemandDetailAuditRequest {

    @JsonProperty("consumercode")
    private String consumercode;
    
    @JsonProperty("tenantId")
    private String tenantId;

    @JsonProperty("taxperiodfrom")
    private Long taxperiodfrom;

    @JsonProperty("taxperiodto")
    private Long taxperiodto;

    @JsonProperty("demandid")
    private String demandid;

    @JsonProperty("pageNumber")
    private int pageNumber;

    @JsonProperty("pageSize")
    private int pageSize;
    
    @JsonProperty("demandAdjusted")
    private Boolean demandAdjusted;
}