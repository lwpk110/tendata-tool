package cn.tendata.batch.northamercia;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/simple-job-launcher-context.xml", 
        "/META-INF/spring/batch/jobs/northamericaImportsMongoStore-job.xml", 
        "/job-runner-context.xml", "/mongo-context.xml" })
@ActiveProfiles("test")
public class NorthamericaImportsMongoStoreJobXmlConfigTest {

    @Autowired
    private JobLauncherTestUtils jobLauncherTestUtils;
    
    @Test
    public void testLaunchJob() throws Exception {
        JobParameters jobParameters = new JobParametersBuilder()
                .addString("inputPathToFile", "classpath:input/20160101.txt")
                .toJobParameters();
        JobExecution jobExecution = jobLauncherTestUtils.launchJob(jobParameters);
        assertEquals(BatchStatus.COMPLETED, jobExecution.getStatus());
    }
}
