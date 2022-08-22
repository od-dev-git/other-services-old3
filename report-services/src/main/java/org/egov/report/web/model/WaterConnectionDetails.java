package org.egov.report.web.model;

import java.math.BigDecimal;

import org.egov.report.model.DemandDetails;
import org.egov.report.model.DemandDetails.DemandDetailsBuilder;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WaterConnectionDetails {
	
	@JsonProperty("tenantid")
	private String tenantid;

	@JsonProperty("ward")
	private String ward;

	@JsonProperty("connectiontype")
	private String connectiontype;

	@JsonProperty("userid")
	private String userid;

	@JsonProperty("oldconnectionno")
	private String oldconnectionno;

	@JsonProperty("userName")
	private String userName;

	@JsonProperty("userAddress")
	private String userAddress;

	@JsonProperty("mobile")
	private String mobile;

}
