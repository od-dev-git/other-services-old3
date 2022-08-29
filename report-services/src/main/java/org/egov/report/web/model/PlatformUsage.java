package org.egov.report.web.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlatformUsage {

	@JsonProperty("serviceAvailed")
	private String serviceAvailed;

	@JsonProperty("personName")
	private String personName;

	@JsonProperty("contactNumber")
	private String contactNumber;

	@JsonProperty("contactEmail")
	private String contactEmail;

	@JsonProperty("Address")
	private String Address;

	@JsonProperty("dateOfServiceAvailedOrApproved")
	private String dateOfServiceAvailedOrApproved;

	@JsonProperty("applicationNo")
	private String applicationNo;

	@JsonProperty("consumerOrPermitNo")
	private String consumerOrPermitNo;
}