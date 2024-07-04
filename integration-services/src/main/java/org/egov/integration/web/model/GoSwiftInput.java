package org.egov.integration.web.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GoSwiftInput {

    @JsonProperty("tenantId")
    private String tenantId;

    @JsonProperty("applicationNo")
    private String applicationNo;

    @JsonProperty("serviceId")
    private String serviceId;

    @JsonProperty("mobileNo")
    private String mobileNo;

    @JsonProperty("emailId")
    private String emailId;

    @JsonProperty("companyName")
    private String companyName;
    
    @JsonProperty("name")
    private String name;
    
    @JsonProperty("securityToken")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String securityToken;
}
