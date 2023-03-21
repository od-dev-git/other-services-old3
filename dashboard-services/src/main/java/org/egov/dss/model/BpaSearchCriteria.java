package org.egov.dss.model;

import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BpaSearchCriteria {

	private Set<String> ids;

	private String tenantId;

	private Set<String> tenantIds;

	private Set<String> districtId;

	private Set<String> city;

	private Set<String> businessServices;

	private Long fromDate;

	private Long toDate;

	private Integer offset;

	private Integer limit;

	private String excludedTenantId;

	private String status;
	
	private Long slaThreshold;

}
