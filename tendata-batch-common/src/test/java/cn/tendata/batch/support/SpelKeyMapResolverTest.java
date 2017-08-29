package cn.tendata.batch.support;

import static org.hamcrest.Matchers.hasEntry;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;

import cn.tendata.batch.support.KeyMap;
import cn.tendata.batch.support.SpelKeyMapResolver;

public class SpelKeyMapResolverTest {

    private SpelKeyMapResolver resolver;
    
    @Before
    public void setup(){
        resolver = new SpelKeyMapResolver("#root['key']");
        resolver.setResource(new ClassPathResource("test.json"));
    }
    
    @Test
    public void testResolve(){
        KeyMap keyMap = resolver.resolve();
        assertNotNull(keyMap);
        assertThat(keyMap.get("001"), hasEntry("value", "abc"));
        assertThat(keyMap.get("002"), hasEntry("value", "efg"));
    }
}
