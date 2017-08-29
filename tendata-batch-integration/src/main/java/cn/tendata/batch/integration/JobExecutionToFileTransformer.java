package cn.tendata.batch.integration;

import java.io.File;
import java.io.IOException;

import org.springframework.batch.core.JobExecution;
import org.springframework.integration.annotation.Transformer;
import org.springframework.util.ResourceUtils;

import cn.tendata.batch.util.JobParameterUtils;

public class JobExecutionToFileTransformer {
    
    @Transformer
    public File toFile(JobExecution jobExecution) throws IOException{
        return ResourceUtils.getFile(jobExecution.getJobParameters().getString(JobParameterUtils.INPUT_PATH_TO_FILE));
    }
}
