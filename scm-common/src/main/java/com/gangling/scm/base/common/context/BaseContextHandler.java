package com.gangling.scm.base.common.context;

import com.gangling.scm.base.common.ConstantValue;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.MDC;

import java.util.HashMap;
import java.util.Map;

@Data
public class BaseContextHandler {
    public static final String USER_ID = "userId";
    public static final String LOGIN_NAME = "loginName";

    public static ThreadLocal<Map<String, String>> threadLocal = new ThreadLocal<>();

    public static Long getUserId() {
        String userId = get(USER_ID);
        return StringUtils.isEmpty(userId) ? 0L : Long.valueOf(userId);
    }

    public static String getLoginName() {
        String username = get(LOGIN_NAME);
        return StringUtils.isEmpty(username) ? "SYSTEM" : username;
    }

    private static Map<String, String> init() {
        Map<String, String> map = getAll();
        if (map == null) {
            map = new HashMap<>();
            threadLocal.set(map);
        }
        return map;
    }

    public static void put(String key, String value) {
        Map<String, String> map = init();
        map.put(key, value);
    }

    public static void putAll(Map<String, String> anotherMap) {
        Map<String, String> map = init();
        map.putAll(anotherMap);
    }

    public static String get(String key) {
        Map<String, String> map = init();
        return map.get(key);
    }

    public static String getTraceId() {
        Map<String, String> map = init();
        return map.getOrDefault(ConstantValue.X_GLOBAL_REQUEST_ID, MDC.get(ConstantValue.X_GLOBAL_REQUEST_ID));
    }

    public static Map<String, String> getAll() {
        return threadLocal.get();
    }

    public static void remove() {
        threadLocal.remove();
    }

    public static void del(String key) {
        getAll().remove(key);
    }
}
