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
public class ULBWiseTaxCollectionResponse {

	@JsonProperty("ulb")
    private String ulb ;
	
	@JsonProperty("propertyId")
    private String propertyId ;
 
    @JsonProperty("oldPropertyId")
    private String oldpropertyid ;

    @JsonProperty("ward")
    private String ward ;
    
    @JsonProperty("currentYearDemandAmount")
    private String totaltaxamount ;
    
    @JsonProperty("totalArrearDemandAmount")
    private String totalarreartaxamount ;
    
   
    @JsonProperty("totalCollectedAmount")
    private String totalcollectionamount ;
    
    @JsonProperty("dueAmount")
    private String dueamount ;
	
}
