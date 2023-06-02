package org.egov.dx.web.models;

import java.util.LinkedHashMap;
import java.util.List;

import org.egov.common.contract.response.ResponseInfo;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class FileStoreUrlResponse {
	
	 @JsonProperty("fileStoreIds")
	 private List<LinkedHashMap<String, String>> filestoreIds;

}
