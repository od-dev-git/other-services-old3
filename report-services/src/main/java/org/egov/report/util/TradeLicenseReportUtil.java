package org.egov.report.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.egov.common.contract.request.RequestInfo;
import org.egov.mdms.model.MasterDetail;
import org.egov.mdms.model.MdmsCriteria;
import org.egov.mdms.model.MdmsCriteriaReq;
import org.egov.mdms.model.ModuleDetail;
import org.egov.report.config.ReportServiceConfiguration;
import org.egov.report.repository.TradeLicenseReportRepository;
import org.egov.report.service.UserService;
import org.egov.report.web.model.TradeLicenseEscallationDetailsResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class TradeLicenseReportUtil {
    
    @Autowired
    private TradeLicenseReportRepository tlRepository;
    
    @Autowired
    private UserService userService;

    @Autowired
    private ReportServiceConfiguration config;
    
    public static final String LOCAL_ZONE_ID = "Asia/Kolkata";
    
//    private final Integer BATCH_SIZE = config.getUserTLServiceSearchLimit(); // Define the chunk size ISSUE HERE


    public Object mdmsCallForCalender(RequestInfo requestInfo) {
        log.info("Preparing MDMS call for holiday calendar");
        ModuleDetail moduleDetail = getHolidayCalenderRequest();
        List<ModuleDetail> holidayCalenderRequest = new LinkedList<>();
        holidayCalenderRequest.add(moduleDetail);
        MdmsCriteria mdmsCriteria = MdmsCriteria.builder()
                .moduleDetails(holidayCalenderRequest)
                .tenantId("od")
                .build();
        MdmsCriteriaReq mdmsCriteriaReq = MdmsCriteriaReq.builder()
                .mdmsCriteria(mdmsCriteria)
                .requestInfo(requestInfo)
                .build();
        log.info("Fetching holiday calendar data from MDMS");
        Object result = tlRepository.fetchResult(getMdmsSearchUrl(), mdmsCriteriaReq);
        log.info("Received holiday calendar data from MDMS");
        return result;
    }

    public ModuleDetail getHolidayCalenderRequest() {
        log.info("Creating module detail request for HolidayCalender");
        List<MasterDetail> mrMasterDetails = new ArrayList<>();
        mrMasterDetails.add(MasterDetail.builder().name("HolidayCalender").build());
        ModuleDetail mrModuleDtls = ModuleDetail.builder()
                .masterDetails(mrMasterDetails)
                .moduleName("common-masters")
                .build();
        return mrModuleDtls;
    }

    public StringBuilder getMdmsSearchUrl() {
        StringBuilder mdmsSearchUrl = new StringBuilder()
                .append(config.getMdmsHost())
                .append(config.getMdmsSearchUrl());
        log.info("Constructed MDMS search URL: {}", mdmsSearchUrl);
        return mdmsSearchUrl;
    }

    public void enrichingWithUserDetailsUsingUUIDS(RequestInfo requestInfo,
            List<TradeLicenseEscallationDetailsResponse> tradeLicenseEscallationDetailsResponse) {
        log.info("Setting UserIds for enrichment");
        List<String> userIds = tradeLicenseEscallationDetailsResponse.stream()
                .flatMap(item -> Stream.of(item.getEscalatedFrom(), item.getEscalatedTo()))
                .distinct()
                .collect(Collectors.toList());
        log.info("Total Unique User IDs: {}", userIds.size());

        Map<String, org.egov.report.user.User> userMap = fetchUsersInChunks(userIds, requestInfo);
        log.info("Enriching response with User Details");
        tradeLicenseEscallationDetailsResponse.forEach(item -> {
            String escalatedFromId = item.getEscalatedFrom();
            String escalatedToId = item.getEscalatedTo();

            org.egov.report.user.User escalatedFromUser = userMap.get(escalatedFromId);
            org.egov.report.user.User escalatedToUser = userMap.get(escalatedToId);

            if (escalatedFromUser != null) {
                item.setEscalatedFrom(escalatedFromUser.getName());
            }

            if (escalatedToUser != null) {
                item.setEscalatedTo(escalatedToUser.getName());
            }
        });
        log.info("Completed enriching response with user details");
    }

    public Map<String, org.egov.report.user.User> fetchUsersInChunks(List<String> userIds, RequestInfo requestInfo) {
        log.info("Starting to fetch user details in chunks");
        Map<String, org.egov.report.user.User> userMap = new HashMap<>();
        final Integer BATCH_SIZE = config.getUserTLServiceSearchLimit();

        for (int i = 0; i < userIds.size(); i += BATCH_SIZE) {
            int end = Math.min(userIds.size(), i + BATCH_SIZE);
            List<String> chunk = userIds.subList(i, end);
            log.info("Fetching user details for chunk: {} - {}", i, end);
            
            org.egov.report.user.UserSearchCriteria userSearchCriteria = org.egov.report.user.UserSearchCriteria
                    .builder()
                    .uuid(chunk)
                    .build();
            List<org.egov.report.user.User> usersInfo = userService.searchUsers(userSearchCriteria, requestInfo);
            userMap.putAll(usersInfo.stream()
                    .collect(Collectors.toMap(org.egov.report.user.User::getUuid, Function.identity())));
            log.info("Fetched and mapped user details for chunk: {} - {}", i, end);
        }

        log.info("Completed fetching user details for all chunks");
        return userMap;
    }
}
