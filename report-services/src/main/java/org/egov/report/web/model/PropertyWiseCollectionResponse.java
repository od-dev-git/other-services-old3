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
public class PropertyWiseCollectionResponse {

    @JsonProperty("consumerId")
    private String consumercode ;
 
    @JsonProperty("oldPropertyId")
    private String oldpropertyid ;

    @JsonProperty("ward")
    private String ward ;
  
    @JsonProperty("name")
    private String name ;
    
    @JsonProperty("mobileNumber")
    private String mobilenumber ;
    
    @JsonProperty("dueBeforePayment")
    private String due ;
    
    @JsonProperty("amountPaid")
    private String amountpaid ;
    
    @JsonProperty("currentDue")
    private String currentdue ;
    
    @JsonProperty("receiptNumber")
    private String receiptnumber ;

    @JsonProperty("receiptDate")
    private String receiptdate ;
    
  
    @JsonProperty("paymentMode")
    private String paymentMode ;
}
