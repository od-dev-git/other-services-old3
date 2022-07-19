package org.egov.report;

import org.egov.tracer.config.TracerConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@ComponentScan(basePackages = { "org.egov.report" })
@EnableAutoConfiguration
@Import({ TracerConfiguration.class })
public class ReportServicesApplication {

	public static void main(String[] args) {
		SpringApplication.run(ReportServicesApplication.class, args);
	}
}
