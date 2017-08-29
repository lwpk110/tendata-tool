package cn.tendata.batch.northamerica.item;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.support.SingleItemPeekableItemReader;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;

public class AggregateGroupItemReader<T> implements ItemReader<List<T>> {

    private static final Log LOG = LogFactory.getLog(AggregateGroupItemReader.class);

    private SingleItemPeekableItemReader<T> delegate;
    
    private final ExpressionParser parser = new SpelExpressionParser();
    private final Expression aggregateExpression;
    
    public AggregateGroupItemReader(String aggregateExpression) {
        this.aggregateExpression = parser.parseExpression(aggregateExpression);
    }

    @Override
    public List<T> read() throws Exception {
        ResultHolder holder = new ResultHolder();

        while (process(delegate.read(), delegate.peek(), holder)) {
            continue;
        }

        if (!holder.isExhausted()) {
            return holder.getRecords();
        }
        else {
            return null;
        }
    }
    
    private boolean process(T current, T next, ResultHolder holder) {
        // finish processing if we hit the end of file
        if (current == null) {
            LOG.debug("Exhausted ItemReader");
            holder.setExhausted(true);
            return false;
        }
        
        if(!aggregateExpression.getValue(new WrapItem(current, next), boolean.class)){
            LOG.debug("End of record detected");
            holder.addRecord(current);
            return false;
        }
        
        // add a simple record to the current collection
        LOG.debug("Mapping: " + current);
        holder.addRecord(current);
        return true;
    }
    
    public void setDelegate(SingleItemPeekableItemReader<T> delegate) {
        this.delegate = delegate;
    }
    
    public class WrapItem {
        private final T current;
        private final T next;
        
        public WrapItem(T current, T next){
            this.current = current;
            this.next = next;
        }
        
        public T getCurrent() {
            return current;
        }
        
        public T getNext() {
            return next;
        }
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
