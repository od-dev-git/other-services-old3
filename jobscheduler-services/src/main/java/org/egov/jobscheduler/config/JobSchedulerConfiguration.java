package org.egov.jobscheduler.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Component
@Getter
@Setter
public class JobSchedulerConfiguration {


    //MDMS
    @Value("${egov.mdms.host}")
    private String mdmsHost;

    @Value("${egov.mdms.search.endpoint}")
    private String mdmsEndPoint;
    
    @Value("${spring.task.scheduling.role.code}")
    private String schedulingRoleCode;
    
    @Value("${spring.task.scheduling.role.tenantid}")
    private String schedulingRoleTenantId;
    
    @Value("${spring.task.scheduling.user.uuid}")
    private String schedulingUserUUID;
    
    @Value("${spring.task.scheduling.user.type}")
    private String schedulingUserType;
    

    @Value("${spring.scheduler.enabled}")
    private boolean isSchedulerEnabled;

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        // Do any additional configuration here
        return builder.build();
    }
}
