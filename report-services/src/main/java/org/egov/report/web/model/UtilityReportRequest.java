package org.egov.report.web.model;

import org.egov.common.contract.request.RequestInfo;
import org.egov.report.model.UtilityReportDetails;
import org.springframework.validation.annotation.Validated;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Validated
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class UtilityReportRequest {

	@Default
	@JsonProperty("RequestInfo")
	private RequestInfo requestInfo = null;

	@Default
	@JsonProperty("reportDetails")
	private UtilityReportDetails reportDetails = null;

}
