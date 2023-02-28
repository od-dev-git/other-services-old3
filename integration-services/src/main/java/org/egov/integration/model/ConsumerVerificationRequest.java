package org.egov.integration.model;

import java.util.List;

import javax.validation.Valid;

import org.egov.common.contract.request.RequestInfo;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ConsumerVerificationRequest {

	@JsonProperty("RequestInfo")
	private RequestInfo requestInfo = null;

	@JsonProperty("ConsumerVerificationRequestInfo")
	private @Valid ConsumerVerificationSearchCriteria consumerVerificationRequest;
}
