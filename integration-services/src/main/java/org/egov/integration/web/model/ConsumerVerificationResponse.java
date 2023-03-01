package org.egov.integration.web.model;

import java.util.List;

import org.egov.common.contract.response.ResponseInfo;
import org.egov.integration.model.ConsumerVerification;
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
	
	@JsonProperty("ResponseInfo")
    private ResponseInfo responseInfo;
	
	@JsonProperty("consumerVerificationInfo")
	private List<ConsumerVerificationServiceResponse> consumerVerification;
	
}
