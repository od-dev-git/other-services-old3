package org.egov.integration.model;

import java.util.List;

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

    @JsonProperty("tenantId")
    private String tenantId = null;

    @JsonProperty("businessService")
    private String businessService = "TL";

    @JsonProperty("licenseNumber")
    private String licenseNumber = null;

    @JsonProperty("tradeName")
    private String tradeName = null;

    @JsonProperty("applicationDate")
    private Long applicationDate = null;


    @JsonProperty("status")
    private String status = null;

    @JsonProperty("tradeLicenseDetail")
    private TradeLicenseDetail tradeLicenseDetail = null;

}
