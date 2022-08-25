package org.egov.report.web.model;

import java.math.BigDecimal;

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
    @Builder.Default
    private BigDecimal totaltaxamount = BigDecimal.ZERO;
    
    @JsonProperty("totalArrearDemandAmount")
    @Builder.Default
    private BigDecimal totalarreartaxamount = BigDecimal.ZERO ;
    
    @JsonProperty("totalCollectedAmount")
    @Builder.Default
    private BigDecimal totalcollectionamount = BigDecimal.ZERO ;
    
    @JsonProperty("dueAmount")
    @Builder.Default
    private BigDecimal dueamount = BigDecimal.ZERO ;
	
}
