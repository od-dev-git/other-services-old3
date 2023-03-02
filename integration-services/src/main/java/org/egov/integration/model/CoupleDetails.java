package org.egov.integration.model;

import javax.validation.Valid;
import javax.validation.constraints.Size;
import org.springframework.validation.annotation.Validated;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Validated
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CoupleDetails {
	
    @JsonProperty("isGroom")
    private Boolean isGroom;
    
    @Size(max=64)
    @JsonProperty("title")
    private String title;
    
    @Size(max=64)
    @JsonProperty("firstName")
    private String firstName;
    
    @JsonProperty("address")
    @Valid
    private Address address = null ;
}
