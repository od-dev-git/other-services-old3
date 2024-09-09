package org.egov.report.web.model.issuefix;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IssueFix {

	@JsonProperty("tenant")
	private String tenantId=null;

    @JsonProperty("issueName")
    private String issueName=null;

    @JsonProperty("txnId")
    private String txnId = null;
    
    @JsonProperty("consumerCode")
    private String consumerCode = null;
    
    @JsonProperty("empID")
    private String empID;
    
    @JsonProperty("empName")
    private String empName;

    @JsonProperty("consumerCodes")
    private List<String> consumerCodes = null;

    @JsonProperty("allowExtendedLimit")
    private Boolean allowExtendedLimit = false;  // New field to allow extended limit for consumerCodes
}
