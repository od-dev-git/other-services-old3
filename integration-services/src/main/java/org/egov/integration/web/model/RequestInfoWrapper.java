package org.egov.integration.web.model;

import java.util.List;

import javax.validation.Valid;

import org.egov.common.contract.request.RequestInfo;
import org.egov.integration.model.ConsumerVerificationRequest;
import org.egov.integration.model.ConsumerVerificationSearchCriteria;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RequestInfoWrapper {
	
	@JsonProperty(value="RequestInfo")
	private RequestInfo requestInfo;
	
	@JsonProperty("criteria")
	private @Valid ConsumerVerificationSearchCriteria consumerVerificationRequest;
}