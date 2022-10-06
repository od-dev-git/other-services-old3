package org.egov.report.web.model;

import java.math.BigDecimal;

import org.egov.report.web.model.WaterConnectionDetails.WaterConnectionDetailsBuilder;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WaterDemandResponse {


    @JsonProperty("tenantid")
    private String tenantid;

    @JsonProperty("id")
    private String demandId; // I think its not required

    @JsonProperty("taxperiodfrom")
    private Long taxperiodfrom;

    @JsonProperty("taxperiodto")
    private Long taxperiodto;

    @JsonProperty("taxheadcode")
    private String taxheadcode;

    @JsonProperty("taxamount")
    private BigDecimal taxamount;

    @JsonProperty("collectionamount")
    private BigDecimal collectionamount;
    
    @JsonProperty("ward")
    private String ward;
    
    @JsonProperty("oldconnectionno")
    private String oldconnectionno;
    
    @JsonProperty("consumercode")
    private String consumercode;
    
    @JsonProperty("connectiontype")
    private String connectiontype;
    
    @JsonProperty("userid")
    private String userid;
    
    @JsonProperty("address")
    private String address;
    
    @JsonProperty("connectionHolderName")
    private String connectionHolderName;
    
    @JsonProperty("contactNo")
    private String mobile;
	
}
