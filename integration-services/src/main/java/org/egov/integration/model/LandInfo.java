package org.egov.integration.model;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class LandInfo {
	
	  @JsonProperty("address")
	  private Address address = null;

	  @JsonProperty("owners")
	  private List<OwnerInfo> owners = new ArrayList<OwnerInfo>();

}
