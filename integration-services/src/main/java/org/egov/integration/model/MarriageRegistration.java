package org.egov.integration.model;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

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

	@NotNull
	@Size(max = 64)
	@JsonProperty("tenantId")
	private String tenantId = null;

	@JsonProperty("businessService")
	private String businessService = "MR";
	@Size(max = 64)
	@JsonProperty("mrNumber")
	private String mrNumber = null;

	@JsonProperty("applicationDate")
	private Long applicationDate = null;

	@JsonProperty("issuedDate")
	private Long issuedDate = null;

	@Size(max = 64)
	@JsonProperty("status")
	private String status = null;

	@JsonProperty("coupleDetails")
	@Valid
	private List<Couple> coupleDetails = null;

}
