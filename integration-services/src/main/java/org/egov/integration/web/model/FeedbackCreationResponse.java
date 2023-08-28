package org.egov.integration.web.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.egov.common.contract.request.RequestInfo;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FeedbackCreationResponse {

    @JsonProperty("RequestInfo")
    private RequestInfo requestInfo;

    @JsonProperty("Feedback")
    private Feedback feedback;
}
