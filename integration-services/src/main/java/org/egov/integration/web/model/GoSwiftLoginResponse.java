package org.egov.integration.web.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
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

    @JsonProperty("name")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String name;

    @JsonProperty("tenantId")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String tenantId;
    
    @JsonProperty("goSwiftDetails")
    private Object goSwiftDetails;
}
