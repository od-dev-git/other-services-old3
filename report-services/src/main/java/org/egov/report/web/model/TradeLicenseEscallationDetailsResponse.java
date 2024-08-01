package org.egov.report.web.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor

public class TradeLicenseEscallationDetailsResponse {

    @JsonProperty("tenantId")
    private String tenantId;
    
    @JsonProperty("licenseType")
    private String licenseType;
    
    @JsonProperty("applicationType")
    private String applicationType;
    
    @JsonProperty("applicationNumber")
    private String applicationNumber;
    
    @JsonProperty("dateOfPayment")
    private String dateOfPayment;

    @JsonProperty("daysSinceApplicationSubmission")
    private Long daysSinceApplicationSubmission ;

    @JsonProperty("status")
    private String status;

    @JsonProperty("comment")
    private String comment;
    
    @JsonProperty("escalatedFrom")
    private String escalatedFrom;
    
    @JsonProperty("escalatedTo")
    private String escalatedTo;



    @JsonProperty("autoEscallationDate")
    private String autoEscallationDate ;
    




}