package cn.tendata.batch.northamerica.item.file.multiline;

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.support.SingleItemPeekableItemReader;

public class AggregateItemReader<T> implements ItemReader<List<T>> {

    private static final Log LOG = LogFactory.getLog(AggregateItemReader.class);

    private SingleItemPeekableItemReader<AggregateItem<T>> delegate;

    private final Object lock = new Object();

    @Override
    public List<T> read() throws Exception {
        synchronized (lock) {
            ResultHolder holder = new ResultHolder();
            while (process(delegate.read(), delegate.peek(), holder)) {
                continue;
            }
            if (holder.isExhausted() && holder.getRecords().size() < 1) {
                return null;
            }
            return holder.getRecords();
        }
    }

    private boolean process(AggregateItem<T> current, AggregateItem<T> next, ResultHolder holder) {
        // finish processing if we hit the end of file
        if (current == null) {
            LOG.debug("Exhausted ItemReader");
            holder.setExhausted(true);
            return false;
        }

        // start a new collection
        if (current.isHeader()) {
            LOG.debug("Start of new record detected");
            holder.addRecord(current.getItem());
            return true;
        }

        // mark we are finished with current collection
        if (current.isFooter()) {
            LOG.debug("End of record detected");
            return false;
        }

        if (next != null && next.isHeader()) {
            LOG.debug("End of record detected");
            holder.addRecord(current.getItem());
            return false;
        }

        // add a simple record to the current collection
        LOG.debug("Mapping: " + current);
        holder.addRecord(current.getItem());
        return true;
    }

    public void setDelegate(SingleItemPeekableItemReader<AggregateItem<T>> delegate) {
        this.delegate = delegate;
    }

    private class ResultHolder {
        private List<T> records = new ArrayList<>();
        private boolean exhausted = false;

        public List<T> getRecords() {
            return records;
        }

        public boolean isExhausted() {
            return exhausted;
        }

        public void addRecord(T record) {
            records.add(record);
        }

        public void setExhausted(boolean exhausted) {
            this.exhausted = exhausted;
        }
    }

}
