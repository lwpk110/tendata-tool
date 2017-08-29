package cn.tendata.batch.support;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("serial")
public class KeyMap extends HashMap<String, Map<String, Object>> {

    public KeyMap() {
        super();
    }

    public KeyMap(int initialCapacity) {
        super(initialCapacity);
    }

    public KeyMap(Map<? extends String, ? extends Map<String, Object>> m) {
        super(m);
    }
}
