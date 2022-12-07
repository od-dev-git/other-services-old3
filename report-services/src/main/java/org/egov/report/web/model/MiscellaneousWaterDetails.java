package org.egov.report.web.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MiscellaneousWaterDetails {
    
    @JsonProperty("ward")
    private String ward;
    
    @JsonProperty("oldconnectionno")
    private String oldconnectionno;

}
