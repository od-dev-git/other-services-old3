package org.egov.integration.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class ConsumerVerification {

	@JsonProperty("consumerNo")
	private String consumerNo;
	
	@JsonProperty("tenantId")
	private String tenantId;
	
	@JsonProperty("businessService")
	private String businessService;
	
	@JsonProperty("verificationowner")
	private List<OwnerInfo> consumerVerificationOwner;
	
	@JsonProperty("status")
	private String status;
	
}
