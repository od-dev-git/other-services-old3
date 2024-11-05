package org.egov.report.web.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TicketDetails {

	@JsonProperty("tenantid")
	private String tenantId;
	
	@JsonProperty("city")
	private String city;
	
	@JsonProperty("ticketNo")
	private String serviceRequestId;
	
	@JsonProperty("createdDate")
	private Long createdDate;
	
	@JsonProperty("closedDate")
	private Long closedDate;
	
	@JsonProperty("raisedby")
	private String raisedby;
	
	@JsonProperty("name")
	private String name;
	
	@JsonProperty("mobilenumber")
	private String mobilenumber;
	
	@JsonProperty("module")
	private String service;
	
	@JsonProperty("type")
	private String type;
	
	@JsonProperty("description")
	private String description;
	
	@JsonProperty("priority")
	private String priority;
	
	@JsonProperty("status")
	private String status;
}
