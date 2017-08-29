package cn.tendata.batch.support;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.TreeTraversingParser;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.dataformat.yaml.YAMLParser;
import java.io.IOException;
import org.springframework.core.io.Resource;

public class YmalFactory<T> {

    private final YAMLFactory yamlFactory;
    private final ObjectMapper mapper;
    private final Class<T> klass;

    private  Resource resource;

    public void setResource(Resource resource) {
        this.resource = resource;
    }

    public YmalFactory(Class<T> klass) {
        this.klass = klass;
        this.yamlFactory = new YAMLFactory();
        this.mapper = new ObjectMapper();
        mapper.enable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

    }

    public T build() throws IOException {
        YAMLParser yamlParser = yamlFactory.createParser(resource.getInputStream());
        final JsonNode node = mapper.readTree(yamlParser);
        TreeTraversingParser treeTraversingParser = new TreeTraversingParser(node);
        return mapper.readValue(treeTraversingParser, klass);
    }

   /* public static void main(String[] args) {
        Contact contact = new Contact();
        YmalFc<Contact> ymalFc = new YmalFc<Contact>(Contact.class);
        try {
            contact = ymalFc.build(new FileConfigurationSourceProvider(), "contact.yml");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }*/
}