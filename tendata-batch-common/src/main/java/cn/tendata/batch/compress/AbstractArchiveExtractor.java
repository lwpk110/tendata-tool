package cn.tendata.batch.compress;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.springframework.util.Assert;

public abstract class AbstractArchiveExtractor implements ArchiveExtractor {

    @Override
    public List<File> extract(String archiveName, String destRootPath) throws IOException {
        Assert.hasText(archiveName, "'archiveName' must not be empty");
        Assert.hasText(destRootPath, "'destRootPath' must not be empty");
        File archive = new File(archiveName);
        if (!archive.exists()) {
            throw new ArchiveExtractingException("the archive does not exit: " + archiveName);
        }
        File dest = new File(destRootPath);
        if (!dest.exists()) {
            dest.mkdirs();
        }
        return extractCore(archive, dest);
    }
    
    protected abstract List<File> extractCore(File archive, File dest) throws IOException;
}
