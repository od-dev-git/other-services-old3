package org.egov.report.model;

import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserSearchCriteria {
	
	public static final String CITIZEN="CITIZEN";
	public static final String EMPLOYEE="EMPLOYEE";
	
	@JsonProperty("id")
    private List<Long> id;

    @JsonProperty("uuid")
    private Set<String> uuid;
	
	@JsonProperty("tenantId")
    private String tenantId;
	
	@JsonProperty("active")
    private Boolean active;
	
	@JsonProperty("userType")
    private String userType;
	
	@JsonProperty("financialYear")
    private String financialYear;

	@JsonProperty("tenantIds")
	private List<String> tenantIds; 
	
}
