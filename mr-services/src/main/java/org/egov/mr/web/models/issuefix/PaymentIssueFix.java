package org.egov.mr.web.models.issuefix;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Builder;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentIssueFix {
	
	private String tenantId;

	private String applicationNumber;

	private String businessService;

}
