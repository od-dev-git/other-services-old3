package org.egov.report.web.model;
import org.egov.common.contract.response.ResponseInfo;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReportResponse {
	
	@JsonProperty("ResponseInfo")
    private ResponseInfo responseInfo;
	
	@JsonProperty("IncentiveInfo")
	private IncentiveResponse incentiveResponse;
}
