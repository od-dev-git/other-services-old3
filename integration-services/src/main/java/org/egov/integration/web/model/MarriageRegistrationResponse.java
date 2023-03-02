package org.egov.integration.web.model;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.egov.common.contract.response.ResponseInfo;
import org.egov.integration.model.MarriageRegistration;

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
public class MarriageRegistrationResponse {

	 @JsonProperty("ResponseInfo")
     private ResponseInfo responseInfo = null;

     @JsonProperty("MarriageRegistrations")
     @Valid
     private List<MarriageRegistration> marriageRegistrations = null;


     public MarriageRegistrationResponse addMarriageRegistrationsItem(MarriageRegistration marriageRegistrationItem) {
         if (this.marriageRegistrations == null) {
         this.marriageRegistrations = new ArrayList<>();
         }
     this.marriageRegistrations.add(marriageRegistrationItem);
     return this;
     }
}
