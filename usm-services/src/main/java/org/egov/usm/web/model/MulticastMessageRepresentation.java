package org.egov.usm.web.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MulticastMessageRepresentation {

	@JsonProperty("data")
	private String data;
	
	@JsonProperty("registrationTokens")
	private List<String> registrationTokens;
	
}
