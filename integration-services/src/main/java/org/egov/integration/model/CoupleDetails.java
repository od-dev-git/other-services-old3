package org.egov.integration.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CoupleDetails {
	
    @JsonProperty("isGroom")
    private Boolean isGroom;
    
    @JsonProperty("title")
    private String title;
    
    @JsonProperty("firstName")
    private String firstName;
    
    @JsonProperty("address")
    private Address address = null ;
}
