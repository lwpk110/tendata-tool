package cn.tendata.batch.northamerica.item.file.sorting;

import java.util.Comparator;
import java.util.Map;

import org.apache.commons.lang3.ObjectUtils;

public class MapBeanComparator implements Comparator<Map<String, Object>> {

    private final String[] compareKeys;
    
    public MapBeanComparator(String[] compareKeys) {
        this.compareKeys = compareKeys;
    }

	@Override
    public int compare(Map<String, Object> o1, Map<String, Object> o2) {
		int result = 0;
    	for (String key : compareKeys) {
    		result = ObjectUtils.compare((String)o1.get(key), (String)o2.get(key));
    		if(result == 0){
    			continue;
    		}
    		else{
    			break;
    		}
		}
        return result;
    }
}
