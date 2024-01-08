package org.egov.report.web.model;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor

public class PropertyDetailsResponse {

	@JsonProperty("ulbName")
	private String ulbName;

	@JsonProperty("wardNumber")
	private String wardNumber;

	@JsonProperty("oldPropertyId")
	private String oldPropertyId;

	@JsonProperty("propertyId")
	private String propertyId;
	
	@JsonProperty("uuid")
	private String uuid;

	@JsonProperty("name")
	private String name;

	@JsonProperty("mobileNumber")
	private String mobileNumber;

	@JsonProperty("doorNo")
	private String doorNo;

	@JsonProperty("buildingName")
	private String buildingName;

	@JsonProperty("street")
	private String street;

	@JsonProperty("city")
	private String city;

	@JsonProperty("pincode")
	private String pincode;

	@JsonProperty("address")
	private String address;
	
	@JsonProperty("ownershipCategory")
	private String ownershipCategory;
	
	@JsonProperty("propertyType")
	private String propertyType;
	
	@JsonProperty("usageCategory")
	private String usageCategory;




}