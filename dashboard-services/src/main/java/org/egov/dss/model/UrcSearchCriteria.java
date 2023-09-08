package org.egov.dss.model;

import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UrcSearchCriteria {

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

	private String excludedTenant;

	private Long slaThreshold;

	private Set<String> statusNotIn;
	
	private String hrmsCode;
	
	private Boolean isActive;

}
