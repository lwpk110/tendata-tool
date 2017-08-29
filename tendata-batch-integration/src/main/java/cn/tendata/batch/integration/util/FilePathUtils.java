package cn.tendata.batch.integration.util;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;

public class FilePathUtils {

    private FilePathUtils(){
        
    }
    
    public static String[] getRelativeFolders(File rootDir,  File file){
        String relativeFilename =  file.getAbsolutePath().substring(
                (rootDir.getAbsoluteFile() + File.separator).length());
        return StringUtils.split(FilenameUtils.getPathNoEndSeparator(relativeFilename), File.separatorChar);
    }
    
    public static File concatFolders(File basePath, File rootDir, File file) throws IOException{
        String[] relativeFolders = getRelativeFolders(rootDir, file);
        return concatFolders(basePath, relativeFolders);
    }
    
    public static File concatFolders(File basePath, String[] relativeFolders){
        if(relativeFolders.length > 0){
            return new File(basePath, StringUtils.join(relativeFolders, File.separator));
        }
        return basePath;
    }
}
