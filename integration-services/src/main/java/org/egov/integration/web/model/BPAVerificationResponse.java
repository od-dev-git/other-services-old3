package org.egov.integration.web.model;

import org.egov.integration.model.BPAVerification;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BPAVerificationResponse {

	@JsonProperty("bpaVerificationDetails")
	private BPAVerification bpaVerification;

}
