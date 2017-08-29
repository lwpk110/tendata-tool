package cn.tendata.batch.integration.support;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

public class ConfigFileMessageJobPropertiesResolver implements FileMessageJobPropertiesResolver, InitializingBean {

    public static final String BATCH_INTEGRATION_JOB_PREFIX = "batch.integration.job.";
    
    private final Map<String, FileMessageJobProperties> jobPropertiesMap = new HashMap<>();
    
    private Properties configProps;
    
    public void setConfigProps(Properties configProps) {
        this.configProps = configProps;
    }
    
    @Override
    public void afterPropertiesSet() throws Exception {
        Assert.notNull(configProps, "'configProps' must not be null");
    }

    @Override
    public FileMessageJobProperties resolve(String key) {
        FileMessageJobProperties jobProperties = jobPropertiesMap.get(key);
        if(jobProperties != null){
            return jobProperties;
        }
        
        String jobPropertiesPrefix = BATCH_INTEGRATION_JOB_PREFIX + "properties.";
        String jobPropertiesKeyPrefix = jobPropertiesPrefix + key + ".";
        String jobName = configProps.getProperty(jobPropertiesKeyPrefix + "job-name");
        if(jobName == null){
            throw new IllegalStateException("job name is not found, key:" + key);
        }
        jobProperties = new FileMessageJobProperties();
        jobProperties.setJobName(jobName);
        jobPropertiesMap.put(key, jobProperties);
        return jobProperties;
    }
}
