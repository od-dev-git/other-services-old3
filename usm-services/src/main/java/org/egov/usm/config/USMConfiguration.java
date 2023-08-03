package org.egov.usm.config;

import org.egov.tracer.config.TracerConfiguration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Import({TracerConfiguration.class})
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Component
public class USMConfiguration {
	
	//Idgen Config
    @Value("${egov.idgen.host}")
    private String idGenHost;

    @Value("${egov.idgen.path}")
    private String idGenPath;

    @Value("${egov.idgen.usm.surveyno.name}")
    private String surveyNoIdgenName;

    @Value("${egov.idgen.usm.surveyno.format}")
    private String surveyNoIdgenFormat;

    @Value("${egov.idgen.usm.ticketNo.name}")
    private String ticketNoIdgenName;

    @Value("${egov.idgen.usm.ticketNo.format}")
    private String ticketNoIdgenFormat;
    
    //Persister Config
    
    @Value("${persister.create.survey.topic}")
    private String createSurveyTopic;
    
    @Value("${persister.update.survey.topic}")
    private String updateSurveyTopic;
    
    @Value("${persister.delete.survey.topic}")
    private String deleteSurveyTopic;
    
    
    @Value("${persister.save.submit.survey.topic}")
    private String saveSubmitSurveyTopic;
    
    @Value("${persister.update.submit.survey.topic}")
    private String updateSubmitSurveyTopic;
    
    @Value("${persister.delete.submit.survey.topic}")
    private String deleteSubmitSurveyTopic;
    

    @Value("${persister.create.ticket.topic}")
    private String createTicketTopic;

    @Value("${persister.update.ticket.topic}")
    private String updateTicketTopic;
    
    
    @Value("${persister.create.lookup.topic}")
    private String createQuestionLookupTopic;

    @Value("${persister.update.lookup.topic}")
    private String updateQuestionLookupTopic;


}
