package cn.tendata.batch.integration;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionException;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.annotation.ServiceActivator;

public class JobRestart {

    private static final Log logger = LogFactory.getLog(JobRestart.class);

    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    private JobRegistry jobRegistry;

    @ServiceActivator
    public void restartIfPossible(JobExecution execution) throws JobExecutionException {
        logger.info("Restarting job...");
        Job job = jobRegistry.getJob(execution.getJobInstance().getJobName());
        jobLauncher.run(job, execution.getJobParameters());
    }
}
