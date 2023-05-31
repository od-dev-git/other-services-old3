package org.egov.dx.web.models.MR;

import java.util.List;
import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MRSearchCriteria {
	
	private String tenantId;

	private String status;

	private List<String> ids;

	private String applicationNumber;

	private List<String> mrNumbers;

	private String mobileNumber;

	private String accountId;

	private String ownerId;

	private String applicationType;

	private Long fromDate = null;

	private Long toDate = null;

	private String businessService = null;

	private String employeeUuid;

	private Integer offset;

	private Integer limit;

	private Boolean isTatkalApplication;

	private Boolean isInworkflow;

}
