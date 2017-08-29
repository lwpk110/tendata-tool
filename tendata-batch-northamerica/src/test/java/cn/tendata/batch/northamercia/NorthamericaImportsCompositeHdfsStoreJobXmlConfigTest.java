package cn.tendata.batch.northamercia;

import static org.hamcrest.Matchers.greaterThan;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import org.apache.hadoop.fs.Path;
import org.junit.Test;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.hadoop.test.junit.AbstractHadoopClusterTests;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.ContextHierarchy;

@ContextHierarchy({
    @ContextConfiguration(locations = "/hadoop-context.xml"),
    @ContextConfiguration(locations = { "/simple-job-launcher-context.xml", 
            "/META-INF/spring/batch/jobs/northamericaImportsCompositeHdfsStore-job.xml", 
            "/job-runner-context.xml" })
})
@ActiveProfiles("test")
public class NorthamericaImportsCompositeHdfsStoreJobXmlConfigTest extends AbstractHadoopClusterTests {

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
        
        Path avroFile = new Path("/user/tendata/data/original/customs/imports/northamerica/avro/2016/01/20160101.avro");
        assertThat(getFileSystem().getFileStatus(avroFile).getLen(), greaterThan(0L));
        
        Path parquetFile = new Path("/user/tendata/data/original/customs/imports/northamerica/parquet/2016/01/20160101.parquet");
        assertThat(getFileSystem().getFileStatus(parquetFile).getLen(), greaterThan(0L));
    }
}
