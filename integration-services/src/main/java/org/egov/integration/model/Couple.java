package org.egov.integration.model;

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
public class Couple {

	@JsonProperty("bride")
	private CoupleDetails bride ;
	
	@JsonProperty("groom")
	private CoupleDetails groom ;
}
