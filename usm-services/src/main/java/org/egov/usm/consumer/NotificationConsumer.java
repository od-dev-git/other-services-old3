package org.egov.usm.consumer;

import java.util.HashMap;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

@Component
public class NotificationConsumer {

	@KafkaListener(topics = {"${persister.create.survey.topic}","{persister.update.survey.topic}","${persister.delete.survey.topic}","{persister.save.submit.survey.topic}","${persister.update.submit.survey.topic}","{persister.delete.submit.survey.topic}","${persister.create.ticket.topic}","${persister.update.ticket.topic}"})
	public void listen(final HashMap<String, Object> record, @Header(KafkaHeaders.RECEIVED_TOPIC) String topic) {
		
	}
}
