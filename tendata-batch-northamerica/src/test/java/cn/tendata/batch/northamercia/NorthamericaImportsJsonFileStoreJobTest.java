package cn.tendata.batch.northamercia;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import cn.tendata.batch.northamerica.config.NorthamericaImportsJsonFileStoreJobConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
@ActiveProfiles("test")
public class NorthamericaImportsJsonFileStoreJobTest {

    @Autowired
    private JobLauncherTestUtils jobLauncherTestUtils;
    
    @Test
    public void testLaunchJob() throws Exception {
        JobParameters jobParameters = new JobParametersBuilder()
                .addString("inputPathToFile", "classpath:input/20160101.txt")
                .addString("outputPathToDir", "file:build/output")
                .toJobParameters();
        JobExecution jobExecution = jobLauncherTestUtils.launchJob(jobParameters);
        assertEquals(BatchStatus.COMPLETED, jobExecution.getStatus());
    }
    
    @Configuration
    @EnableBatchProcessing
    @Import(NorthamericaImportsJsonFileStoreJobConfiguration.class)
    static class TestConfig {
        
        @Bean
        public JobLauncherTestUtils jobLauncherTestUtils(){
            return new JobLauncherTestUtils();
        }
    }
}
