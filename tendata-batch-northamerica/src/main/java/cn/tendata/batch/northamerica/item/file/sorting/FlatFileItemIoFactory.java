package cn.tendata.batch.northamerica.item.file.sorting;

import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.core.io.Resource;

/**
 * Defines an interface for getting a FlatFileItemReader or FlatFileItemWriter
 * instance for a given resource.
 *
 * @param <T> Type of object associated with reader/writer.
 */
public interface FlatFileItemIoFactory<T> {

    public FlatFileItemReader<T> getReader(Resource r);
    
    public FlatFileItemWriter<T> getWriter(Resource r);
}
