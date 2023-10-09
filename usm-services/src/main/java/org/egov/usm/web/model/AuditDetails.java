package org.egov.usm.web.model;

import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Builder.Default;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuditDetails   {

		@Default
        @Size(max=64)
        @JsonProperty("createdBy")
        private String createdBy = null;

        @Default
        @Size(max=64)
        @JsonProperty("lastModifiedBy")
        private String lastModifiedBy = null;

        @Default
        @JsonProperty("createdTime")
        private Long createdTime = null;

        @Default
        @JsonProperty("lastModifiedTime")
        private Long lastModifiedTime = null;


}

