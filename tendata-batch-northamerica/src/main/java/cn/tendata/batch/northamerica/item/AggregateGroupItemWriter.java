package cn.tendata.batch.northamerica.item;

import java.util.List;

import org.springframework.batch.item.ItemWriter;

public class AggregateGroupItemWriter<T> implements ItemWriter<List<T>> {

    private ItemWriter<T> delegate;
    
    @Override
    public void write(List<? extends List<T>> items) throws Exception {
        for (List<T> subItems : items) {
            delegate.write(subItems);
        }
    }
    
    public void setDelegate(ItemWriter<T> delegate) {
        this.delegate = delegate;
    }
}
