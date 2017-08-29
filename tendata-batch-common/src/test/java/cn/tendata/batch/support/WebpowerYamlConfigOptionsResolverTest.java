package cn.tendata.batch.support;

import java.util.Collection;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;

import static org.junit.Assert.assertNotNull;

public class WebpowerYamlConfigOptionsResolverTest {


    private YamlConfigOptionsResolver resolver;

    @Before
    public void setup(){
        resolver = new YamlConfigOptionsResolver();
        resolver.setResource(new ClassPathResource("webpower-parse.yml"));
    }

    @Test
    public void testResolve(){
        Collection<WebpowerYamlConfigOptionsResolverTest.WebpowerDto>
            fields = resolver.resolve(WebpowerYamlConfigOptionsResolverTest.WebpowerDto.class);
        assertNotNull(fields);
    }

    enum FieldType {
        DATETIME,FLOAT,STR
    }

    static class WebpowerDto {

        public String webpowerVersion;
        public List<String> comments;
        public List<String> fields;
    }
}
