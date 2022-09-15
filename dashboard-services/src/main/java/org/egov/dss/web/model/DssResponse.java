package org.egov.dss.web.model;

import org.egov.common.contract.response.ResponseInfo;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class DssResponse {

	@JsonProperty("ResponseInfo")
    private ResponseInfo responseInfo;
	
	@JsonProperty("responseData")
	private ResponseData responseData;
}
