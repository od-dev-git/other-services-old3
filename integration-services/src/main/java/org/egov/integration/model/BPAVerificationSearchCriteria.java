package org.egov.integration.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BPAVerificationSearchCriteria {
	
	@JsonProperty("permitNumber")
	private String permitNumber;
	
	@JsonProperty("tenantId")
	private String tenantId;

}
