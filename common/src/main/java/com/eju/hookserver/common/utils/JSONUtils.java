package com.eju.hookserver.common.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.collections4.CollectionUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * @author qiushengming
 */
public class JSONUtils {

    public static JSONArray getJsonArrayByKey(JSONObject var, String keys) {
        List<String> keyList = new ArrayList<>(Arrays.asList(keys.split("\\.")));
        JSONArray array = new JSONArray();
        getJsonArrayByKey(var, keyList, 0, array);
        return array;
    }

    /**
     * 根据key获取指定结果 key样例
     *
     * @param var  json
     * @param keys 样例：data.groups.items
     * @return json
     */
    public static JSON getJsonByKey(JSONObject var, String keys) {
        return (JSON) getObjectByKey(var, keys);
    }

    public static String getStringByKey(JSONObject var, String keys) {
        Object var1 = getObjectByKey(var, keys);
        if (var1 instanceof String) {
            return (String) var1;
        }
        if (var1 instanceof Long) {
            return ((Long) var1).toString();
        }

        if (var1 instanceof Integer) {
            return ((Integer) var1).toString();
        }
        return var1.toString();
    }

    private static Object getObjectByKey(JSONObject var, String keys) {
        List<String> keyList = new ArrayList<>(Arrays.asList(keys.split("\\.")));

        return getJsonByKey(var, keyList);
    }

    private static Object getJsonByKey(JSONObject var, List<String> keys) {
        String key = keys.get(0);
        keys.remove(key);

        if (var.containsKey(key)) {
            Object var1 = var.get(key);
            if (CollectionUtils.isEmpty(keys)) {
                return var1;
            }
            if (var1 instanceof JSONArray) {
                var = ((JSONArray) var1).getJSONObject(0);
            } else {
                var = (JSONObject) var1;
            }
            return getJsonByKey(var, keys);
        }

        return var;
    }

    private static void getJsonArrayByKey(Object var, List<String> keys, Integer index, JSONArray array) {
        String key = keys.get(index);

        if (!(var instanceof JSONObject)) {
            return;
        }

        JSONObject varJson = (JSONObject) var;

        if (varJson.containsKey(key)) {
            Object var1 = varJson.get(key);

            if (index + 1 == keys.size()) {
                if (var1 instanceof JSONArray) {
                    array.addAll((JSONArray) var1);
                } else {
                    array.add(var1);
                }
                return;
            }

            if (var1 instanceof JSONArray) {
                ((JSONArray) var1).forEach(var2 -> getJsonArrayByKey(var2, keys, index + 1, array));
            } else {
                getJsonArrayByKey(var1, keys, index + 1, array);
            }

        }
    }
}
