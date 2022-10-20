package org.egov.report.web.model;

import com.fasterxml.jackson.annotation.JsonSubTypes.Type;

import java.math.BigDecimal;
import java.util.Set;

import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Builder.Default;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DemandCriteria {

    @NotNull
    private String tenantId; 
    
    private Set<String> demandId;
    
    private Set<String> payer;
    
    private Set<String> consumerCode;
    
    private String businessService;
    
    private BigDecimal demandFrom;
    
    private BigDecimal demandTo;
    
    private Long periodFrom;
    
    private Long periodTo;
    
    private Type type;
    
    private String mobileNumber;
    
    private String email;
    
    private String status;
    
    private Boolean isPaymentCompleted;
    
    @Default
    private Boolean receiptRequired=false;
}
