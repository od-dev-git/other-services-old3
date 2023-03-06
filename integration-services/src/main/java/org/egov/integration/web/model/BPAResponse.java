package org.egov.integration.web.model;

import java.util.List;

import org.egov.common.contract.response.ResponseInfo;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.egov.integration.model.BPA;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
