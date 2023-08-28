package org.egov.integration.web.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.springframework.util.StringUtils;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FeedbackSearchCriteria {

    @JsonProperty("tenantId")
    private String tenantId;

    @JsonProperty("module")
    private String module;

    @JsonProperty("submittedBy")
    private String submittedBy;

    @JsonProperty("limit")
    private Integer limit;

    @JsonProperty("offset")
    private Integer offset;


    public boolean isEmpty(){
        return StringUtils.isEmpty(this.tenantId) && StringUtils.isEmpty(this.module)
                && StringUtils.isEmpty(this.submittedBy);
    }

}
