package org.egov.integration.web.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.egov.common.contract.response.ResponseInfo;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FeedbackCreationResponse {

    @JsonProperty("ResponseInfo")
    private ResponseInfo responseInfo;

    @JsonProperty("Feedback")
    private Feedback feedback;
}
