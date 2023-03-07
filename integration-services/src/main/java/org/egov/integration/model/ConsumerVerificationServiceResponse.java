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
public class ConsumerVerificationServiceResponse {
	
	@JsonProperty("consumerNo")
	private String consumerNo;
	
	@JsonProperty("tenantId")
	private String tenantId;
	
	@JsonProperty("businessService")
	private String businessService;
	
	@JsonProperty("owners")
	private List<VerificationOwner> verificationOwner;
	
	@JsonProperty("status")
	private String status;
	
	@JsonProperty("address")
	private String address;

}
