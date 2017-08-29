package cn.tendata.batch.northamerica.item;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("serial")
public class Shipment extends HashMap<String, Object> {

    private Shipment parent;
    private Collection<Shipment> children = null;
    
    public Shipment() {
        super();
    }

    public Shipment(Map<? extends String, ? extends Object> m) {
        super(m);
    }

    public Shipment getParent() {
        return parent;
    }

    public void setParent(Shipment parent) {
        this.parent = parent;
    }

    public Collection<Shipment> getChildren() {
        return children;
    }

    public void setChildren(Collection<Shipment> children) {
        this.children = Collections.unmodifiableCollection(children);
    }
}
