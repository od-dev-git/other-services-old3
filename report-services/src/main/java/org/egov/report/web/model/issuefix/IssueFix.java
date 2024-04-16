package org.egov.report.web.model.issuefix;

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

}
