package org.egov.dx.web.models;

import java.util.List;

import javax.validation.constraints.NotNull;

import org.egov.common.contract.response.ResponseInfo;
import org.egov.dx.web.models.MarriageRegistration.ApplicationTypeEnum;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Setter
@Getter
public class MarriageResponse {
	
	@JsonProperty("ResponseInfo")
	private ResponseInfo responseInfo;
	
	@JsonProperty("MarriageRegistrations")
	private List<MarriageRegistration> marriageRegistrations;

}
