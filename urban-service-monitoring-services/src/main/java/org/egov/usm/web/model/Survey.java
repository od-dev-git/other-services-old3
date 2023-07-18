package org.egov.usm.web.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Survey {

	@JsonProperty("id")
	private String id;
	
	@JsonProperty("tenantId")
	private String tenantId;
	
	@JsonProperty("title")
    private String title;
	
	@JsonProperty("status")
    private String status;

    @JsonProperty("description")
    private String description;
    
    @JsonProperty("startDate")
    private Long startDate;

    @JsonProperty("endDate")
    private Long endDate;
    
    @JsonProperty("postedBy")
    private String postedBy;
    
    @JsonProperty("collectCitizenInfo")
    private boolean collectCitizenInfo;

	@JsonProperty("auditDetails")
	private AuditDetails auditDetails;
	
	@JsonProperty("questionDetails")
	private List<QuestionDetail> questionDetails;
	
}
