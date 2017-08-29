package cn.tendata.batch.northamerica.item;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

import cn.tendata.batch.item.mapping.ResultMapper;

public class AggregateShipmentItemProcessor implements ItemProcessor<List<Map<String, Object>>, List<Map<String, Object>>>, InitializingBean {

	private static final Log LOG = LogFactory.getLog(AggregateShipmentItemProcessor.class);
	
    private ShipmentResolver<List<Map<String, Object>>> shipmentResolver;
    private ResultMapper resultMapper;
    
    private boolean includeParent;
    
    @Override
    public void afterPropertiesSet() throws Exception {
        Assert.notNull(shipmentResolver, "'shipmentResolver' must not be null");
        Assert.notNull(resultMapper, "'resultMapper' must not be null");
    }

    @Override
    public List<Map<String, Object>> process(List<Map<String, Object>> item) throws Exception {
        Shipment shipment = shipmentResolver.resolve(item);
        if(shipment == null){
        	LOG.warn("removed orphan item:" + item);
        	return null;
        }
        List<Map<String, Object>> resultList = new ArrayList<>(item.size());
        if(shipment.getChildren() == null){
            resultList.add(resultMapper.mapResult(shipment));
        }
        else{
            for (Shipment child : shipment.getChildren()) {
                resultList.add(resultMapper.mapResult(child));
            }
            if(includeParent){
                resultList.add(resultMapper.mapResult(shipment));
            }
        }
        return resultList;
    }
    
    public void setShipmentResolver(ShipmentResolver<List<Map<String, Object>>> shipmentResolver) {
        this.shipmentResolver = shipmentResolver;
    }

    public void setResultMapper(ResultMapper resultMapper) {
        this.resultMapper = resultMapper;
    }

    public void setIncludeParent(boolean includeParent) {
        this.includeParent = includeParent;
    }
}
