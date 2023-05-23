package org.sujog.dba.scheduler;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.sujog.dba.service.SujogDbaService;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class SujogDbaController {
	
	@Autowired
	private SujogDbaService sujogDbaService;
	
	@Scheduled(cron = "${scheduled.cron.exp}")
	public void closeIdleConnectionScheduler() {
		log.info("Scheduler started: "+new Date());
		sujogDbaService.closeIdleConnection();
	}
}
