package org.egov.dx.web.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DGLModel {
	
	@JsonProperty("tenantId")
	private String tenantId;
	
	@JsonProperty("consumerCode")
	private String consumerCode;
	
	@JsonProperty("filestore")
	private String filestore;
	
	@JsonProperty("maskedId")
	private String maskedId;
	
	@JsonProperty("additionalDetails")
	private Object additionalDetails = null;

}
