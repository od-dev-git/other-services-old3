package org.egov.integration.model;

import javax.validation.Valid;
import javax.validation.constraints.Size;

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
	
    @Size(max=64)
    @JsonProperty("tenantId")
    private String tenantId = null;

    @Size(max=64)
    @JsonProperty("doorNo")
    private String doorNo = null;

    @Size(max=64)
    @JsonProperty("type")
    private String type = null;

    @JsonProperty("addressLine1")
    private String addressLine1 = null;

    @Size(max=256)
    @JsonProperty("addressLine2")
    private String addressLine2 = null;

    @Size(max=64)
    @JsonProperty("landmark")
    private String landmark = null;

    @Size(max=64)
    @JsonProperty("city")
    private String city = null;

    @Size(max=64)
    @JsonProperty("pincode")
    private String pincode = null;

    @Size(max=64)
    @JsonProperty("detail")
    private String detail = null;

    @Size(max=64)
    @JsonProperty("buildingName")
    private String buildingName = null;

    @Size(max=64)
    @JsonProperty("street")
    private String street = null;

    @Valid
    @JsonProperty("locality")
    private Boundary locality = null;

    @Size(max=64)
    @JsonProperty("ward")
    private String ward = null;


}
