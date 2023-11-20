package org.egov.usm.utility;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.egov.usm.repository.SurveyTicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class ScheduledTasks {

	@Autowired
	private SurveyTicketRepository repository;

	@Scheduled(cron = "0 0 0 * * *")
	public void updateDailyTickets() {
		log.info("The time is now {}", new SimpleDateFormat("HH:mm:ss").format(new Date()));
		repository.updateDailyTickets();
	}
}
