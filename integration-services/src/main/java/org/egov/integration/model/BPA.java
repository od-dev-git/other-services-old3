package org.egov.integration.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class BPA {

	  @JsonProperty("applicationNo")
	  private String applicationNo = null;

	  @JsonProperty("tenantId")
	  private String tenantId = null;
	  
	  @JsonProperty("status")
	  private String status = null;

	  @JsonProperty("landInfo")
	  private LandInfo landInfo = null;
	  
	  @JsonProperty("approvalNo")
	  private String approvalNo = null;
	  
	  @JsonProperty("approvalDate")
	  private Long approvalDate = null;
	  
	  @JsonProperty("permitExpiryDate")
	  private Long permitExpiryDate = null;

}
