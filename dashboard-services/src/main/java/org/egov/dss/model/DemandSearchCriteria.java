package org.egov.dss.model;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DemandSearchCriteria {
	
	private String id;

    private String tenantId;
    
    private Set<String> tenantIds;
    
    private String businessService;
    
    private Long taxPeriodFrom;
    
    private Long taxPeriodTo;
    
    private String financialYear;
    
    private BigDecimal amount;
    
    private String excludedTenantId;
    
    private Boolean isArrearDemand;
    

}
