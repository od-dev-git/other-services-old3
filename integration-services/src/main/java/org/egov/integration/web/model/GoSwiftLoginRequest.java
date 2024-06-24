package org.egov.integration.web.model;

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
public class GoSwiftLoginRequest {
	
	 @JsonProperty("RequestInfo")
	 private RequestInfo requestInfo;
	 
	 @JsonProperty("Code")
	 private String code;

}
