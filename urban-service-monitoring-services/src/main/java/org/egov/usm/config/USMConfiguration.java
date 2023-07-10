package org.egov.usm.config;

import org.egov.tracer.config.TracerConfiguration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Import({TracerConfiguration.class})
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Component
public class USMConfiguration {
	
	//Idgen Config
    @Value("${egov.idgen.host}")
    private String idGenHost;

    @Value("${egov.idgen.path}")
    private String idGenPath;

    @Value("${egov.idgen.usm.surveyno.name}")
    private String surveyNoIdgenName;

    @Value("${egov.idgen.usm.surveyno.format}")
    private String surveyNoIdgenFormat;

    @Value("${egov.idgen.usm.ticketNo.name}")
    private String ticketNoIdgenName;

    @Value("${egov.idgen.usm.ticketNo.format}")
    private String ticketNoIdgenFormat;

}
