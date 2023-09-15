package org.egov.usm.web.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class USMNotification {
	
	@JsonProperty("topic")
	private String topic;
	
	@JsonProperty("message")
	private String message;
	
	@JsonProperty("id")
	private String id;
	 
	
	
}
