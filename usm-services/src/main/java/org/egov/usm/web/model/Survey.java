package org.egov.usm.web.model;

import java.util.ArrayList;
import java.util.List;

import org.egov.usm.model.enums.Status;

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
	
	@JsonProperty("tenant")
	private String tenant;
	
	@JsonProperty("title")
    private String title;
	
	@JsonProperty("status")
	private Status status;

    @JsonProperty("description")
    private String description;
    
    @JsonProperty("startDate")
    private Long startDate;

    @JsonProperty("endDate")
    private Long endDate;
    
    @JsonProperty("startTime")
    private String startTime;

    @JsonProperty("endTime")
    private String endTime;
    
    @JsonProperty("postedBy")
    private String postedBy;
    
    @JsonProperty("collectCitizenInfo")
    private boolean collectCitizenInfo;

	@JsonProperty("auditDetails")
	private AuditDetails auditDetails;
	
	@JsonProperty("questionDetails")
	private List<QuestionDetail> questionDetails;

	public Survey addQuestionsItem(QuestionDetail questionDetail) {
		if (this.questionDetails == null) {
            this.questionDetails = new ArrayList<>();
        }

        if (null != questionDetail)
            this.questionDetails.add(questionDetail);
        return this;
	}
}
