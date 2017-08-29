package cn.tendata.batch.item.file.avro;

import org.apache.avro.file.DataFileStream;
import org.apache.avro.io.DatumReader;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.batch.item.file.ResourceAwareItemReaderItemStream;
import org.springframework.batch.item.support.AbstractItemCountingItemStreamItemReader;
import org.springframework.core.io.Resource;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;

public abstract class AbstractAvroFileItemReader<T, D> extends AbstractItemCountingItemStreamItemReader<T>
        implements ResourceAwareItemReaderItemStream<T> {

    private static final Log LOG = LogFactory.getLog(AbstractAvroFileItemReader.class);

    private Resource resource;

    private DataFileStream<D> reader;

    private boolean strict = true;

    public AbstractAvroFileItemReader() {
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
        if (reader.hasNext()) {
            return createItem(reader.next());
        }
        return null;
    }

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

        DatumReader<D> datumReader = createDatumReader();
        this.reader = new DataFileStream<>(resource.getInputStream(), datumReader);
    }

    protected abstract T createItem(D datum);
    
    protected abstract DatumReader<D> createDatumReader();

    @Override
    protected void doClose() throws Exception {
        if (reader != null) {
            reader.close();
        }
    }
}
