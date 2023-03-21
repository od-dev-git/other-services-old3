package org.egov.dss.model;

import java.util.List;
import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PropertySerarchCriteria {
  
	private Set<String> ids;
	
	private String tenantId;

    private Set<String> tenantIds;
    
    private Set<String> districtId;
    
    private Set<String> city;
    
    private String status;

    private Set<String> businessServices;

    private Long fromDate;

    private Long toDate;

    private Integer offset;

    private Integer limit;
    
    private String excludedTenantId;
    
    private Long slaThreshold;
    
}
