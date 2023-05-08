package org.egov.dss.model;

import java.math.BigDecimal;
import java.util.Set;

import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DemandPayload {
   
	@Size(max=64)
    @JsonProperty("id")
    private String id;
	
	@JsonProperty("tenantId")
	private String tenantId;
	
	@JsonProperty("businessService")
	private String businessService;
	
	@JsonProperty("financialYear")
	private String financialYear;
	
	@JsonProperty("amount")
	private BigDecimal amount;
	
	@JsonProperty("taxPeriodFrom")
	private Long taxPeriodFrom;
	
	@JsonProperty("taxPeriodTo")
	private Long taxPeriodTo;
	
	@JsonProperty("taxHeadCode")
	private Set<String> taxHeadCode;
	
	@JsonProperty("excludedTenantId")
	private String excludedTenantId;
	
	@JsonProperty("lastModifiedTime")
	private Long lastModifiedTime;
	
}
