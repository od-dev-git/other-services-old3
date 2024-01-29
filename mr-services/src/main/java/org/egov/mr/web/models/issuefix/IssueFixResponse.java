package org.egov.mr.web.models.issuefix;

import org.egov.common.contract.response.ResponseInfo;

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
public class IssueFixResponse {
	
	@JsonProperty("ResponseInfo")
	private ResponseInfo responseInfo;

	@JsonProperty("IssueFix")
	private IssueFix issueFix = null;

	@JsonProperty("Status")
	private String status = null;

}
