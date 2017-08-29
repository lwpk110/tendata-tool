package cn.tendata.batch.compress;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveStreamFactory;
import org.apache.commons.compress.utils.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class DefaultArchiveExtractor extends AbstractArchiveExtractor {

    private static final Log LOG = LogFactory.getLog(DefaultArchiveExtractor.class);
    
    @Override
    public List<File> extractCore(File archive, File dest) throws IOException {
        List<File> files = new LinkedList<>();
        try(ArchiveInputStream in = new ArchiveStreamFactory().createArchiveInputStream(
                new BufferedInputStream(new FileInputStream(archive)))){
            ArchiveEntry entry;
            while (( entry = in.getNextEntry()) != null) {
                LOG.debug( "Extracting: " + entry.getName() );
    
                if (entry.isDirectory()) {
                    new File( dest, entry.getName() ).mkdirs();
                    continue;
                } 
                else {
                    int di = entry.getName().lastIndexOf( File.separator );
                    if (di != -1) {
                        new File( dest, entry.getName().substring( 0, di ) ).mkdirs();
                    }
                }
                File outputFile = new File(dest ,entry.getName());
                try(OutputStream out = new FileOutputStream(outputFile)){
                    IOUtils.copy(in, out);
                }
                files.add(outputFile);
            }
            return files;
        }
        catch(ArchiveException e){
            throw new ArchiveExtractingException(e);
        }
    }
}
