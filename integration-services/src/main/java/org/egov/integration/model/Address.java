package org.egov.integration.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Address {

	@JsonProperty("tenantId")
	private String tenantId = null;

	@JsonProperty("doorNo")
	private String doorNo = null;

	@JsonProperty("type")
	private String type = null;

	@JsonProperty("addressLine1")
	private String addressLine1 = null;

	@JsonProperty("addressLine2")
	private String addressLine2 = null;

	@JsonProperty("landmark")
	private String landmark = null;

	@JsonProperty("city")
	private String city = null;

	@JsonProperty("pincode")
	private String pincode = null;

	@JsonProperty("detail")
	private String detail = null;

	@JsonProperty("buildingName")
	private String buildingName = null;

	@JsonProperty("street")
	private String street = null;

	@JsonProperty("locality")
	private Boundary locality = null;

	@JsonProperty("ward")
	private String ward = null;

	@JsonProperty("country")
	private String country = null;

	@JsonProperty("state")
	private String state = null;

	@JsonProperty("district")
	private String district = null;

	@JsonProperty("pinCode")
	private String pinCodeTL = null;

}
