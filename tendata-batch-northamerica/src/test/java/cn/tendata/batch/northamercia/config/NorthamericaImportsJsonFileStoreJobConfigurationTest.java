package cn.tendata.batch.northamercia.config;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import cn.tendata.batch.northamerica.config.NorthamericaImportsJsonFileStoreJobConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
@ActiveProfiles("test")
public class NorthamericaImportsJsonFileStoreJobConfigurationTest {

    @Autowired ApplicationContext ctx;
    
    @Test
    public void testContextLoads() throws Exception {
        assertNotNull(ctx);
        assertTrue(ctx.containsBean("northamericaImportsJsonFileStoreJob"));
    }
    
    @Configuration
    @EnableBatchProcessing
    @Import(NorthamericaImportsJsonFileStoreJobConfiguration.class)
    static class TestConfig {
        
    }
}
