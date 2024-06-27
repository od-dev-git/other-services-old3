package org.egov.integration.web.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.egov.common.contract.response.ResponseInfo;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GoSwiftLoginResponse {

    @JsonProperty("ResponseInfo")
    private ResponseInfo responseInfo;

    @JsonProperty("isSuccessful")
    private Boolean isSuccessful;

    @JsonProperty("username")
    private String username;

    @JsonProperty("type")
    private String type;
}
