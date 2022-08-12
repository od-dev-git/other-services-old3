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
public class TaxCollectorWiseCollectionResponse {
	
	@JsonProperty("collectorName")
    private String name ;
	
	@JsonProperty("collectorEmployeeId")
    private String employeeid ;
	
	@JsonProperty("mobileNumber")
    private String mobilenumber ;
	
	@JsonProperty("ammountCollected")
    private String amountpaid ;
	
	@JsonProperty("paymentMode")
    private String paymentMode ;
	
	@JsonProperty("receiptNumber")
    private String receiptnumber ;
	
	@JsonProperty("paymentDate")
    private String paymentdate ;
	
	@JsonProperty("propertyId")
	private String consumercode ;
 
    @JsonProperty("oldPropertyId")
    private String oldpropertyid ;

}
