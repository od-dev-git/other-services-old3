package org.egov.jobscheduler;

import org.egov.tracer.config.TracerConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@EnableAutoConfiguration
@Import({ TracerConfiguration.class })
public class JobschedulerServicesApplication {

	public static void main(String[] args) {
		SpringApplication.run(JobschedulerServicesApplication.class, args);
	}

}
