package org.egov.report.web.model;

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
public class PropertyWiseDemandResponse {
	
	@JsonProperty("ulb")
    private String ulb ;
	
	@JsonProperty("propertyId")
    private String propertyId ;
 
    @JsonProperty("oldPropertyId")
    private String oldpropertyid ;

    @JsonProperty("ward")
    private String ward ;
    
    @JsonProperty("name")
    private String name ;
    
    @JsonProperty("mobileNumber")
    private String mobilenumber ;
    
    @JsonProperty("taxPeriodFrom")
    private String taxperiodfrom ;
    
    @JsonProperty("taxPeriodTo")
    private String taxperiodto ;
  
    @JsonProperty("taxDemandAmount")
    private String taxamount ;
    
    @JsonProperty("paidAmount")
    private String collectionamount ;
    
    @JsonProperty("dueAmount")
    private String dueamount ;

}
