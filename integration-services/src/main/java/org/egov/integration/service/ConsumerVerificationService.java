package org.egov.integration.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.request.User;
import org.egov.integration.config.IntegrationConfiguration;
import org.egov.integration.model.ConsumerVerification;
import org.egov.integration.model.ConsumerVerificationSearchCriteria;
import org.egov.integration.model.ConsumerVerificationServiceResponse;
import org.egov.integration.model.OwnerInfo;
import org.egov.integration.model.VerificationOwner;
import org.egov.integration.web.model.ConsumerVerificationResponse;
import org.egov.integration.web.model.RequestInfoWrapper;
import org.egov.integration.web.model.WaterConnectionDetailResponse;
import org.egov.integration.model.WSConnection;
import org.egov.integration.repository.ServiceRepository;
import org.egov.integration.validator.ConsumerVerificationValidator;
import org.egov.tracer.model.CustomException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ConsumerVerificationService implements InitializingBean{
	
	private static final RequestInfo requestInfo;
	
    static {
        User userInfo = User.builder()
                .uuid("DAILY_RECONC_JOB")
                .type("SYSTEM")
                .roles(Collections.emptyList()).id(0L).build();

        requestInfo = new RequestInfo("", "", 0L, "", "", "", "", "", "", userInfo);
    }
    
	
	@Autowired
	private IntegrationConfiguration configuration;
	
	@Autowired
    private ServiceRepository repository;
	
	@Autowired
	private ObjectMapper mapper;
	
	@Autowired
	private ConsumerVerificationValidator consumerVerificationValidator;
	
	
    @Override
	public void afterPropertiesSet() throws Exception {

    	User userInfo = User.builder()
                .uuid(configuration.getConsumerVerificationUserUuid())
                .type(configuration.getConsumerVerificationUserType())
                .roles(Collections.emptyList()).id(0L).build();

        requestInfo.setUserInfo(userInfo);
	}
	
	public ConsumerVerificationServiceResponse search(ConsumerVerificationSearchCriteria searchCriteria) {
		
	consumerVerificationValidator.validateSearch(searchCriteria);

	ConsumerVerification response = ConsumerVerification.builder()
			.consumerNo(null)
			.consumerVerificationOwner(null)
			.tenantId(null)
			.status(null)
			.build(); 	 	
	OwnerInfo ownerinfo = OwnerInfo.builder()
		.name("Amitabh").correspondenceAddress("Rajori Garden , Lal Chowk").build();
    String businessService = searchCriteria.getBusinessService();
    
    switch(businessService){
	case "BPA":
		response = ConsumerVerification.builder().consumerNo("DCR72021ISSU0").businessService(businessService).consumerVerificationOwner(Arrays.asList(ownerinfo))
				.tenantId("Cuttack").status("INACTIVE").build();
		break;
    case "MR" :
    	response = ConsumerVerification.builder().consumerNo("MR/CTC/000011").businessService(businessService).consumerVerificationOwner(null)
		.tenantId("Cuttack").status("ACTIVE").build();
    	break;
    case "TL" :
    	OwnerInfo ownerinfotl = OwnerInfo.builder()
		.name("Shahrukh").correspondenceAddress("Shop No 68 ,Near Udyog Vihar ").build();
    	response = ConsumerVerification.builder().consumerNo("TL/CTC/000219").businessService(businessService).consumerVerificationOwner(Arrays.asList(ownerinfotl))
		.tenantId("Cuttack").status("INACTIVE").build();
    	break;
    	
    case "WS" :
		getWSResponse(searchCriteria, response);
    	break;
    	
    case "PT" :
    	OwnerInfo ownerinfotpt = OwnerInfo.builder()
		.name("Charan").correspondenceAddress("29-d , Arya Nagar , Sundargarh Colony ").build();
    	response = ConsumerVerification.builder().consumerNo("PT-SNG-689867").businessService(businessService).consumerVerificationOwner(Arrays.asList(ownerinfotpt))
		.tenantId("Sundargarh").status("INACTIVE").build();
    	
    
    }
    
    if(!StringUtils.hasText(response.getConsumerNo()))
    	return  ConsumerVerificationServiceResponse.builder().build();
    
    ConsumerVerificationServiceResponse finalResponse = ConsumerVerificationServiceResponse.builder()
    		.consumerNo(response.getConsumerNo())
    		.businessService(response.getBusinessService())
    		.tenantId(response.getTenantId())
    		.status(response.getStatus()).build();
    
    List<VerificationOwner> owners = new ArrayList<>();
	response.getConsumerVerificationOwner().stream().forEach(item -> {
		VerificationOwner owner = VerificationOwner.builder()
				.name(item.getName())
				.address(item.getCorrespondenceAddress()).build();
		
		owners.add(owner);
	});
	finalResponse.setVerificationOwner(owners);

		return finalResponse;
	}

	private void getWSResponse(ConsumerVerificationSearchCriteria searchCriteria, ConsumerVerification response) {
		List<WSConnection> wsConnections = getWaterConnections(searchCriteria);
		WSConnection connectionResponse=null;
		if(!CollectionUtils.isEmpty(wsConnections)) {
			connectionResponse = wsConnections.get(0);
		} 
		
			setWSResponseInfo(response, connectionResponse);
	}

	private List<WSConnection> getWaterConnections(ConsumerVerificationSearchCriteria searchCriteria) {
		StringBuilder uri = new StringBuilder(configuration.getWsHost())
				.append(configuration.getWsSearchEndpoint()).append("?")
				.append("tenantId="+searchCriteria.getTenantId()).append("&")
				.append("searchType=CONNECTION").append("&")
				.append("connectionNumber="+searchCriteria.getConsumerNo());
		
		List<WSConnection> wsConnections = new ArrayList<>();
		RequestInfoWrapper requestWrapper = RequestInfoWrapper.builder()
				.requestInfo(requestInfo).build();
		try {
		Object fetchResponse = repository.fetchResult(uri, requestWrapper);
		log.info("Water response: ", fetchResponse);
		
		WaterConnectionDetailResponse res = mapper.convertValue(fetchResponse, WaterConnectionDetailResponse.class);
		log.info("Water response: " + String.valueOf(res));
		wsConnections.addAll(res.getConnections());
		}catch(Exception ex) {
			log.error("External Service Call Erorr", ex);
			throw new CustomException("WS_CONNECTION FETCH ERROR", "Unable to fetch WS Connection Information");
		}
		return wsConnections;
	}

	private void setWSResponseInfo(ConsumerVerification response, WSConnection connectionResponse) {
		if(connectionResponse != null) {
			response.setTenantId(connectionResponse.getTenantId());
			response.setConsumerNo(connectionResponse.getConnectionNo());
			response.setStatus(connectionResponse.getApplicationStatus());
			List<OwnerInfo> owners = new ArrayList<>();
			
			
			connectionResponse.getConnectionHolders().stream().forEach(item -> {
				OwnerInfo owner = OwnerInfo.builder()
						.name(item.getName())
						.correspondenceAddress(item.getCorrespondenceAddress()).build();
				owners.add(owner);
			});
			response.setConsumerVerificationOwner(owners);	
			
		}
	}

}
