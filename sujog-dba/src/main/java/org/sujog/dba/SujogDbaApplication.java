package org.sujog.dba;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class SujogDbaApplication {

	public static void main(String[] args) {
		SpringApplication.run(SujogDbaApplication.class, args);
	}

}
