package org.egov.jobscheduler;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class JobschedulerServicesApplication {

	public static void main(String[] args) {
		SpringApplication.run(JobschedulerServicesApplication.class, args);
	}

}
