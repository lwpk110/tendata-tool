package cn.tendata.batch.northamercia;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.apache.avro.generic.GenericRecord;
import org.apache.hadoop.fs.Path;
import org.apache.parquet.avro.AvroParquetReader;
import org.apache.parquet.hadoop.ParquetReader;
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
import org.springframework.util.ResourceUtils;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/simple-job-launcher-context.xml", 
        "/META-INF/spring/batch/jobs/northamericaImportsAvroParquetFileStore-job.xml", "/job-runner-context.xml"})
@ActiveProfiles("test")
public class NorthamericaImportsAvroParquetFileStoreJobXmlConfigTest {

    @Autowired
    private JobLauncherTestUtils jobLauncherTestUtils;
    
    @SuppressWarnings("unchecked")
    @Test
    public void testLaunchJob() throws Exception {
        JobParameters jobParameters = new JobParametersBuilder()
                .addString("inputPathToFile", "classpath:input/20160101.txt")
                .addString("outputPathToDir", "file:build/output")
                .toJobParameters();
        JobExecution jobExecution = jobLauncherTestUtils.launchJob(jobParameters);
        assertEquals(BatchStatus.COMPLETED, jobExecution.getStatus());
        
        ParquetReader<GenericRecord> reader = AvroParquetReader.<GenericRecord>builder(
                new Path(ResourceUtils.getFile("file:build/output/20160101.parquet").getAbsolutePath())).build();
        assertNotNull(reader.read());
    }
}
