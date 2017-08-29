package cn.tendata.batch.item.file.avro;

import java.util.Map;
import java.util.Map.Entry;

import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericDatumWriter;
import org.apache.avro.generic.GenericRecord;
import org.apache.avro.io.DatumWriter;

public class GenericAvroFileItemWriter extends AbstractAvroFileItemWriter<Map<String, Object>, GenericRecord> {

    @Override
    protected DatumWriter<GenericRecord> createDatumWriter() {
        return new GenericDatumWriter<>(getSchema());
    }

    @Override
    protected GenericRecord createDatum(Map<String, Object> item) {
        GenericRecord record = new GenericData.Record(getSchema());
        for (Entry<String, Object> entry : item.entrySet()) {
            record.put(entry.getKey(), entry.getValue());
        }
        return record;
    }
}
