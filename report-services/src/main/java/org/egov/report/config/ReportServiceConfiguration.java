package org.egov.report.config;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import org.egov.tracer.config.TracerConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.TimeZone;


@Component
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Import({TracerConfiguration.class})
public class ReportServiceConfiguration {

    @Value("${app.timezone}")
    private String timeZone;

    @PostConstruct
    public void initialize() {
        TimeZone.setDefault(TimeZone.getTimeZone(timeZone));
    }

    @Bean
	public ObjectMapper objectMapper() {
		return new ObjectMapper().configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true)
				.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES).setTimeZone(TimeZone.getTimeZone(timeZone));
	}

    @Bean
    @Autowired
    @Lazy
    public MappingJackson2HttpMessageConverter jacksonConverter(ObjectMapper objectMapper) {
    MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
    converter.setObjectMapper(objectMapper);
    return converter;
    }
    
    /** Properties data */
    
    @Value("${egov.collection.host}")
    private String collectionHost;
    
    @Value("${egov.collection.search.endpoint}")
    private String collectionSearchEndPoint;
    
    @Value("${egov.user.host}")
	private String userHost;

	@Value("${egov.user.search.path}")
	private String userSearchEndpoint;

}