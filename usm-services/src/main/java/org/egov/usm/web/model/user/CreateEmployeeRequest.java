package org.egov.usm.web.model.user;

import org.egov.common.contract.request.RequestInfo;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
@Builder
public class CreateEmployeeRequest {

	private RequestInfo requestInfo;

	@JsonProperty("user")
	private Employee employee;

}
