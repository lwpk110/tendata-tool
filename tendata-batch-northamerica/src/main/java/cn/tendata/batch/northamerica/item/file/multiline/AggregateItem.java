package cn.tendata.batch.northamerica.item.file.multiline;

public class AggregateItem<T> {

    private T item;
    private boolean header = false;
    private boolean footer = false;

    public AggregateItem(T item) {
        super();
        this.item = item;
    }

    public AggregateItem(T item, boolean header, boolean footer) {
        this(item);
        this.header = header;
        this.footer = footer;
    }

    public T getItem() {
        return item;
    }

    public boolean isHeader() {
        return header;
    }
    
    public boolean isFooter() {
        return footer;
    }
}
