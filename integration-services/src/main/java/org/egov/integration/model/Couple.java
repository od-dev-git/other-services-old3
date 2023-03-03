package org.egov.integration.model;

import javax.validation.Valid;
import javax.validation.constraints.Size;
import org.springframework.validation.annotation.Validated;

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
