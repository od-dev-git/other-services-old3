package org.egov.integration.config;

import org.egov.tracer.config.TracerConfiguration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Import({ TracerConfiguration.class })
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Component
public class IntegrationConfiguration {

	@Value("${egov.is.default.limit}")
	private Integer defaultLimit;

	@Value("${egov.is.default.offset}")
	private Integer defaultOffset;

	@Value("${egov.is.max.limit}")
	private Integer maxSearchLimit;
	
	@Value("${egov.integration.revenue.notification.save.topic}")
	private String saveRevenueNotification;
	
	@Value("${egov.mdms.host}")
    private String mdmsHost;
	
	@Value("${egov.mdms.search.endpoint}")
    private String mdmsEndpoint;
	
	@Value("${egov.integration.revenue.notification.update.topic}")
	private String updateRevenueNotification;
	
	@Value("${egov.ws.service.host}")
	private String wsHost;
	
	@Value("${egov.ws.search.path}")
	private String wsSearchEndpoint;
	
	@Value("${daily.reconciliation.user.uuid}")
    private String dailyReconciliationUserUuid;
    
	@Value("${daily.reconciliation.user.type}")
    private String dailyReconciliationUserType;

}
