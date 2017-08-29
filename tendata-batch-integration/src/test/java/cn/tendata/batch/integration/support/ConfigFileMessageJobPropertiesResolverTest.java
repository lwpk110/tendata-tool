package cn.tendata.batch.integration.support;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.io.IOException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import cn.tendata.batch.integration.support.ConfigFileMessageJobPropertiesResolver;
import cn.tendata.batch.integration.support.FileMessageJobProperties;
import cn.tendata.batch.integration.support.FileMessageJobPropertiesResolver;

@ActiveProfiles("test")
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
@TestPropertySource("classpath:test.properties")
public class ConfigFileMessageJobPropertiesResolverTest {

    @Autowired FileMessageJobPropertiesResolver jobPropertiesResolver;
    
    @Test
    public void testResolve() throws IOException{
        FileMessageJobProperties jobProperties = jobPropertiesResolver.resolve("dir1");
        assertThat(jobProperties.getJobName(), is("job1"));
    }
    
    @Configuration
    static class TestConfig {
        
        @Bean
        public FileMessageJobPropertiesResolver jobPropertiesResolver() throws IOException{
            ConfigFileMessageJobPropertiesResolver jobPropertiesResolver = new ConfigFileMessageJobPropertiesResolver();
            jobPropertiesResolver.setConfigProps(
                    PropertiesLoaderUtils.loadProperties(new ClassPathResource("batch-integration-job.properties")));
            return jobPropertiesResolver;
        }
    }
}
