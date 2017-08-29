package cn.tendata.batch.item.file.avro.parquet;

import java.util.Map;
import java.util.Map.Entry;

import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericRecord;

public class GenericAvroParquetFileItemWriter extends AbstractAvroParquetFileItemWriter<Map<String, Object>, GenericRecord> {

    @Override
    protected GenericRecord createDatum(Map<String, Object> item) {
        GenericRecord record = new GenericData.Record(getSchema());
        for (Entry<String, Object> entry : item.entrySet()) {
            record.put(entry.getKey(), entry.getValue());
        }
        return record;
    }
}
