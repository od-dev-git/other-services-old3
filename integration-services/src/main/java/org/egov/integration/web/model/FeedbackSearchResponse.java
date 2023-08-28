package org.egov.integration.web.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.egov.common.contract.request.RequestInfo;

import java.util.List;
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FeedbackSearchResponse {

    @JsonProperty("RequestInfo")
    private RequestInfo requestInfo;

    @JsonProperty("Feedbacks")
    private List<Feedback> feedbacks;

}
