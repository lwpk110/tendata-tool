package cn.tendata.batch.northamerica.item.data.hadoop;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.Date;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.io.Resource;
import org.springframework.data.hadoop.fs.FsShell;
import org.springframework.util.Assert;

public class LocalFileToHdfsTasklet implements Tasklet, InitializingBean {

    private static final Log LOG = LogFactory.getLog(LocalFileToHdfsTasklet.class);
    
    private static final String[] DEFAULT_PARSE_PATTERNS = new String[] { "yyyyMMdd" };
    
    private Resource localSourceFile;
    private String rootDir;
    private FsShell fsh;
    
    private String[] parsePatterns = DEFAULT_PARSE_PATTERNS;
    
    private boolean checkCrc;
    
    public void setLocalSourceFile(Resource localSourceFile) {
        this.localSourceFile = localSourceFile;
    }

    public void setRootDir(String rootDir) {
        this.rootDir = rootDir;
    }

    public void setFsh(FsShell fsh) {
        this.fsh = fsh;
    }
    
    public void setParsePatterns(String[] parsePatterns) {
        this.parsePatterns = parsePatterns;
    }
    
    public void setCheckCrc(boolean checkCrc) {
        this.checkCrc = checkCrc;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Assert.notNull(fsh, "'fsh' must not be null");
    }

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        String destDir = resolveDestDir(rootDir, localSourceFile.getFile());
        if(!fsh.test(destDir)){
            fsh.mkdir(destDir);
            fsh.chmod(750, destDir);
        }
        
        File localFile = localSourceFile.getFile();
        String localFilePath = localFile.getPath();
        fsh.moveFromLocal(localFilePath, destDir);
        
        //crc
        if(checkCrc){
            String crcFilePath = FilenameUtils.getFullPath(localFilePath) + "." + localSourceFile.getFilename() + ".crc";
            File crcFile = new File(crcFilePath);
            if(crcFile.exists()){
                fsh.moveFromLocal(crcFilePath, destDir);
            }
        }
        return RepeatStatus.FINISHED;
    }
    
    private String resolveDestDir(String rootDir, File localFile) throws IOException {
        String str = FilenameUtils.getBaseName(localFile.getName());
        try{
            Date date = DateUtils.parseDate(str, parsePatterns);
            return String.format("%1$s/%2$tY/%2$tm", rootDir, date);
        }
        catch(ParseException e){
            LOG.warn(e.getMessage());
        }
        return rootDir;
    }
}
