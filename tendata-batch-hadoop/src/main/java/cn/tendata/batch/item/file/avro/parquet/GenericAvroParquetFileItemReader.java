package cn.tendata.batch.item.file.avro.parquet;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.avro.Schema.Field;
import org.apache.avro.generic.GenericRecord;

public class GenericAvroParquetFileItemReader extends AbstractAvroParquetFileItemReader<Map<String, Object>, GenericRecord> {
    
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
