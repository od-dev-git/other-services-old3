package org.egov.integration.model;

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

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TradeLicense {

    @NotNull
    @Size(max=64)
    @JsonProperty("tenantId")
    private String tenantId = null;

    @JsonProperty("businessService")
    private String businessService = "TL";

    @Size(max=64)
    @JsonProperty("licenseNumber")
    private String licenseNumber = null;

    @Size(max=256)
    @JsonProperty("tradeName")
    private String tradeName = null;

    @JsonProperty("applicationDate")
    private Long applicationDate = null;


    @Size(max=64)
    @JsonProperty("status")
    private String status = null;

    @Valid
    @NotNull
    @JsonProperty("tradeLicenseDetail")
    private TradeLicenseDetail tradeLicenseDetail = null;

}
