package org.egov.dx.web.models.BPA;

import java.math.BigDecimal;
import java.util.List;

import org.egov.common.contract.request.Role;
import org.springframework.validation.annotation.Validated;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Validated
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Address {

	@JsonProperty("tenantId")
	private String tenantId = null;

	@JsonProperty("doorNo")
	private String doorNo = null;

	@JsonProperty("plotNo")
	private String plotNo = null;

	@JsonProperty("id")
	private String id = null;

	@JsonProperty("landmark")
	private String landmark = null;

	@JsonProperty("city")
	private String city = null;

	@JsonProperty("district")
	private String district = null;

	@JsonProperty("region")
	private String region = null;

	@JsonProperty("state")
	private String state = null;

	@JsonProperty("country")
	private String country = null;

	@JsonProperty("pincode")
	private String pincode = null;

	@JsonProperty("additionDetails")
	private String additionDetails = null;

	@JsonProperty("buildingName")
	private String buildingName = null;

	@JsonProperty("street")
	private String street = null;

}
