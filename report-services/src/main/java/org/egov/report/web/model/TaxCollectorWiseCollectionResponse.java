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
public class TaxCollectorWiseCollectionResponse {
	
	@JsonProperty("uuId")
    private String uuid ;
	
	@JsonProperty("userId")
    private String userid ;
	
	@JsonProperty("tenantId")
    private String tenantid ;
	
	@JsonProperty("type")
    private String type ;
	
	@JsonProperty("propertyId")
	private String consumercode ;

    @JsonProperty("oldPropertyId")
    private String oldpropertyid ;

    @JsonProperty("ddnNo")
    private String ddnNo;

    @JsonProperty("legacyHoldingNo")
    private String legacyHoldingNo;
    
    @JsonProperty("collectorEmployeeId")
    private String employeeid ;
	
	@JsonProperty("collectorName")
    private String name ;
	
	@JsonProperty("mobileNumber")
    private String mobilenumber ;

	@JsonProperty("paymentDate")
    private String paymentdate ;

	@JsonProperty("paymentMode")
    private String paymentMode ;
	
	@JsonProperty("paymentStatus")
    private String paymentStatus ;
	
	@JsonProperty("paidBy")
    private String paidby ;
	
	@JsonProperty("createdTime")
    private String createdtime ;
	
	@JsonProperty("ammountCollected")
    private String amountpaid ;
	
	@JsonProperty("currentCollection")
	@Builder.Default
    private BigDecimal currentCollection = BigDecimal.ZERO;
	
	@JsonProperty("arrearCollection")
	@Builder.Default
    private BigDecimal arrearCollection = BigDecimal.ZERO;
	
	@JsonProperty("receiptNumber")
    private String receiptnumber ;
    
}
