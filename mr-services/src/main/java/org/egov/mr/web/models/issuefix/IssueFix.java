package org.egov.mr.web.models.issuefix;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IssueFix {
	
	@JsonProperty("tenant")
	private String tenantId;

    @JsonProperty("issueName")
    private String issueName=null;

    @JsonProperty("applicationNo")
    private String applicationNo = null;

    @JsonProperty("mrNumber")
    private String mrNumber = null;

}
