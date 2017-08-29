package cn.tendata.batch.northamerica.item.data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.BasicQuery;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.util.Assert;

import com.mongodb.util.JSON;

import cn.tendata.batch.northamerica.item.Shipment;
import cn.tendata.batch.northamerica.item.ShipmentResolver;

public class MongoShipmentResolver implements ShipmentResolver<Map<String, Object>>, InitializingBean {

    private static final Pattern PLACEHOLDER = Pattern.compile("\\?(\\d+)");
    
    private MongoOperations template;
    private String query;
    private Class<? extends Map<String, Object>> type;
    private String collection;
    private List<String> parameterKeys = Collections.emptyList();
    
    public void setTemplate(MongoOperations template) {
        this.template = template;
    }
    
    public void setQuery(String query) {
        this.query = query;
    }
    
    public void setTargetType(Class<? extends Map<String, Object>> type) {
        this.type = type;
    }
    
    public void setCollection(String collection) {
        this.collection = collection;
    }
    
    public void setParameterKeys(List<String> parameterKeys) {
        this.parameterKeys = parameterKeys;
    }
    
    @Override
    public void afterPropertiesSet() throws Exception {
        Assert.state(template != null, "An implementation of MongoOperations is required.");
        Assert.state(query != null, "A query is required.");
        Assert.state(type != null, "A type to convert the input into is required.");
        Assert.state(collection != null, "A collection is required.");
    }
    
    @Override
    public Shipment resolve(Map<String, Object> item) {
        Shipment shipment = new Shipment(item);
        List<Object> parameterValues = createParameterValues(item);
        String populatedQuery = replacePlaceholders(query, parameterValues);
        Query mongoQuery = new BasicQuery(populatedQuery);
        Map<String, Object> parent = template.findOne(mongoQuery, type, collection);
        if(parent != null){
            shipment.setParent(new Shipment(parent));
        }
        
        return shipment;
    }
    
    private List<Object> createParameterValues(Map<String, Object> item){
        List<Object> values = new ArrayList<>(parameterKeys.size());
        for (Object key : parameterKeys) {
            values.add(item.get(key));
        }
        return values;
    }
    
    // Copied from StringBasedMongoQuery...is there a place where this type of logic is already exposed?
    private String replacePlaceholders(String input, List<Object> values) {
        Matcher matcher = PLACEHOLDER.matcher(input);
        String result = input;

        while (matcher.find()) {
            String group = matcher.group();
            int index = Integer.parseInt(matcher.group(1));
            result = result.replace(group, getParameterWithIndex(values, index));
        }

        return result;
    }

    // Copied from StringBasedMongoQuery...is there a place where this type of logic is already exposed?
    private String getParameterWithIndex(List<Object> values, int index) {
        return JSON.serialize(values.get(index));
    }
}
