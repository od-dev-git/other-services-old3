package org.egov.integration.model;

import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ConsumerVerificationOwner {

	@JsonProperty("ownername")
	private String ownerName;
	
	@JsonProperty("address")
	private String address; 
	
}
