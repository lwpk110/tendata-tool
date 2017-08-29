package cn.tendata.batch.support;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.springframework.core.io.Resource;
import org.springframework.util.Assert;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

public class SpelKeyMapResolver extends SpelEvaluationSupport {

    private final ObjectMapper objectMapper;
    
    private Resource resource;
    
    public SpelKeyMapResolver(String keyExpression) {
        this(keyExpression, new ObjectMapper()
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false));
    }
    
    public SpelKeyMapResolver(String keyExpression, ObjectMapper objectMapper){
        super(keyExpression);
        this.objectMapper = objectMapper;
    }

    public void setResource(Resource resource) {
        this.resource = resource;
    }

    public KeyMap resolve(){
        Assert.notNull(resource, "Input resource must be set");
        
        try {
            KeyMap keyMap = new KeyMap();
            Collection<Map<String, Object>> items = objectMapper.readValue(resource.getInputStream(), 
                    objectMapper.getTypeFactory().constructCollectionType(List.class, Map.class));
            for (Map<String, Object> map : items) {
                keyMap.put(getParsedExpression().getValue(map, String.class), map);
            }
            return keyMap;
        } catch (IOException e) {
            throw new IllegalStateException("parse error, file:" + resource.getFilename(), e);
        }
    }
}
