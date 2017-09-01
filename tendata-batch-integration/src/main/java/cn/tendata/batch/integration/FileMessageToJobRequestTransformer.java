package cn.tendata.batch.integration;

import java.io.File;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.launch.NoSuchJobException;
import org.springframework.batch.integration.launch.JobLaunchRequest;
import org.springframework.integration.annotation.Transformer;
import org.springframework.messaging.Message;
import org.springframework.util.ResourceUtils;

import cn.tendata.batch.integration.support.FileMessageJobProperties;
import cn.tendata.batch.integration.support.FileMessageJobPropertiesResolver;
import cn.tendata.batch.integration.util.FilePathUtils;
import cn.tendata.batch.util.JobParameterUtils;

public class FileMessageToJobRequestTransformer {

    private JobRegistry jobRegistry;
    private FileMessageJobPropertiesResolver jobPropertiesResolver;
    
    private File inputDirectory;
    private File outputDirectory;
    
    public void setJobRegistry(JobRegistry jobRegistry) {
        this.jobRegistry = jobRegistry;
    }

    public void setJobPropertiesResolver(FileMessageJobPropertiesResolver jobPropertiesResolver) {
        this.jobPropertiesResolver = jobPropertiesResolver;
    }
    
    public void setInputDirectory(File inputDirectory) {
        this.inputDirectory = inputDirectory;
    }

    public void setOutputDirectory(File outputDirectory) {
        this.outputDirectory = outputDirectory;
    }

    @Transformer
    public JobLaunchRequest toRequest(Message<File> message) throws NoSuchJobException {
        String[] relativeFolders = FilePathUtils.getRelativeFolders(inputDirectory, message.getPayload());
        FileMessageJobProperties jobProperties = jobPropertiesResolver.resolve(getJobKey(relativeFolders));
        if(jobProperties == null){
            throw new IllegalStateException("Job properties is not found, file:" + message.getPayload());
        }
        JobParametersBuilder jobParametersBuilder = new JobParametersBuilder();
        jobParametersBuilder.addString(JobParameterUtils.INPUT_PATH_TO_FILE, 
                ResourceUtils.FILE_URL_PREFIX + message.getPayload().getAbsolutePath());
        jobParametersBuilder.addString(JobParameterUtils.OUTPUT_PATH_TO_DIR, 
                ResourceUtils.FILE_URL_PREFIX + outputDirectory.getAbsolutePath());
        Job job = jobRegistry.getJob(jobProperties.getJobName());
        return new JobLaunchRequest(job, jobParametersBuilder.toJobParameters());
    }
   
    private String getJobKey(String[] relativeFolders){
        return relativeFolders[0];
    }
}
