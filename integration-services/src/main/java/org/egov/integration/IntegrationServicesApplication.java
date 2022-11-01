package org.egov.integration;

import org.egov.tracer.config.TracerConfiguration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import({ TracerConfiguration.class })
public class IntegrationServicesApplication {
	
	public static void main(String[] args) {
		SpringApplication.run(IntegrationServicesApplication.class, args);
	}

}
