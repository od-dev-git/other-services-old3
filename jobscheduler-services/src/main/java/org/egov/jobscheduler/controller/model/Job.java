package org.egov.jobscheduler.controller.model;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class Job {

    private String jobName;
    private String url;
    private String cron;
    private String lastExecutionTime;
}