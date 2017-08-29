package cn.tendata.batch.compress;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

import org.apache.commons.io.FileUtils;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;

import cn.tendata.batch.compress.DefaultArchiveExtractor;

public class DefaultArchiveExtractorTest {

	DefaultArchiveExtractor extractor = new DefaultArchiveExtractor();
	
	@Test
	public void test_extract() throws IOException{
		File archive = new ClassPathResource("data/test.zip").getFile();
		File dest = new File(new File("build", "compress"), UUID.randomUUID().toString());
		List<File> files = extractor.extract(archive.getAbsolutePath(), dest.getAbsolutePath());
		Assert.assertEquals(2, files.size());
		FileUtils.deleteDirectory(dest);
	}
}
