package org.egov.dss.model;

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
public class TargetSearchCriteria {
	
	private Set<String> ids;
	
	private String tenantId;
	
	private Set<String> tenantIds;
	
	private Set<String> businessServices;
	
	private Long fromDate;

    private Long toDate;
    
    private String financialYear;

}
