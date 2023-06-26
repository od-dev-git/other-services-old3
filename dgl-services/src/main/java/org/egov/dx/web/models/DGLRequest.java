package org.egov.dx.web.models;

import org.egov.common.contract.request.RequestInfo;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DGLRequest {
	
	@JsonProperty("RequestInfo")
	private RequestInfo requestInfo;
	
	@JsonProperty("DGLModel")
	private DGLModel dglModel;

}
