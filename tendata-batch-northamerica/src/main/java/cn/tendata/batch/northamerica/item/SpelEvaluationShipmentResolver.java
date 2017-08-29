package cn.tendata.batch.northamerica.item;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import cn.tendata.batch.support.SpelEvaluationSupport;

public class SpelEvaluationShipmentResolver extends SpelEvaluationSupport implements ShipmentResolver<List<Map<String, Object>>> {

    public SpelEvaluationShipmentResolver(String expression) {
        super(expression);
    }

    @Override
    public Shipment resolve(List<Map<String, Object>> items) {
        if(items.size() == 1){
        	Map<String, Object> map = items.get(0);
        	Shipment item = new Shipment(map);
        	if(getParsedExpression().getValue(map, boolean.class)){
        		item.setChildren(Collections.emptyList());
        	}
            return item;
        }
        
        Shipment parent = null;
        List<Shipment> children = new ArrayList<>(items.size());
        for (Map<String, Object> map : items) {
            if(getParsedExpression().getValue(map, boolean.class)){
                parent = new Shipment(map);
            }
            else{
                Shipment item = new Shipment(map);
                children.add(item);
            }
        }
        if(parent != null){
            for (Shipment child : children) {
                child.setParent(parent);
            }
            parent.setChildren(children);
        }
        return parent;
    }
}
