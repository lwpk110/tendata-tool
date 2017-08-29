package cn.tendata.batch.item.file.avro.parquet;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.avro.Schema;
import org.apache.hadoop.fs.Path;
import org.apache.parquet.avro.AvroParquetWriter;
import org.apache.parquet.hadoop.ParquetFileWriter.Mode;
import org.apache.parquet.hadoop.ParquetWriter;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemStreamException;
import org.springframework.batch.item.WriteFailedException;
import org.springframework.batch.item.file.ResourceAwareItemWriterItemStream;
import org.springframework.batch.item.support.AbstractItemStreamItemWriter;
import org.springframework.core.io.Resource;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;

public abstract class AbstractAvroParquetFileItemWriter<T, D> extends AbstractItemStreamItemWriter<T>
    implements ResourceAwareItemWriterItemStream<T> {

    private Resource resource;
    private Schema schema;

    private ParquetWriter<D> writer;

    public AbstractAvroParquetFileItemWriter() {
        setName(ClassUtils.getShortName(getClass()));
    }

    @Override
    public void setResource(Resource resource) {
        this.resource = resource;
    }

    public Schema getSchema() {
        return schema;
    }

    public void setSchema(Resource schema) throws IOException {
        this.schema = new Schema.Parser().parse(schema.getInputStream());
    }

    @Override
    public void write(List<? extends T> items) throws Exception {
        for (T item : items) {
            try {
                writer.write(createDatum(item));
            } catch (IOException e) {
                throw new WriteFailedException("Could not write data.  The file may be corrupt.",
                        e);
            }
        }
    }
    
    protected abstract D createDatum(T item);

    @Override
    public void open(ExecutionContext executionContext) {
        super.open(executionContext);

        Assert.notNull(resource, "The resource must be set");
        Assert.notNull(schema, "The schema must be set");

        File file;
        try {
            file = resource.getFile();
        } catch (IOException e) {
            throw new ItemStreamException("Could not convert resource to file: [" + resource + "]",
                    e);
        }
        Assert.state(!file.exists() || file.canWrite(),
                "Resource is not writable: [" + resource + "]");
        try {
            this.writer = AvroParquetWriter.<D>builder(new Path(file.getAbsolutePath()))
                    .withSchema(getSchema()).withWriteMode(Mode.OVERWRITE).build();
        } catch (IOException e) {
            throw new ItemStreamException("Failed to initialize writer", e);
        }
    }

    @Override
    public void close() {
        super.close();
        if (writer != null) {
            try {
                writer.close();
            } catch (Exception e) {
                throw new ItemStreamException("Error while closing item writer", e);
            }
        }
    }
}
