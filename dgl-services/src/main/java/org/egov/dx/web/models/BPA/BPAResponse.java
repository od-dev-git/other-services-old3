package org.egov.dx.web.models.BPA;

import java.util.List;

import org.egov.common.contract.response.ResponseInfo;
import org.springframework.validation.annotation.Validated;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Validated
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class BPAResponse {
	
	@JsonProperty("ResponseInfo")
	  private ResponseInfo responseInfo;

	  @JsonProperty("BPA")
	  private List<BPA> BPA;

}
