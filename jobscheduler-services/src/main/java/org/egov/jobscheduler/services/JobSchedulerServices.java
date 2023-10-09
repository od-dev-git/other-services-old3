package org.egov.jobscheduler.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONArray;
import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.request.Role;
import org.egov.common.contract.request.User;
import org.egov.jobscheduler.config.JobSchedulerConfiguration;
import org.egov.jobscheduler.controller.model.Job;
import org.egov.jobscheduler.controller.model.RequestInfoWrapper;
import org.egov.jobscheduler.repository.ServiceRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.util.*;

@Service
@Slf4j
public class JobSchedulerServices {

	@Autowired
	private EnrichmentService enrichmentService;

	@Autowired
	private ServiceRequestRepository serviceRequestRepository;

	@Autowired
	private JobSchedulerConfiguration jobSchedulerConfiguration;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	JobSchedulerServices(TaskScheduler taskScheduler) {
		this.taskScheduler = taskScheduler;

	}

	private TaskScheduler taskScheduler;

	private Map<String, LinkedHashMap> schedulerMap = new HashMap<>();

	/**
	 * Purpose: To Auto trigger whenever service is up and running
	 */
	@PostConstruct
	public void autoTriggerScheduledTasks() {
		if (jobSchedulerConfiguration.isSchedulerEnabled()) {
			log.info(
					"@Class: JobSchedulerServices @method: autoTriggerScheduledTasks @message: Before Mdms Call to fetch scheduler details");
			JSONArray mdmsResponse = enrichmentService.enrichMDMSData();
			log.info(
					"@Class: JobSchedulerServices @method: autoTriggerScheduledTasks @message: Data fetched from MDMS Service");
			scheduleJob(mdmsResponse);
			log.info("@Class: JobSchedulerServices @method: autoTriggerScheduledTasks @message: Job Scheduling Triggered");
		}

	}

	/**
	 * Purpose: Schedules the Job with the data obtained from MDMS
	 * @param jsonArray
	 */

	private void scheduleJob(JSONArray jsonArray) {
		for (int i = 0; i < jsonArray.size(); i++) {
			LinkedHashMap<String, Object> linkedHashMap = objectMapper.convertValue(jsonArray.get(i),
					LinkedHashMap.class);
			boolean isActive = Boolean.parseBoolean(linkedHashMap.get("active").toString());
			RequestInfoWrapper requestInfoWrapper = setUserDetails();
			if (isActive) {
				log.info("Scheduling job:" + linkedHashMap.get("jobName"));
				schedulerMap.put(linkedHashMap.get("jobName").toString(), linkedHashMap);
				taskScheduler.schedule(() -> {
					if (schedulerMap.containsKey(linkedHashMap.get("jobName"))) {
						log.info("URL : ", linkedHashMap.get("url"));
						LinkedHashMap<String, String> payLoadMap = objectMapper
								.convertValue(linkedHashMap.get("payload"), LinkedHashMap.class);
						Object response = serviceRequestRepository.fetchResult(
								new StringBuilder(linkedHashMap.get("url").toString()), requestInfoWrapper);
						long currentTime = System.currentTimeMillis();
						DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
						formatter.setTimeZone(TimeZone.getTimeZone(ZoneId.of("Asia/Kolkata")));
						String currentDateTime = formatter.format(currentTime);
						log.info("@Class: JobSchedulerServices @method: scheduleJob @message:Triggered Job "
								+ linkedHashMap.get("jobName") + " @timestamp: " + currentDateTime);
						LinkedHashMap<String,Object> stringObjectLinkedHashMap=schedulerMap.get(linkedHashMap.get("jobName"));
						stringObjectLinkedHashMap.put("lastExecutionTime",currentDateTime);
						schedulerMap.replace(linkedHashMap.get("jobName").toString(),stringObjectLinkedHashMap);
					}
				}, new CronTrigger(linkedHashMap.get("cron").toString(),
						TimeZone.getTimeZone(ZoneId.of("Asia/Kolkata"))));
			}
		}
	}

	public RequestInfoWrapper setUserDetails() {

		Role role = Role.builder().code(jobSchedulerConfiguration.getSchedulingRoleCode())
				.tenantId(jobSchedulerConfiguration.getSchedulingRoleTenantId()).build();
		User userInfo = User.builder().uuid(jobSchedulerConfiguration.getSchedulingUserUUID())
				.type(jobSchedulerConfiguration.getSchedulingUserType()).roles(Arrays.asList(role)).id(0L).build();
		return RequestInfoWrapper.builder().requestInfo(RequestInfo.builder().userInfo(userInfo).build()).build();

	}

	/**
	 * Purpose: To fetch active schedulers that are running
	 * @param requestInfoWrapper
	 * @return
	 */
	public List<Job> getScheduledJobs(RequestInfoWrapper requestInfoWrapper) {
		List<Job> jobs= getActiveJobResponse();
		return jobs;
	}

	private List<Job> getActiveJobResponse() {
		List<Job> jobs= new ArrayList<>();
		for(String jobName: schedulerMap.keySet()){
			LinkedHashMap<String,Object> linkedHashMap = schedulerMap.get(jobName);
			Job job = Job.builder().jobName(linkedHashMap.get("jobName").toString())
					.url(linkedHashMap.get("url").toString())
					.cron(linkedHashMap.get("cron").toString())
					.lastExecutionTime(linkedHashMap.get("lastExecutionTime").toString()).build();
			jobs.add(job);
		}
		return jobs;

	}

	/**
	 * Deletes an existing Job
	 * @param jobId
	 */
	public void deleteScheduledJob(String jobId) {
		if (schedulerMap.containsKey(jobId))
			schedulerMap.remove(jobId);
		else
			log.error("No jobs exists with Id:" + jobId);
	}

}
