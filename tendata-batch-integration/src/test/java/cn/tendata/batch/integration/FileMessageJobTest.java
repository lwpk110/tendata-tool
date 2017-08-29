package cn.tendata.batch.integration;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.support.ApplicationContextFactory;
import org.springframework.batch.core.configuration.support.GenericApplicationContextFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.integration.channel.QueueChannel;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import cn.tendata.batch.integration.config.BatchIntegrationConfig;

@ActiveProfiles("test")
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
public class FileMessageJobTest {

    @Autowired 
    @Qualifier("batch-job-statuses")
    private QueueChannel statusesChannel;
    
    @Test
    public void testRunBatch() throws Exception {
        JobExecution jobExecution1 = (JobExecution)statusesChannel.receive(10000).getPayload();
        assertThat(jobExecution1.getExitStatus(), is(ExitStatus.COMPLETED));
        
        JobExecution jobExecution2 = (JobExecution)statusesChannel.receive(10000).getPayload();
        assertThat(jobExecution2.getExitStatus(), is(ExitStatus.COMPLETED));
    }
    
    @Configuration
    @EnableBatchProcessing(modular = true)
    @Import(BatchIntegrationConfig.class)
    @PropertySource("classpath:test.properties")
    static class TestConfig {
        
        @Bean
        public ApplicationContextFactory dir1Job(){
            return new GenericApplicationContextFactory(new ClassPathResource("dir1-job.xml"));
        }
        
        @Bean
        public ApplicationContextFactory dir2Job(){
            return new GenericApplicationContextFactory(new ClassPathResource("dir2-job.xml"));
        }
    }
}
