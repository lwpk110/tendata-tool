package cn.tendata.batch.item.file.avro;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.avro.Schema;
import org.apache.avro.Schema.Field;
import org.apache.avro.generic.GenericDatumReader;
import org.apache.avro.generic.GenericRecord;
import org.apache.avro.io.DatumReader;
import org.springframework.core.io.Resource;
import org.springframework.util.Assert;

public class GenericAvroFileItemReader extends AbstractAvroFileItemReader<Map<String, Object>, GenericRecord> {

    private Schema schema;
    
    public void setSchema(Resource schema) throws IOException {
        this.schema = new Schema.Parser().parse(schema.getInputStream());
    }

    @Override
    protected DatumReader<GenericRecord> createDatumReader() {
        Assert.notNull(schema, "The schema must be set");
        
        return new GenericDatumReader<>(schema);
    }

    @Override
    protected Map<String, Object> createItem(GenericRecord datum) {
        List<Field> fields = datum.getSchema().getFields();
        Map<String, Object> item = new LinkedHashMap<>(fields.size());
        for (Field field : fields) {
            item.put(field.name(), datum.get(field.name()));
        }
        return item;
    }
}
