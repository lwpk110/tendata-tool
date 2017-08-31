package cn.tendata.batch.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.joda.time.DateTime;
import org.springframework.util.ResourceUtils;

public abstract class JobParameterUtils {

    public static final String INPUT_PATH_TO_FILE = "inputPathToFile";
    public static final String OUTPUT_PATH_TO_DIR = "outputPathToDir";
    
    private JobParameterUtils(){
        
    }
    
    public static String getInputFilename(Map<String, Object> jobParameters){
        String inputPathToFile = getInputPathToFile(jobParameters);
        return FilenameUtils.getBaseName(inputPathToFile);
    }
    
    public static String getInputPathToFile(Map<String, Object> jobParameters){
        return (String)jobParameters.get(INPUT_PATH_TO_FILE);
    }

    public static String getOutputPathToFile(Map<String, Object> jobParameters,boolean hasTimeStamp, String extension){
        if(hasTimeStamp){
            extension = "-" + new DateTime().toString("yyyyMMddHHmmss")+ extension;
        }
        return getOutputPathToFile(jobParameters, extension);
    }
    public static String getOutputPathToFile(Map<String, Object> jobParameters, String extension){
        return getOutputPathToFile(jobParameters, null, extension);
    }
    
    public static String getOutputPathToFile(Map<String, Object> jobParameters, String[] folders, String extension){
        File outputPathToDir;
        try {
            outputPathToDir = ResourceUtils.getFile((String)jobParameters.get(OUTPUT_PATH_TO_DIR));
        } catch (FileNotFoundException e) {
            throw new IllegalStateException("job parameter [outputPathToDir] error", e);
        }
        if(folders != null){
            outputPathToDir = FileUtils.getFile(outputPathToDir, folders);
        }
        if(!outputPathToDir.exists()){
            outputPathToDir.mkdirs();
        }
        File outputPathToFile = FileUtils.getFile(outputPathToDir, getInputFilename(jobParameters) + extension);
        return ResourceUtils.FILE_URL_PREFIX + outputPathToFile.getPath();
    }
}
