package org.egov.jobscheduler.controller;

import com.google.gson.JsonObject;
import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONObject;
import org.egov.jobscheduler.controller.model.Job;
import org.egov.jobscheduler.controller.model.JobSchedulerResponse;
import org.egov.jobscheduler.controller.model.RequestInfoWrapper;
import org.egov.jobscheduler.services.JobSchedulerServices;
import org.egov.jobscheduler.util.ResponseInfoFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@Slf4j
@RequestMapping("/scheduler")
public class JobSchedulerController {

    @Autowired
    private JobSchedulerServices schedulerService;

    @Autowired
    private ResponseInfoFactory responseInfoFactory;

    /**
     *
     * @return
     */
    @PostMapping("/_scheduledJobs")
    public ResponseEntity<JobSchedulerResponse> getScheduledJobs(@RequestBody RequestInfoWrapper requestInfoWrapper){

        List<Job> jobs= schedulerService.getScheduledJobs(requestInfoWrapper);
        JobSchedulerResponse jobSchedulerResponse=JobSchedulerResponse.builder()
                .responseInfo(responseInfoFactory.createResponseInfoFromRequestInfo(requestInfoWrapper.getRequestInfo(),true))
                .jobs(jobs).build();
        return new ResponseEntity<JobSchedulerResponse>(jobSchedulerResponse, HttpStatus.OK);
    }

    /**
     *
     * @param jobId
     */
    @DeleteMapping("/_deleteJob/{jobId}")
    public void deleteScheduledJob(@PathVariable String jobId){
        schedulerService.deleteScheduledJob(jobId);
    }
}
