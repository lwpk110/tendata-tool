package cn.tendata.batch.item.file.transform;

import java.util.Map;

import org.springframework.batch.item.file.transform.LineAggregator;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Map2JSONLineAggregator implements LineAggregator<Map<String, Object>>{

	private ObjectMapper mapper = new ObjectMapper();
	
	@Override
	public String aggregate(Map<String, Object> item) {
		try {
			return mapper.writeValueAsString(item);
		} catch (JsonProcessingException e) {
			throw new IllegalArgumentException("line aggregator error: " + item, e);
		}
	}
}
