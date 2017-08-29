package cn.tendata.batch.northamerica.item.file.multiline.support;

import static org.junit.Assert.assertNotNull;

import java.util.Collection;

import org.junit.Before;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;

import cn.tendata.batch.northamerica.item.file.multiline.parsing.FieldDescriptor;
import cn.tendata.batch.support.YamlConfigOptionsResolver;

public class FieldDescriptorResolverTest {

    private YamlConfigOptionsResolver resolver;
    
    @Before
    public void setup(){
        resolver = new YamlConfigOptionsResolver();
        resolver.setResource(new ClassPathResource("northamerica/imports/parse.yml"));
    }
    
    @Test
    public void testParse(){
        Collection<FieldDescriptor> fieldDescriptors = resolver.resolve(FieldDescriptor.class);
        assertNotNull(fieldDescriptors);
    }
}
