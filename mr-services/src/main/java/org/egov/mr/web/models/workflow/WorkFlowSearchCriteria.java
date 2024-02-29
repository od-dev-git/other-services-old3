package org.egov.mr.web.models.workflow;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WorkFlowSearchCriteria {
	
	@JsonProperty("tenantId")
	private String tenantId=null;
	
	@JsonProperty("id")
	private String id=null;
	
	@JsonProperty("businessId")
	private String businessId=null;

}
