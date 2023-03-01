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
public class OwnerInfo {
	
	@JsonProperty("userName")
	private String name;
	
	@JsonProperty("correspondenceAddress")
	private String correspondenceAddress;
	
}
