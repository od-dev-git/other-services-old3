package org.egov.report.web.model.issuefix;

import org.egov.common.contract.request.RequestInfo;

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
public class IssueFixRequest {

	@JsonProperty("RequestInfo")
    private RequestInfo requestInfo = null;

    @JsonProperty("IssueFix")
    private IssueFix issueFix=null;

}
