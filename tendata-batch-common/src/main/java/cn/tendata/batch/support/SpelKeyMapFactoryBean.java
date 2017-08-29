package cn.tendata.batch.support;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.io.Resource;
import org.springframework.util.Assert;

public class SpelKeyMapFactoryBean implements FactoryBean<KeyMap>, InitializingBean {

    private Resource resource; 
    
    private String keyExpression;
    
    private SpelKeyMapResolver resolver;
    
    private KeyMap keyMap;
    
    public void setResource(Resource resource) {
        this.resource = resource;
    }
    
    public void setKeyExpression(String keyExpression) {
        this.keyExpression = keyExpression;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Assert.notNull(keyExpression, "'keyExpression' must not be null");
        Assert.notNull(resource, "'resource' must not be null");
        
        resolver = new SpelKeyMapResolver(keyExpression);
        resolver.setResource(resource);
        keyMap = resolver.resolve();
    }
    
    @Override
    public KeyMap getObject() throws Exception {
        return keyMap;
    }

    @Override
    public Class<?> getObjectType() {
        return KeyMap.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }
}
