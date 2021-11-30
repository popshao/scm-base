package com.gangling.scm.base.utils;


import com.gangling.scm.base.common.annotation.SFunction;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.Column;
import java.lang.invoke.SerializedLambda;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

@Slf4j
public class CommonUtil {

    public static final Collection NULL_COLLECTION = new NullCollection();

    public static boolean isEmpty(Object obj) {
        if (obj == null) {
            return true;
        }
        if (obj.toString().trim().isEmpty()) {
            return true;
        }
        if ((obj instanceof Collection)) {
            return ((Collection) obj).size() == 0;
        }
        if ((obj instanceof Map)) {
            return ((Map) obj).size() == 0;
        }
        return false;
    }

    public static boolean isNotEmpty(Object obj) {
        return !isEmpty(obj);
    }

    /**
     * <pre>
     * list 生成一个Map<K,List<V>>
     * </pre>
     */
    public static <K, V, E> Map<K, List<V>> listforListMap(List<E> list,
                                                           String keyProp, String valueProp) {
        Map<K, List<V>> rs = new HashMap<K, List<V>>();
        if (CommonUtil.isEmpty(list)) {
            return rs;
        }
        list.removeAll(nullCollection());
        rs = new HashMap<K, List<V>>(list.size());
        V value = null;
        for (E object : list) {
            K key = getFildByName(keyProp, object);
            if (CommonUtil.isEmpty(valueProp)) {
                value = (V) object;
            } else {
                value = (V) getFildByName(valueProp, object);
            }
            if (key == null) {
                continue;
            }
            List<V> values = rs.get(key);
            if (values == null) {
                values = new ArrayList<V>();
            }
            values.add(value);
            rs.put(key, values);
        }
        return rs;
    }

    /**
     * <pre>
     * list ����һ��Map<K,List<V>>
     * </pre>
     *
     * @param <K>
     * @param <V>
     * @param <E>
     * @param list
     * @param keyProp
     * @param valueProp
     * @return
     * @author lei.zhang
     */
    public static <K, V, E> Map<K, List<V>> listforLinkedListMap(List<E> list,
                                                                 String keyProp, String valueProp) {
        Map<K, List<V>> rs = new HashMap<K, List<V>>();
        if (CommonUtil.isEmpty(list)) {
            return rs;
        }
        list.removeAll(nullCollection());
        rs = new HashMap<K, List<V>>(list.size());
        V value = null;
        for (E object : list) {
            K key = getFildByName(keyProp, object);
            if (CommonUtil.isEmpty(valueProp)) {
                value = (V) object;
            } else {
                value = (V) getFildByName(valueProp, object);
            }
            List<V> values = rs.get(key);
            if (values == null) {
                values = new LinkedList<V>();
            }
            values.add(value);
            rs.put(key, values);
        }
        return rs;
    }

    /**
     * listToMap
     *
     * @param list
     * @param keyProp
     * @param valueProp
     * @return Map<Object, Object>
     * @throws Exception
     * @author lei.zhang
     * @date 2018��6��7��
     */
    public static <K, V, E> Map<K, V> listForMap(List<E> list, String keyProp, String valueProp) {
        Map<K, V> rs = new HashMap<K, V>(list.size());
        if (CommonUtil.isEmpty(list)) {
            return rs;
        }

        list.removeAll(nullCollection());
        for (E object : list) {
            K key = getFildByName(keyProp, object);
            Object value = null;
            if (CommonUtil.isEmpty(valueProp)) {
                value = object;
            } else {
                value = getFildByName(valueProp, object);
            }

            if (value != null) {
                rs.put(key, (V) value);
            }
        }

        return rs;
    }

    /**
     * listForList
     *
     * @param list
     * @param keyProp
     * @return
     */
    public static <K, E> List<K> listForList(List<E> list, String keyProp) {
        List<K> rs = new ArrayList<>(list.size());
        if (CommonUtil.isEmpty(list)) {
            return rs;
        }

        list.removeAll(nullCollection());
        for (E object : list) {
            K key = getFildByName(keyProp, object);
            rs.add(key);
        }

        return rs;
    }

    /**
     * listForList
     *
     * @param list
     * @param keyProp
     * @return
     */
    public static <K, E> List<K> listForNoRepeatList(List<E> list, String keyProp) {
        List<K> rs = new ArrayList<>();
        if (CommonUtil.isEmpty(list)) {
            return rs;
        }

        list.removeAll(nullCollection());
        for (E object : list) {
            K key = getFildByName(keyProp, object);
            rs.add(key);
        }
        if (CommonUtil.isNotEmpty(rs)) {
            Set<K> set = new HashSet<>(rs);
            rs = new ArrayList<>(set);
        }

        return rs;
    }

    public static <K, E> Set<K> listForSet(List<E> list, String keyProp) {
        Set<K> rs = new HashSet<>();
        if (CommonUtil.isEmpty(list)) {
            return rs;
        }

        list.removeAll(nullCollection());
        for (E object : list) {
            K key = getFildByName(keyProp, object);
            rs.add(key);
        }
        return rs;
    }

    public static final <T> Collection<T> nullCollection() {
        return (List<T>) NULL_COLLECTION;
    }

    public static <K, V, T> K getFildByName(String key, T obj) {
        Field field = null;
        Class<?> clazz = obj.getClass();
        for (; clazz != Object.class; clazz = clazz.getSuperclass()) {
            try {
                field = clazz.getDeclaredField(key);
                field.setAccessible(true);
                return (K) field.get(obj);
            } catch (Exception e) {
            }
        }
        return null;
    }

    public static <T> void setFildValueByName(String key, Object value, T obj) {
        Field field = null;
        Class<?> clazz = obj.getClass();
        for (; clazz != Object.class; clazz = clazz.getSuperclass()) {
            try {
                field = clazz.getDeclaredField(key);
                field.setAccessible(true);
                field.set(obj, value);
                break;
            } catch (Exception e) {
                // log.error(e.getMessage());
            }
        }
    }

    public static String genKey(Object... keys) {
        return genKey('_', keys);
    }

    public static String genKey(Character separatorChar, Object... keys) {
        StringBuilder result = new StringBuilder();
        for (Object obj : keys) {
            if (isNotEmpty(result)) {
                result.append(separatorChar);
            }
            result.append(obj);
        }
        return result.toString();
    }

    /**
     * @return
     */
    public static <E, K, V> Map<String, V> listForMap(List<E> list, String keyProp, String valueProp, String... keyProps) {
        Map<String, V> rs = new HashMap<>();
        if (CommonUtil.isEmpty(list)) {
            return rs;
        }

        list.removeAll(nullCollection());
        for (E object : list) {
            Object obj = getFildByName(keyProp, object);
            String key = Objects.isNull(obj) ? "" : obj.toString();
            if (isNotEmpty(keyProps)) {
                for (String tempKeyProp : keyProps) {
                    String tempKey = getFildByName(tempKeyProp, object) == null ? "" : getFildByName(tempKeyProp, object).toString();
                    key = genKey(key, tempKey);
                }
            }

            Object value = null;
            if (CommonUtil.isEmpty(valueProp)) {
                value = object;
            } else {
                value = getFildByName(valueProp, object);
            }

            if (value != null) {
                rs.put(key, (V) value);
            }
        }

        return rs;
    }

    /**
     * <pre>
     * list 生成一个Map<K,List<V>>
     * </pre>
     */
    public static <K, V, E> Map<String, List<V>> listforListMap(List<E> list,
                                                                String keyProp, String valueProp, String... keyProps) {
        Map<String, List<V>> rs = new HashMap<>();
        if (CommonUtil.isEmpty(list)) {
            return rs;
        }
        list.removeAll(nullCollection());
        rs = new HashMap<>(list.size());
        V value = null;
        for (E object : list) {
            String key = getFildByName(keyProp, object) == null ? "" : getFildByName(keyProp, object).toString();
            if (isNotEmpty(keyProps)) {
                for (String tempKeyProp : keyProps) {
                    String tempKey = getFildByName(tempKeyProp, object) == null ? "" : getFildByName(tempKeyProp, object).toString();
                    key = genKey(key, tempKey);
                }
            }
            if (CommonUtil.isEmpty(valueProp)) {
                value = (V) object;
            } else {
                value = (V) getFildByName(valueProp, object);
            }
            List<V> values = rs.get(key);
            if (values == null) {
                values = new ArrayList<V>();
            }
            values.add(value);
            rs.put(key, values);
        }
        return rs;
    }

    /**
     * listToMap
     */
    public static <K, V> Map<K, V> mapListToMap(List<Map<String, Object>> list) {
        return mapListToMap(list, "key", "value");
    }

    @SuppressWarnings("unchecked")
    public static <K, V> Map<K, V> mapListToMap(List<Map<String, Object>> list, String keyProp, String valueProp) {
        Map<K, V> rs = new HashMap<>(list.size());
        if (CommonUtil.isEmpty(list)) {
            return rs;
        }
        list.removeAll(nullCollection());
        for (Map<String, Object> map : list) {
            K key = (K) map.get(keyProp);
            V value = (V) map.get(valueProp);
            if (value != null) {
                rs.put(key, value);
            }
        }
        return rs;
    }

    public static <T> String getAnnotationName(SFunction<T, ?> fn) {
        try {
            Method writeReplaceMethod = fn.getClass().getDeclaredMethod("writeReplace");
            writeReplaceMethod.setAccessible(true);

            SerializedLambda serializedLambda = (SerializedLambda) writeReplaceMethod.invoke(fn);
            writeReplaceMethod.setAccessible(writeReplaceMethod.isAccessible());

            String fieldName = serializedLambda.getImplMethodName().substring("get".length());
            fieldName = fieldName.replaceFirst(fieldName.charAt(0) + "", (fieldName.charAt(0) + "").toLowerCase());
            Field field = Class.forName(serializedLambda.getImplClass().replace("/", ".")).getDeclaredField(fieldName);
            Column column = field.getAnnotation(Column.class);
            if (column != null && column.name().length() > 0) {
                return column.name();
            }
        } catch (Exception e) {
            // do nothing
        }
        return null;
    }
}
