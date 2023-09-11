package org.egov.usm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class UrbanServiceMonitoringServicesApplication {

	public static void main(String[] args) {
		SpringApplication.run(UrbanServiceMonitoringServicesApplication.class, args);
	}

}
