package org.egov.integration.model;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Boundary {

    @JsonProperty("name")
    private String name = null;

}
