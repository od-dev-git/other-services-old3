package org.egov.report.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.egov.common.contract.request.RequestInfo;
import org.egov.report.service.DemandService;
import org.egov.report.repository.DemandRepository;
import org.egov.report.util.DemandEnrichmentUtil;
//import org.egov.report.repository.ServiceRequestRepository;
import org.egov.report.util.Util;
import org.egov.report.validator.DemandValidatorV1;
//import org.egov.demand.web.contract.User;
//import org.egov.demand.web.contract.UserResponse;
//import org.egov.demand.web.contract.UserSearchRequest;
import org.egov.report.repository.DemandRepository;
import org.egov.report.config.DemandServiceConfiguration;
import org.egov.report.config.ReportServiceConfiguration;
import org.egov.report.model.Demand;
import org.egov.report.model.UserSearchRequest;
import org.egov.report.repository.ServiceRepository;
import org.egov.report.web.contract.User;
import org.egov.report.web.contract.UserResponse;
import org.egov.report.web.model.DemandCriteria;
//import org.egov.report.web.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class DemandService {
    
    @Autowired
    private Util util;

    @Autowired
    private DemandValidatorV1 demandValidatorV1;
    
    @Autowired
    private ReportServiceConfiguration applicationProperties;
    
    @Autowired
    private ServiceRepository serviceRequestRepository;
    
    @Autowired
    private DemandRepository demandRepository;
    
    @Autowired
    private ObjectMapper mapper;
    
    @Autowired
    private DemandEnrichmentUtil demandEnrichmentUtil;
    

//    @Value("${kafka.topic.ws.installment.update}")
//    private String wsInstallmentUpdateTopic;

    public List<Demand> getDemands(DemandCriteria demandCriteria, RequestInfo requestInfo) {

        demandValidatorV1.validateDemandCriteria(demandCriteria, requestInfo);

        UserSearchRequest userSearchRequest = null;
        List<User> payers = null;
        List<Demand> demands = null;
        
//        String userUri = applicationProperties.getUserServiceHostName()
//                .concat(applicationProperties.getUserServiceSearchPath());
        
//        String userUri = applicationProperties.getUserHost()
//                .concat(applicationProperties.getUserSearchEndpoint());
        
        /*
         * user type is CITIZEN by default because only citizen can have demand or payer can be null
         */
        String citizenTenantId = demandCriteria.getTenantId().split("\\.")[0];
        
        /*
         * If payer related data is provided first then user search has to be made first followed by demand search
         */
        if (demandCriteria.getEmail() != null || demandCriteria.getMobileNumber() != null) {
            
//            userSearchRequest = UserSearchRequest.builder().requestInfo(requestInfo)
//                    .tenantId(citizenTenantId).emailId(demandCriteria.getEmail())
//                    .mobileNumber(demandCriteria.getMobileNumber()).build();
//            
//            StringBuilder userUrl = new StringBuilder(userUri);
//            payers = mapper.convertValue(serviceRequestRepository.fetchResult(userUrl, userSearchRequest), UserResponse.class).getUser();
//            
//            if(CollectionUtils.isEmpty(payers))
//                return new ArrayList<>();
//            
//            Set<String> ownerIds = payers.stream().map(User::getUuid).collect(Collectors.toSet());
//            demandCriteria.setPayer(ownerIds);
            demands = demandRepository.getDemands(demandCriteria);
            
        } else {
            
            /*
             * If no payer related data given then search demand first then enrich payer(user) data
             */
            demands = demandRepository.getDemands(demandCriteria);
            if (!demands.isEmpty()) {

                Set<String> payerUuids = demands.stream().filter(demand -> null != demand.getPayer())
                        .map(demand -> demand.getPayer().getUuid()).collect(Collectors.toSet());

//                if (!CollectionUtils.isEmpty(payerUuids)) {
//
//                    userSearchRequest = UserSearchRequest.builder().requestInfo(requestInfo).uuid(payerUuids).build();
//
//                    StringBuilder userUrl = new StringBuilder(userUri);
//                    payers = mapper.convertValue(serviceRequestRepository.fetchResult(userUrl, userSearchRequest),
//                            UserResponse.class).getUser();
//                }
            }
        }
        
        if (!CollectionUtils.isEmpty(demands) && !CollectionUtils.isEmpty(payers))
            demands = demandEnrichmentUtil.enrichPayer(demands, payers);

        return demands;

    }

}
