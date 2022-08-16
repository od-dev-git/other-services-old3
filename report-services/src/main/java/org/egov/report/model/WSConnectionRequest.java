package org.egov.report.model;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.egov.common.contract.request.RequestInfo;
import org.egov.report.model.UserSearchRequest.UserSearchRequestBuilder;

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
public class WSConnectionRequest {
	
	@JsonProperty("RequestInfo")
	private RequestInfo requestInfo;

}
