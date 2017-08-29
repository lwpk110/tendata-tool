package cn.tendata.batch.compress;

import java.io.File;
import java.io.IOException;
import java.util.List;

public interface ArchiveExtractor {

    List<File> extract(String archiveName, String destRootPath) throws IOException;
}
