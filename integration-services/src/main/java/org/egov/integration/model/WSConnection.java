package org.egov.integration.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode
public class WSConnection {

	@JsonProperty("connectionHolders")
	private List<OwnerInfo> connectionHolders;
	
	@JsonProperty("tenantId")
	private String tenantId;
	
	@JsonProperty("applicationStatus")
	private String applicationStatus;
	
	@JsonProperty("connectionNo")
	private String connectionNo;
	
	
}
