package org.egov.integration.web.model;

import org.egov.integration.model.ConsumerVerificationServiceResponse;

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
public class ConsumerVerificationResponse {
	
	@JsonProperty("consumerVerificationInfo")
	private ConsumerVerificationServiceResponse consumerVerification;
	
}
