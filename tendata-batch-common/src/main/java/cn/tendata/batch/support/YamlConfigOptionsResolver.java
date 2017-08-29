package cn.tendata.batch.support;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

import org.springframework.core.io.Resource;
import org.springframework.util.Assert;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

public class YamlConfigOptionsResolver implements ConfigOptionsResolver {

    private final ObjectMapper objectMapper;
    private Resource resource;
    
    public YamlConfigOptionsResolver(){
        this(new ObjectMapper(new YAMLFactory())
                .configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true)
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false));
    }
    
    public YamlConfigOptionsResolver(ObjectMapper objectMapper){
        this.objectMapper = objectMapper;
    }
    
    public void setResource(Resource resource) {
        this.resource = resource;
    }

    @Override
    public <T> Collection<T> resolve(Class<T> type) {
        Assert.notNull(resource, "Input resource must be set");
        
        try {
            return objectMapper.readValue(resource.getInputStream(), 
                    objectMapper.getTypeFactory().constructCollectionType(List.class, type));
        } catch (IOException e) {
            throw new IllegalStateException("parse error, file:" + resource.getFilename(), e);
        }
    }
}
