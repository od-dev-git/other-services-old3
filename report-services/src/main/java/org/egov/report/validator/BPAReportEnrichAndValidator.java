package org.egov.report.validator;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang3.ObjectUtils;
import org.egov.common.contract.request.RequestInfo;
import org.egov.report.model.AuditDetails;
import org.egov.report.model.UtilityReportDetails;
import org.egov.report.util.Util;
import org.egov.report.web.model.UtilityReportRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BPAReportEnrichAndValidator {
	
	@Autowired
	private Util util;

	
	public UtilityReportDetails enrichSaveReport(RequestInfo requestInfo, String reportType) {
		AuditDetails auditDetails = util.getAuditDetails(requestInfo.getUserInfo().getUuid(), true);
		
		UtilityReportDetails reportDetails = UtilityReportDetails.builder()
				.id(UUID.randomUUID().toString())
				.tenantId("od")
				.reportType(reportType)
				.fileStoreId("")
				.fileName("")
				.auditDetails(auditDetails)
				.build();
		
		return reportDetails;
	}


	public UtilityReportRequest enrichUpdateReport(RequestInfo requestInfo, List<UtilityReportDetails> reportList, String fileName, Object filestoreDetails) {
		UtilityReportDetails reportDetails = reportList.get(0);
		
		String filestoreId = getFilestoreIdfromObject(filestoreDetails);
		
		AuditDetails auditDetails = util.getAuditDetails(requestInfo.getUserInfo().getUuid(), false);
		auditDetails.setCreatedBy(reportDetails.getAuditDetails().getCreatedBy());
		auditDetails.setCreatedTime(reportDetails.getAuditDetails().getCreatedTime());
		
		reportDetails.setFileStoreId(filestoreId);
		reportDetails.setFileName(fileName);
		reportDetails.setAuditDetails(auditDetails);
		
		return new UtilityReportRequest(requestInfo, reportDetails);
	}


	@SuppressWarnings("unchecked")
	private String getFilestoreIdfromObject(Object filestoreDetails) {
		String filestoreId = "";
		if(ObjectUtils.isNotEmpty(filestoreDetails)) {
			Object listObject = ((Map<String, Object>)filestoreDetails).get("files");
			List<Map<String, Object>> fileDetailsList =  (List<Map<String, Object>>)listObject;
			if(!fileDetailsList.isEmpty()) {
				filestoreId = String.valueOf(fileDetailsList.get(0).get("fileStoreId"));
			}
		}
		return filestoreId;
	}
	
}

