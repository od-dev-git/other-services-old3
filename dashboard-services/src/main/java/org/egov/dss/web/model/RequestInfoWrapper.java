package org.egov.dss.web.model;

import org.egov.common.contract.request.RequestInfo;
import org.egov.dss.model.PayloadDetails;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RequestInfoWrapper {
	
	@JsonProperty(value="requestInfo")
	private RequestInfo requestInfo;
	
	@JsonProperty(value = "criteria")
	private ChartCriteria chartCriteria;
	
	@JsonProperty(value = "payloadDetails")
	private PayloadDetails payloadDetails;
	
	
	
	
}
