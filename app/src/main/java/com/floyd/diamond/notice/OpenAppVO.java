package com.floyd.diamond.notice;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by floyd on 16-1-10.
 */
public class OpenAppVO {

    public int notifyType;
    public String packageName;
    public Map<String, String> params = new HashMap<String, String>();

    public void put(String key, String o) {
        params.put(key, o);
    }
}
