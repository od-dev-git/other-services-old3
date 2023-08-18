package org.egov.usm.consumer;

import java.util.HashMap;

import org.egov.usm.service.notification.SurveyNotificationService;
import org.egov.usm.web.model.SurveyDetailsRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class SurveyNotificationConsumer {

	@Autowired
	private SurveyNotificationService surveyNotificationService;
	
	@Autowired
	private ObjectMapper mapper;
	
	
	@KafkaListener(topics = {"${persister.save.submit.survey.topic}","${persister.update.submit.survey.topic}"})
	public void listenSurveySubmitted(final HashMap<String, Object> record, @Header(KafkaHeaders.RECEIVED_TOPIC) String topic) {
		SurveyDetailsRequest surveyDetailsRequest = new SurveyDetailsRequest();
        try {
        	surveyDetailsRequest = mapper.convertValue(record, SurveyDetailsRequest.class);
        } catch (final Exception e) {
            log.error("Error while listening to value: " + record + " on topic: " + topic + ": " + e);
        }
       
        surveyNotificationService.processNotification(surveyDetailsRequest);
	}
}
