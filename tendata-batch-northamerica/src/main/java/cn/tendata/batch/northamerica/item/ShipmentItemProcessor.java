package cn.tendata.batch.northamerica.item;

import java.util.Map;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

import cn.tendata.batch.item.mapping.ResultMapper;

public class ShipmentItemProcessor implements ItemProcessor<Map<String, Object>, Map<String, Object>>, InitializingBean {

    private ShipmentResolver<Map<String, Object>> shipmentResolver;
    private ResultMapper resultMapper;
    
    @Override
    public void afterPropertiesSet() throws Exception {
        Assert.notNull(shipmentResolver, "'shipmentResolver' must not be null");
        Assert.notNull(resultMapper, "'resultMapper' must not be null");
    }
    
    @Override
    public Map<String, Object> process(Map<String, Object> item) throws Exception {
        Shipment shipment = shipmentResolver.resolve(item);
        return resultMapper.mapResult(shipment);
    }
    
    public void setShipmentResolver(ShipmentResolver<Map<String, Object>> shipmentResolver) {
        this.shipmentResolver = shipmentResolver;
    }

    public void setResultMapper(ResultMapper resultMapper) {
        this.resultMapper = resultMapper;
    }
}
