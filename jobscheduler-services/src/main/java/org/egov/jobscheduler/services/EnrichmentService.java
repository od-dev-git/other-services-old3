package org.egov.jobscheduler.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONArray;
import org.egov.common.contract.request.RequestInfo;
import org.egov.jobscheduler.config.JobSchedulerConfiguration;
import org.egov.jobscheduler.constants.JobSchedulerConstants;
import org.egov.jobscheduler.repository.ServiceRequestRepository;
import org.egov.jobscheduler.util.JobSchedulerUtil;
import org.egov.mdms.model.MdmsCriteriaReq;
import org.egov.mdms.model.MdmsResponse;
import org.egov.tracer.model.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
@Slf4j
public class EnrichmentService {

    @Autowired
    private JobSchedulerUtil jobSchedulerUtil;

    @Autowired
    private JobSchedulerConfiguration config;

    @Autowired
    private ServiceRequestRepository serviceRequestRepository;

    @Autowired
    private ObjectMapper mapper;

    /**
     * Purpose : Reads MDMS Data about All the configured scheduled jobs and returns them
     * @return
     */
    public JSONArray enrichMDMSData() {

        List<String> names = new ArrayList<>(Arrays.asList(JobSchedulerConstants.JOB_SCHEDULER_API_CONFIG));
        MdmsCriteriaReq mdmsCriteriaReq=jobSchedulerUtil.prepareMdMsRequest(JobSchedulerConstants.OD,JobSchedulerConstants.COMMON_MASTERS,
              names,"$.*",new RequestInfo());
        StringBuilder uri = new StringBuilder(config.getMdmsHost()).append(config.getMdmsEndPoint());
        try {
            Object result = serviceRequestRepository.fetchResult(uri, mdmsCriteriaReq);
            MdmsResponse mdmsResponse = mapper.readValue(result.toString(), MdmsResponse.class);
            JSONArray jobSchedulerAPIConfig=mdmsResponse.getMdmsRes().get("common-masters").get("JobSchedulerAPIConfig");
            log.info(jobSchedulerAPIConfig.toJSONString());
            return jobSchedulerAPIConfig;

        } catch (Exception e) {
            throw new CustomException("@Class EnrichmentService @method enrichMDMSData ","@message "+e.getMessage());
        }

    }
}
