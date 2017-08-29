package cn.tendata.batch.item.mapping;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

import java.util.LinkedHashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import cn.tendata.batch.item.mapping.MappingFieldResultMapper;
import cn.tendata.batch.support.YamlConfigOptionsResolver;

public class MappingFieldResultMapperTest {

    private MappingFieldResultMapper resultMapper = new MappingFieldResultMapper();
    
    @Before
    public void setup(){
        YamlConfigOptionsResolver configOptionsResolver = new YamlConfigOptionsResolver();
        configOptionsResolver.setResource(new ClassPathResource("test.yml"));
        resultMapper.setConfigOptionsResolver(configOptionsResolver);
        resultMapper.setEvaluationContext(new StandardEvaluationContext());
    }
    
    @Test
    public void testMapResult(){
        Map<String, Object> item = new LinkedHashMap<>();
        item.put("Actual_Arrival_Date_10", "20160101");
        item.put("Shipper_Name_30", "Test Shipper");
        item.put("Shipper_Address_Line_1_30", "Address 1");
        item.put("Shipper_Address_Line_2_30", "Address 2");
        item.put("Shipper_Address_Line_3_30", "Address 3");
        item.put("Shipper_Address_Line_4_30", "Address 4");
        item.put("Weight_10", "100.5");
        item.put("Container_Number_60", "BMOU5179333");
        
        Map<String, Object> resultMap = resultMapper.mapResult(item);
        assertThat(resultMap.get("shipper"), is("Test Shipper"));
        assertThat(resultMap.get("shipperAddrs"), is(
                new Object[] { "Address 1", "Address 2", "Address 3", "Address 4" }));
        assertThat((float)resultMap.get("wt"), is(100.5F));
        assertThat(resultMap.get("wtU"), is("KG"));
        assertThat(resultMap.get("teu"), is(11F));
        assertThat(resultMap.get("lloyds"), nullValue());
        assertNotNull(resultMap);
    }
}
