package cn.tendata.batch.northamercia.item.file.multiline;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Test;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.core.io.ClassPathResource;

import cn.tendata.batch.northamerica.item.file.multiline.FixedLengthCompositeLineTokenizer;
import cn.tendata.batch.support.YamlConfigOptionsResolver;

public class FixedLengthCompositeLineTokenizerTest {

    private FixedLengthCompositeLineTokenizer tokenizer;
    
    @Before
    public void setup(){
        YamlConfigOptionsResolver resolver = new YamlConfigOptionsResolver();
        resolver.setResource(new ClassPathResource("northamerica/imports/parse.yml"));
        tokenizer = new FixedLengthCompositeLineTokenizer(resolver);
    }
    
    @Test
    public void testTokenize(){
        String line = "00NYKS2005769601                                    SHKK344747513516                                                                                    024E                                              140000012015040220160614                                                                                                                                                                              ";
        FieldSet fieldSet = tokenizer.tokenize(line);
        assertThat(fieldSet.readString(0), is("00"));
        assertThat(fieldSet.readString("MasterBOLNumber_00"), is("NYKS2005769601"));
        assertThat(fieldSet.readString("VoyageNumber_00"), is("024E"));
    }
}
