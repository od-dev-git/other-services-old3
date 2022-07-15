package org.egov.report.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Bill {
	
	private String consumerCode;
	
	private String billNumber;
	
	private Long billDate;
	
	private List<BillDetail> billDetails;
}
