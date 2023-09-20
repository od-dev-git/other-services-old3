package org.egov.usm.web.model;

import org.egov.common.contract.request.RequestInfo;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class NotificationRequest {
	
	private RequestInfo requestInfo;

	private USMNotification notification;
	
	private MulticastNotification multicastNotification;
	
}
