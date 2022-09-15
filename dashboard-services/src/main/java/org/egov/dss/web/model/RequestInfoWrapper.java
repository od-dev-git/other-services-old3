package org.egov.dss.web.model;

import org.egov.common.contract.request.RequestInfo;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;

@Getter
public class RequestInfoWrapper {
	
	@JsonProperty(value="requestInfo")
	private RequestInfo requestInfo;
	
	@JsonProperty(value = "criteria")
	private ChartCriteria chartCriteria;
}
