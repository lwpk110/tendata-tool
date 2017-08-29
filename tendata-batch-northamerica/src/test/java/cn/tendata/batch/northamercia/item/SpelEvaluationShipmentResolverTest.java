package cn.tendata.batch.northamercia.item;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import cn.tendata.batch.northamerica.item.Shipment;
import cn.tendata.batch.northamerica.item.SpelEvaluationShipmentResolver;

public class SpelEvaluationShipmentResolverTest {

    private SpelEvaluationShipmentResolver resolver;
    
    @Before
    public void setup(){
        resolver = new SpelEvaluationShipmentResolver("{'1','2'}.contains(#root['type'])");
    }
    
    @Test
    public void testResolveWhenIsNotMaster(){
        List<Map<String, Object>> items = new ArrayList<>(2);
        Map<String, Object> item1 = new HashMap<>();
        item1.put("type", "3");
        item1.put("name", "item1");
        items.add(item1);
        Shipment item = resolver.resolve(items);
        assertNotNull(item);
    }
    
    @Test
    public void testResolveWhenIsMaster(){
        List<Map<String, Object>> items = new ArrayList<>(2);
        Map<String, Object> item1 = new HashMap<>();
        item1.put("type", "1");
        item1.put("name", "item1");
        items.add(item1);
        Map<String, Object> item2 = new HashMap<>();
        item2.put("type", "3");
        item2.put("name", "item2");
        items.add(item2);
        
        Shipment item = resolver.resolve(items);
        assertNotNull(item);
        assertThat(item.getChildren().size(), is(1));
    }
}
