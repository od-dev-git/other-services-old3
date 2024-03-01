package org.egov.report.model;

import java.util.List;

import org.egov.common.contract.response.ResponseInfo;
import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class BillResponse {
	
	@JsonProperty("responseInfo")
	 ResponseInfo responseInfo;

	 @JsonProperty("Bill")
	 List<Bill> bills;

}
