package cn.tendata.batch.item.mapping;

import java.util.Map;

public interface ResultMapper {

    Map<String, Object> mapResult(Map<String, Object> item);
}
