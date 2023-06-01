package org.egov.dx.web.models.TL;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.Size;


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
public class Boundary {
	
	@Size(max=64)
    @JsonProperty("code")
    private String code = null;

    @JsonProperty("name")
    private String name = null;

    @JsonProperty("label")
    private String label = null;

    @JsonProperty("latitude")
    private String latitude = null;

    @JsonProperty("longitude")
    private String longitude = null;

    @JsonProperty("children")
    @Valid
    private List<Boundary> children = null;

    @JsonProperty("materializedPath")
    private String materializedPath = null;


    public Boundary addChildrenItem(Boundary childrenItem) {
        if (this.children == null) {
        this.children = new ArrayList<>();
        }
    this.children.add(childrenItem);
    return this;
    }

}
