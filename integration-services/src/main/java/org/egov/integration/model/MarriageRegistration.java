package org.egov.integration.model;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MarriageRegistration {

	@JsonProperty("tenantId")
	private String tenantId = null;

	@JsonProperty("businessService")
	private String businessService = "MR";
	
	@JsonProperty("mrNumber")
	private String mrNumber = null;

	@JsonProperty("applicationDate")
	private Long applicationDate = null;

	@JsonProperty("issuedDate")
	private Long issuedDate = null;

	@JsonProperty("status")
	private String status = null;

	@JsonProperty("coupleDetails")
	private List<Couple> coupleDetails = null;
	
	@JsonProperty("marriagePlace")
    private MarriagePlace  marriagePlace ; 

}
