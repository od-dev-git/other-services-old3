package org.egov.report;

import java.util.TimeZone;

import org.egov.tracer.config.TracerConfiguration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootApplication
@ComponentScan(basePackages = { "org.egov.report" })
@EnableAutoConfiguration
@Import({ TracerConfiguration.class })
public class ReportServicesApplication {

	public static void main(String[] args) {
		SpringApplication.run(ReportServicesApplication.class, args);
	}
	
	@Value("${app.timezone}")
    private String timeZone;
	
//	@Bean
//	public ObjectMapper objectMapper() {
//		return new ObjectMapper().configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true)
//				.setDefaultPropertyInclusion(Include.NON_NULL)
//				.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES).setTimeZone(TimeZone.getTimeZone(timeZone));
//	}
}
