package cn.tendata.batch.northamerica.item.file.multiline;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.util.StringUtils;

import static cn.tendata.batch.northamerica.item.file.multiline.FixedLengthCompositeLineTokenizer.MULTI_MARK;

public class AggregateItemProcessor implements ItemProcessor<List<FieldSet>, Map<String, Object>> {

    private boolean preserveNull;

    @Override
    public Map<String, Object> process(List<FieldSet> item) throws Exception {
        Map<String, Object> resultMap = new LinkedHashMap<>();
        for (FieldSet fieldSet : item) {
            for (String name : fieldSet.getNames()) {
                if (name.startsWith("__")) {
                    continue;
                }
                String value = fieldSet.readString(name);
                resolveMulti(name, resultMap, value);

             /*   if (StringUtils.hasText(value)) {
                    resolveMulti(name, resultMap, value);
                } else {
                    if (preserveNull) {
                        resultMap.put(name, null);
                    }
                }*/
            }
        }
        return resultMap;
    }

    private void resolveMulti(String name, Map<String, Object> resultMap, String value) {
        if (name.startsWith(MULTI_MARK)) {
            String realName = name.substring(1);
            Object mapValue = resultMap.get(realName);
            List<Object> list = new ArrayList<>();
            if (null != mapValue) {
                list = (List<Object>) mapValue;
            }
            list.add(value);
            resultMap.put(realName, list);
        } else {
            if (StringUtils.hasText(value)) {
                resultMap.put(name, value);
            } else {
                if (preserveNull) {
                    resultMap.put(name, null);
                }
            }
        }
    }

    public void setPreserveNull(boolean preserveNull) {
        this.preserveNull = preserveNull;
    }
}
