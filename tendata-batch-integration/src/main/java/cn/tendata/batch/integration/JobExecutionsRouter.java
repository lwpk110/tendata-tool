package cn.tendata.batch.integration;

import java.util.ArrayList;
import java.util.List;

import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.integration.annotation.Router;

public class JobExecutionsRouter {

    @Router
    public List<String> routeJobExecution(JobExecution jobExecution) {

        final List<String> routeToChannels = new ArrayList<>();

        if (jobExecution.getStatus().equals(BatchStatus.FAILED)) {
            routeToChannels.add("batch-job-restarts");
        }
        else {

            if (jobExecution.getStatus().equals(BatchStatus.COMPLETED)) {
                routeToChannels.add("batch-job-completes");
            }

            routeToChannels.add("batch-job-notifiable-executions");
        }

        return routeToChannels;
    }
}
