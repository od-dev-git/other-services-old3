package org.egov.integration.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ConsumerVerificationSearchCriteria {

	@JsonProperty("tenantId")
	private String tenantId;

	@JsonProperty("businessService")
	private String businessService;
	
	@JsonProperty("consumerNo")
	private String consumerNo;

}
