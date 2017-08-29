package cn.tendata.batch.northamerica.item.file.sorting;

import java.util.Map;

import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.core.io.Resource;

import cn.tendata.batch.item.file.transform.Map2JSONLineAggregator;
import cn.tendata.batch.northamerica.item.file.mapping.JsonLineMapper;

public class MapBeanIoFactory implements FlatFileItemIoFactory<Map<String, Object>> {

    @Override
    public FlatFileItemReader<Map<String, Object>> getReader(Resource r) {
        FlatFileItemReader<Map<String, Object>> reader = new FlatFileItemReader<>();
        reader.setResource(r);
        reader.setLineMapper(new JsonLineMapper());
        return reader;
    }

    @Override
    public FlatFileItemWriter<Map<String, Object>> getWriter(Resource r) {
        FlatFileItemWriter<Map<String, Object>> writer = new FlatFileItemWriter<>();
        writer.setResource(r);
        writer.setLineAggregator(new Map2JSONLineAggregator());
        return writer;
    }
}
