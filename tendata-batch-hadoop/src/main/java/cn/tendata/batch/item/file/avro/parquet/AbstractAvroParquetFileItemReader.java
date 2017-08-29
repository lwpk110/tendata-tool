package cn.tendata.batch.item.file.avro.parquet;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.fs.Path;
import org.apache.parquet.avro.AvroParquetReader;
import org.apache.parquet.hadoop.ParquetReader;
import org.springframework.batch.item.file.ResourceAwareItemReaderItemStream;
import org.springframework.batch.item.support.AbstractItemCountingItemStreamItemReader;
import org.springframework.core.io.Resource;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;

public abstract class AbstractAvroParquetFileItemReader<T, D> extends AbstractItemCountingItemStreamItemReader<T>
    implements ResourceAwareItemReaderItemStream<T> {

    private static final Log LOG = LogFactory.getLog(AbstractAvroParquetFileItemReader.class);

    private Resource resource;

    private ParquetReader<D> reader;

    private boolean strict = true;

    public AbstractAvroParquetFileItemReader() {
        setName(ClassUtils.getShortName(getClass()));
    }

    @Override
    public void setResource(Resource resource) {
        this.resource = resource;
    }

    public void setStrict(boolean strict) {
        this.strict = strict;
    }

    @Override
    protected T doRead() throws Exception {
        D datum = reader.read();
        if (datum != null) {
            return createItem(datum);
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void doOpen() throws Exception {
        Assert.notNull(resource, "Input resource must be set");

        if (!resource.exists()) {
            if (strict) {
                throw new IllegalStateException(
                        "Input resource must exist (reader is in 'strict' mode): " + resource);
            }
            LOG.warn("Input resource does not exist " + resource.getDescription());
            return;
        }

        if (!resource.isReadable()) {
            if (strict) {
                throw new IllegalStateException(
                        "Input resource must be readable (reader is in 'strict' mode): "
                                + resource);
            }
            LOG.warn("Input resource is not readable " + resource.getDescription());
            return;
        }

        this.reader = AvroParquetReader.<D>builder(new Path(resource.getURI())).build();
    }

    protected abstract T createItem(D datum);
    
    @Override
    protected void doClose() throws Exception {
        if (reader != null) {
            reader.close();
        }
    }
}
