package org.egov.report.model;

import java.util.ArrayList;
import java.util.List;

import org.egov.report.model.Owner;
import org.egov.report.web.contract.UserSearchRequest;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Owner {

    @JsonProperty("tenantId")
    private String tenantId;

    @JsonProperty("id")
    private Long id;

    @JsonProperty("userName")
    private String userName;

    @JsonProperty("name")
    private String name;
    
    @JsonProperty("permanentAddress")
    private String permanentAddress;

    @JsonProperty("mobileNumber")
    private String mobileNumber;

    @JsonProperty("emailId")
    private String emailId;

    @JsonProperty("aadhaarNumber")
    private String aadhaarNumber;
    
    private String userType;

    public UserSearchRequest toUserSearchRequest() {

        List<Long> ids = new ArrayList<>();
        ids.add(this.id);
        return UserSearchRequest.builder().aadhaarNumber(this.aadhaarNumber).emailId(this.emailId)
                .mobileNumber(this.mobileNumber).name(this.name).tenantId(this.tenantId).id(ids)
                .userName(this.userName).userType("CITIZEN").pageSize(500).build();
    }
}

