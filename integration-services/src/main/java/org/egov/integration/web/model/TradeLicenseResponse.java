package org.egov.integration.web.model;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.egov.common.contract.response.ResponseInfo;
import org.egov.integration.model.TradeLicense;

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
public class TradeLicenseResponse {
	
	 @JsonProperty("ResponseInfo")
     private ResponseInfo responseInfo = null;

     @JsonProperty("Licenses")
     private List<TradeLicense> licenses = null;


     public TradeLicenseResponse addLicensesItem(TradeLicense licensesItem) {
         if (this.licenses == null) {
         this.licenses = new ArrayList<>();
         }
     this.licenses.add(licensesItem);
     return this;
     }

}
