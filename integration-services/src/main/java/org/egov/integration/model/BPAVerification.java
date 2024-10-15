package org.egov.integration.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BPAVerification {
	
	@JsonProperty("permitApprovalDate")
	private Long permitApprovalDate;
	
	@JsonProperty("permitExpiryDate")
	private Long permitExpiryDate;
	
	@JsonProperty("permitNumber")
	private String permitNumber;
	
	@JsonProperty("tenantId")
	private String tenantId;

}
