package org.egov.report.config;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import org.egov.encryption.config.EncryptionConfiguration;
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
@Import({TracerConfiguration.class, EncryptionConfiguration.class})
public class ReportServiceConfiguration {

	@Autowired
	ObjectMapper objectMapper;
	
    @Value("${app.timezone}")
    private String timeZone;

    @PostConstruct
    public void initialize() {
        TimeZone.setDefault(TimeZone.getTimeZone(timeZone));
    }

    @Bean
    @Autowired
    @Lazy
    public MappingJackson2HttpMessageConverter jacksonConverter(ObjectMapper objectMapper) {
    MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
    converter.setObjectMapper(objectMapper);
    return converter;
    }
    
    @PostConstruct
    public void postConstruct() {
		objectMapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);
		objectMapper.setDefaultPropertyInclusion(Include.NON_NULL);
		objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES).setTimeZone(TimeZone.getTimeZone(timeZone));
    	
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
	
	@Value("${egov.ws.service.host}")
	private String wsHost;
	
	@Value("${egov.ws.search.path}")
	private String wsSearchEndpoint;

    @Value("${egov.pt.service.host}")
	private String ptHost;
	
	@Value("${egov.pt.search.path}")
	private String ptSearchEndpoint;
	
	@Value("${egov.report.service.limit}")
	private Integer reportLimit;

	@Value("${egov.report.service.connections.limit}")
    private Integer reportConnectionsLimit;
	
	
	@Value("${egov.filestore.host}")
	private String filestoreHost;
	
	@Value("${egov.filestore.path}")
	private String filestorePath;
	
	@Value("${egov.filestore.fetch.path}")
	private String filestoreFetchPath;
	
	@Value("${egov.filestore.upload.path}")
	private String filestoreUploadPath;
	
	@Value("${egov.mdms.host}")
	private String mdmsHost;
	
	@Value("${egov.mdms.search.endpoint}")
	private String mdmsSearchUrl;

	@Value("${persister.save.utilityreport.topic}")
	private String saveUtilityReportTopic;

	@Value("${persister.update.utilityreport.topic}")
	private String updateUtilityReportTopic;
	
	@Value("${egov.report.temp.location}")
	private String reportTemporaryLocation;
	
	@Value("${egov.billing.host}")
	private String billingHost;
	
	@Value("${egov.billing.fetchbill.endpoint}")
	private String fetchBillEndpoint;
	
	@Value("${egov.pg.host}")
	private String pgServiceHost;
	
	@Value("${egov.pg.updatetransaction.endpoint}")
	private String updateTransactionEndpoint;
	
	@Value("${dcb.report.request.gap}")
	private Long requestGap;
}