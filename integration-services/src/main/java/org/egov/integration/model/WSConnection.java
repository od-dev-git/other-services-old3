package org.egov.integration.model;

import java.util.LinkedHashMap;
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

	@JsonProperty("waterSource")
	private String waterSource;

	@JsonProperty("meterId")
	private String meterId;

	@JsonProperty("meterInstallationDate")
	private Long meterInstallationDate;
	
	@JsonProperty("connectionHolders")
	private List<OwnerInfo> connectionHolders;
	
	@JsonProperty("additionalDetails")
	private LinkedHashMap<String, Object> additionalDetails;
	
	@JsonProperty("tenantId")
	private String tenantId;
	
	@JsonProperty("applicationStatus")
	private String applicationStatus;
	
	@JsonProperty("usageCategory")
	private String usageCategory;
	
	@JsonProperty("connectionNo")
	private String connectionNo;
	
	@JsonProperty("connectionFacility")
	private String connectionFacility;
	
}
