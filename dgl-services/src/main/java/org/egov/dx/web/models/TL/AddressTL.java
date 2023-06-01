package org.egov.dx.web.models.TL;

import javax.validation.Valid;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.thoughtworks.xstream.annotations.XStreamAlias;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@XStreamAlias("Address")
public class AddressTL {

	@Size(max = 64)
	@JsonProperty("id")
	private String id;

	@Size(max = 64)
	@JsonProperty("tenantId")
	private String tenantId = null;

	@Size(max = 64)
	@JsonProperty("doorNo")
	private String doorNo = null;

	@JsonProperty("latitude")
	private Double latitude = null;

	@JsonProperty("longitude")
	private Double longitude = null;

	@Size(max = 64)
	@JsonProperty("addressId")
	private String addressId = null;

	@Size(max = 64)
	@JsonProperty("addressNumber")
	private String addressNumber = null;

	@Size(max = 64)
	@JsonProperty("type")
	private String type = null;

	@JsonProperty("addressLine1")
	private String addressLine1 = null;

	@Size(max = 256)
	@JsonProperty("addressLine2")
	private String addressLine2 = null;

	@Size(max = 64)
	@JsonProperty("landmark")
	private String landmark = null;

	@Size(max = 64)
	@JsonProperty("city")
	private String city = null;

	@Size(max = 64)
	@JsonProperty("pincode")
	private String pincode = null;

	@Size(max = 64)
	@JsonProperty("detail")
	private String detail = null;

	@Size(max = 64)
	@JsonProperty("buildingName")
	private String buildingName = null;

	@Size(max = 64)
	@JsonProperty("street")
	private String street = null;

	@Valid
	@JsonProperty("locality")
	private Boundary locality = null;

	@Size(max = 64)
	@JsonProperty("ward")
	private String ward = null;
}
