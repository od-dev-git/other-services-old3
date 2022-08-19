package org.egov.report.web.model;

import java.util.List;

import org.egov.common.contract.response.ResponseInfo;
import org.egov.report.model.Property;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class PropertyResponse {

	@JsonProperty("responseInfo")
	 ResponseInfo responseInfo;

	 @JsonProperty("Properties")
	 List<Property> propInfo;
}
