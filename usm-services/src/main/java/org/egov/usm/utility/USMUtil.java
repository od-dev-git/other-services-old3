package org.egov.usm.utility;

import java.util.UUID;

import org.egov.usm.web.model.AuditDetails;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class USMUtil {
	
	/**
	 * Method to return auditDetails for create/update flows
	 *
	 * @param by
	 * @param isCreate
	 * @return AuditDetails
	 */
	public static AuditDetails getAuditDetails(String by, Boolean isCreate) {
		Long time = System.currentTimeMillis();
		log.info("Audit user by :", by);
		if(isCreate)
			return AuditDetails.builder().createdBy(by).lastModifiedBy(by).createdTime(time).lastModifiedTime(time).build();
		else
			return AuditDetails.builder().lastModifiedBy(by).lastModifiedTime(time).build();
	}

	
	/**
	 * To generate UUID
	 * @return generated UUID
	 */
	public static String generateUUID () {
		return UUID.randomUUID().toString();
	}
	
	
}
