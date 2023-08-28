package org.egov.integration.web.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.egov.common.contract.request.RequestInfo;
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FeedbackSearchRequest {

    @JsonProperty("RequestInfo")
    private RequestInfo requestInfo;

}
