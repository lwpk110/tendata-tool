package cn.tendata.batch.support;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.core.io.Resource;
import org.springframework.util.Assert;

public class ResourceFilePathFactoryBean implements FactoryBean<String> {

    private Resource resource;
    
    @Override
    public String getObject() throws Exception {
        Assert.notNull(resource, "The 'resource' must not be null");
        
        return resource.getFile().getAbsolutePath();
    }

    @Override
    public Class<?> getObjectType() {
        return String.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    public void setResource(Resource resource) {
        this.resource = resource;
    }
}
