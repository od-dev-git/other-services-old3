package org.egov.report.model;


import java.util.ArrayList;
import java.util.List;

import org.egov.common.contract.response.ResponseInfo;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ConsumerDemandAuditResponse {

	@JsonProperty("ResponseInfo")
	private ResponseInfo responseInfo;

	@JsonProperty("demandDetailAuditResponse")
	private List<DemandDetailAuditResponse> demandDetailAuditResponse = new ArrayList<>();

}