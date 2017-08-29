package cn.tendata.batch.support;

import static org.junit.Assert.assertNotNull;

import java.util.Collection;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;

import cn.tendata.batch.support.YamlConfigOptionsResolver;

public class YamlConfigOptionsResolverTest {

    private YamlConfigOptionsResolver resolver;
    
    @Before
    public void setup(){
        resolver = new YamlConfigOptionsResolver();
        resolver.setResource(new ClassPathResource("test.yml"));
    }
    
    @Test
    public void testResolve(){
        Collection<Field> fields = resolver.resolve(Field.class);
        assertNotNull(fields);
    }
    
    enum FieldType {
        DATETIME,FLOAT,STR
    }
    
    static class Field {
        
        public String name;
        public FieldType type;
        public List<?> sources;
        public Object defaultValue;
    }
}
