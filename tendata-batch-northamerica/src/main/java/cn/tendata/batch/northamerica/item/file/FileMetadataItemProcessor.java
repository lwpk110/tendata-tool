package cn.tendata.batch.northamerica.item.file;

import cn.tendata.batch.util.JodaDateTimeUtils;
import java.io.File;
import java.text.ParseException;
import java.util.Map;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.DateTime;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.core.io.Resource;

public class FileMetadataItemProcessor
    implements ItemProcessor<Map<String, Object>, Map<String, Object>> {

    private static final Log LOG = LogFactory.getLog(FileMetadataItemProcessor.class);

    private static final String[] DEFAULT_PARSE_PATTERNS = new String[] {"yyyyMMdd"};

    private static final String INPUT_FILE_NAME = "_inputFileName";
    private static final String INPUT_FILE_DATE = "_inputFileDate";

    private Resource resource;
    private String[] parsePatterns = DEFAULT_PARSE_PATTERNS;

    private final Object lock = new Object();

    public void setResource(Resource resource) {
        this.resource = resource;
    }

    public void setParsePatterns(String[] parsePatterns) {
        this.parsePatterns = parsePatterns;
    }

    @Override
    public Map<String, Object> process(Map<String, Object> item) throws Exception {
        synchronized (lock) {
            if (resource != null) {
                File inputFile = resource.getFile();
                item.put(INPUT_FILE_NAME, inputFile.getName());
                String str = FilenameUtils.getBaseName(inputFile.getPath());
                try {
                    DateTime dateTime = JodaDateTimeUtils.parseDateTime(str, parsePatterns);
                    item.put(INPUT_FILE_DATE, dateTime);
                } catch (ParseException e) {
                    LOG.warn(e.getMessage());
                }
            }
            return item;
        }
    }
}
